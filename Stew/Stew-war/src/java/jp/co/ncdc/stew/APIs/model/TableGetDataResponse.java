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
public class TableGetDataResponse {

    private String status;
    private List<RowData> results;
    private int remainningRecord;
    private String description;
    private String deleteIds;

    public TableGetDataResponse() {
    }

    public TableGetDataResponse(String status, String description) {
        this.status = status;
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<RowData> getResults() {
        return results;
    }

    public void setResults(List<RowData> results) {
        this.results = results;
    }

    public int getRemainningRecord() {
        return remainningRecord;
    }

    public void setRemainningRecord(int remainningRecord) {
        this.remainningRecord = remainningRecord;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDeleteIds() {
        return deleteIds;
    }

    public void setDeleteIds(String deleteIds) {
        this.deleteIds = deleteIds;
    }
}
