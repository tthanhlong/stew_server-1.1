/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jp.co.ncdc.stew.APIs.model;

import java.util.List;

/**
 * Data return model for ajax request
 * @author tthanhlong
 */
public class DataModel {
    
    private int status;
    private int total;
    private List listItems;

    public DataModel(int status, int total, List listItems) {
        this.status = status;
        this.total = total;
        this.listItems = listItems;
    }

    public DataModel() {
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List getListItems() {
        return listItems;
    }

    public void setListItems(List listItems) {
        this.listItems = listItems;
    }
    
}
