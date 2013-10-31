/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jp.co.ncdc.stew.Controllers;

import java.sql.SQLException;
import java.util.List;
import jp.co.ncdc.stew.APIs.model.ClientAppData;
import jp.co.ncdc.stew.APIs.model.PostDataModel;
import jp.co.ncdc.stew.APIs.model.RowData;
import jp.co.ncdc.stew.APIs.model.Status;
import jp.co.ncdc.stew.APIs.model.TableGetDataResponse;
import jp.co.ncdc.stew.APIs.model.TrackTable;
import jp.co.ncdc.stew.APIs.model.TrackingRowDatas;
import jp.co.ncdc.stew.Adapters.AppsAdapter;
import jp.co.ncdc.stew.Adapters.UserSessionAdapter;
import jp.co.ncdc.stew.Entities.Apps;
import jp.co.ncdc.stew.Entities.UserSession;
import jp.co.ncdc.stew.Managers.DataChangeTrackingManager;
import jp.co.ncdc.stew.Utils.APIResponse;
import jp.co.ncdc.stew.Utils.StewConstant;

/**
 *
 * @author tthanhlong
 */
public class AppsController {

    /**
     * @brief: This function is to control create database with data which is
     * posted from client
     * @param clientAppData
     * @return
     * @throws SQLException
     */
    public Status createDatabase(ClientAppData clientAppData) throws SQLException {
        Status result = null;
        AppsAdapter appsAdapter = new AppsAdapter();
        String userToken = clientAppData.getToken();

        //if valid check user authentication
        UserSessionAdapter userSessionAdapter = new UserSessionAdapter();
        UserSession userSession = userSessionAdapter.getUserSessionByToken(userToken);

        //check user is existed or token expire
        if (userSession == null || userSessionAdapter.checkTokenIsExpire(userSession)) {
            if (userSession == null) {
                result = new APIResponse().loggingResponse(StewConstant.ERR_STATUS, StewConstant.ERR_USER_NOT_EXIST);
            }else{
                result = new APIResponse().loggingResponse(StewConstant.ERR_STATUS, StewConstant.ERR_TOKEN_EXPIRE);
            }
        } else {
            if (appsAdapter.createDatabase(clientAppData, userSession)) {
                userSessionAdapter.updateTokenExpireDate(userSession);
                result = new APIResponse().loggingResponse(StewConstant.OK_STATUS, "");
            }else{
                result = new APIResponse().loggingResponse(StewConstant.ERR_STATUS, StewConstant.ERR_CREATE_APP_CLIENT);
            }
        }

        return result;
    }

    /**
     * @brief API get data for table
     * @param userToken
     * @param tableName
     * @param lastUpdate
     * @param maxRecord
     * @return
     */
    public TableGetDataResponse getDataForTable(String userToken, String tableName, int maxRecord, String primaryKey, boolean isFirst) throws SQLException {
        TableGetDataResponse result = new TableGetDataResponse();
        //check data input
        if (userToken == null || "".equals(userToken) || tableName == null || "".equals(tableName) || primaryKey == null || "".equals(primaryKey)) {
            return new TableGetDataResponse(StewConstant.ERR_STATUS, StewConstant.ERR_REQUIRED);
        } else {
            //if valid check user authentication
            UserSessionAdapter userSessionAdapter = new UserSessionAdapter();
            UserSession userSession = userSessionAdapter.getUserSessionByToken(userToken);
            if (userSession == null) {
                return new TableGetDataResponse(StewConstant.ERR_STATUS, StewConstant.ERR_TOKEN_NOT_EXIST);
            } else {
                // check user token expire
                if (userSessionAdapter.checkTokenIsExpire(userSession)) {
                    return new TableGetDataResponse(StewConstant.ERR_STATUS, StewConstant.ERR_EXPIRE_TOKEN);
                } else {
                    AppsAdapter appsAdapter = new AppsAdapter();
                    String appId = userSession.getAppId();
                    Apps apps = appsAdapter.getAppByAppID(appId);
                    if (apps == null) {
                        return new TableGetDataResponse(StewConstant.ERR_STATUS, StewConstant.ERR_APPID_NOT_EXIST);
                    } else {
                        String databaseName = apps.getDatabaseName();
                        if (databaseName != null || "".equals(databaseName)) {
                            if (isFirst) {
                                List<RowData> listRowData = appsAdapter.getAllRowDataByRecordId(databaseName, tableName);
                                if (listRowData == null) {
                                    result.setResults(null);
                                    result.setStatus(StewConstant.ERR_STATUS);
                                    result.setDescription(StewConstant.ERR_NOT_EXIST_TABLE);
                                } else {
                                    if (listRowData.size() > 0) {
                                        result.setResults(listRowData);
                                        result.setStatus(StewConstant.OK_STATUS);
                                        result.setDescription(StewConstant.OK_STATUS);
                                    } else {
                                        result.setStatus(StewConstant.ERR_STATUS);
                                        result.setDescription(StewConstant.ERR_LIST_RECORD_EMPTY);
                                    }
                                }
                            } else {
                                result = getDataChangeForTable(result, userSession, databaseName, tableName, primaryKey, maxRecord);
                            }
                        } else {
                            result.setStatus(StewConstant.ERR_STATUS);
                            result.setDescription(StewConstant.ERR_DATABASE_NAME_EMPTY);
                        }
                    }
                }
            }
        }
        return result;
    }

