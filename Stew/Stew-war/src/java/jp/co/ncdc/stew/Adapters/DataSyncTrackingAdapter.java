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
import jp.co.ncdc.stew.APIs.model.Status;
import jp.co.ncdc.stew.APIs.model.TrackData;
import jp.co.ncdc.stew.APIs.model.TrackTable;
import jp.co.ncdc.stew.Entities.DataSyncTracking;
import jp.co.ncdc.stew.Entities.UserSession;
import jp.co.ncdc.stew.Managers.DataChangeTrackingManager;
import jp.co.ncdc.stew.Managers.EventLogManager;
import jp.co.ncdc.stew.Utils.StewConstant;

/**
 *
 * @author vcnduong
 */
public class DataSyncTrackingAdapter {

    /**
     * @brief: This function is get metadata for sync by userToken
     * @param userToken
     * @return
     */
    public Status getTableNameForSync(String userToken) {
        Status status = new Status();
        String listTable = null;
        UserSessionAdapter userSessionAdapt = new UserSessionAdapter();
        UserSession userSession;
        userSession = userSessionAdapt.getUserSessionByToken(userToken);

        if (userSession != null) {
            if (userSessionAdapt.checkTokenIsExpire(userSession)) {
                status.setDescription(StewConstant.ERR_EXPIRE_TOKEN);
                status.setStatus(StewConstant.ERR_STATUS);
            } else {
                String userId = userSession.getUserId();
                String deviceUDID = userSession.getDeviceUDID();
                String appId = userSession.getAppId();
                
                
                // Check data Trackdata
                TrackData trackData = DataChangeTrackingManager.getInstance().getTrackData(userId, appId, deviceUDID);
                if (trackData != null) {
                    if (trackData.getIsFirstRegister() != null && trackData.getIsFirstRegister()) {
                         status.setDescription("");
                         status.setStatus(StewConstant.FIRST);   
                         
                         //Update status
                         DataChangeTrackingManager.getInstance().updateStatusTrackData(userId, appId, deviceUDID);
                    } else {
                        DataSyncTracking dataSync = getDataSyncTracking(userId, appId, deviceUDID);
                        if (dataSync != null) {
                            listTable = dataSync.getDataToSync_Metadata();
                            status.setResult(listTable);
                            status.setStatus(StewConstant.OK_STATUS);
                            userSessionAdapt.updateTokenExpireDate(userSession);
                        } else {
                            status.setDescription(StewConstant.ERR_APPID_DOES_NOT_MATCH);
                            status.setStatus(StewConstant.ERR_STATUS);
                        }
                    }
                }else {
                    status.setDescription(StewConstant.ERR_NOT_EXIST_RECORD);
                    status.setStatus(StewConstant.ERR_STATUS);
                }                               
            }
        } else {
            status.setDescription(StewConstant.ERR_TOKEN_NOT_EXIST);
            status.setStatus(StewConstant.ERR_STATUS);
        }
        return status;
    }

    /**
     * @brief get DataSyncTracking
     * @param userId
     * @param appId
     * @param deviceUDID
     * @return
     */
    public DataSyncTracking getDataSyncTracking(String userId, String appId, String deviceUDID) {
        DataSyncTracking dataSync = new DataSyncTracking();        
        String lstTableName = "";        
        List<TrackTable> listTrackTable = DataChangeTrackingManager.getInstance().getTrackingData(appId, deviceUDID, userId);
        for (TrackTable trackTable:listTrackTable) {
            if ("".equals(lstTableName))
                lstTableName = trackTable.getTableName();
            else
                lstTableName += "," + trackTable.getTableName();
        }
        dataSync.setDataToSync_Metadata(lstTableName);
        dataSync.setAppId(appId);
        dataSync.setDeviceUDID(deviceUDID);
        dataSync.setUserId(userId);        
        return dataSync;
    }
    
