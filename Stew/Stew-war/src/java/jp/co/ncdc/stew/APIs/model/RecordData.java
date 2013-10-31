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
public class RecordData {
    private List<ColumnData> records;
    private String type;

    public RecordData() {
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

    public RecordData(List<ColumnData> records, String type) {
        this.records = records;
        this.type = type;
    }

    public String getPrimaryValue(String key){
        String result = "";
        if (records != null && !records.isEmpty()) {
            int countColumn = records.size();
            
            for (int i = 0; i < countColumn; i++) {
                if (records.get(i).getName().equals(key)) {
                    result = records.get(i).getValue();
                    break;
                }
            }
        }
        
        return result;
    }
}
