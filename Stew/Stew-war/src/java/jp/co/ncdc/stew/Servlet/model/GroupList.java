/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jp.co.ncdc.stew.Servlet.model;

import java.util.ArrayList;
import java.util.List;
import jp.co.ncdc.stew.Entities.GroupUser;

/**
 *
 * @author vcnduong
 */
public class GroupList {

    private int status;
    private int total;
    private List<GroupUser> items = new ArrayList<GroupUser>();
    private String description;

    public GroupList() {
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

    public List<GroupUser> getItems() {
        return items;
    }

    public void setItems(List<GroupUser> items) {
        this.items = items;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
