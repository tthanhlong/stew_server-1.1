/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jp.co.ncdc.stew.Adapters;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import jp.co.ncdc.stew.Entities.Message;
import jp.co.ncdc.stew.Managers.EventLogManager;
import jp.co.ncdc.stew.Utils.EntityManagerFactorUtils;

/**
 *
 * @author tquangthai
 */
public class MessageAdapter {
    
    public MessageAdapter() {
    }
    
    /**
     * @brief: this function is add new message to message table
     * @param message
     * @return -1 if insert fail
     * @return messageId if insert success
     */
    public long addMessage(Message message) {
        long result = -1;
        EntityManagerFactory entityManagerFactory = null;
        EntityManager entityManager = null;
        
        try
        {
            entityManagerFactory = Persistence.createEntityManagerFactory("JPA");   
            entityManager = entityManagerFactory.createEntityManager();
            entityManager.getTransaction().begin();
            
            entityManager.persist(message);
            
            entityManager.getTransaction().commit();
            result = message.getMessageId();
        }
        catch (Exception e)
        {
            EventLogManager.getInstance().log(e.getMessage());
        }
        finally{
            entityManager.close(); 
            entityManagerFactory.close();
        }
        return result;
    }
    
    /**
     * @brief: This function is get message by messageID
     * @param messageId
     * @return 
     */
    public Message getMessageByMessageID(Long messageId) {
        Message messageResult = null;
        EntityManagerFactory entityManagerFactory = null;
        EntityManager entityManager = null;
        
        try {
            entityManagerFactory = Persistence.createEntityManagerFactory("JPA");   
            entityManager = entityManagerFactory.createEntityManager();
            entityManager.getTransaction().begin();
            
            Query query = entityManager.createQuery("from Message where id =:id");
            query.setParameter("id", messageId);
            List queryResult = query.getResultList();
            
            if (queryResult != null && queryResult.size() > 0) {
                messageResult = (Message)queryResult.get(0);
            }

            entityManager.getTransaction().commit();
        } catch (Exception e) {
            EventLogManager.getInstance().log(e.getMessage());
        }finally{
            entityManager.close();  
            entityManagerFactory.close();
        }
        
        return messageResult;
    } 
    
    /**
     * @brief: This function is get message by message title
     * @param title
     * @return 
     */
    public Message getMessageByTitle(String title) {
        Message messageResult = null;
        EntityManagerFactory entityManagerFactory = null;
        EntityManager entityManager = null;
        
        try {
            entityManagerFactory = Persistence.createEntityManagerFactory("JPA");   
            entityManager = entityManagerFactory.createEntityManager();
            entityManager.getTransaction().begin();
            
            Query query = entityManager.createQuery("from Message where title =:title");
            query.setParameter("title", title);
            List queryResult = query.getResultList();
            
            if (queryResult != null && queryResult.size() > 0) {
                messageResult = (Message)queryResult.get(0);
            }

            entityManager.getTransaction().commit();
        } catch (Exception e) {
            EventLogManager.getInstance().log(e.getMessage());
        }finally{
            entityManager.close();  
            entityManagerFactory.close();
        }
        
        return messageResult;
    } 
    
    /**
     * @brief: This function is get all messages
     * @return 
     */
    public List<Message> getMessages(){
        EntityManagerFactory entityManagerFactory = null;
        EntityManager entityManager = null;
        List<Message> messages = new ArrayList<Message>();
        
        try{
            entityManagerFactory = Persistence.createEntityManagerFactory("JPA");   
            entityManager = entityManagerFactory.createEntityManager();
            entityManager.getTransaction().begin();
            List result = (entityManager.createQuery("from Message").getResultList());
            messages = (List<Message>)result;

            entityManager.getTransaction().commit();
        } catch (Exception e) {
            EventLogManager.getInstance().log(e.getMessage());
        }finally{
            entityManager.close();
            entityManagerFactory.close();
        }
        
        return messages;
    }
    
