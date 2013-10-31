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
import jp.co.ncdc.stew.Entities.GroupAppDetail;
import jp.co.ncdc.stew.Entities.GroupUser;
import jp.co.ncdc.stew.Managers.EventLogManager;
import jp.co.ncdc.stew.Utils.StewUtils;

/**
 *
 * @author vcnduong
 */
public class GroupAppDetailAdapter {
    /**
     * @brief create log
     * @param user
     * @return
     */
    public boolean addGroupAppDetail(GroupAppDetail groupAppDetail) {
        boolean result = false;
        EntityManagerFactory entityManagerFactory = null;
        EntityManager entityManager = null;
        
        try
        {
            entityManagerFactory = Persistence.createEntityManagerFactory("JPA");   
            entityManager = entityManagerFactory.createEntityManager();
            entityManager.getTransaction().begin();
            
            entityManager.persist(groupAppDetail);
            
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
     * @brief: This function is delete user by user name
     * @param userName
     * @return boolean
     */
    public boolean deleteGroupAppDetailByApp(String appId) {
        boolean result = false;
        EntityManagerFactory entityManagerFactory = null;
        EntityManager entityManager = null;

        try {
            entityManagerFactory = Persistence.createEntityManagerFactory("JPA");
            entityManager = entityManagerFactory.createEntityManager();
            entityManager.getTransaction().begin();

            Query query = entityManager.createQuery("Delete from GroupAppDetail where appId =:appId");
            query.setParameter("appId", appId);
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
     * @brief: 
     * @param appId
     * @return boolean
     */
    public List<GroupAppDetail> getGroupAppByApp(String appId) {
        List<GroupAppDetail>  result = new ArrayList<GroupAppDetail>();
        EntityManagerFactory entityManagerFactory = null;
        EntityManager entityManager = null;

        try {
            entityManagerFactory = Persistence.createEntityManagerFactory("JPA");
            entityManager = entityManagerFactory.createEntityManager();
            entityManager.getTransaction().begin();
            Query query = entityManager.createQuery("from GroupAppDetail where appId =:appId");
            query.setParameter("appId", appId);
            result  = (List<GroupAppDetail>) query.getResultList();
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            EventLogManager.getInstance().log(e.getMessage());
        } finally {
            entityManager.close();
            entityManagerFactory.close();
        }
        return result;
    }
    
     public List<String> getListGroupNameOfApp(String appId) {
        List<String> result = new ArrayList<String>();
        EntityManagerFactory entityManagerFactory = null;
        EntityManager entityManager = null;
        GroupAdapter groupAdapter = new GroupAdapter();
        try {
            entityManagerFactory = Persistence.createEntityManagerFactory("JPA");
            entityManager = entityManagerFactory.createEntityManager();
            entityManager.getTransaction().begin();
            Query query = entityManager.createQuery("from GroupAppDetail where appId =:appId");
            query.setParameter("appId", appId);
            List<GroupAppDetail> list  = (List<GroupAppDetail>) query.getResultList();
            for(int i =0; i< list.size(); i++){
                GroupUser groupUser = groupAdapter.getGroupByGroupID(list.get(i).getGroupId());
                if(groupUser !=null && !StewUtils.isListContainStr(result, groupUser.getName())){
                    result.add(groupUser.getName());
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

    public boolean deleteGroupAppDetailByGroupID(long deleteIDLong) {
        boolean result = false;
        EntityManagerFactory entityManagerFactory = null;
        EntityManager entityManager = null;

        try {
            entityManagerFactory = Persistence.createEntityManagerFactory("JPA");
            entityManager = entityManagerFactory.createEntityManager();
            entityManager.getTransaction().begin();

            Query query = entityManager.createQuery("Delete from GroupAppDetail where groupId =:groupId");
            query.setParameter("groupId", deleteIDLong);
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
}
