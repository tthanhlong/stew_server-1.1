/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jp.co.ncdc.stew.Entities;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Temporal;
import jp.co.ncdc.stew.Utils.StewConstant;

/**
 *
 * @author vcnduong
 */
@Entity
public class User {
    @Id
    private String email;
    private String passwordHash;
    private String firstName;
    private String lastName;
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date createDate;

    public User(String passwordHash, String email) {
        this.passwordHash = passwordHash;
        this.email = email;
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

    public User(String email, String passwordHash, String firstName, String lastName, Date createDate) {
        this.email = email;
        this.passwordHash = passwordHash;
        this.firstName = firstName;
        this.lastName = lastName;
        this.createDate = createDate;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public User() {
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCreateDateString() {
        DateFormat df = new SimpleDateFormat(StewConstant.DATE_FORMAT);
        return df.format(this.createDate);
    }
}
