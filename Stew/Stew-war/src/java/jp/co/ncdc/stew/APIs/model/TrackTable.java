/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jp.co.ncdc.stew.APIs.model;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author vcnduong
 */
public class TrackTable {
    private String tableName;
    private List<String> update;
    private List<String> insert;
    private List<String> delete;

    public TrackTable() {
        update = new ArrayList<String>();
        insert = new ArrayList<String>();
        update = new ArrayList<String>();
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public List<String> getUpdate() {
        return update;
    }

    public void setUpdate(List<String> update) {
        this.update = update;
    }

    public List<String> getInsert() {
        return insert;
    }

    public void setInsert(List<String> insert) {
        this.insert = insert;
    }

    public List<String> getDelete() {
        return delete;
    }

    public void setDelete(List<String> delete) {
        this.delete = delete;
    }
    
}
