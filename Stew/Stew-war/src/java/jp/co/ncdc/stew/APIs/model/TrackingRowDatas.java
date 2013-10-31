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
public class TrackingRowDatas {

    private List<RowData> rowDatas;
    private int remaining;
    private TrackTable trackTable;

    public TrackingRowDatas() {
    }

    public TrackingRowDatas(List<RowData> rowDatas, int remaining, TrackTable trackTable) {
        this.rowDatas = rowDatas;
        this.remaining = remaining;
        this.trackTable = trackTable;
    }

    public List<RowData> getRowDatas() {
        return rowDatas;
    }

    public void setRowDatas(List<RowData> rowDatas) {
        this.rowDatas = rowDatas;
    }

    public int getRemaining() {
        return remaining;
    }

    public void setRemaining(int remaining) {
        this.remaining = remaining;
    }

    public TrackTable getTrackTable() {
        return trackTable;
    }

    public void setTrackTable(TrackTable trackTable) {
        this.trackTable = trackTable;
    }
}
