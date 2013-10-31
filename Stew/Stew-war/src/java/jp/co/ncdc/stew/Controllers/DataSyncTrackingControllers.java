/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jp.co.ncdc.stew.Controllers;

import jp.co.ncdc.stew.APIs.model.Status;
import jp.co.ncdc.stew.Adapters.DataSyncTrackingAdapter;

/**
 *
 * @author tquangthai
 */
public class DataSyncTrackingControllers {

    /**
     * @brief: This function is get metadata for sync by userToken
     * @param userToken
     * @return
     */
    public Status getTableNameForSync(String userToken) {
        
        DataSyncTrackingAdapter dataSync = new DataSyncTrackingAdapter();
        return dataSync.getTableNameForSync(userToken);
    }
}
