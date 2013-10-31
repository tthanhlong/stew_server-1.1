/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jp.co.ncdc.stew.Entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 *
 * @author vcnduong
 */
@Entity
public class UserData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userDataId;
    private String databaseName;
    private String userId;
    private String appId;
    private String schemaXML;
    private Long appVersion;

    public UserData() {
    }

    public UserData(String databaseName, String userId, String appId, String schemaXML, Long appVersion) {
        this.databaseName = databaseName;
        this.userId = userId;
        this.appId = appId;
        this.schemaXML = schemaXML;
        this.appVersion = appVersion;
    }

    public String getDatabaseName() {
        return databaseName;
    }

    public void setDatabaseName(String databaseName) {
        this.databaseName = databaseName;
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

    public String getSchemaXML() {
        return schemaXML;
    }

    public void setSchemaXML(String schemaXML) {
        this.schemaXML = schemaXML;
    }

    public Long getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(Long appVersion) {
        this.appVersion = appVersion;
    }

    public Long getUserDataId() {
        return userDataId;
    }

    public void setUserDataId(Long userDataId) {
        this.userDataId = userDataId;
    }
}
