/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jp.co.ncdc.stew.Managers;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import jp.co.ncdc.stew.APIs.model.TrackData;
import jp.co.ncdc.stew.APIs.model.TrackTable;
import jp.co.ncdc.stew.Adapters.DataSyncTrackingAdapter;
import jp.co.ncdc.stew.Controllers.AsyncTask;
import jp.co.ncdc.stew.Controllers.DataSyncTrackingUpdateTask;
import jp.co.ncdc.stew.Entities.DataSyncTracking;
import jp.co.ncdc.stew.Utils.StewConstant;

/**
 *
 * @author tquangthai
 */
public class DataChangeTrackingManager {
   
    // Instance
    private static DataChangeTrackingManager instance;
    public static DataChangeTrackingManager getInstance() {
        if (instance == null) {
            instance = new DataChangeTrackingManager();
        }
        return instance;
    }
    
    private DataChangeTrackingManager()
    {
        lstTrackData=new ArrayList<TrackData>();
        loadFromDatabase();
    }
    private List<TrackData> lstTrackData = null;

    public void setLstTrackData(List<TrackData> lstTrackData) {
        this.lstTrackData = lstTrackData;
    }

    public List<TrackData> getLstTrackData() {
        return lstTrackData;
    }
    
    
    public TrackData getTrackData(String userId, String appId, String deviceUDID)
    {
        TrackData _trackData=null;
        if (appId != null && deviceUDID != null && userId != null) {
            for(TrackData trackData:lstTrackData)
            {
                if(trackData.getAppId().equals(appId)&&trackData.getUserId().equals(userId)&&trackData.getDeviceUDID().equals(deviceUDID))
                {
                   _trackData=trackData;
                    break; 
                }
            } 
        }   
        return _trackData;
    } 
    
    
    public Boolean updateStatusTrackData(String userId, String appId, String deviceUDID)
    {
        if (appId != null && deviceUDID != null && userId != null) {
            for(TrackData trackData:lstTrackData)
            {
                if(trackData.getAppId().equals(appId)&&trackData.getUserId().equals(userId)&&trackData.getDeviceUDID().equals(deviceUDID))
                {
                   trackData.setIsFirstRegister(false);
                   return true;
                    
                }
            } 
        }     
        return false;
    }
    
    public void UpdateDeviceRegister(String appId, String deviceUDID, String userId)
    {
        Boolean isExist=false;
        if (appId != null && deviceUDID != null && userId != null) {
            for(TrackData trackData:lstTrackData)
            {
                if(trackData.getAppId().equals(appId)&&trackData.getUserId().equals(userId)&&trackData.getDeviceUDID().equals(deviceUDID))
                {
                    isExist=true;
                    break; 
                }
            } 

            if(!isExist)
            {
                TrackData trackData=new TrackData();
                trackData.setAppId(appId);
                trackData.setDeviceUDID(deviceUDID);
                trackData.setUserId(userId);
                trackData.setIsFirstRegister(true);
                lstTrackData.add(trackData);
                //get all data for new device
                
            }
        }
    }
    

    
    private void loadFromDatabase()
    {
        DataSyncTrackingAdapter dataSyncAdapt = new DataSyncTrackingAdapter();
        List<DataSyncTracking> lstDataSync = dataSyncAdapt.getSyncTrackDatas();
        for (DataSyncTracking dataSync:lstDataSync) {
            TrackData trackData = new TrackData();
            trackData.setAppId(dataSync.getAppId());
            trackData.setUserId(dataSync.getUserId());
            trackData.setDeviceUDID(dataSync.getDeviceUDID());
            Gson gson = new Gson();
            List<TrackTable> lstTrackTable = gson.fromJson(dataSync.getDataToSync_Metadata(), new TypeToken<List<TrackTable>>(){}.getType());           
            trackData.setTrackDatas(lstTrackTable);
            lstTrackData.add(trackData);            
        }            
    }
    
