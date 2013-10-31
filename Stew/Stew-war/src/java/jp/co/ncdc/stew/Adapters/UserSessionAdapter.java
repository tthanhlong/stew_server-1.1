/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jp.co.ncdc.stew.Adapters;

import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import jp.co.ncdc.stew.Entities.UserSession;
import jp.co.ncdc.stew.Managers.EventLogManager;
import jp.co.ncdc.stew.Utils.StewConfig;

/**
 *
 * @author vcnduong
 */
public class UserSessionAdapter {

    /**
     * @brief: this function is add new userSession
     * @param userSession
     * @return boolean
     */
    public boolean addUserSession(UserSession userSession) {
        boolean result = false;
        EntityManagerFactory entityManagerFactory = null;
        EntityManager entityManager = null;

        try {
            entityManagerFactory = Persistence.createEntityManagerFactory("JPA");
            entityManager = entityManagerFactory.createEntityManager();
            entityManager.getTransaction().begin();
            entityManager.persist(userSession);

            entityManager.getTransaction().commit();
            result = true;
        } catch (Exception e) {
            EventLogManager.getInstance().log(e.getMessage());
            e.printStackTrace();
        } finally {
            entityManager.close();
            entityManagerFactory.close();
        }
        return result;
    }

    /**
     * @brief: This function is get usersession by userToken
     * @param token
     * @return
     */
    public UserSession getUserSessionByToken(String token) {
        UserSession result = null;
        EntityManagerFactory entityManagerFactory = null;
        EntityManager entityManager = null;

        try {
            entityManagerFactory = Persistence.createEntityManagerFactory("JPA");
            entityManager = entityManagerFactory.createEntityManager();
            entityManager.getTransaction().begin();

            Query query = entityManager.createQuery("from UserSession where userToken =:userToken");
            query.setParameter("userToken", token);
            List queryResult = query.getResultList();

            if (queryResult != null && queryResult.size() > 0) {
                result = (UserSession) queryResult.get(0);
            }
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            EventLogManager.getInstance().log(e.getMessage());
        } finally {
            entityManager.close();
            entityManagerFactory.close();
        }
        return result;
    }

    /**
     * @brief: update usersesion
     *
     * @param userSession
     * @return
     */
    public boolean updateUserSession(UserSession userSession) {
        boolean result = false;
        EntityManagerFactory entityManagerFactory = null;
        EntityManager entityManager = null;

        try {
            entityManagerFactory = Persistence.createEntityManagerFactory("JPA");
            entityManager = entityManagerFactory.createEntityManager();
            entityManager.getTransaction().begin();
            entityManager.merge(userSession);

            entityManager.getTransaction().commit();
            result = true;
        } catch (Exception e) {
            EventLogManager.getInstance().log(e.getMessage());
        } finally {
            entityManager.close();
            entityManagerFactory.close();
        }
        return result;
    }

    /**
     * @brief: This function is delete userSession by user token
     * @param userToken
     * @return
     */
    public boolean deleteUserSession(String userToken) {
        boolean result = false;
        EntityManagerFactory entityManagerFactory = null;
        EntityManager entityManager = null;

        try {
            entityManagerFactory = Persistence.createEntityManagerFactory("JPA");
            entityManager = entityManagerFactory.createEntityManager();
            entityManager.getTransaction().begin();

            Query query = entityManager.createQuery("Delete from UserSession where userToken =:userToken");
            query.setParameter("userToken", userToken);
            query.executeUpdate();

            entityManager.getTransaction().commit();
            result = true;
        } catch (Exception e) {
            EventLogManager.getInstance().log(e.getMessage());
        } finally {
            entityManager.close();
            entityManagerFactory.close();
        }
        return result;
    }

    /**
     * @brief: get user session by appId, deviceUDID, userId
     *
     * @param appId
     * @param deviceUDID
     * @param userId
     * @return
     */
    public UserSession getUserSessionByAppIdDeviceUDIDUserId(String appId, String deviceUDID, String userId) {
        UserSession result = null;
        EntityManagerFactory entityManagerFactory = null;
        EntityManager entityManager = null;

        try {
            entityManagerFactory = Persistence.createEntityManagerFactory("JPA");
            entityManager = entityManagerFactory.createEntityManager();
            entityManager.getTransaction().begin();

            Query query = entityManager.createQuery("from UserSession where userId =:userId and appId=:appId and deviceUDID=:deviceUDID");
            query.setParameter("userId", userId);
            query.setParameter("appId", appId);
            query.setParameter("deviceUDID", deviceUDID);
            List queryResult = query.getResultList();
            if (queryResult != null && queryResult.size() > 0) {
                result = (UserSession) queryResult.get(0);
            }
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            EventLogManager.getInstance().log(e.getMessage());
        } finally {
            entityManager.close();
            entityManagerFactory.close();
        }

        return result;
    }

    /**
     * check token is expire or not
     * @param userSession
     * @return boolean
     */
    public boolean checkTokenIsExpire(UserSession userSession) {
        Date currentExpireDate = userSession.getTokenExpireDate();
        Date now = new Date();
        if (now.getTime() > currentExpireDate.getTime()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * update token expire date
     * @param userSession 
     */
    public void updateTokenExpireDate(UserSession userSession) {
        Date now = new Date();
        int defaultTokenAge = StewConfig.getInstance().defaulTokenAge;
        userSession.setTokenExpireDate(new Date(now.getTime() + defaultTokenAge * 3600 * 1000));
        updateUserSession(userSession);
    }
}