    /**
     * @brief: This function is get messages depend on user role
     * @return 
     */
    public List<Message> getMessages(String userId, boolean isSupperAdmin) {
        List<Message> lstMessages = new ArrayList<Message>();        
        
        if (isSupperAdmin) {
            lstMessages = getMessages();
        }else {            
            EntityManagerFactory entityManagerFactory = null;
            EntityManager entityManager = null;        
            UserGroupDetailAdapter userGroup = new UserGroupDetailAdapter();
            MessageSentAdapter messageSent = new MessageSentAdapter();
            List<Long> lstGroupId = userGroup.getGroupByManagerId(userId);
            List<Long> lstMessageId = new ArrayList<Long>();
            //get list messageId depend on list sent groupId
            for (Long groupId:lstGroupId) {
                List<Long> lstMessageIdTemp = messageSent.getMessageId(groupId);
                for (Long messageId:lstMessageIdTemp) {
                    if (!lstMessageId.contains(messageId))
                        lstMessageId.add(messageId);
                }                
            }       

            try{
                entityManagerFactory = Persistence.createEntityManagerFactory("JPA");   
                entityManager = entityManagerFactory.createEntityManager();
                entityManager.getTransaction().begin();
                //get messages by list messageId
                for (Long messageId:lstMessageId) {                                
                    Query query = entityManager.createQuery("from Message where messageId =:messageId");
                    query.setParameter("messageId", messageId);
                    Message message = (Message) query.getSingleResult();
                    if (message != null)
                        lstMessages.add(message);
                }
                entityManager.getTransaction().commit();
            } catch (Exception e) {
                EventLogManager.getInstance().log(e.getMessage());
            }finally{
                entityManager.close();
                entityManagerFactory.close();
            }
        }
        
        return lstMessages;
    }
    
    /**
     * @brief: This function is get all messages with filter keyword
     * @return 
     */
    public List<Message> getMessagesFilter(String appId, String templateName, String keySearch){
        //EntityManagerFactory entityManagerFactory = null;
        EntityManager entityManager = null;
        List<Message> messages = new ArrayList<Message>();
        
        try{
            //entityManagerFactory = Persistence.createEntityManagerFactory("JPA");
            //entityManager = entityManagerFactory.createEntityManager();
            entityManager =EntityManagerFactorUtils.getCurrentEntityManagerFactory().createEntityManager();
            entityManager.getTransaction().begin();
            List result; 
            if ("".equals(appId) && "".equals(templateName) && "".equals(keySearch))
                result = (entityManager.createQuery("from Message").getResultList());
            else {
                String queryString = "from Message where 1=1";
                if (!"".equals(appId)) {
                    queryString += " and sentToApp ='" + appId + "'";
                }
                if (!"".equals(templateName)) {
                    queryString += " and title ='" + templateName + "'";
                }
                if (!"".equals(keySearch)) {
                    queryString += " and message like '%" + keySearch + "%'";
                }
                
                Query query = entityManager.createQuery(queryString);
                result = query.getResultList();                
            }
            messages = (List<Message>)result;
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            EventLogManager.getInstance().log(e.getMessage());
        }finally{
            entityManager.close();
            //entityManagerFactory.close();
        }
        
        return messages;
    }
    
    /**
     * @brief: This function is get messages depend on user role
     * @return 
     */
    public List<Message> getMessagesFilter(String userId, boolean isSupperAdmin, String appId, String templateName, String keySearch) {
        List<Message> lstMessages = new ArrayList<Message>();        
        
        if (isSupperAdmin) {
            lstMessages = getMessagesFilter(appId, templateName, keySearch);
        }else {            
            //EntityManagerFactory entityManagerFactory = null;
            EntityManager entityManager = null;        
            UserGroupDetailAdapter userGroup = new UserGroupDetailAdapter();
            MessageSentAdapter messageSent = new MessageSentAdapter();
            List<Long> lstGroupId = userGroup.getGroupByManagerId(userId);
            List<Long> lstMessageId = new ArrayList<Long>();
            //get list messageId depend on list sent groupId
            for (Long groupId:lstGroupId) {
                List<Long> lstMessageIdTemp = messageSent.getMessageId(groupId);
                for (Long messageId:lstMessageIdTemp) {
                    if (!lstMessageId.contains(messageId))
                        lstMessageId.add(messageId);
                }                
            }       

            try{
                String appIdCondition = "", templateNameCondition = "", keySearchCondition = "";                
                if (!"".equals(appId))
                    appIdCondition = " and sentToApp='" + appId + "'";                
                if (!"".equals(templateName)) {                        
                    templateNameCondition = " AND title='" + templateName + "'";                    
                }
                if (!"".equals(keySearch)) {
                   keySearchCondition = " AND message like '%" + keySearch + "%'";
                }                
                entityManager =EntityManagerFactorUtils.getCurrentEntityManagerFactory().createEntityManager();
                entityManager.getTransaction().begin();
                //get messages by list messageId
                for (Long messageId:lstMessageId) {         
                    String queryString = "from Message where messageId ='" + messageId + "'" + appIdCondition + templateNameCondition + keySearchCondition;
                    Query query = entityManager.createQuery(queryString);                                                            
                    Message message = (Message) query.getSingleResult();
                    if (message != null)
                        lstMessages.add(message);
                }
                entityManager.getTransaction().commit();
            } catch (Exception e) {
                EventLogManager.getInstance().log(e.getMessage());
            }finally{
                entityManager.close();
                //entityManagerFactory.close();
            }
        }
        
        return lstMessages;
    }
    
}
