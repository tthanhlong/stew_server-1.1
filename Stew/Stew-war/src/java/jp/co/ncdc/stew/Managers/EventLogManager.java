/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jp.co.ncdc.stew.Managers;

import jp.co.ncdc.stew.Utils.StewConfig;
import org.apache.log4j.*;
/**
 * @Class Name: EventLogManager.java
 * @version: Beta  
 * @brief: This class is implementation for managing event log.
 */
public class EventLogManager {
    private static EventLogManager instance = null;
    
    private org.apache.log4j.Logger log = Logger.getRootLogger();
    
    public static EventLogManager getInstance() {
        if (instance == null) {
            instance = new EventLogManager();
        }
        return instance;
    }
    
   /**
     * @brief: This method is used to get current log level.
     * @return Level
     */
   public Level getCurrentLevel(){
       return log.getLevel();
   }
   
   /**
    * @brief: This method is used to set log level.
    * @param level 
    */
   public void setLevel(Level level){
       log.setLevel(level);
   }
   
   /**
    * @brief: This method is used to log message to file.
    * @param message 
    */
   public void log(String message){
      String strLevel = StewConfig.getInstance().logLevel;
      setLevel(Level.toLevel(strLevel));
      Level level = getCurrentLevel();
      if (level == Level.DEBUG){
          log.debug(message);
      }
      if (level == Level.ERROR){
          log.error(message);
      }
      if (level == Level.FATAL){
          log.fatal(message);
      }
      if (level == Level.INFO){
          log.info(message);
      }
      if (level == Level.TRACE){
          log.trace(message);
      }
      if (level == Level.WARN){
          log.warn(message);
      }
      if (level == Level.ALL){
          log.trace(message);
          log.debug(message);
          log.info(message);
          log.warn(message);
          log.error(message);
          log.fatal(message);
      }
   }
}
