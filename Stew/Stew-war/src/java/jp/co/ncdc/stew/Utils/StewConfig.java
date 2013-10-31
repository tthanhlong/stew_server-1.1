/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jp.co.ncdc.stew.Utils;

import java.io.IOException;
import java.util.Properties;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

/**
 *
 * @author vcnduong
 */
public class StewConfig {

    public int defaulTokenAge;
    public int numberOfPushNotifyService;
    public static StewConfig instance;
    public String logLevel;
    public String languageDefault;
    public String[] languageSupports;
    public String APNSCertificatePath;
    public String APNSCertificatePassWord;
    public String urlServer;
    public String usernameServer;
    public String passwordServer;

    /**
     * singleton for stew config
     *
     * @author vcnduong
     * @return
     */
    public static StewConfig getInstance() {
        if (instance == null) {
            instance = new StewConfig();
        }
        return instance;
    }

    /**
     * Load all configuration form stew.properties
     *
     * @author vcnduong
     */
    private StewConfig() {
        Properties prop = new Properties();
        try {
            prop.load(this.getClass().getClassLoader().getResourceAsStream("stew.properties"));
            defaulTokenAge = Integer.parseInt(prop.getProperty("stew.default.token.age"));
            numberOfPushNotifyService = Integer.parseInt(prop.getProperty("stew.numberOfPushNotifyService"));
            logLevel = prop.getProperty("stew.log.level");
            
            //MultiLanguage
            languageSupports = prop.getProperty("languageSupports").split(",");
            languageDefault = prop.getProperty("languageDefault");
            
            //apple push notification certificate
            APNSCertificatePath = prop.getProperty("stew.certificate.path");
            APNSCertificatePassWord = prop.getProperty("stew.certificate.password");
            urlServer = prop.getProperty("stew.url");
            usernameServer = prop.getProperty("stew.username");
            passwordServer = prop.getProperty("stew.password");
        } catch (IOException ex) {
            Logger.getLogger(StewConfig.class.getName()).log(Level.ERROR, null, ex);
        }
    }
}
