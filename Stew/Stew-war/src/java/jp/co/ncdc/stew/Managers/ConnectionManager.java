/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jp.co.ncdc.stew.Managers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import jp.co.ncdc.stew.Utils.StewConfig;

/**
 *
 * @author tthanhlong
 */
public class ConnectionManager {
    Connection connection = null;
    
    public Connection createConnection() throws ClassNotFoundException{
        
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection(StewConfig.getInstance().urlServer, StewConfig.getInstance().usernameServer, StewConfig.getInstance().passwordServer);
        } catch (SQLException ex) {
            Logger.getLogger(ConnectionManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return connection;
    }
    
    public void closeConnection(){
        try {
            connection.close();
        } catch (SQLException ex) {
            Logger.getLogger(ConnectionManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public Connection createConnection(String databaseName) throws ClassNotFoundException{
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection(StewConfig.getInstance().urlServer + "/" + databaseName, StewConfig.getInstance().usernameServer, StewConfig.getInstance().passwordServer);
        } catch (SQLException ex) {
            Logger.getLogger(ConnectionManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return connection;
    }
}
