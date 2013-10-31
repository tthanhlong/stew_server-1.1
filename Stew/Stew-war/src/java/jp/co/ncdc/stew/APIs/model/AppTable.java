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
public class AppTable {
    private String name;
    private String primary_key;
    private List<AppColumn> columns;

    public AppTable() {
    }

    public AppTable(String name, String primary_key, List<AppColumn> columns) {
        this.name = name;
        this.primary_key = primary_key;
        this.columns = columns;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrimary_key() {
        return primary_key;
    }

    public void setPrimary_key(String primary_key) {
        this.primary_key = primary_key;
    }

    public List<AppColumn> getColumns() {
        return columns;
    }

    public void setColumns(List<AppColumn> columns) {
        this.columns = columns;
    }
    
}
