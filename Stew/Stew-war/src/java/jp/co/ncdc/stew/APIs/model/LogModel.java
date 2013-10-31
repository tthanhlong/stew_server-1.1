/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jp.co.ncdc.stew.APIs.model;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author tthanhlong
 */
public class LogModel {
    private long logID;
    private String appName;
    private String userId;
    private String logLevel;
    private String logMessage;
    private Date logDate;

    public LogModel() {
    }

    public LogModel(long logID, String appName, String userId, String logLevel, String logMessage, Date logDate) {
        this.logID = logID;
        this.appName = appName;
        this.userId = userId;
        this.logLevel = logLevel;
        this.logMessage = logMessage;
        this.logDate = logDate;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getLogLevel() {
        return logLevel;
    }

    public void setLogLevel(String logLevel) {
        this.logLevel = logLevel;
    }

    public String getLogMessage() {
        return logMessage;
    }

    public void setLogMessage(String logMessage) {
        this.logMessage = logMessage;
    }

    public Date getLogDate() {
        return logDate;
    }

    public void setLogDate(Date logDate) {
        this.logDate = logDate;
    }

    public long getLogID() {
        return logID;
    }

    public void setLogID(long logID) {
        this.logID = logID;
    }
    public String getLogDateString(){
        DateFormat df = new SimpleDateFormat("yyyy.MM.dd");
        return df.format(this.logDate);
    }
}
