/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jp.co.ncdc.stew.Controllers;

import com.google.gson.Gson;
import java.util.List;
import jp.co.ncdc.stew.APIs.model.TrackData;
import jp.co.ncdc.stew.APIs.model.TrackTable;
import jp.co.ncdc.stew.Adapters.DataSyncTrackingAdapter;
import jp.co.ncdc.stew.Entities.DataSyncTracking;
import jp.co.ncdc.stew.Managers.DataChangeTrackingManager;
import jp.co.ncdc.stew.Managers.EventLogManager;

/**
 *
 * @author tquangthai
 */
public class DataSyncTrackingUpdateTask extends AbstractTask{

    @Override
    public synchronized void doTask() {
       
       //TODO 
       // Process update database here 
        saveDataChangeTrackingToDatabase();
    } 
    
    private synchronized void saveDataChangeTrackingToDatabase()
    {
          //TODO 
       // Process update database here 
        
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
    
}
