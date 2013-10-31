/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jp.co.ncdc.stew.Entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 *
 * @author vcnduong
 */
@Entity
public class DeviceRegister {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long deviceRegisterId;
    private String deviceUDID;
    private String appId;
    private String userId;
    private String deviceToken;

    public DeviceRegister() {
    }

    public String getDeviceUDID() {
        return deviceUDID;
    }

    public void setDeviceUDID(String deviceUDID) {
        this.deviceUDID = deviceUDID;
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

    public String getDeviceToken() {
        return deviceToken;
    }

    public void setDeviceToken(String deviceToken) {
        this.deviceToken = deviceToken;
    }

    public Long getDeviceRegisterId() {
        return deviceRegisterId;
    }

    public void setDeviceRegisterId(Long deviceRegisterId) {
        this.deviceRegisterId = deviceRegisterId;
    }
}
