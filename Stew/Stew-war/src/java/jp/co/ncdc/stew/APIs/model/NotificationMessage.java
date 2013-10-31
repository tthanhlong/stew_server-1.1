/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jp.co.ncdc.stew.APIs.model;

import java.util.Date;

/**
 *
 * @author lxanh
 */
public class NotificationMessage {
    
    private Long id;
    private String message; 
    private String userId;
    private String appId;
    private String deviceToken;
    private String status;
    private Date scheduleSend;

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public void setScheduleSend(Date scheduleSend) {
        this.scheduleSend = scheduleSend;
    }

    public String getUserId() {
        return userId;
    }

    public String getAppId() {
        return appId;
    }

    public Date getScheduleSend() {
        return scheduleSend;
    }


    public void setUserID(String userID) {
        this.userId = userID;
    }

    public void setDeviceToken(String deviceToken) {
        this.deviceToken = deviceToken;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUserID() {
        return userId;
    }

    public String getDeviceToken() {
        return deviceToken;
    }

    public String getStatus() {
        return status;
    }   
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
    
}
