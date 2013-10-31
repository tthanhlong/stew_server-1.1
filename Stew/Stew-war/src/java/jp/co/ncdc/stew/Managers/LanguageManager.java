/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jp.co.ncdc.stew.Managers;

import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;
import javax.servlet.http.HttpServletRequest;
import jp.co.ncdc.stew.Utils.StewConfig;

/**
 * @Class Name: LanguageManager.java 
 * @created: Jan 2, 2013 
 * @version: Beta 
 * @brief: LanguageManager for control multi language 
 */
public class LanguageManager {
    private static LanguageManager instance =null;
    private static String languageID="_languageIDSession";
    public static  ResourceBundle language = null;
   
    private List<ResourceBundle> languageContents;

     /**
     * @brief: new instance for language resource 
     * @return 
     */
    public static LanguageManager getInstance() {
       if (instance == null) {
            instance = new LanguageManager();
        }
        return instance;
    }
    /**
     * @brief: load language of user login
     */
    public  LanguageManager(){
       loadLanguageContents();
    }

    /**
     * @brief: update current language's user 
     * @param lowerLanguageCode
     * @param uperLanguageCode 
     */
    public static void setCurentLanguage(String lowerLanguageCode, String uperLanguageCode) {
       //TODO      
    }
    
     public static void setCurentLanguage(String lowerLanguageCode,HttpServletRequest request) {
         request.getSession().setAttribute(languageID, lowerLanguageCode);
    }
     
     /**
      * 
      * @param key
      * @param request
      * @return 
      */
    public String getText(String key,HttpServletRequest request)
    {
        String textLanguage="";
        String languageId=null;
        try {
             languageId=request.getSession().getAttribute(languageID).toString();
        } catch (Exception e) {
             EventLogManager.getInstance().log(e.getMessage());
        }
        try {
            if(languageId==null)
            languageId= StewConfig.getInstance().languageDefault;
        
            for(ResourceBundle language:languageContents)
            {
                if(language.getString("_languageID").equals(languageId))
                    textLanguage= language.getString(key);
            }
            
        } catch (Exception e) {
            EventLogManager.getInstance().log(e.getMessage());            
        }
        return textLanguage;
    }  
    
    
         /**
      * 
      * @param key
      * @param languageId
      * @return 
      */
    public String getText(String key, String languageId)
    {
        String textLanguage="";
        try {
            if(languageId==null)
               languageId=StewConfig.getInstance().languageDefault;
        
            for(ResourceBundle language:languageContents)
            {
                if(language.getString("_languageID").equals(languageId))
                    textLanguage= language.getString(key);
            }
            
        } catch (Exception e) {
            EventLogManager.getInstance().log(e.getMessage());            
        }
        return textLanguage;
    } 
    
    /**
     * loadLanguageContents
     */
    private  void loadLanguageContents()
    {
        try {             
             String[]languageSuport=StewConfig.getInstance().languageSupports;
             languageContents=new LinkedList<ResourceBundle>();
             System.out.println("languageContents count:"+languageContents.size());
             for(String languageID:languageSuport)
             {
                ResourceBundle languageItem = ResourceBundle.getBundle("MessagesBundle_"+languageID);
                languageContents.add(languageItem);
             }
            
        } catch (Exception e) {
            EventLogManager.getInstance().log(e.getMessage());
        }
    }
    
}