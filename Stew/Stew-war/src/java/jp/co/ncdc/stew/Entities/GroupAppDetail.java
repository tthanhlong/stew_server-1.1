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
public class GroupAppDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long groupAppId;
    private String appId;
    private Long groupId;

    public GroupAppDetail() {
    }

    public GroupAppDetail(String appId, Long groupId) {
        this.appId = appId;
        this.groupId = groupId;
    }

    public Long getGroupAppId() {
        return groupAppId;
    }

    public void setGroupAppId(Long groupAppId) {
        this.groupAppId = groupAppId;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }
}
