/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jp.co.ncdc.stew.Servlet.model;

import java.util.List;
import jp.co.ncdc.stew.Entities.User;
import jp.co.ncdc.stew.Utils.StewConstant;
import jp.co.ncdc.stew.Utils.StewUtils;

/**
 *
 * @author vcnduong
 */
public class UserAndRole {

    private String email;
    private String roleName;
    private String firstName;
    private String lastName;
    private String createDate;

    public UserAndRole() {
    }

    public UserAndRole(User user, List<String> listRole) {
        this.email = user.getEmail();
        this.firstName = user.getFirstName() != null ? user.getFirstName() : "";
        this.lastName = user.getLastName() != null ? user.getLastName() : "";
        this.createDate = StewUtils.convertDateToString(user.getCreateDate(), StewConstant.DATE_FORMAT);
        this.roleName = StewUtils.convertListStringToString(listRole);
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }
}
