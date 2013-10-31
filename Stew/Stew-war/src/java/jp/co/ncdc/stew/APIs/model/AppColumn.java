/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jp.co.ncdc.stew.APIs.model;

/**
 *
 * @author tthanhlong
 */
public class AppColumn {
    private String colName;
    private String type;

    public String getColName() {
        return colName;
    }

    public void setColName(String colName) {
        this.colName = colName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public AppColumn(String colName, String type) {
        this.colName = colName;
        this.type = type;
    }

    public AppColumn() {
    }
}
