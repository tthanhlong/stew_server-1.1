/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jp.co.ncdc.stew.APIs.model;

/**
 *
 * @author vcnduong
 */
public class TableGetDataRequest {

    private String token;
    private String tableName;
    private int maxRecord;
    private String primaryKey;

    public TableGetDataRequest() {
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public int getMaxRecord() {
        return maxRecord;
    }

    public void setMaxRecord(int maxRecord) {
        this.maxRecord = maxRecord;
    }

    public String getPrimaryKey() {
        return primaryKey;
    }

    public void setPrimaryKey(String primaryKey) {
        this.primaryKey = primaryKey;
    }
}
