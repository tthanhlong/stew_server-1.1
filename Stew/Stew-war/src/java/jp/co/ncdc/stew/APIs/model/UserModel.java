/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jp.co.ncdc.stew.APIs.model;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import jp.co.ncdc.stew.Entities.User;

/**
 *
 * @author tthanhlong
 */
public class UserModel {
    private Long userGroupDetailId;
    private String email;
    private String groupName;
    private String roleName;
    private String firstName;
    private String lastName;
    private Date dateCreated;

    public UserModel() {
    }

    public UserModel(Long userGroupDetailId, String email, String groupName, String roleName, String firstName, String lastName, Date dateCreated) {
        this.userGroupDetailId = userGroupDetailId;
        this.email = email;
        this.groupName = groupName;
        this.roleName = roleName;
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateCreated = dateCreated;
    }

    public Long getUserGroupDetailId() {
        return userGroupDetailId;
    }

    public void setUserGroupDetailId(Long userGroupDetailId) {
        this.userGroupDetailId = userGroupDetailId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }
    
    public String getDateCreatedString() {
        DateFormat df = new SimpleDateFormat("yyyy.MM.dd");
        return df.format(this.dateCreated);
    }
}
