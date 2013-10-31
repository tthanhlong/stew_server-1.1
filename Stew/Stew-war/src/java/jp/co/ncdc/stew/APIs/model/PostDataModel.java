/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jp.co.ncdc.stew.APIs.model;

import java.util.List;

/**
 *
 * @author tthanhlong
 */
public class PostDataModel {
    private String token;
    private String table_name;
    private String primary_key;
    private String delete_ids;
    private List<RecordData> data;

    public PostDataModel() {
    }

    public PostDataModel(String token, String table_name, String primary_key, String delete_ids, List<RecordData> data) {
        this.token = token;
        this.table_name = table_name;
        this.primary_key = primary_key;
        this.delete_ids = delete_ids;
        this.data = data;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getTable_name() {
        return table_name;
    }

    public void setTable_name(String table_name) {
        this.table_name = table_name;
    }

    public String getPrimary_key() {
        return primary_key;
    }

    public void setPrimary_key(String primary_key) {
        this.primary_key = primary_key;
    }

    public String getDelete_ids() {
        return delete_ids;
    }

    public void setDelete_ids(String delete_ids) {
        this.delete_ids = delete_ids;
    }

    public List<RecordData> getData() {
        return data;
    }

    public void setData(List<RecordData> data) {
        this.data = data;
    }

}
