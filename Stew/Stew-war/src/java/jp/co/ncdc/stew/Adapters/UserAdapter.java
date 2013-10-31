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
import jp.co.ncdc.stew.APIs.model.UserModel;
import jp.co.ncdc.stew.Entities.User;
import jp.co.ncdc.stew.Managers.EventLogManager;
import jp.co.ncdc.stew.Utils.CipherUtils;
import jp.co.ncdc.stew.Utils.EntityManagerFactorUtils;
import jp.co.ncdc.stew.Utils.StewConstant;

/**
 * @name: UserAdapter.java
 * @create: Aug 21, 2013
 * @version 1.0
 * @brief: This class is UserAdapter which connect to database and query data
 */
public class UserAdapter {

    public UserAdapter() {
    }

    /**
     * @brief: this function is add new user
     * @param user
     * @return
     */
    public boolean addUser(User user) {
        boolean result = false;
        EntityManager entityManager = null;

        try {
            entityManager =EntityManagerFactorUtils.getCurrentEntityManagerFactory().createEntityManager();
            entityManager.getTransaction().begin();

            String passwordHash = CipherUtils.encrypt(user.getPasswordHash());
            user.setPasswordHash(passwordHash);
            entityManager.persist(user);

            entityManager.getTransaction().commit();
            result = true;
        } catch (Exception e) {
            EventLogManager.getInstance().log(e.getMessage());
            e.printStackTrace();
        } finally {
            entityManager.close();
        }
        return result;
    }

    /**
     * @brief: This function is get user by username and password
     * @param userName
     * @param password
     * @return
     */
    public User getUser(String userName, String password) {
        User userResult = null;
        EntityManager entityManager = null;

        try {
            entityManager =EntityManagerFactorUtils.getCurrentEntityManagerFactory().createEntityManager();
            entityManager.getTransaction().begin();

            String passwordHash = CipherUtils.encrypt(password);
            Query query = entityManager.createQuery("from User where email =:email and passwordHash=:password");
            query.setParameter("email", userName);
            query.setParameter("password", passwordHash);
            List queryResult = query.getResultList();

            if (queryResult != null && queryResult.size() > 0) {
                userResult = (User) queryResult.get(0);
            }
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            EventLogManager.getInstance().log(e.getMessage());
        } finally {
            entityManager.close();
        }

        return userResult;
    }