    /**
     * @brief all data from DataSyncTracking table
     * @return
     */
    public List<DataSyncTracking> getSyncTrackDatas() {
        EntityManagerFactory entityManagerFactory = null;
        EntityManager entityManager = null;
        List<DataSyncTracking> dataSync = new ArrayList<DataSyncTracking>();

        try {
            entityManagerFactory = Persistence.createEntityManagerFactory("JPA");
            entityManager = entityManagerFactory.createEntityManager();
            entityManager.getTransaction().begin();
            List result = (entityManager.createQuery("from DataSyncTracking").getResultList());
            dataSync = (List<DataSyncTracking>) result;

            entityManager.getTransaction().commit();
        } catch (Exception e) {
            EventLogManager.getInstance().log(e.getMessage());
        } finally {
            entityManager.close();
            entityManagerFactory.close();
        }

        return dataSync;
    }

    /**
     * @brief update dataSyncTracking
     * @param dataSyncTracking
     * @return
     */
    public boolean updateDataSyncTracking(DataSyncTracking dataSyncTracking) {
        boolean result = false;
        EntityManagerFactory entityManagerFactory = null;
        EntityManager entityManager = null;
        try {
            entityManagerFactory = Persistence.createEntityManagerFactory("JPA");
            entityManager = entityManagerFactory.createEntityManager();
            entityManager.getTransaction().begin();

            entityManager.merge(dataSyncTracking);

            entityManager.getTransaction().commit();
            result = true;
        } catch (Exception e) {
            EventLogManager.getInstance().log(e.getMessage());
            result = false;
        } finally {
            entityManager.close();
            entityManagerFactory.close();
        }
        return result;
    }
    
    List<DataSyncTracking> getDataSyncTrackingByUserIDAndAppID(String userID, String appID) {
        EntityManagerFactory entityManagerFactory = null;
        EntityManager entityManager = null;
        List<DataSyncTracking> dataSyncTracking = null;

        try {
            entityManagerFactory = Persistence.createEntityManagerFactory("JPA");
            entityManager = entityManagerFactory.createEntityManager();
            entityManager.getTransaction().begin();
            Query query = entityManager.createQuery("from DataSyncTracking where userId =:userId and appId =:appId");
            query.setParameter("userId", userID);
            query.setParameter("appId", appID);
            List queryResult = query.getResultList();
            
            dataSyncTracking = (List<DataSyncTracking>)queryResult;
            
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            EventLogManager.getInstance().log(e.getMessage());
        } finally {
            entityManager.close();
            entityManagerFactory.close();
        }

        return dataSyncTracking;
    }
    
    public boolean addDataSyncTracking(DataSyncTracking dataSyncTracking) {
        boolean result = false;
        EntityManagerFactory entityManagerFactory = null;
        EntityManager entityManager = null;

        try {
            entityManagerFactory = Persistence.createEntityManagerFactory("JPA");
            entityManager = entityManagerFactory.createEntityManager();
            entityManager.getTransaction().begin();
            
            entityManager.persist(dataSyncTracking);

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
	
	public DataSyncTracking getDataSyncTrackingFromDatabase(String userId, String appId, String deviceUDID) {
        DataSyncTracking dataSync = null;
        EntityManagerFactory entityManagerFactory = null;
        EntityManager entityManager = null;
        try {
            entityManagerFactory = Persistence.createEntityManagerFactory("JPA");
            entityManager = entityManagerFactory.createEntityManager();
            entityManager.getTransaction().begin();
            
            Query query = entityManager.createQuery("from DataSyncTracking where deviceUDID =:deviceUDID and userId =:userId and appId =:appId");
            query.setParameter("deviceUDID", deviceUDID);
            query.setParameter("userId", userId);
            query.setParameter("appId", appId);
            List queryResult = query.getResultList();

            if (queryResult != null && queryResult.size() > 0) {
                dataSync = (DataSyncTracking) queryResult.get(0);
            }
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            EventLogManager.getInstance().log(e.getMessage());
        } finally {
            entityManager.close();
            entityManagerFactory.close();
        } 
         System.err.println("get dataSync");
        System.err.println(dataSync);
        return dataSync;
    }
}
