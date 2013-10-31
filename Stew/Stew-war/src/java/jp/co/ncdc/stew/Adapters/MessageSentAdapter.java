/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jp.co.ncdc.stew.Adapters;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import jp.co.ncdc.stew.Entities.MessageSent;
import jp.co.ncdc.stew.Managers.EventLogManager;
import jp.co.ncdc.stew.Utils.EntityManagerFactorUtils;
import jp.co.ncdc.stew.Utils.StewConstant;

/**
 * @name: MessageAdapter.java
 * @create: Aug 26, 2013 
 * @version 1.0
 * @brief: This class is MessageAdapter which connect to database and query data
 */
public class MessageSentAdapter {
    public MessageSentAdapter() {
    }
    
    /**
     * @brief: this function is add new message sent
     * @param messageSent
     * @return
     */
    public boolean addMessage(MessageSent messageSent) {
        boolean result = false;
        EntityManagerFactory entityManagerFactory = null;
        EntityManager entityManager = null;
        
        try
        {
            entityManagerFactory = Persistence.createEntityManagerFactory("JPA");   
            entityManager = entityManagerFactory.createEntityManager();
            entityManager.getTransaction().begin();
            
            entityManager.persist(messageSent);
            
            entityManager.getTransaction().commit();
            result = true;
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
     * @brief: this function is add new message sent
     * @param messageSent
     * @return
     */
    public long addMessageSent(MessageSent messageSent) {
        long result = -1;
        EntityManagerFactory entityManagerFactory = null;
        EntityManager entityManager = null;
        
        try
        {
            entityManagerFactory = Persistence.createEntityManagerFactory("JPA");   
            entityManager = entityManagerFactory.createEntityManager();
            entityManager.getTransaction().begin();
            
            entityManager.persist(messageSent);
            
            entityManager.getTransaction().commit();
            result = messageSent.getMessageSentId();
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
     * @brief: This function is update message sent with new information
     * @param messageSent
     * @return 
     */
    public boolean updateMessage(MessageSent messageSent) {
        boolean result = false;
        EntityManagerFactory entityManagerFactory = null;
        EntityManager entityManager = null;
        
        try
        {
            entityManagerFactory = Persistence.createEntityManagerFactory("JPA");   
            entityManager = entityManagerFactory.createEntityManager();
            entityManager.getTransaction().begin();
            
            entityManager.merge(messageSent);
            
            entityManager.getTransaction().commit();
            result = true;
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
     * @brief: This function is delete message sent by id
     * @param messageId
     * @return boolean
     */
    public boolean deleteMessageSent(Long messageId) {
        boolean result = false;
        EntityManagerFactory entityManagerFactory = null;
        EntityManager entityManager = null;
        
        try
        {
            entityManagerFactory = Persistence.createEntityManagerFactory("JPA");   
            entityManager = entityManagerFactory.createEntityManager();
            entityManager.getTransaction().begin();
            
            Query query = entityManager.createQuery("Delete from MessageSent where id =:id");       
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
            entityManagerFactory.close();
        }
        return result;
    }
    
    /**
     * @brief: This function is get message by messageSentID
     * @param messageSentId
     * @return 
     */
    public MessageSent getMessageSentByMessageSentID(Long messageSentId) {
        MessageSent messageResult = null;
        EntityManagerFactory entityManagerFactory = null;
        EntityManager entityManager = null;
        
        try {
            entityManagerFactory = Persistence.createEntityManagerFactory("JPA");   
            entityManager = entityManagerFactory.createEntityManager();
            entityManager.getTransaction().begin();
            
            Query query = entityManager.createQuery("from MessageSent where messageSentId =:messageSentId");
            query.setParameter("messageSentId", messageSentId);
            List queryResult = query.getResultList();
            
            if (queryResult != null && queryResult.size() > 0) {
                messageResult = (MessageSent)queryResult.get(0);
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
     * @brief: This function is get all messages sent
     * @return 
     */
    public List<MessageSent> getMessages(){
        EntityManagerFactory entityManagerFactory = null;
        EntityManager entityManager = null;
        List<MessageSent> messages = null;
        
        try{
            entityManagerFactory = Persistence.createEntityManagerFactory("JPA");   
            entityManager = entityManagerFactory.createEntityManager();
            entityManager.getTransaction().begin();
            List result = (entityManager.createQuery("from MessageSent").getResultList());
            messages = (List<MessageSent>)result;

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
     * @brief: This function is get all messages which are waiting to send
     * @return 
     */
    public List<MessageSent> getMessagesWaiting(){
        EntityManagerFactory entityManagerFactory = null;
        EntityManager entityManager = null;
        List<MessageSent> messages = new ArrayList<MessageSent>();
        
        try{
            entityManagerFactory = Persistence.createEntityManagerFactory("JPA");   
            entityManager = entityManagerFactory.createEntityManager();
            entityManager.getTransaction().begin();
            Query query = entityManager.createQuery("from MessageSent where status =:status");
            query.setParameter("status", StewConstant.WAITING_SEND);
            List result = query.getResultList();
            messages = (List<MessageSent>)result;

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
     * @brief: This function is get messageId by groupId     
     * @param groupId
     * @return 
     */
    public List<Long> getMessageId(Long groupId){
        //EntityManagerFactory entityManagerFactory = null;
        EntityManager entityManager = null;        
        List<Long> listMessageId = new ArrayList();
        
        try{
            entityManager =EntityManagerFactorUtils.getCurrentEntityManagerFactory().createEntityManager();
            //entityManagerFactory = Persistence.createEntityManagerFactory("JPA");   
            //entityManager = entityManagerFactory.createEntityManager();
            entityManager.getTransaction().begin();
            Query query = entityManager.createQuery("select distinct messageId from MessageSent where groupId =:groupId");           
            query.setParameter("groupId", groupId);            
            listMessageId = (List<Long>) query.getResultList();            
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            EventLogManager.getInstance().log(e.getMessage());
        }finally{
            entityManager.close();
            //entityManagerFactory.close();
        }
        
        return listMessageId;
    }
    
    /**
     * @brief: This function is get message sent depend on messageId,list sent to group, appId
     * @param messageId
     * @param lstGroupId
     * @param appId     
     * @param isSupperAdmin
     * @return 
     */
    public Map<String, MessageSent> getLogMessageSent(Long messageId, List<Long> lstGroupId, String appId, boolean isSupperAdmin){
        EntityManagerFactory entityManagerFactory = null;
        EntityManager entityManager = null;        
        Map<String, MessageSent> mapMessageSent = new HashMap<String, MessageSent>();
        List<MessageSent> lstMessageSent = new ArrayList<MessageSent>();
        
        try{
            entityManagerFactory = Persistence.createEntityManagerFactory("JPA");   
            entityManager = entityManagerFactory.createEntityManager();
            entityManager.getTransaction().begin();
            //get all messages sent by messageId
            if (isSupperAdmin) {
                Query query = entityManager.createQuery("from MessageSent where messageId =:messageId");                           
                query.setParameter("messageId", messageId);
                lstMessageSent = (List<MessageSent>) query.getResultList();
                //add messageSent to map
                for (MessageSent messageSent:lstMessageSent) {
                    if (messageSent != null)
                        mapMessageSent.put(messageSent.getUserId(), messageSent);
                }
            }
            //get message depend on groupId, appId & messageId
            else
            {
                for (Long groupId:lstGroupId) {
                    Query query = entityManager.createQuery("from MessageSent where groupId =:groupId and appId =:appId and messageId =:messageId");           
                    query.setParameter("groupId", groupId);            
                    query.setParameter("appId", appId);
                    query.setParameter("messageId", messageId);
                    lstMessageSent = (List<MessageSent>) query.getResultList();
                    //add messageSent to map
                    for (MessageSent messageSent:lstMessageSent) {
                        if (messageSent != null)
                            mapMessageSent.put(messageSent.getUserId(), messageSent);
                    }
                }                            
            }
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            EventLogManager.getInstance().log(e.getMessage());
        }finally{
            entityManager.close();
            entityManagerFactory.close();
        }
        
        return mapMessageSent;
    }
    
    /**
     * @brief: This function is get all messages which are waiting to send today by messageId
     * @param messageId
     * @return 
     */
    public List<MessageSent> getMessagesSendTodayById(Long messageId){
        EntityManagerFactory entityManagerFactory = null;
        EntityManager entityManager = null;
        List<MessageSent> messages = new ArrayList<MessageSent>();
        
        try{
            entityManagerFactory = Persistence.createEntityManagerFactory("JPA");   
            entityManager = entityManagerFactory.createEntityManager();
            entityManager.getTransaction().begin();            
            Query query = entityManager.createQuery("from MessageSent where messageId =:messageId and status =:status");
            query.setParameter("status", StewConstant.WAITING_SEND);
            query.setParameter("messageId", messageId);
            List result = query.getResultList();
            messages = (List<MessageSent>)result;

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
     * @brief: This function is get all messages which are waiting to send today by list messageId
     * @param lstMessageIds
     * @return 
     */
    public List<MessageSent> getMessagesSendToday(List<Long> lstMessageIds){
        List<MessageSent> lstMessageSents = new ArrayList<MessageSent>();
        for (Long messageId:lstMessageIds) {
            List<MessageSent> lstMessageSentTemp = getMessagesSendTodayById(messageId);
            lstMessageSents.addAll(lstMessageSentTemp);
        }            
        return lstMessageSents;
    }
    
    /**
     * @brief: This function is update messageSent status by messageId, appId, userId
     * @param status
     * @param messageId
     * @param appId
     * @param userId
     * @return 
     */
    public boolean updateMessageSentStatus(String status, long messageId, String appId, String userId) {
        boolean result = false;        
        EntityManager entityManager = null;

        try {
            entityManager =EntityManagerFactorUtils.getCurrentEntityManagerFactory().createEntityManager();            
            entityManager.getTransaction().begin();
            Query query = entityManager.createQuery("Update MessageSent Set status =:status Where appId=:appId and userId=:userId and messageId=:messageId");
            query.setParameter("status", status);
            query.setParameter("appId", appId);
            query.setParameter("userId", userId);
            query.setParameter("messageId", messageId);
            query.executeUpdate();

            entityManager.getTransaction().commit();
            result = true;
        } catch (Exception e) {
            EventLogManager.getInstance().log(e.getMessage());
        } finally {
            entityManager.close();            
        }
        return result;
    }
    
    /**
     * @brief: This function is get message sent depend on messageId,list sent to group, appId
     * @param messageId
     * @param lstGroupId
     * @param appId     
     * @param isSupperAdmin
     * @param status
     * @param keySearch
     * @return 
     */
    public Map<String, MessageSent> getLogMessageSentFilter(Long messageId, List<Long> lstGroupId, String appId, boolean isSupperAdmin, String status, String keySearch){        
        EntityManager entityManager = null;        
        Map<String, MessageSent> mapMessageSent = new HashMap<String, MessageSent>();
        List<MessageSent> lstMessageSent;
        
        try{
            entityManager =EntityManagerFactorUtils.getCurrentEntityManagerFactory().createEntityManager();            
            entityManager.getTransaction().begin();
            //get all messages sent by messageId
            List result;
            String statusCondition = "", keySearchCondition = "";
            if (!"".equals(status)) {
                statusCondition = " and status ='" + status + "'";
            }
            if (!"".equals(keySearch)) {
                keySearchCondition = " and userId like '%" + keySearch + "%'";
            }
            
            if (isSupperAdmin) {
                Query query = entityManager.createQuery("from MessageSent where messageId =:messageId" + statusCondition + keySearchCondition);                
                query.setParameter("messageId", messageId);
                result = query.getResultList();                                   
                lstMessageSent = (List<MessageSent>) result;
                //add messageSent to map
                for (MessageSent messageSent:lstMessageSent) {
                    if (messageSent != null)
                        mapMessageSent.put(messageSent.getUserId(), messageSent);
                }
            }
            //get message depend on groupId, appId & messageId
            else
            {                                
                for (Long groupId:lstGroupId) {
                    Query query = entityManager.createQuery("from MessageSent where groupId =:groupId and appId =:appId and messageId =:messageId" + statusCondition + keySearchCondition);           
                    query.setParameter("groupId", groupId);            
                    query.setParameter("appId", appId);
                    query.setParameter("messageId", messageId);
                    lstMessageSent = (List<MessageSent>) query.getResultList();
                    //add messageSent to map
                    for (MessageSent messageSent:lstMessageSent) {
                        if (messageSent != null)
                            mapMessageSent.put(messageSent.getUserId(), messageSent);
                    }
                }                                
            }
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            EventLogManager.getInstance().log(e.getMessage());
        }finally{
            entityManager.close();
        }        
        return mapMessageSent;
    }    
}
