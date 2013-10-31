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
import jp.co.ncdc.stew.Entities.Role;
import jp.co.ncdc.stew.Entities.UserGroupDetail;
import jp.co.ncdc.stew.Managers.EventLogManager;
import jp.co.ncdc.stew.Utils.StewConstant;


/**
 * @name: RoleAdapter.java
 * @create: Aug 21, 2013 
 * @version 1.0
 * @brief: This class is UserAdapter which connect to database and query data
 */
public class RoleAdapter {
    
    /**
     * @brief: this function is add new role
     * @param role
     * @return
     */
    public boolean addRole(Role role) {
        boolean result = false;
        EntityManagerFactory entityManagerFactory = null;
        EntityManager entityManager = null;
        
        try
        {
            entityManagerFactory = Persistence.createEntityManagerFactory("JPA");   
            entityManager = entityManagerFactory.createEntityManager();
            entityManager.getTransaction().begin();
            
            entityManager.persist(role);
            
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
     * Get all roles from database
     * @return 
     */
    public List<Role> getRoles() {
        EntityManagerFactory entityManagerFactory = null;
        EntityManager entityManager = null;
        List<Role> roles = null;
        
        try{
            entityManagerFactory = Persistence.createEntityManagerFactory("JPA");   
            entityManager = entityManagerFactory.createEntityManager();
            entityManager.getTransaction().begin();
            List result = (entityManager.createQuery("from Role").getResultList());
            roles = (List<Role>)result;

            entityManager.getTransaction().commit();
        } catch (Exception e) {
            EventLogManager.getInstance().log(e.getMessage());
        }finally{
            entityManager.close();
            entityManagerFactory.close();
        }
        
        return roles;
    }
    /**
     * Get all roles from database
     * @return 
     */
    public List<Role> getRolesForGroup() {
        EntityManagerFactory entityManagerFactory = null;
        EntityManager entityManager = null;
        List<Role> roles = null;
        
        try{
            entityManagerFactory = Persistence.createEntityManagerFactory("JPA");   
            entityManager = entityManagerFactory.createEntityManager();
            entityManager.getTransaction().begin();
            List result = (entityManager.createQuery("from Role where roleId<>" + StewConstant.ROLE_SUPPER).getResultList());
            roles = (List<Role>)result;

            entityManager.getTransaction().commit();
        } catch (Exception e) {
            EventLogManager.getInstance().log(e.getMessage());
        }finally{
            entityManager.close();
            entityManagerFactory.close();
        }
        
        return roles;
    }

    public Role getRoleByRoleID(Long roleId) {
        Role roleResult = null;
        EntityManagerFactory entityManagerFactory = null;
        EntityManager entityManager = null;
        
        try {
            entityManagerFactory = Persistence.createEntityManagerFactory("JPA");   
            entityManager = entityManagerFactory.createEntityManager();
            entityManager.getTransaction().begin();
            
            Query query = entityManager.createQuery("from Role where roleId =:roleId");
            query.setParameter("roleId", roleId);
            List queryResult = query.getResultList();
            
            if (queryResult != null && queryResult.size() > 0) {
                roleResult = (Role)queryResult.get(0);
            }

            entityManager.getTransaction().commit();
        } catch (Exception e) {
            EventLogManager.getInstance().log(e.getMessage());
        }finally{
            entityManager.close();  
            entityManagerFactory.close();
        }
        
        return roleResult;
    }

    public List<Role> getRolesLower(long roleID) {
        EntityManagerFactory entityManagerFactory = null;
        EntityManager entityManager = null;
        List<Role> roles = null;
        
        try{
            entityManagerFactory = Persistence.createEntityManagerFactory("JPA");   
            entityManager = entityManagerFactory.createEntityManager();
            entityManager.getTransaction().begin();
            
            Query query = entityManager.createQuery("from Role where roleId >:roleId");
            query.setParameter("roleId", roleID);
            roles = (List<Role>)query.getResultList();

            entityManager.getTransaction().commit();
        } catch (Exception e) {
            EventLogManager.getInstance().log(e.getMessage());
        }finally{
            entityManager.close();
            entityManagerFactory.close();
        }
        
        return roles;
    }

    public List<Role> getRolesOfUserForGroup(String email, String group) {        
        long role = StewConstant.ROLE_USER;
        if (new UserGroupDetailAdapter().checkUserIsSupperAdmin(email)) {
            role = StewConstant.ROLE_SUPPER;
        }else{
            UserGroupDetail userGroupDetail = new UserGroupDetailAdapter().getUserGroupByEmailAndGroup(email, group);
            if (userGroupDetail != null) {
                role = userGroupDetail.getRoleId();
            }
        }
        
        List<Role> lstRole = getRolesLower(role);
        return lstRole;
    }
}
