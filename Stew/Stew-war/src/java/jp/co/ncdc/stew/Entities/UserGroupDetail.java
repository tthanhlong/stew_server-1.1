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
public class UserGroupDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userGroupid;
    private String userId;
    private Long groupId;
    private Long roleId;

    public UserGroupDetail(Long groupId, String userId, Long roleId) {
        this.groupId = groupId;
        this.userId = userId;
        this.roleId = roleId;
    }

    public UserGroupDetail() {
    }

    public Long getUserGroupid() {
        return userGroupid;
    }

    public void setUserGroupid(Long userGroupid) {
        this.userGroupid = userGroupid;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }
}
