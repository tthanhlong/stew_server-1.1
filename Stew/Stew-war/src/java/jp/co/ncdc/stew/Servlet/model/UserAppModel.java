/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jp.co.ncdc.stew.Servlet.model;

import java.util.List;
import jp.co.ncdc.stew.Entities.UserApp;
import jp.co.ncdc.stew.Utils.StewConstant;
import jp.co.ncdc.stew.Utils.StewUtils;

/**
 *
 * @author vcnduong
 */
public class UserAppModel {

    private String groups;
    private Long idUserApp;
    private String userId;
    private String appId;
    private String status;
    private String dateModified;

    public UserAppModel() {
    }

    public UserAppModel(UserApp userApp, List<String> groups) {
        this.groups = StewUtils.convertListStringToString(groups);
        this.idUserApp = userApp.getIdUserApp();
        this.userId = userApp.getUserId();
        this.appId = userApp.getAppId();
        this.status = userApp.getStatus();
        this.dateModified = StewUtils.convertDateToString(userApp.getDateModified(), StewConstant.DATE_FORMAT);
    }

    public String getGroups() {
        return groups;
    }

    public void setGroups(String groups) {
        this.groups = groups;
    }

    public Long getIdUserApp() {
        return idUserApp;
    }

    public void setIdUserApp(Long idUserApp) {
        this.idUserApp = idUserApp;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDateModified() {
        return dateModified;
    }

    public void setDateModified(String dateModified) {
        this.dateModified = dateModified;
    }
}
