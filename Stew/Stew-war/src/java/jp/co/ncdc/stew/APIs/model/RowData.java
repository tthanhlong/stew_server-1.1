/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jp.co.ncdc.stew.APIs.model;

import java.util.List;

/**
 *
 * @author vcnduong
 */
public class RowData {

    private List<ColumnData> records;
    private String type;

    public RowData() {
    }

    public List<ColumnData> getRecords() {
        return records;
    }

    public void setRecords(List<ColumnData> records) {
        this.records = records;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
