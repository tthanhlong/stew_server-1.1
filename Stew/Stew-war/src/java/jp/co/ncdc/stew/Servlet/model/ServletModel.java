/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jp.co.ncdc.stew.Servlet.model;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author vcnduong
 */
public class ServletModel {

    private int status;
    private int total;
    private String groupName;
    private List<UserAndRole> items = new ArrayList<UserAndRole>();
    private List<GroupApp> lstApp = new ArrayList<GroupApp>();
    private List<UserAppModel> lstUserApp = new ArrayList<UserAppModel>();
    private String description;

    public ServletModel() {
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
    
    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public List<UserAndRole> getItems() {
        return items;
    }

    public void setItems(List<UserAndRole> items) {
        this.items = items;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<GroupApp> getLstApp() {
        return lstApp;
    }

    public void setLstApp(List<GroupApp> lstApp) {
        this.lstApp = lstApp;
    }

    public List<UserAppModel> getLstUserApp() {
        return lstUserApp;
    }

    public void setLstUserApp(List<UserAppModel> lstUserApp) {
        this.lstUserApp = lstUserApp;
    }
}
