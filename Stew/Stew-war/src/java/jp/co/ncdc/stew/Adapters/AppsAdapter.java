/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jp.co.ncdc.stew.Adapters;

import com.google.gson.Gson;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import jp.co.ncdc.stew.APIs.model.AppColumn;
import jp.co.ncdc.stew.APIs.model.AppTable;
import jp.co.ncdc.stew.APIs.model.ClientAppData;
import jp.co.ncdc.stew.APIs.model.ColumnData;
import jp.co.ncdc.stew.APIs.model.PostDataModel;
import jp.co.ncdc.stew.APIs.model.RecordData;
import jp.co.ncdc.stew.APIs.model.RowData;
import jp.co.ncdc.stew.APIs.model.TrackTable;
import jp.co.ncdc.stew.APIs.model.TrackingRowDatas;
import jp.co.ncdc.stew.Controllers.AsyncTask;
import jp.co.ncdc.stew.Controllers.DataSyncTrackingUpdateTask;
import jp.co.ncdc.stew.Controllers.UserAppController;
import jp.co.ncdc.stew.Entities.Apps;
import jp.co.ncdc.stew.Entities.UserSession;
import jp.co.ncdc.stew.Managers.ConnectionManager;
import jp.co.ncdc.stew.Managers.DataChangeTrackingManager;
import jp.co.ncdc.stew.Managers.EventLogManager;
import jp.co.ncdc.stew.Servlet.model.GroupApp;
import jp.co.ncdc.stew.Utils.EntityManagerFactorUtils;
import jp.co.ncdc.stew.Utils.StewConstant;

/**
 * @name: AppUserAdapter.java
 * @create: Aug 21, 2013
 * @version 1.0
 * @brief: This class is AppUserAdapter which connect to database and query data
 */
public class AppsAdapter {

    UserAppController userAppController = new UserAppController();

    public AppsAdapter() {
    }