    /**
     * get Data Change for Table
     *
     * @param result
     * @param userSession
     * @param databaseName
     * @param tableName
     * @param primaryKey
     * @param maxRecord
     * @return
     */
    public TableGetDataResponse getDataChangeForTable(TableGetDataResponse result, UserSession userSession, String databaseName, String tableName, String primaryKey, int maxRecord) {
        UserSessionAdapter userSessionAdapter = new UserSessionAdapter();
        AppsAdapter appsAdapter = new AppsAdapter();
        String appId = userSession.getAppId();
        String userId = userSession.getUserId();
        String deviceUDID = userSession.getDeviceUDID();
        //get list table change from data tracking
        List<TrackTable> listTrackTables = DataChangeTrackingManager.getInstance().getTrackingData(appId, deviceUDID, userId);
        if (listTrackTables != null && !listTrackTables.isEmpty()) {
            // get table change by table name
            TrackTable trackTable = appsAdapter.getTrackChangeRecordOfTable(listTrackTables, tableName);
            if (trackTable != null) {
                //get rowdata changed and update track table
                TrackingRowDatas trackingRowDatas = appsAdapter.getRowsDataForTable(databaseName, tableName, trackTable, appId, primaryKey, maxRecord);
                //set remainning record
                result.setRemainningRecord(trackingRowDatas.getRemaining());
                //set rows data
                result.setResults(trackingRowDatas.getRowDatas());
                //set delete record Id
                String deleteIds = getDeleteIdsOfTable(trackTable);
                result.setDeleteIds(deleteIds);
                //update user token expire
                userSessionAdapter.updateTokenExpireDate(userSession);
                //add status of API
                result.setStatus(StewConstant.OK_STATUS);
                result.setDescription(StewConstant.OK_STATUS);
                //remove track table
                try {
                    DataChangeTrackingManager.getInstance().cleanTrackingData(appId, deviceUDID, userId, trackingRowDatas.getTrackTable());
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            } else {
                result.setStatus(StewConstant.ERR_STATUS);
                result.setDescription(StewConstant.TRACK_TABLE_EMPTY);
            }
        } else {
            result.setStatus(StewConstant.ERR_STATUS);
            result.setDescription(StewConstant.TRACK_TABLE_EMPTY);
        }
        return result;
    }

    /**
     * get all data for Table
     *
     * @param result
     * @param userSession
     * @param databaseName
     * @param tableName
     * @param primaryKey
     * @param maxRecord
     * @return
     */
    public TableGetDataResponse getAllRecordForTable(TableGetDataResponse result, UserSession userSession, String databaseName, String tableName, String primaryKey, int maxRecord) {
        UserSessionAdapter userSessionAdapter = new UserSessionAdapter();
        AppsAdapter appsAdapter = new AppsAdapter();
        String appId = userSession.getAppId();
        String userId = userSession.getUserId();
        String deviceUDID = userSession.getDeviceUDID();
        //get list table change from data tracking
        List<TrackTable> listTrackTables = DataChangeTrackingManager.getInstance().getTrackingData(appId, deviceUDID, userId);
        if (listTrackTables != null && !listTrackTables.isEmpty()) {
            // get table change by table name
            TrackTable trackTable = appsAdapter.getTrackChangeRecordOfTable(listTrackTables, tableName);
            if (trackTable != null) {
                //get rowdata changed and update track table
                TrackingRowDatas trackingRowDatas = appsAdapter.getRowsDataForTable(databaseName, tableName, trackTable, appId, primaryKey, maxRecord);
                //set remainning record
                result.setRemainningRecord(trackingRowDatas.getRemaining());
                //set rows data
                result.setResults(trackingRowDatas.getRowDatas());
                //set delete record Id
                String deleteIds = getDeleteIdsOfTable(trackTable);
                result.setDeleteIds(deleteIds);
                //update user token expire
                userSessionAdapter.updateTokenExpireDate(userSession);
                //add status of API
                result.setStatus(StewConstant.OK_STATUS);
                result.setDescription(StewConstant.OK_STATUS);
                //remove track table
                try {
                    DataChangeTrackingManager.getInstance().cleanTrackingData(appId, deviceUDID, userId, trackingRowDatas.getTrackTable());
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            } else {
                result.setStatus(StewConstant.ERR_STATUS);
                result.setDescription(StewConstant.TRACK_TABLE_EMPTY);
            }
        } else {
            result.setStatus(StewConstant.ERR_STATUS);
            result.setDescription(StewConstant.TRACK_TABLE_EMPTY);
        }
        return result;
    }

    /**
     * This function is to post data to table controller. Steps below: 1. Check
     * user token is expired. 2. If user token is not expired. Call post data to
     * table from Adapter. 3. Update user token with new expiration date
     *
     * @param postModel
     * @return
     * @throws SQLException
     */
    public Status postDataToTable(PostDataModel postModel) throws SQLException {
        Status result = null;
        AppsAdapter appsAdapter = new AppsAdapter();
        String userToken = postModel.getToken();

        //if valid check user authentication
        UserSessionAdapter userSessionAdapter = new UserSessionAdapter();
        UserSession userSession = userSessionAdapter.getUserSessionByToken(userToken);

        //check user is existed or token expire
        if (userSession == null || userSessionAdapter.checkTokenIsExpire(userSession)) {
            if (userSession == null) {
                result = new APIResponse().loggingResponse(StewConstant.ERR_STATUS, StewConstant.ERR_USER_NOT_EXIST);
            }else{
                result = new APIResponse().loggingResponse(StewConstant.ERR_STATUS, StewConstant.ERR_TOKEN_EXPIRE);
            }
        } else {
            if (appsAdapter.postDataToTable(postModel, userSession)) {
                userSessionAdapter.updateTokenExpireDate(userSession);
                result = new APIResponse().loggingResponse(StewConstant.OK_STATUS, "");
            }else{
                result = new APIResponse().loggingResponse(StewConstant.ERR_STATUS, StewConstant.ERR_POST_APP_CLIENT);
            }
        }

        return result;
    }

    /**
     * get String IDs of record deleted
     *
     * @param trackTable
     * @return
     */
    private String getDeleteIdsOfTable(TrackTable trackTable) {
        String result = "";
        List<String> listDeleteIds = trackTable.getDelete();
        if (listDeleteIds != null && !listDeleteIds.equals("")) {
            for (int i = 0; i < listDeleteIds.size(); i++) {
                result += listDeleteIds.get(i);
                if (i != listDeleteIds.size() - 1) {
                    result += ",";
                }
            }
        }
        return result;
    }

    /**
     *
     * @param tokenString
     * @return
     */
    public boolean checkAppDataExist(String tokenString) {
        boolean result = false;
        AppsAdapter appsAdapter = new AppsAdapter();

        //if valid check user authentication
        UserSessionAdapter userSessionAdapter = new UserSessionAdapter();
        UserSession userSession = userSessionAdapter.getUserSessionByToken(tokenString);

        //check user is existed or token expire
        if (userSession == null || userSessionAdapter.checkTokenIsExpire(userSession)) {
            result = true;
        } else {
            if (appsAdapter.checkAppDataExist(userSession)) {
                userSessionAdapter.updateTokenExpireDate(userSession);
                result = true;
            }
        }

        return result;

    }
}