    /**
     * @brief: This function is update user with new information
     * @param user
     * @return
     */
    public boolean updateUser(User user) {
        boolean result = false;
        EntityManager entityManager = null;

        try {
            entityManager =EntityManagerFactorUtils.getCurrentEntityManagerFactory().createEntityManager();
            entityManager.getTransaction().begin();

            String passwordHash = CipherUtils.encrypt(user.getPasswordHash());
            user.setPasswordHash(passwordHash);
            entityManager.merge(user);

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
     * @brief: This function is delete user by user name
     * @param userName
     * @return boolean
     */
    public boolean deleteUser(String userName) {
        boolean result = false;
        EntityManager entityManager = null;

        try {
            entityManager =EntityManagerFactorUtils.getCurrentEntityManagerFactory().createEntityManager();
            entityManager.getTransaction().begin();

            Query query = entityManager.createQuery("Delete from User where email =:email");
            query.setParameter("email", userName);
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
     * @brief: This function is get all users
     * @return
     */
    public List<User> getUsers() {
        EntityManager entityManager = null;
        List<User> users = new ArrayList<User>();

        try {
            entityManager = EntityManagerFactorUtils.getCurrentEntityManagerFactory().createEntityManager();
            entityManager.getTransaction().begin();
            List result = (entityManager.createQuery("from User").getResultList());
            users = (List<User>) result;

            entityManager.getTransaction().commit();
        } catch (Exception e) {
            EventLogManager.getInstance().log(e.getMessage());
        } finally {
            entityManager.close();
        }

        return users;
    }

    /**
     * @brief: This function is get user by userID
     * @param userId
     * @return
     */
    public User getUserByEmail(String email) {
        User userResult = null;
        EntityManager entityManager = null;

        try {
            entityManager = EntityManagerFactorUtils.getCurrentEntityManagerFactory().createEntityManager();
            entityManager.getTransaction().begin();

            Query query = entityManager.createQuery("from User where email =:email");
            query.setParameter("email", email);
            List queryResult = query.getResultList();

            if (queryResult != null && queryResult.size() > 0) {
                userResult = (User) queryResult.get(0);
            }

            entityManager.getTransaction().commit();
        } catch (Exception e) {
            EventLogManager.getInstance().log(e.getMessage());
        } finally {
            entityManager.close();
        }

        return userResult;
    }
    /**
     * @brief: This function is get user by userID
     * @param userId
     * @return
     */
    public User getUserByEmailAndKeySearch(String email, String key) {
        User userResult = null;
        EntityManager entityManager = null;

        try {
            entityManager = EntityManagerFactorUtils.getCurrentEntityManagerFactory().createEntityManager();
            entityManager.getTransaction().begin();
            
            String searchCondition = " and (firstName like '%" + key + "%' OR lastName like '%" + key + "%')";
            Query query = entityManager.createQuery("from User where email ='" + email + "'" + searchCondition);
            List queryResult = query.getResultList();

            if (queryResult != null && queryResult.size() > 0) {
                userResult = (User) queryResult.get(0);
            }

            entityManager.getTransaction().commit();
        } catch (Exception e) {
            EventLogManager.getInstance().log(e.getMessage());
        } finally {
            entityManager.close();
        }

        return userResult;
    }

    public List<UserModel> getUserInformationByGroupID(long groupID, String keySearch) {
        List<UserModel> lstResult = null;
        EntityManager entityManager = null;

        try {
            entityManager =EntityManagerFactorUtils.getCurrentEntityManagerFactory().createEntityManager();
            entityManager.getTransaction().begin();
            
            String groupCondition = "";
            if (groupID != -1) {
                groupCondition = " and D.groupId=" + groupID;
            }
            String keySearchCondition = "";
            if (!keySearch.equals("")) {
                keySearchCondition = " and (U.firstName like '%" + keySearch + "%' OR U.lastName like '%" + keySearch + "%')";
            }
            
            Query query = entityManager.createQuery("select D.userGroupid, D.userId, G.name, R.roleName, U.firstName, U.lastName, U.createDate from User U, Role R, GroupUser G, UserGroupDetail D where U.email=D.userId and R.roleId=D.roleId and G.groupId=D.groupId" + groupCondition + keySearchCondition);
            List queryResult = query.getResultList();
            lstResult = (List<UserModel>)queryResult;

            entityManager.getTransaction().commit();
        } catch (Exception e) {
            EventLogManager.getInstance().log(e.getMessage());
        } finally {
            entityManager.close();
        }
        return lstResult;
    }

    public List<UserModel> getUserInformationManageByGroupID(String email, long groupID, String keySearch) {
        List<UserModel> lstResult = null;
        EntityManager entityManager = null;

        try {
            entityManager =EntityManagerFactorUtils.getCurrentEntityManagerFactory().createEntityManager();
            entityManager.getTransaction().begin();
            
            String groupCondition = "";
            if (groupID != -1) {
                groupCondition = " and D.groupId=" + groupID;
            }
            String keySearchCondition = "";
            if (!keySearch.equals("")) {
                keySearchCondition = " and (U.firstName like '%" + keySearch + "%' OR U.lastName like '%" + keySearch + "%')";
            }
            
            Query query = entityManager.createQuery("select D.userGroupid, D.userId, G.name, R.roleName, U.firstName, U.lastName, U.createDate from User U, Role R, GroupUser G, UserGroupDetail D where U.email=D.userId and R.roleId=D.roleId and G.groupId=D.groupId and D.userId in (select userId from UserGroupDetail where groupId in(select groupId from UserGroupDetail where userId ='" + email + "' and (roleId =" + StewConstant.ROLE_ADMIN + " OR roleId =" + StewConstant.ROLE_MANAGER+ ")) and userId<>'" + email + "')" + groupCondition + keySearchCondition);
//            Query query = entityManager.createQuery("select userId from UserGroupDetail where groupId in(select groupId from UserGroupDetail where userId ='" + email + "' and (roleId =" + StewConstant.ROLE_ADMIN + " OR roleId =" + StewConstant.ROLE_MANAGER+ ")) and userId<>'" + email + "'");
            List queryResult = query.getResultList();
            lstResult = (List<UserModel>)queryResult;

            entityManager.getTransaction().commit();
        } catch (Exception e) {
            EventLogManager.getInstance().log(e.getMessage());
        } finally {
            entityManager.close();
        }
        return lstResult;
    }
    public List<Object> getRemainingUsersOfGroup(String email, long groupID, String keySearch) {
        List<Object> lstResult = null;
        EntityManager entityManager = null;

        try {
            entityManager =EntityManagerFactorUtils.getCurrentEntityManagerFactory().createEntityManager();
            entityManager.getTransaction().begin();
            
            String groupCondition = "";
            if (groupID != -1) {
                groupCondition = " and D.groupId=" + groupID;
            }
            String keySearchCondition = "";
            if (!keySearch.equals("")) {
                keySearchCondition = " and (U.firstName like '%" + keySearch + "%' OR U.lastName like '%" + keySearch + "%')";
            }
            String sql = " and D.userId not in (select D.userId from User U, Role R, GroupUser G, UserGroupDetail D where U.email=D.userId and R.roleId=D.roleId and G.groupId=D.groupId and D.userId in (select userId from UserGroupDetail where groupId in(select groupId from UserGroupDetail where userId ='" + email + "' and (roleId =" + StewConstant.ROLE_ADMIN + " OR roleId =" + StewConstant.ROLE_MANAGER+ ")) and userId<>'" + email + "')" + groupCondition + keySearchCondition + ")";
            
            Query query;
            if (new UserGroupDetailAdapter().checkUserIsSupperAdmin(email)) {
                keySearchCondition = " and (firstName like '%" + keySearch + "%' OR lastName like '%" + keySearch + "%')";
                query = entityManager.createQuery("select email, firstName, lastName, createDate from User where email not in (select D.userId from User U, Role R, GroupUser G, UserGroupDetail D where D.userId=U.email and R.roleId=D.roleId and G.groupId=D.groupId and G.groupId=" + groupID + ")" + " and email<>'" + email + "'" + keySearchCondition);
            }else{
                query = entityManager.createQuery("select D.userId, U.firstName, U.lastName, U.createDate from User U, Role R, GroupUser G, UserGroupDetail D where U.email=D.userId and R.roleId=D.roleId and G.groupId=D.groupId and D.userId in (select userId from UserGroupDetail where groupId in(select groupId from UserGroupDetail where userId ='" + email + "' and (roleId =" + StewConstant.ROLE_ADMIN + " OR roleId =" + StewConstant.ROLE_MANAGER+ ")) and D.userId<>'" + email + "')" + sql + keySearchCondition);
            }
            List queryResult = query.getResultList();
            lstResult = (List<Object>)queryResult;

            entityManager.getTransaction().commit();
        } catch (Exception e) {
            EventLogManager.getInstance().log(e.getMessage());
        } finally {
            entityManager.close();
        }
        return lstResult;
    }
    
//    public static void main(String[] args){
//        UserAdapter userAdapter = new UserAdapter();
//        List<Object> lstAllUsers = userAdapter.getRemainingUsersOfGroup("thinh@tma.com.vn", 2, "");
//        
//        Map<String, Object> lstUserDistinct = new HashMap<String, Object>();
//        for (Object userModel:lstAllUsers) {
//            if (userModel != null){
//                Object[] ojb = (Object[]) userModel;
//                lstUserDistinct.put((String) ojb[1], userModel);
//            }
//        }
//    }
}
