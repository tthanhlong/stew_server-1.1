/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jp.co.ncdc.stew.Adapters;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import jp.co.ncdc.stew.Entities.DeviceRegister;
import jp.co.ncdc.stew.Managers.EventLogManager;

/**
 *
 * @author vcnduong
 */
public class DeviceRegisterAdapter {

    /**
     * register device for push notification
     *
     * @param deviceRegister
     * @return
     */
    public boolean registerDevice(DeviceRegister deviceRegister) {
        boolean result = false;
        EntityManagerFactory entityManagerFactory = null;
        EntityManager entityManager = null;

        try {
            entityManagerFactory = Persistence.createEntityManagerFactory("JPA");
            entityManager = entityManagerFactory.createEntityManager();
            entityManager.getTransaction().begin();

            entityManager.persist(deviceRegister);

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
     * get device register recode by userId, appId and deviceUDID
     *
     * @param userId
     * @param appId
     * @param deviceUDID
     * @return
     */
    public DeviceRegister getDeviceRegister(String userId, String appId, String deviceUDID) {
        DeviceRegister result = null;
        EntityManagerFactory entityManagerFactory = null;
        EntityManager entityManager = null;
        try {
            entityManagerFactory = Persistence.createEntityManagerFactory("JPA");
            entityManager = entityManagerFactory.createEntityManager();
            entityManager.getTransaction().begin();
            Query query;
            if (userId != null) {
                query = entityManager.createQuery("from DeviceRegister where userId =:userId and appId=:appId and deviceUDID=:deviceUDID");
                query.setParameter("userId", userId);
                query.setParameter("appId", appId);
                query.setParameter("deviceUDID", deviceUDID);
                List queryResult = query.getResultList();
                if (queryResult != null && queryResult.size() > 0) {
                    result = (DeviceRegister) queryResult.get(0);
                }
            } else {
                query = entityManager.createQuery("from DeviceRegister where appId=:appId and deviceUDID=:deviceUDID");
                query.setParameter("appId", appId);
                query.setParameter("deviceUDID", deviceUDID);
                List queryResult = query.getResultList();
                if (queryResult != null && queryResult.size() > 0) {
                    for (int i = 0; i < queryResult.size(); i++) {
                        DeviceRegister register = (DeviceRegister) queryResult.get(i);
                        if (register.getUserId() == null || "".equals(register.getUserId())) {
                            result = register;
                            break;
                        }
                    }
                }
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
     * get DeviceRegister by device token
     *
     * @param deviceToken
     * @return DeviceRegister
     */
    public DeviceRegister getDeviceRegisterByDeviceToken(String deviceToken) {
        DeviceRegister result = null;
        EntityManagerFactory entityManagerFactory = null;
        EntityManager entityManager = null;
        try {
            entityManagerFactory = Persistence.createEntityManagerFactory("JPA");
            entityManager = entityManagerFactory.createEntityManager();
            entityManager.getTransaction().begin();

            Query query = entityManager.createQuery("from DeviceRegister where deviceToken =:deviceToken");
            query.setParameter("deviceToken", deviceToken);
            List queryResult = query.getResultList();

            if (queryResult != null && queryResult.size() > 0) {
                result = (DeviceRegister) queryResult.get(0);
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
     * get device register recode by userId
     *
     * @param userId
     * @param appId
     * @param deviceUDID
     * @return
     */
    public List<DeviceRegister> getDeviceRegisterByUserID(String userId) {
        List<DeviceRegister> result = null;
        EntityManagerFactory entityManagerFactory = null;
        EntityManager entityManager = null;
        try {
            entityManagerFactory = Persistence.createEntityManagerFactory("JPA");
            entityManager = entityManagerFactory.createEntityManager();
            entityManager.getTransaction().begin();

            Query query = entityManager.createQuery("from DeviceRegister where userId =:userId");
            query.setParameter("userId", userId);
            result = (List<DeviceRegister>) query.getResultList();

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
     * get device register recode by userId
     *
     * @param userId
     * @param appId
     * @param deviceUDID
     * @return
     */
    public List<DeviceRegister> getDeviceRegister(String userId,String appId) {
        List<DeviceRegister> result = null;
        EntityManagerFactory entityManagerFactory = null;
        EntityManager entityManager = null;
        try {
            entityManagerFactory = Persistence.createEntityManagerFactory("JPA");
            entityManager = entityManagerFactory.createEntityManager();
            entityManager.getTransaction().begin();

            Query query = entityManager.createQuery("from DeviceRegister where userId =:userId and appId=:appId");
            query.setParameter("userId", userId);
            query.setParameter("appId", appId);
            result = (List<DeviceRegister>) query.getResultList();

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
     * @brief: This function is update user with new information
     * @param user
     * @return
     */
    public boolean updateDeviceRegister(DeviceRegister deviceRegister) {
        boolean result = false;
        EntityManagerFactory entityManagerFactory = null;
        EntityManager entityManager = null;

        try {
            entityManagerFactory = Persistence.createEntityManagerFactory("JPA");
            entityManager = entityManagerFactory.createEntityManager();
            entityManager.getTransaction().begin();
            entityManager.merge(deviceRegister);

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
}