    /**
     * add record id to list insert/update/delete of track table
     * @param tracktable
     * @param recordId
     * @param action
     * @return 
     */
    //private
    private void addDataToTable (TrackTable trackTable, String recordId, int action) {
        switch (action) {            
            case StewConstant.CREATE:
                List<String> insert = trackTable.getInsert();
                if (insert == null) {
                    insert = new ArrayList<String>();
                    insert.add(recordId);     
                    trackTable.setInsert(insert);                    
                }                    
                else if (!insert.contains(recordId))
                    insert.add(recordId);                
                break;
            case StewConstant.EDIT:
                List<String> update = trackTable.getUpdate();
                if (update == null)
                {
                    update = new ArrayList<String>();
                    update.add(recordId);
                    trackTable.setUpdate(update);
                }                
                else if (!update.contains(recordId))                    
                    update.add(recordId);
                break;
            case StewConstant.DELETE:
                List<String> delete = trackTable.getDelete();                
                if (delete == null)
                {
                    delete = new ArrayList<String>();
                    delete.add(recordId);
                    trackTable.setDelete(delete);
                }                
                else if (!delete.contains(recordId))                    
                    delete.add(recordId);
                break;
        }    
    }                     
    
    /**
     * clean data from a table
     * @param destable
     * @param sourceTable     
     * @return TrackTable
     */
    //private
      private TrackTable cleanTrackTable(TrackTable desTable, TrackTable sourceTable) {
        //clean data from a track table
        List<String> lstSourceInsert = sourceTable.getInsert();
        int countSourceInsert = lstSourceInsert.size();
        List<String> lstDesInsert = desTable.getInsert();        
//        for (String insert:lstSourceInsert) {
//            lstDesInsert.remove(insert);
//        }    
        
        for (int i = 0; i < countSourceInsert; i++) {
            if (null != lstDesInsert && lstDesInsert.contains(lstSourceInsert.get(i))) {
                lstDesInsert.remove(lstSourceInsert.get(i));
            }
        }
        //
        List<String> lstSourceUpdate = sourceTable.getUpdate();
        int countSourceUpdate = lstSourceUpdate.size();
        List<String> lstDesUpdate = desTable.getUpdate();        
//        for (String update:lstSourceUpdate) {
//            lstDesUpdate.remove(update);
//        }
        
        for (int i = 0; i < countSourceUpdate; i++) {
            if (null != lstDesUpdate && lstDesUpdate.contains(lstSourceUpdate.get(i))) {
                lstDesUpdate.remove(lstSourceUpdate.get(i));
            }
        }
        
        //
        List<String> lstSourceDelete = sourceTable.getDelete();
        int countSourceDelete = lstSourceDelete.size();
        List<String> lstDesDelete = desTable.getDelete();        
//        for (String delete:lstSourceDelete) {
//            lstDesDelete.remove(delete);
//        }
        for (int i = 0; i < countSourceDelete; i++) {
            if (null != lstDesDelete && lstDesDelete.contains(lstSourceDelete.get(i))) {
                lstDesDelete.remove(lstSourceDelete.get(i));
            }
        }
        return desTable;   
    }
    
    /**
     * clean data from a track table in list track table
     * @param trackingData
     * @param trackTable     
     * @return List<TrackTable>
     */
     //private
     private List<TrackTable> cleanTableData(List<TrackTable> trackingData, TrackTable trackTable) {
        for (TrackTable trackingTable:trackingData) {
            //clean data from a track table in list has same name as input track table
            if (trackingTable.getTableName().equals(trackTable.getTableName())) {
                cleanTrackTable(trackingTable, trackTable);
            }
        }
        return trackingData;
    }                
    
    /**
     * clean tracking data depend on appId, deviceUDID & userId
     * @param appId
     * @param deviceUDID     
     * @param userId
     * @param lstTrackTable 
     * @return List<TrackTable>
     */
    private List<TrackTable> cleanTrackingData(String appId, String deviceUDID, String userId, List<TrackTable> lstTrackTable) {
        //get list track table need to clean
        List<TrackTable> trackingData = getTrackingData(appId, deviceUDID, userId);
        for (TrackTable trackingTable:lstTrackTable) {
            trackingData = cleanTableData(trackingData, trackingTable);
        }                
        return trackingData;
    } 
            
     /**
     * clean tracking data depend on appId, deviceUDID & userId
     * @param appId
     * @param deviceUDID     
     * @param userId
     * @param trackTable 
     * @return TrackTable
     */
    public List<TrackTable> cleanTrackingData(String appId, String deviceUDID, String userId, TrackTable trackTable) {
        //get list track table need to clean
        List<TrackTable> trackingData = getTrackingData(appId, deviceUDID, userId);
        cleanTableData(trackingData, trackTable);   
        
        // Call for backup data change
        saveDataChangeTrackingToDatabase();
        return trackingData;
    } 
    
