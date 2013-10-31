/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jp.co.ncdc.stew.Servlet.model;

/**
 *
 * @author vcnduong
 */
public class Status {

    private int status;
    private String errCode;

    public Status() {
    }

    public Status(int status, String errCode) {
        this.status = status;
        this.errCode = errCode;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getErrCode() {
        return errCode;
    }

    public void setErrCode(String errCode) {
        this.errCode = errCode;
    }
}
