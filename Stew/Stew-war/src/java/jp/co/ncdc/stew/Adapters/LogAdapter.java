/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jp.co.ncdc.stew.Adapters;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import jp.co.ncdc.stew.APIs.model.LogModel;
import jp.co.ncdc.stew.Entities.Log;
import jp.co.ncdc.stew.Managers.EventLogManager;
import jp.co.ncdc.stew.Utils.EntityManagerFactorUtils;
import jp.co.ncdc.stew.Utils.StewConstant;

/**
 * @name: LogAdapter.java
 * @create: Aug 21, 2013 
 * @version 1.0
 * @brief: This class is LogAdapter which connect to database and query data
 */
public class LogAdapter {
    /**
     * @brief create log
     * @param user
     * @return
     */
    public boolean addLog(Log log) {
        boolean result = false;
        EntityManagerFactory entityManagerFactory = null;
        EntityManager entityManager = null;
        
        try
        {
            entityManagerFactory = Persistence.createEntityManagerFactory("JPA");   
            entityManager = entityManagerFactory.createEntityManager();
            entityManager.getTransaction().begin();
            
            entityManager.persist(log);
            
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

    public List<Log> getAllLogs() {
        EntityManagerFactory entityManagerFactory = null;
        EntityManager entityManager = null;
        List<Log> lstLogs = null;

        try {
            entityManagerFactory = Persistence.createEntityManagerFactory("JPA");
            entityManager = entityManagerFactory.createEntityManager();
            entityManager.getTransaction().begin();
            List result = (entityManager.createQuery("from Log").getResultList());
            lstLogs = (List<Log>) result;

            entityManager.getTransaction().commit();
        } catch (Exception e) {
            EventLogManager.getInstance().log(e.getMessage());
        } finally {
            entityManager.close();
            entityManagerFactory.close();
        }
        return lstLogs;
    }

    public List<Log> getLogByAppID(String appID) {
        EntityManagerFactory entityManagerFactory = null;
        EntityManager entityManager = null;
        List<Log> lstLogs = null;
        try {
            entityManagerFactory = Persistence.createEntityManagerFactory("JPA");
            entityManager = entityManagerFactory.createEntityManager();
            entityManager.getTransaction().begin();

            Query query = entityManager.createQuery("from Log where appId =:appId");
            query.setParameter("appId", appID);
            lstLogs = (List<Log>) query.getResultList();
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            EventLogManager.getInstance().log(e.getMessage());
        } finally {
            entityManager.close();
            entityManagerFactory.close();
        }

        return lstLogs;
    }
    
    public List<LogModel> getLogsFilter(String userId, boolean isSupperAdmin, String appId, String level, String fromDateString, String toDateString, String keySearch) {
        List<LogModel> lstLogs = new ArrayList<LogModel>();
        EntityManager entityManager = null;        
        String levelCondition = "", appIdCondition = "", fromDateCondition = "", toDateCondition = "", keySearchCondition = "";                
        if (!"".equals(appId))
            appIdCondition = " and A.appId='" + appId + "'";
        if (!"".equals(level))
            levelCondition = " and L.type='" + level + "'";
        if (!"".equals(fromDateString))
            fromDateCondition = " and L.timeLog>='" + fromDateString + "'"; 
        if (!"".equals(toDateString))
            toDateCondition = " and L.timeLog<='" + toDateString + "'"; 
        if (!"".equals(keySearch)) {
            keySearchCondition = " and L.messageLog like '%" + keySearch + "%'";
        }
        try {
            entityManager =EntityManagerFactorUtils.getCurrentEntityManagerFactory().createEntityManager();
            entityManager.getTransaction().begin();                
            Query query;
            if (isSupperAdmin)                    
                query = entityManager.createQuery("select distinct L.id, A.appName, L.userId, L.type, L.messageLog, L.timeLog from Log L, Apps A where L.appId=A.appId" + appIdCondition + levelCondition + fromDateCondition + toDateCondition + keySearchCondition);
            else
                query = entityManager.createQuery("select distinct L.id, A.appName, L.userId, L.type, L.messageLog, L.timeLog from Log L, Apps A, UserGroupDetail U, GroupAppDetail GA where L.appId=A.appId and U.userId='" + userId + "' and U.roleId<=" + StewConstant.ROLE_MANAGER + " and GA.groupId=U.groupId and GA.appId=A.appId" + appIdCondition + levelCondition + fromDateCondition + toDateCondition + keySearchCondition);    

            List queryResult = query.getResultList();
            lstLogs = (List<LogModel>)queryResult;
            entityManager.getTransaction().commit();
        }catch (Exception e) {
            EventLogManager.getInstance().log(e.getMessage());
        } finally {
            entityManager.close();
        }        
        return lstLogs;
    }
    
    public Log getLogByLogId(Long logId) {        
        EntityManager entityManager = null;
        Log log = null;
        try {
            entityManager =EntityManagerFactorUtils.getCurrentEntityManagerFactory().createEntityManager();            
            entityManager.getTransaction().begin();

            Query query = entityManager.createQuery("from Log where id =:logId");
            query.setParameter("logId", logId);
            log = (Log) query.getSingleResult();
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            EventLogManager.getInstance().log(e.getMessage());
        } finally {
            entityManager.close();            
        }
        return log;
    }
}
