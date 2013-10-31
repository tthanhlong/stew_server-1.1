/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jp.co.ncdc.stew.Servlet.model;

import java.util.List;
import jp.co.ncdc.stew.Entities.Apps;
import jp.co.ncdc.stew.Utils.StewConstant;
import jp.co.ncdc.stew.Utils.StewUtils;

/**
 *
 * @author vcnduong
 */
public class GroupApp {

    private String appName;
    private String appID;
    private String groups;
    private String createDate;

    public GroupApp() {
    }

    public GroupApp(Apps apps, List<String> groups) {
        this.appName = apps.getAppName();
        this.appID = apps.getAppId();
        this.createDate = StewUtils.convertDateToString(apps.getCreateDate(), StewConstant.DATE_FORMAT);
        this.groups = StewUtils.convertListStringToString(groups);
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getAppID() {
        return appID;
    }

    public void setAppID(String appID) {
        this.appID = appID;
    }

    public String getGroups() {
        return groups;
    }

    public void setGroups(String groups) {
        this.groups = groups;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }
}
