/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jp.co.ncdc.stew.Controllers;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import jp.co.ncdc.stew.APIs.model.DataModel;
import jp.co.ncdc.stew.APIs.model.LogModel;
import jp.co.ncdc.stew.APIs.model.Status;
import jp.co.ncdc.stew.Adapters.AppsAdapter;
import jp.co.ncdc.stew.Adapters.LogAdapter;
import jp.co.ncdc.stew.Adapters.UserGroupDetailAdapter;
import jp.co.ncdc.stew.Adapters.UserSessionAdapter;
import jp.co.ncdc.stew.Entities.Apps;
import jp.co.ncdc.stew.Entities.Log;
import jp.co.ncdc.stew.Entities.UserSession;
import jp.co.ncdc.stew.Utils.APIResponse;
import jp.co.ncdc.stew.Utils.StewConfig;
import jp.co.ncdc.stew.Utils.StewConstant;

/**
 * @name: LoggingController.java
 * @create: Aug 21, 2013
 * @version 1.0
 * @brief: This class is implementation for Logging Controller
 */
public class LoggingController {

    /**
     * @brief: This method is process of log message
     * @param token
     * @param type
     * @param message
     * @return
     */
    public Status logMessage(String token, String type, String message, String appID, String deviceUDID) {
        Status result = new Status();
        UserSessionAdapter adapter = new UserSessionAdapter();
        LogAdapter logController = new LogAdapter();

        //if token or appID is null or not specified: return error
        if (token == null || "".equals(token) || appID == null || "".equals(appID) || deviceUDID == null || "".equals(deviceUDID)) {
            result = new APIResponse().loggingResponse(StewConstant.ERR_STATUS, StewConstant.ERR_REQUIRED);
        } else {
            //get user session by token
            UserSession userSession = adapter.getUserSessionByToken(token);

            //if user is not existed: return error
            if (userSession == null) {
                result = new APIResponse().loggingResponse(StewConstant.ERR_STATUS, StewConstant.ERR_TOKEN_NOT_EXIST);
            } else {
                //if token is expired: return error
                if (adapter.checkTokenIsExpire(userSession)) {
                    result = new APIResponse().loggingResponse(StewConstant.ERR_STATUS, StewConstant.ERR_EXPIRE_TOKEN);
                } else {
                    //update expire date of token
                    adapter.updateTokenExpireDate(userSession);

                    //create log record
                    Date now = new Date();
                    Log log = new Log();
                    log.setAppId(appID);
                    log.setMessageLog(message);
                    log.setTimeLog(now);
                    log.setUserId(userSession.getUserId());
                    log.setDeviceUDID(deviceUDID);
                    log.setType(type);

                    logController.addLog(log);

                    result = new APIResponse().loggingResponse(StewConstant.OK_STATUS, "");
                }
            }
        }

        return result;
    }
    
