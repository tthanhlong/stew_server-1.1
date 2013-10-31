/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jp.co.ncdc.stew.APIs.model;

/**
 *
 * @author lxanh
 */
public class Logging {

    private String appId;
    private String message;
    private String token;
    private String type;
    private String deviceUDID;

    public String getAppId() {
        return appId;
    }

    public String getDeviceUDID() {
        return deviceUDID;
    }

    public String getMessage() {
        return message;
    }

    public String getToken() {
        return token;
    }

    public String getType() {
        return type;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public void setDeviceUDID(String deviceUDID) {
        this.deviceUDID = deviceUDID;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setType(String type) {
        this.type = type;
    }
}
