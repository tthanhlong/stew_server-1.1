/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jp.co.ncdc.stew.Adapters;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import jp.co.ncdc.stew.Entities.MessageTemplate;
import jp.co.ncdc.stew.Managers.EventLogManager;
import jp.co.ncdc.stew.Utils.EntityManagerFactorUtils;

/**
 * @name: MessageTemplateAdapter.java
 * @create: Aug 26, 2013 
 * @version 1.0
 * @brief: This class is MessageTemplateAdapter which connect to database and query data
 */
public class MessageTemplateAdapter {
    public MessageTemplateAdapter() {
    }
    
    /**
     * @brief: this function is add new message
     * @param message
     * @return
     */
    public boolean addMessage(MessageTemplate message) {
        boolean result = false;        
        EntityManager entityManager = null;
        
        try
        {
            entityManager =EntityManagerFactorUtils.getCurrentEntityManagerFactory().createEntityManager();            
            entityManager.getTransaction().begin();
            
            entityManager.persist(message);
            
            entityManager.getTransaction().commit();
            result = true;
        }
        catch (Exception e)
        {
            EventLogManager.getInstance().log(e.getMessage());
        }
        finally{
            entityManager.close();             
        }
        return result;
    }   
    
    /**
     * @brief: This function is update message with new information
     * @param message
     * @return 
     */
    public boolean updateMessage(MessageTemplate message) {
        boolean result = false;        
        EntityManager entityManager = null;
        
        try
        {
            entityManager =EntityManagerFactorUtils.getCurrentEntityManagerFactory().createEntityManager();            
            entityManager.getTransaction().begin();
            
            entityManager.merge(message);
            
            entityManager.getTransaction().commit();
            result = true;
        }
        catch (Exception e)
        {
            EventLogManager.getInstance().log(e.getMessage());
        }
        finally{
            entityManager.close(); 
        }
        return result;
    }
    
    /**
     * @brief: This function is delete message by message id
     * @param messageId
     * @return boolean
     */
    public boolean deleteMessage(Long messageId) {
        boolean result = false;
        EntityManager entityManager = null;
        
        try
        {
            entityManager =EntityManagerFactorUtils.getCurrentEntityManagerFactory().createEntityManager();
            entityManager.getTransaction().begin();
            
            Query query = entityManager.createQuery("Delete from MessageTemplate where id =:id");       
            query.setParameter("id", messageId);            
            query.executeUpdate();
            
            entityManager.getTransaction().commit();
            result = true;
        }
        catch (Exception e)
        {
            EventLogManager.getInstance().log(e.getMessage());
        }
        finally{
            entityManager.close();             
        }
        return result;
    }
    
    /**
     * @brief: This function is get message by messageID
     * @param messageId
     * @return 
     */
    public MessageTemplate getMessageByMessageID(Long messageId) {
        MessageTemplate messageResult = null;
        EntityManager entityManager = null;
        
        try {
            entityManager =EntityManagerFactorUtils.getCurrentEntityManagerFactory().createEntityManager();
            entityManager.getTransaction().begin();            
            Query query = entityManager.createQuery("from MessageTemplate where id =:id");
            query.setParameter("id", messageId);
            List queryResult = query.getResultList();
            
            if (queryResult != null && queryResult.size() > 0) {
                messageResult = (MessageTemplate)queryResult.get(0);
            }

            entityManager.getTransaction().commit();
        } catch (Exception e) {
            EventLogManager.getInstance().log(e.getMessage());
        }finally{
            entityManager.close();  
        }
        
        return messageResult;
    } 
    
    /**
     * @brief: This function is get message by message title
     * @param title
     * @return 
     */
    public MessageTemplate getMessageByTitle(String title) {
        MessageTemplate messageResult = null;        
        EntityManager entityManager = null;
        
        try {
            entityManager =EntityManagerFactorUtils.getCurrentEntityManagerFactory().createEntityManager();
            entityManager.getTransaction().begin();
            
            Query query = entityManager.createQuery("from MessageTemplate where title =:title");
            query.setParameter("title", title);
            List queryResult = query.getResultList();
            
            if (queryResult != null && queryResult.size() > 0) {
                messageResult = (MessageTemplate)queryResult.get(0);
            }

            entityManager.getTransaction().commit();
        } catch (Exception e) {
            EventLogManager.getInstance().log(e.getMessage());
        }finally{
            entityManager.close();              
        }
        
        return messageResult;
    } 
    
    /**
     * @brief: This function is get all messages
     * @return 
     */
    public List<MessageTemplate> getMessages(){
        EntityManager entityManager = null;
        List<MessageTemplate> messages = new ArrayList<MessageTemplate>();
        
        try{
            entityManager =EntityManagerFactorUtils.getCurrentEntityManagerFactory().createEntityManager();
            entityManager.getTransaction().begin();
            List result = (entityManager.createQuery("from MessageTemplate").getResultList());
            messages = (List<MessageTemplate>)result;

            entityManager.getTransaction().commit();
        } catch (Exception e) {
            EventLogManager.getInstance().log(e.getMessage());
        }finally{
            entityManager.close();            
        }
        
        return messages;
    }    
}
