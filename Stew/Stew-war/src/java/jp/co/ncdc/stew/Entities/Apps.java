/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jp.co.ncdc.stew.Entities;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Temporal;

/**
 *
 * @author vcnduong
 */
@Entity
public class Apps {
    @Id
    private String appId;
    private String appName;
    private String databaseName;
    @Column(length = 65536)
    private String schemaJSON;
    private String certificatePath;
    private String certificatePassword;
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date createDate;
    private String adminId;

    public Apps() {
    }

    public Apps(String appID, String appName, String certificatePath, String certificatePassword) {
        this.appId = appID;
        this.appName = appName;
        this.certificatePath = certificatePath;
        this.certificatePassword = certificatePassword;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appID) {
        this.appId = appID;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getDatabaseName() {
        return databaseName;
    }

    public void setDatabaseName(String databaseName) {
        this.databaseName = databaseName;
    }

    public String getSchemaJSON() {
        return schemaJSON;
    }

    public void setSchemaJSON(String schemaJSON) {
        this.schemaJSON = schemaJSON;
    }

    public String getCertificatePath() {
        return certificatePath;
    }

    public void setCertificatePath(String certificatePath) {
        this.certificatePath = certificatePath;
    }

    public String getCertificatePassword() {
        return certificatePassword;
    }

    public void setCertificatePassword(String certificatePassword) {
        this.certificatePassword = certificatePassword;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getAdminId() {
        return adminId;
    }

    public void setAdminId(String adminId) {
        this.adminId = adminId;
    }
}
