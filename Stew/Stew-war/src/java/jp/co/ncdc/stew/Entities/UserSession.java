/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jp.co.ncdc.stew.Entities;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Temporal;
import jp.co.ncdc.stew.Utils.StewConstant;

/**
 *
 * @author vcnduong
 */
@Entity
public class UserSession {

    @Id
    private String userToken;
    private String userId;
    private String deviceUDID;
    private String appId;
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date loginDate;
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date tokenExpireDate;

    public UserSession() {
    }

    public String getUserToken() {
        return userToken;
    }

    public void setUserToken(String userToken) {
        this.userToken = userToken;
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

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public Date getLoginDate() {
        return loginDate;
    }

    public void setLoginDate(Date loginDate) {
        this.loginDate = loginDate;
    }

    public Date getTokenExpireDate() {
        return tokenExpireDate;
    }

    public void setTokenExpireDate(Date tokenExpireDate) {
        this.tokenExpireDate = tokenExpireDate;
    }

    public String getLoginDateString() {
        DateFormat df = new SimpleDateFormat(StewConstant.DATE_FORMAT);
        return df.format(this.loginDate);
    }

    public String getTokenExpireDateString() {
        DateFormat df = new SimpleDateFormat(StewConstant.DATE_FORMAT);
        return df.format(this.tokenExpireDate);
    }
}
