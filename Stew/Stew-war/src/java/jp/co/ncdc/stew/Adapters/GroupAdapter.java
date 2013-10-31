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
import jp.co.ncdc.stew.Entities.GroupUser;
import jp.co.ncdc.stew.Managers.EventLogManager;

/**
 * @name: GroupAdapter.java
 * @create: Aug 26, 2013
 * @version 1.0
 * @brief: This class is GroupAdapter which connect to database and query data
 */
public class GroupAdapter {

    public GroupAdapter() {
    }

    /**
     * @brief: this function is add new group user
     * @param groupUser
     * @return
     */
    public boolean addGroup(GroupUser groupUser) {
        boolean result = false;
        EntityManagerFactory entityManagerFactory = null;
        EntityManager entityManager = null;

        try {
            entityManagerFactory = Persistence.createEntityManagerFactory("JPA");
            
            
            entityManager = entityManagerFactory.createEntityManager();
            entityManager.getTransaction().begin();

            entityManager.persist(groupUser);

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
     * @brief: this function is add new group user
     * @param groupUser
     * @return
     */
    public long getGroupIDAfterInsertGroup(GroupUser groupUser) {
        long result = -1;
        EntityManagerFactory entityManagerFactory = null;
        EntityManager entityManager = null;

        try {
            entityManagerFactory = Persistence.createEntityManagerFactory("JPA");
            entityManager = entityManagerFactory.createEntityManager();
            entityManager.getTransaction().begin();

            entityManager.persist(groupUser);

            entityManager.getTransaction().commit();
            result = groupUser.getGroupId();
        } catch (Exception e) {
            EventLogManager.getInstance().log(e.getMessage());
        } finally {
            entityManager.close();
            entityManagerFactory.close();
        }
        return result;
    }

    /**
     * @brief: This function is update group with new information
     * @param groupUser
     * @return
     */
    public boolean updateGroup(GroupUser groupUser) {
        boolean result = false;
        EntityManagerFactory entityManagerFactory = null;
        EntityManager entityManager = null;

        try {
            entityManagerFactory = Persistence.createEntityManagerFactory("JPA");
            entityManager = entityManagerFactory.createEntityManager();
            entityManager.getTransaction().begin();

            entityManager.merge(groupUser);

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
     * @brief: This function is delete group by group id
     * @param groupId
     * @return boolean
     */
    public boolean deleteGroup(Long groupId) {
        boolean result = false;
        EntityManagerFactory entityManagerFactory = null;
        EntityManager entityManager = null;

        try {
            entityManagerFactory = Persistence.createEntityManagerFactory("JPA");
            entityManager = entityManagerFactory.createEntityManager();
            entityManager.getTransaction().begin();

            Query query = entityManager.createQuery("Delete from GroupUser where id =:id");
            query.setParameter("id", groupId);
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
     * @brief: This function is get group user by groupID
     * @param groupId
     * @return
     */
    public GroupUser getGroupByGroupID(Long groupId) {
        GroupUser groupResult = null;
        EntityManagerFactory entityManagerFactory = null;
        EntityManager entityManager = null;

        try {
            entityManagerFactory = Persistence.createEntityManagerFactory("JPA");
            entityManager = entityManagerFactory.createEntityManager();
            entityManager.getTransaction().begin();

            Query query = entityManager.createQuery("from GroupUser where id =:id");
            query.setParameter("id", groupId);
            List queryResult = query.getResultList();

            if (queryResult != null && queryResult.size() > 0) {
                groupResult = (GroupUser) queryResult.get(0);
            }

            entityManager.getTransaction().commit();
        } catch (Exception e) {
            EventLogManager.getInstance().log(e.getMessage());
        } finally {
            entityManager.close();
            entityManagerFactory.close();
        }

        return groupResult;
    }

    /**
     * get group by group name
     *
     * @param groupName
     * @return
     */
    public GroupUser getGroupByGroupName(String groupName) {
        GroupUser groupResult = null;
        EntityManagerFactory entityManagerFactory = null;
        EntityManager entityManager = null;

        try {
            entityManagerFactory = Persistence.createEntityManagerFactory("JPA");
            entityManager = entityManagerFactory.createEntityManager();
            entityManager.getTransaction().begin();

            Query query = entityManager.createQuery("from GroupUser where name =:name");
            query.setParameter("name", groupName);
            List queryResult = query.getResultList();

            if (queryResult != null && queryResult.size() > 0) {
                groupResult = (GroupUser) queryResult.get(0);
            }

            entityManager.getTransaction().commit();
        } catch (Exception e) {
            EventLogManager.getInstance().log(e.getMessage());
        } finally {
            entityManager.close();
            entityManagerFactory.close();
        }

        return groupResult;
    }

    /**
     * @brief: This function is get all groups
     * @return
     */
    public List<GroupUser> getGroupUsers() {
        EntityManagerFactory entityManagerFactory = null;
        EntityManager entityManager = null;
        List<GroupUser> groupUsers = null;

        try {
            entityManagerFactory = Persistence.createEntityManagerFactory("JPA");
            entityManager = entityManagerFactory.createEntityManager();
            entityManager.getTransaction().begin();
            List result = (entityManager.createQuery("from GroupUser").getResultList());
            groupUsers = (List<GroupUser>) result;

            entityManager.getTransaction().commit();
        } catch (Exception e) {
            EventLogManager.getInstance().log(e.getMessage());
        } finally {
            entityManager.close();
            entityManagerFactory.close();
        }

        return groupUsers;
    }
    
    /**
     *      
     * @return 
     */
    public List<Long> getAllgroupIds() {
        EntityManagerFactory entityManagerFactory = null;
        EntityManager entityManager = null;
        List<Long> lstGroupId = new ArrayList<Long>();
        try {
            entityManagerFactory = Persistence.createEntityManagerFactory("JPA");
            entityManager = entityManagerFactory.createEntityManager();
            entityManager.getTransaction().begin();
            String queryString = "select groupId from GroupUser";
            Query query = entityManager.createQuery(queryString);
            lstGroupId = (List<Long>) query.getResultList();
            entityManager.getTransaction().commit();

        } catch (Exception e) {
            EventLogManager.getInstance().log(e.getMessage());
        } finally {
            entityManager.close();
            entityManagerFactory.close();
        }
        return lstGroupId;        
    }

}
