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
import jp.co.ncdc.stew.Entities.Apps;
import jp.co.ncdc.stew.Entities.Log;
import jp.co.ncdc.stew.Entities.User;
import jp.co.ncdc.stew.Entities.UserApp;
import jp.co.ncdc.stew.Managers.EventLogManager;

/**
 *
 * @author vcnduong
 */
public class UserAppAdapter {

    /**
     * get user list by appId
     * @param appId
     * @return 
     */
    public List<User> getAllUserByAppId(String appId) {
        EntityManagerFactory entityManagerFactory = null;
        EntityManager entityManager = null;
        List<User> listUsers = new ArrayList<User>();
        try {
            entityManagerFactory = Persistence.createEntityManagerFactory("JPA");
            entityManager = entityManagerFactory.createEntityManager();
            entityManager.getTransaction().begin();
            String queryString = "select distinct(u.userId) from UserApp u where appId ='" + appId + "')";
            Query query = entityManager.createQuery(queryString);
            listUsers = (List<User>) query.getResultList();

            entityManager.getTransaction().commit();
        } catch (Exception e) {
            EventLogManager.getInstance().log(e.getMessage());
        } finally {
            entityManager.close();
            entityManagerFactory.close();
        }

        return listUsers;

    }

    /**
     * get listApps by userId
     * @param userId
     * @return 
     */
    public List<Apps> getAllAppByUserId(String userId) {
        EntityManagerFactory entityManagerFactory = null;
        EntityManager entityManager = null;
        List<Apps> listApps = new ArrayList<Apps>();
        try {
            entityManagerFactory = Persistence.createEntityManagerFactory("JPA");
            entityManager = entityManagerFactory.createEntityManager();
            entityManager.getTransaction().begin();
            String queryString = "select distinct(u.appId) from UserApp u where userId ='" + userId + "')";
            Query query = entityManager.createQuery(queryString);
            listApps = (List<Apps>) query.getResultList();

            entityManager.getTransaction().commit();
        } catch (Exception e) {
            EventLogManager.getInstance().log(e.getMessage());
        } finally {
            entityManager.close();
            entityManagerFactory.close();
        }
        return listApps;
    }
    
     public List<UserApp> getAllUserAppByAppId(String appId) {
        EntityManagerFactory entityManagerFactory = null;
        EntityManager entityManager = null;
        List<UserApp> listUserApp = new ArrayList<UserApp>();
        try {
            entityManagerFactory = Persistence.createEntityManagerFactory("JPA");
            entityManager = entityManagerFactory.createEntityManager();
            entityManager.getTransaction().begin();
            Query query = entityManager.createQuery("from UserApp where appId =:appId");
            query.setParameter("appId", appId);
            listUserApp = (List<UserApp>) query.getResultList();
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            EventLogManager.getInstance().log(e.getMessage());
        } finally {
            entityManager.close();
            entityManagerFactory.close();
        }

        return listUserApp;

    }
     /**
      * 
      * @param appId
      * @param userId
      * @return 
      */
      public UserApp getAllUserAppByAppIdAndUserId(String appId, String userId) {
        EntityManagerFactory entityManagerFactory = null;
        EntityManager entityManager = null;
        UserApp userApp = null;
        try {
            entityManagerFactory = Persistence.createEntityManagerFactory("JPA");
            entityManager = entityManagerFactory.createEntityManager();
            entityManager.getTransaction().begin();
            Query query = entityManager.createQuery("from UserApp where appId =:appId and userId=:userId");
            query.setParameter("appId", appId);
            query.setParameter("userId", userId);
            List<UserApp>  listUserApp = (List<UserApp>) query.getResultList();
            if(listUserApp.size() > 0){
                userApp = listUserApp.get(0);
            }
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            EventLogManager.getInstance().log(e.getMessage());
        } finally {
            entityManager.close();
            entityManagerFactory.close();
        }

        return userApp;

    }
      /**
     * @brief create userapp
     * @param user
     * @return
     */
    public boolean addUserApp(UserApp userApp) {
        boolean result = false;
        EntityManagerFactory entityManagerFactory = null;
        EntityManager entityManager = null;
        
        try
        {
            entityManagerFactory = Persistence.createEntityManagerFactory("JPA");   
            entityManager = entityManagerFactory.createEntityManager();
            entityManager.getTransaction().begin();
            
            entityManager.persist(userApp);
            
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
}
