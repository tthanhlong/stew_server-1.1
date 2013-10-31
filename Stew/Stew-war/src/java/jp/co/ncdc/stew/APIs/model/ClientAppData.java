/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jp.co.ncdc.stew.APIs.model;

import java.util.List;

/**
 *
 * @author tthanhlong
 */
public class ClientAppData {
    private String token;
    private List<AppTable> tables;

    public ClientAppData() {
    }

    public ClientAppData(String token, List<AppTable> tables) {
        this.token = token;
        this.tables = tables;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public List<AppTable> getTables() {
        return tables;
    }

    public void setTables(List<AppTable> tables) {
        this.tables = tables;
    }
    
}