    /**
     * get tracking data depend on appId, deviceUDID & userId
     * @param appId
     * @param deviceUDID     
     * @param userId     
     * @return List<TrackTable>
     */
    public List<TrackTable> getTrackingData(String appId, String deviceUDID, String userId) {        
        if (appId != null && deviceUDID != null && userId != null) {
            for (TrackData trackData:lstTrackData){
                //return list track table have appId, deviceUDID, userId same as input parameter
                if (appId.equals(trackData.getAppId()) && deviceUDID.equals(trackData.getDeviceUDID()) &&
                    userId.equals(trackData.getUserId())) {
                    return trackData.getTrackDatas();                
                }
            } 
        }
        return null;
    }
    
    /**
     * insert a changing recordId to track data
     * @param appId
     * @param deviceUDID     
     * @param userId     
     * @param tableName
     * @param recordId 
     * @param action
     * @return boolean
     */
    public boolean trackDataChange(String appId, String deviceUDID, String userId, String tableName, String recordId, int action) {
        if (appId != null && deviceUDID != null && userId != null) {   
            for (TrackData trackData:lstTrackData) {
                boolean isExist = false;
                //insert data if have same appId, userID and different deviceUDID
                if (appId.equals(trackData.getAppId()) && userId.equals(trackData.getUserId()) && !deviceUDID.equals(trackData.getDeviceUDID())) {
                    for (TrackTable trackTable:trackData.getTrackDatas()) {                        
                        if (trackTable.getTableName().equals(tableName)) {
                            isExist = true;
                            addDataToTable(trackTable, recordId, action);
                            break;                            
                        }                            
                    }
                          
                    //create new table in track data if not exist
                    if (!isExist) {
                        TrackTable newTrackTable = new TrackTable();
                        newTrackTable.setTableName(tableName);
                        addDataToTable(newTrackTable, recordId, action);
                        trackData.getTrackDatas().add(newTrackTable);                        
                    }
                }
            }
               
            return true;             
        }
        return false;
    } 
    
    
    //Backup Data change tracking to database
    public void saveDataChangeTrackingToDatabase()
    {
      
        List<TrackData> lstTrackData=DataChangeTrackingManager.getInstance().getLstTrackData();
          System.err.println("get list data Length: "+lstTrackData.size());

        if (lstTrackData != null && !lstTrackData.isEmpty()) {
            int countListTrackData = lstTrackData.size();
            for (int i = 0; i < countListTrackData; i++) {
                List<TrackTable> lstTrackTable = lstTrackData.get(i).getTrackDatas();
                Gson gson = new Gson();
                String trackDataString = gson.toJson(lstTrackTable);
                
                String appID = lstTrackData.get(i).getAppId();
                String deviceID = lstTrackData.get(i).getDeviceUDID();
                String userID = lstTrackData.get(i).getUserId();
                
                DataSyncTrackingAdapter dataSyncTrackingAdapter = new DataSyncTrackingAdapter();
                DataSyncTracking dataSyncTracking  = dataSyncTrackingAdapter.getDataSyncTrackingFromDatabase(userID, appID, deviceID);
                if (dataSyncTracking != null) {
                    dataSyncTracking.setDataToSync_Metadata(trackDataString);
                    dataSyncTrackingAdapter.updateDataSyncTracking(dataSyncTracking);
                }else{
                    DataSyncTracking newDataSyncTracking = new DataSyncTracking();
                    newDataSyncTracking.setDataToSync_Metadata(trackDataString);
                    newDataSyncTracking.setAppId(appID);
                    newDataSyncTracking.setDeviceUDID(deviceID);
                    newDataSyncTracking.setUserId(userID);
                    
                    dataSyncTrackingAdapter.addDataSyncTracking(newDataSyncTracking);
                }
            }
        }   
    } 
    
    
    /**
     * 
     * @param appID 
     */
     public void resetAppData(String appID)
     {
            for(TrackData trackData:lstTrackData)
            {
                if(trackData.getAppId().equals(appID))
                {
                   trackData.setTrackDatas(new ArrayList<TrackTable>());
                }
            } 
     }
    
}