    /**
     * Get max last update of table
     *
     * @param databaseName
     * @param tableName
     * @return
     * @throws SQLException
     */
    private int getMaxLastUpdate(String databaseName, String tableName) throws SQLException {
        int result = -1;
        try {
            ConnectionManager connectionManager = new ConnectionManager();
            Connection conn = connectionManager.createConnection(databaseName);
            Statement stmt = conn.createStatement();

            String sql = "SELECT MAX(`" + StewConstant.LAST_UPDATE + "`) FROM " + tableName;

            ResultSet rs = stmt.executeQuery(sql);

            //STEP 5: Extract data from listRows set
            while (rs.next()) {
                //Retrieve by column name
                result = rs.getInt(0);

                System.out.println(result);
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(AppsAdapter.class.getName()).log(Level.SEVERE, null, ex);
        }

        return result;
    }

    /**
     * @brief: This function is create database with data from client. These
     * steps below: 1. Check token is existed in database or not? 2. If token is
     * not existed, return error. Otherwise, go to 3. 3. Check appID is existed
     * or not? 4. If appID is not existed, return error. Otherwise, go to 5. 5.
     * Generate unique appID, update data to Apps table. 6. Create database for
     * app client with database name above. 7. Create tables with their columns
     * and data type 8. If create unsuccessful, remove database with database
     * name above
     * @param clientAppData
     * @return
     * @throws SQLException
     */
    public boolean createDatabase(ClientAppData clientAppData, UserSession userSession) throws SQLException {
        boolean result = false;
        List<AppTable> lstTables = clientAppData.getTables();
        int countTable = lstTables.size();

        if (userSession != null) {
            ConnectionManager connectionManager = new ConnectionManager();
            Connection connection;
            String appID = userSession.getAppId();

            Apps apps = getAppByAppID(appID);
            if (apps != null) {
                String databaseExist = apps.getDatabaseName();
                if (databaseExist != null && !databaseExist.equals("")) {
                    try {
                        connection = connectionManager.createConnection();
                        Statement statement = connection.createStatement();
                        statement.executeUpdate("DROP DATABASE " + databaseExist);
                    } catch (Exception e) {
                        Logger.getLogger(AppsAdapter.class.getName()).log(Level.SEVERE, null, e);
                    }
                }
                long nowLong = new Date().getTime();
                String uniqueID = String.valueOf(nowLong);
                String uniqueAppID = StewConstant.APP_ID + uniqueID;

                Gson gson = new Gson();
                String tableString = gson.toJson(lstTables);
                apps.setDatabaseName(uniqueAppID);
                apps.setSchemaJSON(tableString);

                DataChangeTrackingManager.getInstance().resetAppData(appID);
                if (updateApps(apps)) {
                    try {
                        connection = connectionManager.createConnection();
                        Statement statement = connection.createStatement();

                        statement.executeUpdate("CREATE DATABASE IF NOT EXISTS " + uniqueAppID + " DEFAULT CHARACTER SET utf8 DEFAULT COLLATE utf8_unicode_ci;");
                        statement.executeUpdate("USE " + uniqueAppID);
                        boolean flag = true;

                        if (countTable != 0 && lstTables != null) {
                            for (int i = 0; i < countTable; i++) {
                                String tableName = lstTables.get(i).getName();
                                String primaryKey = lstTables.get(i).getPrimary_key();
                                List<AppColumn> lstColumns = lstTables.get(i).getColumns();
                                int countColumn = lstColumns.size();
                                String lstColumnsSQL = "";

                                if (countColumn != 0 && lstColumns != null) {
                                    //Add userID column for each table
                                    AppColumn userColumn = new AppColumn(StewConstant.NCDC_STEW_ID, StewConstant.VARCHAR_TYPE);
                                    lstColumns.add(userColumn);
                                    for (int j = 0; j <= countColumn; j++) {
                                        String columName = lstColumns.get(j).getColName();
                                        String type = lstColumns.get(j).getType();

                                        if (type.toLowerCase().equals(StewConstant.BLOB_TYPE)) {
                                            lstColumnsSQL += "`" + columName + "` " + StewConstant.MEDIUM_BLOB_TYPE + ", ";
                                        } else if (type.toLowerCase().equals(StewConstant.VARCHAR_TYPE)) {
                                            if (columName.equals(primaryKey)) {
                                                lstColumnsSQL += "`" + columName + "` " + StewConstant.PRIMARY_KEY_ATTRIBUTE;
                                            } else {
                                                lstColumnsSQL += "`" + columName + "` " + StewConstant.TEXT_TYPE + StewConstant.UTF_8_COLLATION;
                                            }
                                        } else {
                                            lstColumnsSQL += "`" + columName + "` " + StewConstant.BIGINT_TYPE + StewConstant.UTF_8_COLLATION;
                                        }
                                    }
                                }

                                lstColumnsSQL = lstColumnsSQL + "PRIMARY KEY (`" + primaryKey + "`)";

                                String sQLTable = "CREATE TABLE IF NOT EXISTS `" + tableName + "` (" + lstColumnsSQL + ") ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;";
                                try {
                                    statement.executeUpdate(sQLTable);
                                } catch (Exception e) {
                                    statement.executeUpdate("DROP DATABASE " + uniqueAppID);
                                    flag = false;
                                    Logger.getLogger(AppsAdapter.class.getName()).log(Level.SEVERE, null, e);
                                    break;
                                }
                            }
                        }
                        if (flag) {
                            result = true;
                        }
                        connectionManager.closeConnection();
                    } catch (ClassNotFoundException ex) {
                        Logger.getLogger(AppsAdapter.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }

        return result;
    }

    /**
     * @brief: This function is to get Apps object by appID
     * @param appID
     * @return
     */
    public Apps getAppByAppID(String appID) {
        Apps result = null;
        EntityManagerFactory entityManagerFactory = null;
        EntityManager entityManager = null;

        try {
            entityManagerFactory = Persistence.createEntityManagerFactory("JPA");
            entityManager = entityManagerFactory.createEntityManager();
            entityManager.getTransaction().begin();

            Query query = entityManager.createQuery("from Apps where appID =:appID");
            query.setParameter("appID", appID);
            List queryResult = query.getResultList();

            if (queryResult != null && queryResult.size() > 0) {
                result = (Apps) queryResult.get(0);
            }
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            EventLogManager.getInstance().log(e.getMessage());
        } finally {
            entityManager.close();
            entityManagerFactory.close();
        }
        return result;
    }

    /**
     * @brief: This function is to update Apps
     * @param apps
     * @return
     */
    public boolean updateApps(Apps apps) {
        boolean result = false;
        EntityManagerFactory entityManagerFactory = null;
        EntityManager entityManager = null;

        try {
            entityManagerFactory = Persistence.createEntityManagerFactory("JPA");
            entityManager = entityManagerFactory.createEntityManager();
            entityManager.getTransaction().begin();
            entityManager.merge(apps);

            entityManager.getTransaction().commit();
            result = true;
        } catch (Exception e) {
            EventLogManager.getInstance().log(e.getMessage());
        } finally {
            entityManager.close();
            entityManagerFactory.close();
        }
        return result;
    }

    /**
     * @brief: This function is to insert Apps
     * @param apps
     * @return
     */
    public boolean addApps(Apps apps) {
        boolean result = false;
        EntityManagerFactory entityManagerFactory = null;
        EntityManager entityManager = null;

        try {
            entityManagerFactory = Persistence.createEntityManagerFactory("JPA");
            entityManager = entityManagerFactory.createEntityManager();
            entityManager.getTransaction().begin();
            entityManager.persist(apps);

            entityManager.getTransaction().commit();
            result = true;
        } catch (Exception e) {
            EventLogManager.getInstance().log(e.getMessage());
        } finally {
            entityManager.close();
            entityManagerFactory.close();
        }
        return result;
    }

    /**
     * @brief get rows data for table
     * @param databaseName
     * @param tableName
     * @param lastupdate
     * @return
     * @throws SQLException
     */
    public TrackingRowDatas getRowsDataForTable(String databaseName, String tableName, TrackTable trackTable, String appId, String primaryKey, int maxRecord) {
        int remaining = 0;
        List<RowData> listRows = new ArrayList<RowData>();
        int total = 0;
        if (trackTable.getInsert() != null) {
            total += trackTable.getInsert().size();
        }
        if (trackTable.getUpdate() != null) {
            total += trackTable.getUpdate().size();
        }
        TrackTable removeTrack = new TrackTable();
        removeTrack.setDelete(trackTable.getDelete());
        removeTrack.setTableName(trackTable.getTableName());
        if (total > 0) {
            try {
                int number = 0;
                if (trackTable.getInsert() != null) {
                    for (int i = 0; i < trackTable.getInsert().size(); i++) {
                        String insertId = trackTable.getInsert().get(i);
                        RowData rowData = getRowDataByRecordId(databaseName, tableName, insertId, StewConstant.INSERT_TYPE, primaryKey);
                        if (rowData != null) {
                            listRows.add(rowData);
                            number++;
                            removeTrack.getInsert().add(insertId);
                            if (number == maxRecord) {
                                return new TrackingRowDatas(listRows, remaining, trackTable);
                            }
                        }
                    }
                }
                if (trackTable.getUpdate() != null) {
                    for (int j = 0; j < trackTable.getUpdate().size(); j++) {
                        String updateId = trackTable.getUpdate().get(j);
                        RowData rowData = getRowDataByRecordId(databaseName, tableName, updateId, StewConstant.UPDATE_TYPE, primaryKey);
                        if (rowData != null) {
                            listRows.add(rowData);
                            number++;

                            removeTrack.getUpdate().add(updateId);
                            if (number == maxRecord) {
                                return new TrackingRowDatas(listRows, remaining, trackTable);
                            }
                        }
                    }
                }
            } catch (SQLException ex) {
                Logger.getLogger(AppsAdapter.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return new TrackingRowDatas(listRows, remaining, trackTable);
    }

    /**
     * @brief tracking change for table
     * @param listTrackTable
     * @param tableName
     * @return TrackTable
     */
    public TrackTable getTrackChangeRecordOfTable(List<TrackTable> listTrackTable, String tableName) {
        TrackTable trackTable = null;
        for (int i = 0; i < listTrackTable.size(); i++) {
            if (tableName.equals(listTrackTable.get(i).getTableName())) {
                return listTrackTable.get(i);
            }
        }
        return trackTable;
    }

    /**
     *
     * @param databaseName
     * @param tableName
     * @param recordId
     * @param type
     * @param primaryKey
     * @return
     * @throws SQLException
     */
    public RowData getRowDataByRecordId(String databaseName, String tableName, String recordId, String type, String primaryKey) throws SQLException {
        RowData rowData = new RowData();
        ConnectionManager connectionManager = new ConnectionManager();
        try {
            Connection connection = connectionManager.createConnection(databaseName);
            Statement statement = connection.createStatement();
            String query = "select * from " + tableName + " where " + primaryKey + " = '" + recordId + "'";
            System.out.println(query);
            try {
                ResultSet rs = statement.executeQuery(query);
                while (rs.next()) {
                    List<ColumnData> listColumnDatas = new ArrayList<ColumnData>();
                    ResultSetMetaData metadata = rs.getMetaData();
                    for (int i = 1; i < metadata.getColumnCount(); i++) {
                        String columnLabel = metadata.getColumnLabel(i);
                        String value = rs.getString(columnLabel);
                        ColumnData columnData = new ColumnData(columnLabel, value);
                        listColumnDatas.add(columnData);
                    }
                    rowData.setRecords(listColumnDatas);
                    rowData.setType(type);
                }
            } catch (Exception e) {
                Logger.getLogger(AppsAdapter.class.getName()).log(Level.SEVERE, null, e);
                return null;
            }

        } catch (ClassNotFoundException ex) {
            Logger.getLogger(AppsAdapter.class.getName()).log(Level.SEVERE, null, ex);
        }
        return rowData;
    }

    /**
     *
     * @param databaseName
     * @param tableName
     * @param recordId
     * @param type
     * @param primaryKey
     * @return
     * @throws SQLException
     */
    public List<RowData> getAllRowDataByRecordId(String databaseName, String tableName) throws SQLException {
        List<RowData> listRowDatas = new ArrayList<RowData>();
        ConnectionManager connectionManager = new ConnectionManager();
        try {
            Connection connection = connectionManager.createConnection(databaseName);
            Statement statement = connection.createStatement();
            String query = "select * from " + tableName + " where  1";
            System.out.println(query);
            try {
                ResultSet rs = statement.executeQuery(query);
                while (rs.next()) {
                    List<ColumnData> listColumnDatas = new ArrayList<ColumnData>();
                    ResultSetMetaData metadata = rs.getMetaData();
                    for (int i = 1; i < metadata.getColumnCount(); i++) {
                        String columnLabel = metadata.getColumnLabel(i);
                        String value = rs.getString(columnLabel);
                        ColumnData columnData = new ColumnData(columnLabel, value);
                        listColumnDatas.add(columnData);
                    }
                    RowData rowData = new RowData();
                    rowData.setRecords(listColumnDatas);
                    rowData.setType(StewConstant.INSERT_TYPE);
                    listRowDatas.add(rowData);
                }
            } catch (Exception e) {
                Logger.getLogger(AppsAdapter.class.getName()).log(Level.SEVERE, null, e);
                return null;
            }

        } catch (ClassNotFoundException ex) {
            Logger.getLogger(AppsAdapter.class.getName()).log(Level.SEVERE, null, ex);
        }
        return listRowDatas;
    }

    /**
     * This function is to post data to table 1. Check app id is existed or not?
     * 2. If app id is not existed, return false. Otherwise, go to 3 3. Get
     * database name ,table name and list data records 4. Loop for the list and
     * process insert or update 5. Get list delete ids and delete by id
     *
     * @param postModel
     * @param userSession
     * @return
     * @throws SQLException
     */
    public boolean postDataToTable(PostDataModel postModel, UserSession userSession) throws SQLException {
        boolean result = false;
        String appID = userSession.getAppId();
        Apps apps = getAppByAppID(appID);

        if (apps != null) {
            String databaseName = apps.getDatabaseName();
            String tableName = postModel.getTable_name();
            String primaryKey = postModel.getPrimary_key();
            String deleteIDs = postModel.getDelete_ids();
            String userID = userSession.getUserId();
            String deviceUDID = userSession.getDeviceUDID();

            /**
             * ** Process insert or update data **
             */
            List<RecordData> lstRerords = postModel.getData();
            if (lstRerords != null) {
                int countRecord = lstRerords.size();
                for (int j = 0; j < countRecord; j++) {
                    List<ColumnData> lstColumnOfRecord = lstRerords.get(j).getRecords();
                    RecordData recordData = lstRerords.get(j);
                    String recordId = recordData.getPrimaryValue(primaryKey);
                    String type = lstRerords.get(j).getType();

                    if (lstColumnOfRecord != null && !lstColumnOfRecord.isEmpty()) {
                        if (type.equals(StewConstant.INSERT_TYPE)) {
                            if (insertDataToTable(databaseName, tableName, lstColumnOfRecord, userID)) {
                                //track data after inserting
                                DataChangeTrackingManager.getInstance().trackDataChange(appID, deviceUDID, userID, tableName, recordId, StewConstant.CREATE);
                            }
                        } else if (type.equals(StewConstant.UPDATE_TYPE)) {
                            if (updateDataToTable(databaseName, tableName, primaryKey, lstColumnOfRecord, userID)) {
                                //track data after updating
                                DataChangeTrackingManager.getInstance().trackDataChange(appID, deviceUDID, userID, tableName, recordId, StewConstant.EDIT);
                            }
                        }
                    }
                }
            }

            /**
             * * Process delete records**
             */
            if (deleteIDs != null && !deleteIDs.equals("")) {
                String delims = "[,]";
                String[] lstDeleteIDs = deleteIDs.split(delims);
                int countDeleteID = lstDeleteIDs.length;
                for (int i = 0; i < countDeleteID; i++) {
                    if (!lstDeleteIDs[i].isEmpty()) {
                        String selectedID = lstDeleteIDs[i];
                        if (deleteDataFromTable(databaseName, tableName, primaryKey, selectedID)) {
                            //track data after deleting
                            DataChangeTrackingManager.getInstance().trackDataChange(appID, deviceUDID, userID, tableName, selectedID, StewConstant.DELETE);
                        }
                    }
                }
            }

            result = true;

            // Call for backup data change
            DataChangeTrackingManager.getInstance().saveDataChangeTrackingToDatabase();
        }

        return result;
    }

    /**
     * Insert data to table with specific database name and table
     *
     * @param databaseName
     * @param tableName
     * @param lstColumnOfRecord
     * @param userID
     * @return
     * @throws SQLException
     */
    private boolean insertDataToTable(String databaseName, String tableName, List<ColumnData> lstColumnOfRecord, String userID) throws SQLException {
        boolean result = false;
        ColumnData columnData = new ColumnData(StewConstant.NCDC_STEW_ID, userID);
        lstColumnOfRecord.add(columnData);
        try {
            ConnectionManager connectionManager = new ConnectionManager();
            Connection conn = connectionManager.createConnection(databaseName);
            Statement statement = conn.createStatement();
            String sQLInsert = "INSERT INTO `" + databaseName + "`.`" + tableName + "` VALUES ('";

            int countColumnOfRecord = lstColumnOfRecord.size();
            for (int k = 0; k < countColumnOfRecord; k++) {
                sQLInsert += lstColumnOfRecord.get(k).getValue() + "','";
            }

            sQLInsert = sQLInsert.substring(0, sQLInsert.length() - 2);
            sQLInsert += ")";
            try {
                statement.executeUpdate(sQLInsert);
                result = true;
            } catch (Exception e) {
                Logger.getLogger(AppsAdapter.class.getName()).log(Level.SEVERE, null, e);
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(AppsAdapter.class.getName()).log(Level.SEVERE, null, ex);
        }

        return result;
    }

    /**
     * Update data to table with specific database and table
     *
     * @param databaseName
     * @param tableName
     * @param primaryKey
     * @param lstColumnOfRecord
     * @param userID
     * @return
     * @throws SQLException
     */
    private boolean updateDataToTable(String databaseName, String tableName, String primaryKey, List<ColumnData> lstColumnOfRecord, String userID) throws SQLException {
        boolean result = false;
        ColumnData columnData = new ColumnData(StewConstant.NCDC_STEW_ID, userID);
        lstColumnOfRecord.add(columnData);
        try {
            ConnectionManager connectionManager = new ConnectionManager();
            Connection conn = connectionManager.createConnection(databaseName);
            Statement statement = conn.createStatement();
            String sQLUpdate = "UPDATE `" + databaseName + "`.`" + tableName + "` SET `";
            String primaryValue = "";

            int countColumnOfRecord = lstColumnOfRecord.size();
            for (int k = 0; k < countColumnOfRecord; k++) {
                if (!lstColumnOfRecord.get(k).getName().equals(primaryKey)) {
                    sQLUpdate += lstColumnOfRecord.get(k).getName() + "`='" + lstColumnOfRecord.get(k).getValue() + "',`";
                } else {
                    primaryValue = lstColumnOfRecord.get(k).getValue();
                }
            }

            sQLUpdate = sQLUpdate.substring(0, sQLUpdate.length() - 2);
            sQLUpdate += " WHERE `" + primaryKey + "`='" + primaryValue + "'";
            try {
                statement.executeUpdate(sQLUpdate);
                result = true;
            } catch (Exception e) {
                Logger.getLogger(AppsAdapter.class.getName()).log(Level.SEVERE, null, e);
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(AppsAdapter.class.getName()).log(Level.SEVERE, null, ex);
        }

        return result;
    }

    /**
     * Delete data from table by delete id, database name and table name
     *
     * @param databaseName
     * @param tableName
     * @param primaryKey
     * @param selectedID
     * @return
     * @throws SQLException
     */
    private boolean deleteDataFromTable(String databaseName, String tableName, String primaryKey, String selectedID) throws SQLException {
        boolean result = false;
        try {
            ConnectionManager connectionManager = new ConnectionManager();
            Connection conn = connectionManager.createConnection(databaseName);
            Statement statement = conn.createStatement();
            
            String sQLCheckPrimaryType = "SELECT column_type FROM information_schema.columns WHERE table_name = '" + tableName +"' and table_schema = '" + databaseName + "' and column_name = '" + primaryKey + "';";
            
            
            ResultSet rs = statement.executeQuery(sQLCheckPrimaryType);
            String primaryType = "";
            while (rs.next()) {
                primaryType = rs.getString(1);
            }
            
            String sQLDelete = "DELETE FROM `" + databaseName + "`.`" + tableName + "` WHERE `" + primaryKey + "`=" + selectedID;
            if (primaryType.toLowerCase().indexOf(StewConstant.VARCHAR_TYPE) != -1) {
                sQLDelete = "DELETE FROM `" + databaseName + "`.`" + tableName + "` WHERE `" + primaryKey + "`='" + selectedID + "'";
            }
            

            try {
                statement.executeUpdate(sQLDelete);
                result = true;
            } catch (Exception e) {
                Logger.getLogger(AppsAdapter.class.getName()).log(Level.SEVERE, null, e);
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(AppsAdapter.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }
    
    /**
     *
     * @param userSession
     * @return
     */
    public boolean checkAppDataExist(UserSession userSession) {
        String appID = userSession.getAppId();
        boolean result = false;
        Apps checkApp = getAppByAppID(appID);
        if (checkApp != null && checkApp.getDatabaseName() != null && !checkApp.getDatabaseName().equals("")) {
            result = true;
        }

        return result;
    }

    /**
     * @brief: This function is get all apps
     * @return
     */
    public List<Apps> getAllApps() {
        EntityManagerFactory entityManagerFactory = null;
        EntityManager entityManager = null;
        List<Apps> lstApps = new ArrayList<Apps>();

        try {
            entityManagerFactory = Persistence.createEntityManagerFactory("JPA");
            entityManager = entityManagerFactory.createEntityManager();
            entityManager.getTransaction().begin();
            List result = (entityManager.createQuery("from Apps").getResultList());
            lstApps = (List<Apps>) result;

            entityManager.getTransaction().commit();
        } catch (Exception e) {
            EventLogManager.getInstance().log(e.getMessage());
        } finally {
            entityManager.close();
            entityManagerFactory.close();
        }
        return lstApps;
    }

    /**
     *
     * @param listApps
     * @return
     */
    public List<GroupApp> getGroupAppOfApps(List<Apps> listApps, String email) {
        List<GroupApp> list = new ArrayList<GroupApp>();
        GroupAppDetailAdapter gada = new GroupAppDetailAdapter();
        for (int i = 0; i < listApps.size(); i++) {
            List<String> groups = gada.getListGroupNameOfApp(listApps.get(i).getAppId());
            GroupApp groupApp = new GroupApp(listApps.get(i), groups);
            list.add(groupApp);
        }
        return list;
    }

    /**
     *
     * @param adminId
     * @return
     */
    public List<Apps> getAppByAdmin(String adminId) {
        EntityManagerFactory entityManagerFactory = null;
        EntityManager entityManager = null;
        List<Apps> lstApps = new ArrayList<Apps>();
        try {
            entityManagerFactory = Persistence.createEntityManagerFactory("JPA");
            entityManager = entityManagerFactory.createEntityManager();
            entityManager.getTransaction().begin();

            Query query = entityManager.createQuery("from Apps where adminId =:adminId");
            query.setParameter("adminId", adminId);
            lstApps = (List<Apps>) query.getResultList();
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            EventLogManager.getInstance().log(e.getMessage());
        } finally {
            entityManager.close();
            entityManagerFactory.close();
        }

        return lstApps;
    } 
    
    
    //**
        public Apps getAppByDatabaseName(String databaseName) {
        Apps result = null;
        EntityManagerFactory entityManagerFactory = null;
        EntityManager entityManager = null;

        try {
            entityManagerFactory = Persistence.createEntityManagerFactory("JPA");
            entityManager = entityManagerFactory.createEntityManager();
            entityManager.getTransaction().begin();

            Query query = entityManager.createQuery("from Apps where databaseName =:databaseName");
            query.setParameter("databaseName", databaseName);
            List queryResult = query.getResultList();

            if (queryResult != null && queryResult.size() > 0) {
                result = (Apps) queryResult.get(0);
            }
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            EventLogManager.getInstance().log(e.getMessage());
        } finally {
            entityManager.close();
            entityManagerFactory.close();
        }
        return result;
    }
        
     /**
     * @param userId
     * @param isSupperAdmin
     * @return
     */
    public List<Apps> getAppsByManagerId(String userId, boolean isSupperAdmin) {
        EntityManager entityManager = null;
        List<Apps> lstApps = new ArrayList<Apps>();
        try {
            entityManager =EntityManagerFactorUtils.getCurrentEntityManagerFactory().createEntityManager();
            entityManager.getTransaction().begin();                
            Query query;
            if (isSupperAdmin)                    
                query = entityManager.createQuery("from Apps");
            else
                query = entityManager.createQuery("select distinct A from Apps A, UserGroupDetail U, GroupAppDetail GA where U.userId='" + userId + "' and U.roleId<=" + StewConstant.ROLE_MANAGER + " and GA.groupId=U.groupId and GA.appId=A.appId");
            List queryResult = query.getResultList();
            lstApps = (List<Apps>)queryResult;
            entityManager.getTransaction().commit();
        }catch (Exception e) {
            EventLogManager.getInstance().log(e.getMessage());
        } finally {
            entityManager.close();
        }

        return lstApps;
    }
}
