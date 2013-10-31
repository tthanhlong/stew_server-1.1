/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jp.co.ncdc.stew.APIs.model;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author vcnduong
 */
public class TrackData {

    private List<TrackTable> trackDatas;
    private String appId;
    private String userId;
    private String deviceUDID;
    private Boolean isFirstRegister;

    public void setIsFirstRegister(Boolean isFirstRegister) {
        this.isFirstRegister = isFirstRegister;
    }

    public Boolean getIsFirstRegister() {
        return isFirstRegister;
    }

    public TrackData() {
        trackDatas=new ArrayList<TrackTable>();
    }

    public List<TrackTable> getTrackDatas() {
        return trackDatas;
    }

    public void setTrackDatas(List<TrackTable> trackDatas) {
        this.trackDatas = trackDatas;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getDeviceUDID() {
        return deviceUDID;
    }

    public void setDeviceUDID(String deviceUDID) {
        this.deviceUDID = deviceUDID;
    }
}