    public DataModel getLogByUser(int pageNumber, String email){
        DataModel dataModel = new DataModel();
        List<LogModel> lstLogs = new LinkedList<LogModel>();
        UserGroupDetailAdapter userGroupDetailAdapter = new UserGroupDetailAdapter();
        LogAdapter logAdapter = new LogAdapter();
        boolean isSuperAdmin = userGroupDetailAdapter.checkUserIsSupperAdmin(email);
        
        if (isSuperAdmin) {
            List<Log> lstAllLogs = logAdapter.getAllLogs();
            if (lstAllLogs != null) {
                int countAllLog = lstAllLogs.size();
                for (int i = 0; i < countAllLog; i++) {
                    long logID = lstAllLogs.get(i).getId();
                    String appID = lstAllLogs.get(i).getAppId();
                    String messageLog = lstAllLogs.get(i).getMessageLog();
                    Date logDate = lstAllLogs.get(i).getTimeLog();
                    String logType = lstAllLogs.get(i).getType();
                    String userID = lstAllLogs.get(i).getUserId();
                    
                    Apps apps = new AppsAdapter().getAppByAppID(appID);
                    String appName = "";
                    try {
                        appName = apps.getAppName();
                    } catch (Exception e) {
                    }                    
                    
                    LogModel logModel = new LogModel();
                    logModel.setAppName(appName);
                    logModel.setUserId(userID);
                    logModel.setLogDate(logDate);
                    logModel.setLogLevel(logType);
                    logModel.setLogMessage(messageLog);
                    logModel.setLogID(logID);
                    
                    lstLogs.add(logModel);
                }
            }
        }else{
            //not super admin
            List<Apps> lstAppsByUserAdmin = new AppsAdapter().getAppByAdmin(email);
            int countAppsByUserAdmin = lstAppsByUserAdmin.size();
            if (countAppsByUserAdmin != 0) {
                for (int i = 0; i < countAppsByUserAdmin; i++) {
                    String appID = lstAppsByUserAdmin.get(i).getAppId();
                    List<Log> lstLogByUserAmdin = logAdapter.getLogByAppID(appID);
                    if (lstLogByUserAmdin != null) {
                        int countLogByUserAdmin = lstLogByUserAmdin.size();
                        for (int j = 0; j < countLogByUserAdmin; j++) {
                            long logID = lstLogByUserAmdin.get(j).getId();
                            String messageLog = lstLogByUserAmdin.get(j).getMessageLog();
                            Date logDate = lstLogByUserAmdin.get(j).getTimeLog();
                            String logType = lstLogByUserAmdin.get(j).getType();
                            String userID = lstLogByUserAmdin.get(j).getUserId();

                            Apps apps = new AppsAdapter().getAppByAppID(appID);
                            String appName = "";
                            try {
                                appName = apps.getAppName();
                            } catch (Exception e) {
                            }                            

                            LogModel logModel = new LogModel();
                            logModel.setAppName(appName);
                            logModel.setUserId(userID);
                            logModel.setLogDate(logDate);
                            logModel.setLogLevel(logType);
                            logModel.setLogMessage(messageLog);
                            logModel.setLogID(logID);

                            lstLogs.add(logModel);
                        }
                    }
                }
            }
        }
        
        //process with list log model
        int totalRecords = lstLogs.size();

        if (!lstLogs.isEmpty() && totalRecords > 0) {
            int totalPage = totalRecords / StewConstant.ITEM_PER_PAGE_MANAGE + 1;
            if (totalRecords % StewConstant.ITEM_PER_PAGE_MANAGE == 0) {
                totalPage = totalPage - 1;
            }
            if (totalRecords <= StewConstant.ITEM_PER_PAGE_MANAGE) {
                dataModel.setListItems(lstLogs);
            } else {
                List<LogModel> lstLogModels = new LinkedList<LogModel>();
                int getUsersFrom = pageNumber * StewConstant.ITEM_PER_PAGE_MANAGE;
                int getUsersTo = (getUsersFrom + StewConstant.ITEM_PER_PAGE_MANAGE) < totalRecords ? getUsersFrom + StewConstant.ITEM_PER_PAGE_MANAGE : totalRecords;

                for (int i = getUsersFrom; i < getUsersTo; i++) {
                    lstLogModels.add(lstLogs.get(i));
                }

                dataModel.setListItems(lstLogModels);
            }

            dataModel.setStatus(StewConstant.STATUS_CODE_OK);
            dataModel.setTotal(totalPage);
        }
        return dataModel;
    }
    
    public DataModel getLogFilter(int pageNumber, String userId, boolean isSupperAdmin, String appId, String level, String fromDateString, String toDateString, String keySearch){
        DataModel dataModel = new DataModel();
        List<LogModel> lstLogs = new ArrayList<LogModel>();        
        LogAdapter logAdapter = new LogAdapter();                       
        lstLogs = logAdapter.getLogsFilter(userId, isSupperAdmin, appId, level, fromDateString, toDateString, keySearch);                                       
        
        //process with list log model
        int totalRecords = lstLogs.size();

        if (!lstLogs.isEmpty() && totalRecords > 0) {
            int totalPage = totalRecords / StewConstant.ITEM_PER_PAGE_MANAGE + 1;
            if (totalRecords % StewConstant.ITEM_PER_PAGE_MANAGE == 0) {
                totalPage = totalPage - 1;
            }
            if (totalRecords <= StewConstant.ITEM_PER_PAGE_MANAGE) {
                dataModel.setListItems(lstLogs);
            } else {
                List<LogModel> lstLogModels = new LinkedList<LogModel>();
                int getUsersFrom = pageNumber * StewConstant.ITEM_PER_PAGE_MANAGE;
                int getUsersTo = (getUsersFrom + StewConstant.ITEM_PER_PAGE_MANAGE) < totalRecords ? getUsersFrom + StewConstant.ITEM_PER_PAGE_MANAGE : totalRecords;

                for (int i = getUsersFrom; i < getUsersTo; i++) {
                    lstLogModels.add(lstLogs.get(i));
                }

                dataModel.setListItems(lstLogModels);
            }

            dataModel.setStatus(StewConstant.STATUS_CODE_OK);
            dataModel.setTotal(totalPage);
        }
        return dataModel;
    }
}
