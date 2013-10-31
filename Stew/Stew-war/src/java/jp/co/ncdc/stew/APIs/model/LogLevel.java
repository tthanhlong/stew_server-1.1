/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jp.co.ncdc.stew.APIs.model;

/**
 *
 * @author vcnduong
 */
public class LogLevel {

    public String status;
    public String logLevel;

    public LogLevel(String status, String logLevel) {
        this.status = status;
        this.logLevel = logLevel;
    }

    public LogLevel() {
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getLogLevel() {
        return logLevel;
    }

    public void setLogLevel(String logLevel) {
        this.logLevel = logLevel;
    }
}
