/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jp.co.ncdc.stew.APIs.model;

/**
 *
 * @author vcnduong
 */
public class Status {

    public String status;
    public String description;
    public String result;

    public void setResult(String result) {
        this.result = result;
    }

    public Status(String status, String description) {
        this.status = status;
        this.description = description;
    }

    public String getResult() {
        return result;
    }
    public Status() {
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
}
