/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jp.co.ncdc.stew.APIs.model;

/**
 *
 * @author vcnduong
 */
public class Authentication {

    public String status;
    public String description;
    public String userTokens;

    public Authentication() {
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUserTokens() {
        return userTokens;
    }

    public void setUserTokens(String userTokens) {
        this.userTokens = userTokens;
    }
    
}
