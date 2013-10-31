/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jp.co.ncdc.stew.Adapters;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import jp.co.ncdc.stew.Entities.GroupUser;
import jp.co.ncdc.stew.Entities.Role;
import jp.co.ncdc.stew.Entities.User;
import jp.co.ncdc.stew.Entities.UserGroupDetail;
import jp.co.ncdc.stew.Managers.EventLogManager;
import jp.co.ncdc.stew.Servlet.model.UserAndRole;
import jp.co.ncdc.stew.Utils.EntityManagerFactorUtils;
import jp.co.ncdc.stew.Utils.StewConstant;

/**
 *
 * @author vcnduong
 */
public class UserGroupDetailAdapter {

    /**
     * add new usergroup
     *
     * @param userGroupDetail
     * @return
     */
    public boolean addUserGroup(UserGroupDetail userGroupDetail) {
        boolean result = false;
        EntityManagerFactory entityManagerFactory = null;
        EntityManager entityManager = null;

        try {
            entityManagerFactory = Persistence.createEntityManagerFactory("JPA");
            entityManager = entityManagerFactory.createEntityManager();
            entityManager.getTransaction().begin();

            entityManager.persist(userGroupDetail);

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
     * get user group by email
     *
     * @param email
     * @return
     */
    public List<UserGroupDetail> getUserGroupByEmail(String email) {
        List<UserGroupDetail> userResult = null;
        EntityManagerFactory entityManagerFactory = null;
        EntityManager entityManager = null;

        try {
            entityManagerFactory = Persistence.createEntityManagerFactory("JPA");
            entityManager = entityManagerFactory.createEntityManager();
            entityManager.getTransaction().begin();

            Query query = entityManager.createQuery("from UserGroupDetail where userId =:email");
            query.setParameter("email", email);
            List queryResult = query.getResultList();


            userResult = (List<UserGroupDetail>) queryResult;

            entityManager.getTransaction().commit();
        } catch (Exception e) {
            EventLogManager.getInstance().log(e.getMessage());
        } finally {
            entityManager.close();
            entityManagerFactory.close();
        }

        return userResult;
    }

    /**
     * get user group by groupid
     *
     * @param email
     * @return
     */
    public List<UserGroupDetail> getUserGroupByGroupID(Long groupId) {
        List<UserGroupDetail> userResult = null;
        EntityManagerFactory entityManagerFactory = null;
        EntityManager entityManager = null;

        try {
            entityManagerFactory = Persistence.createEntityManagerFactory("JPA");
            entityManager = entityManagerFactory.createEntityManager();
            entityManager.getTransaction().begin();

            Query query = entityManager.createQuery("from UserGroupDetail where groupId =:groupId");
            query.setParameter("groupId", groupId);            
            List queryResult = query.getResultList();
            userResult = (List<UserGroupDetail>) queryResult;

            entityManager.getTransaction().commit();
        } catch (Exception e) {
            EventLogManager.getInstance().log(e.getMessage());
        } finally {
            entityManager.close();
            entityManagerFactory.close();
        }

        return userResult;
    }
    
    /**
     * get user group by groupid
     *
     * @param email
     * @return
     */
    public List<UserGroupDetail> getUserGroups(Long groupId, String appId) {
        List<UserGroupDetail> userResult = new ArrayList<UserGroupDetail>();
        EntityManagerFactory entityManagerFactory = null;
        EntityManager entityManager = null;

        try {
            entityManagerFactory = Persistence.createEntityManagerFactory("JPA");
            entityManager = entityManagerFactory.createEntityManager();
            entityManager.getTransaction().begin(); 
            Query query = entityManager.createQuery("select g.userGroupid,g.userId,g.groupId,g.roleId from UserGroupDetail AS g,DeviceRegister AS d where g.userId =d.userId and g.groupId =:groupId and d.appId =:appId");         
            query.setParameter("appId", appId);
            query.setParameter("groupId", groupId);
            //userResult =(List<UserGroupDetail>) query.getResultList();     
            Iterator items = query.getResultList().iterator();
            if (query.getResultList().size() > 0) {
                while (items.hasNext()) {
                    Object[] row = (Object[]) items.next();                
                    UserGroupDetail userGroupDetail=new UserGroupDetail();
                    userGroupDetail.setUserGroupid(Long.parseLong(row[0].toString()));
                    userGroupDetail.setUserId(row[1].toString());
                    userGroupDetail.setGroupId(Long.parseLong(row[2].toString()));
                    userGroupDetail.setRoleId(Long.parseLong(row[3].toString()));
                    userResult.add(userGroupDetail);
                }
            }
            
            
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            System.err.println(e.getMessage());
            EventLogManager.getInstance().log(e.getMessage());
        } finally {
            entityManager.close();
            entityManagerFactory.close();
        }

        return userResult;
    }

    /**
     * delete group by email
     *
     * @param email
     * @return
     */
    public boolean deleteUserGroupByEmail(String email) {
        boolean result = false;
        EntityManagerFactory entityManagerFactory = null;
        EntityManager entityManager = null;

        try {
            entityManagerFactory = Persistence.createEntityManagerFactory("JPA");
            entityManager = entityManagerFactory.createEntityManager();
            entityManager.getTransaction().begin();

            Query query = entityManager.createQuery("Delete from UserGroupDetail where userId =:email");
            query.setParameter("email", email);
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
     * delete group by group id
     *
     * @param groupID
     * @return
     */
    public boolean deleteUserGroupByGroupID(Long groupID) {
        boolean result = false;
        EntityManagerFactory entityManagerFactory = null;
        EntityManager entityManager = null;

        try {
            entityManagerFactory = Persistence.createEntityManagerFactory("JPA");
            entityManager = entityManagerFactory.createEntityManager();
            entityManager.getTransaction().begin();

            Query query = entityManager.createQuery("Delete from UserGroupDetail where groupId =:groupId");
            query.setParameter("groupId", groupID);
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
     * This function is to delete user group by email and group id
     *
     * @param email
     * @param groupID
     * @return
     */
    public boolean deleteUserGroupByEmailAndGroupID(String email, Long groupID) {
        boolean result = false;
        EntityManagerFactory entityManagerFactory = null;
        EntityManager entityManager = null;

        try {
            entityManagerFactory = Persistence.createEntityManagerFactory("JPA");
            entityManager = entityManagerFactory.createEntityManager();
            entityManager.getTransaction().begin();

            Query query = entityManager.createQuery("Delete from UserGroupDetail where groupId =:groupId and userId =:email");
            query.setParameter("groupId", groupID);
            query.setParameter("email", email);
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
     * This function will get all group managed by this userId
     *
     * @param userId
     * @return
     */
    public List<Long> getGroupByManagerId(String userId) {        
        EntityManager entityManager = null;
        List<Long> lstGroupId = new ArrayList<Long>();
        try {
            entityManager =EntityManagerFactorUtils.getCurrentEntityManagerFactory().createEntityManager();
            //entityManagerFactory = Persistence.createEntityManagerFactory("JPA");
            //entityManager = entityManagerFactory.createEntityManager();
            entityManager.getTransaction().begin();
            String queryString = "select distinct(u.groupId) from UserGroupDetail u"
                    + " where userId ='" + userId + "'and (roleId ='" + StewConstant.ROLE_MANAGER + "' OR roleId ='" + StewConstant.ROLE_ADMIN + "')";
            Query query = entityManager.createQuery(queryString);
            lstGroupId = (List<Long>) query.getResultList();
            entityManager.getTransaction().commit();

        } catch (Exception e) {
            EventLogManager.getInstance().log(e.getMessage());
        } finally {
            entityManager.close();            
        }
        return lstGroupId;
    }

    /**
     * check is user are supper administrator
     *
     * @param userId
     * @return
     */
    public boolean checkUserIsSupperAdmin(String userId) {
        EntityManager entityManager = null;
        boolean result = false;
        try {
            entityManager = EntityManagerFactorUtils.getCurrentEntityManagerFactory().createEntityManager();
            entityManager.getTransaction().begin();

            Query query = entityManager.createQuery("from UserGroupDetail where userId =:userId and roleId =:roleId");
            query.setParameter("userId", userId);
            query.setParameter("roleId", StewConstant.ROLE_SUPPER);
            List queryResult = query.getResultList();
            if (queryResult.size() > 0) {
                result = true;
            }
        } catch (Exception e) {
            EventLogManager.getInstance().log(e.getMessage());
        } finally {
            entityManager.close();
        }
        return result;
    }

    /**
     * This function will get all group managed by this userId
     *
     * @param userId
     * @return
     */
    public List<UserGroupDetail> getUserGroupDetailByUserIdWithManagerRole(String userId) {
        EntityManagerFactory entityManagerFactory = null;
        EntityManager entityManager = null;
        List<UserGroupDetail> userGroups = null;
        try {
            entityManagerFactory = Persistence.createEntityManagerFactory("JPA");
            entityManager = entityManagerFactory.createEntityManager();
            entityManager.getTransaction().begin();

            Query query = entityManager.createQuery("from UserGroupDetail where userId =:userId and (roleId =:admin OR roleId =:manager)");
            query.setParameter("userId", userId);
            query.setParameter("admin", StewConstant.ROLE_ADMIN);
            query.setParameter("manager", StewConstant.ROLE_MANAGER);
            List queryResult = query.getResultList();
            userGroups = (List<UserGroupDetail>) queryResult;
            entityManager.getTransaction().commit();

        } catch (Exception e) {
            EventLogManager.getInstance().log(e.getMessage());
        } finally {
            entityManager.close();
            entityManagerFactory.close();
        }
        return userGroups;
    }
/**
     * This function will get all group managed by this userId
     *
     * @param userId
     * @return
     */
    public List<UserGroupDetail> getUserGroupDetailByUserIdWithManagerRoleAndGroupID(String userId, long groupID) {
        EntityManagerFactory entityManagerFactory = null;
        EntityManager entityManager = null;
        List<UserGroupDetail> userGroups = null;
        try {
            entityManagerFactory = Persistence.createEntityManagerFactory("JPA");
            entityManager = entityManagerFactory.createEntityManager();
            entityManager.getTransaction().begin();

            Query query = entityManager.createQuery("from UserGroupDetail where userId =:userId and (roleId =:admin OR roleId =:manager) and groupId =:groupId");
            query.setParameter("userId", userId);
            query.setParameter("admin", StewConstant.ROLE_ADMIN);
            query.setParameter("manager", StewConstant.ROLE_MANAGER);
            query.setParameter("groupId", groupID);
            List queryResult = query.getResultList();
            userGroups = (List<UserGroupDetail>) queryResult;
            entityManager.getTransaction().commit();

        } catch (Exception e) {
            EventLogManager.getInstance().log(e.getMessage());
        } finally {
            entityManager.close();
            entityManagerFactory.close();
        }
        return userGroups;
    }
    /**
     *
     * @param selectedGroupID
     * @return
     */
    public List<UserGroupDetail> getUserGroupDetailByGroupId(long selectedGroupID) {
        EntityManagerFactory entityManagerFactory = null;
        EntityManager entityManager = null;
        List<UserGroupDetail> userGroups = null;
        try {
            entityManagerFactory = Persistence.createEntityManagerFactory("JPA");
            entityManager = entityManagerFactory.createEntityManager();
            entityManager.getTransaction().begin();

            Query query = entityManager.createQuery("from UserGroupDetail where groupId =:groupId)");
            query.setParameter("groupId", selectedGroupID);
            List queryResult = query.getResultList();
            userGroups = (List<UserGroupDetail>) queryResult;
            entityManager.getTransaction().commit();

        } catch (Exception e) {
            EventLogManager.getInstance().log(e.getMessage());
        } finally {
            entityManager.close();
            entityManagerFactory.close();
        }
        return userGroups;
    }

    /**
     *
     * @return
     */
    public List<UserGroupDetail> getAllUserGroupDetails() {
        EntityManagerFactory entityManagerFactory = null;
        EntityManager entityManager = null;
        List<UserGroupDetail> userGroupDetails = null;

        try {
            entityManagerFactory = Persistence.createEntityManagerFactory("JPA");
            entityManager = entityManagerFactory.createEntityManager();
            entityManager.getTransaction().begin();
            List result = (entityManager.createQuery("from UserGroupDetail").getResultList());
            userGroupDetails = (List<UserGroupDetail>) result;

            entityManager.getTransaction().commit();
        } catch (Exception e) {
            EventLogManager.getInstance().log(e.getMessage());
        } finally {
            entityManager.close();
            entityManagerFactory.close();
        }

        return userGroupDetails;
    }

    /**
     *
     * @param email
     * @param groupIDString
     * @return
     */
    public UserGroupDetail getUserGroupByEmailAndGroup(String email, String groupIDString) {
        UserGroupDetail result = null;
        EntityManagerFactory entityManagerFactory = null;
        EntityManager entityManager = null;

        try {
            entityManagerFactory = Persistence.createEntityManagerFactory("JPA");
            entityManager = entityManagerFactory.createEntityManager();
            entityManager.getTransaction().begin();

            Query query = entityManager.createQuery("from UserGroupDetail where userId =:userId and groupId =:groupId");
            query.setParameter("userId", email);
            query.setParameter("groupId", Long.parseLong(groupIDString));
            List queryResult = query.getResultList();

            if (queryResult != null && queryResult.size() > 0) {
                result = (UserGroupDetail) queryResult.get(0);
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
     * This function will get all user and user role by this groupId
     *
     * @param groupId
     * @return
     */
    public List<UserAndRole> getUserByGroupId(Long groupId, String role, String key) {
        EntityManagerFactory entityManagerFactory = null;
        EntityManager entityManager = null;
        List<UserAndRole> listUser = new ArrayList<UserAndRole>();
        UserAdapter userAdapter = new UserAdapter();
        try {
            entityManagerFactory = Persistence.createEntityManagerFactory("JPA");
            entityManager = entityManagerFactory.createEntityManager();
            entityManager.getTransaction().begin();
            String roleCondition = "";
            try {
                long roleLong = Long.parseLong(role);
                roleCondition = " and roleId = " + roleLong;
            } catch (Exception e) {
            }
            String queryString = "select distinct(u.userId) from UserGroupDetail u where groupId ='" + groupId + "'" + roleCondition;
            Query query = entityManager.createQuery(queryString);
            List<String> listuDetails = (List<String>) query.getResultList();
            for (int i = 0; i < listuDetails.size(); i++) {
                User user = null;
                if (key != null && !key.equals("")) {
                    user = userAdapter.getUserByEmailAndKeySearch(listuDetails.get(i), key);
                }else{
                    user = userAdapter.getUserByEmail(listuDetails.get(i));
                }
                
                if (user != null) {
                    List<String> listRole = getListRoleName(user.getEmail(), groupId);
                    UserAndRole uar = new UserAndRole(user, listRole);
                    listUser.add(uar);
                }
            }
            entityManager.getTransaction().commit();

        } catch (Exception e) {
            EventLogManager.getInstance().log(e.getMessage());
        } finally {
            entityManager.close();
            entityManagerFactory.close();
        }
        return listUser;
    }

    /**
     *
     * @param userId
     * @param GroupId
     * @return
     */
    public List<String> getListRoleName(String userId, Long GroupId) {
        EntityManagerFactory entityManagerFactory = null;
        EntityManager entityManager = null;
        List<String> result = new ArrayList<String>();
        RoleAdapter roleAdapter = new RoleAdapter();
        try {
            entityManagerFactory = Persistence.createEntityManagerFactory("JPA");
            entityManager = entityManagerFactory.createEntityManager();
            entityManager.getTransaction().begin();

            Query query = entityManager.createQuery("from UserGroupDetail where userId =:userId and groupId =:groupId");
            query.setParameter("userId", userId);
            query.setParameter("groupId", GroupId);
            List queryResult = query.getResultList();

            for (int i = 0; i < queryResult.size(); i++) {
                UserGroupDetail userGroupDetail = (UserGroupDetail) queryResult.get(i);
                Long id = StewConstant.ROLE_USER;
                if (userGroupDetail.getRoleId() != null) {
                    id = userGroupDetail.getRoleId();
                }
                Role role = roleAdapter.getRoleByRoleID(id);
                if (role != null) {
                    result.add(role.getRoleName());
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
     *
     * @param userGroupID
     * @return
     */
    public UserGroupDetail getUserGroupDetailByID(long userGroupID) {
        UserGroupDetail result = null;
        EntityManagerFactory entityManagerFactory = null;
        EntityManager entityManager = null;

        try {
            entityManagerFactory = Persistence.createEntityManagerFactory("JPA");
            entityManager = entityManagerFactory.createEntityManager();
            entityManager.getTransaction().begin();

            Query query = entityManager.createQuery("from UserGroupDetail where userGroupid =:userGroupid");
            query.setParameter("userGroupid", userGroupID);
            List queryResult = query.getResultList();

            if (queryResult != null && queryResult.size() > 0) {
                result = (UserGroupDetail) queryResult.get(0);
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
     *
     * @param userGroupID
     * @return
     */
    public boolean deleteUserByUserGroupID(long userGroupID) {
        boolean result = false;
        EntityManagerFactory entityManagerFactory = null;
        EntityManager entityManager = null;

        try {
            entityManagerFactory = Persistence.createEntityManagerFactory("JPA");
            entityManager = entityManagerFactory.createEntityManager();
            entityManager.getTransaction().begin();

            Query query = entityManager.createQuery("Delete from UserGroupDetail where userGroupid =:userGroupid");
            query.setParameter("userGroupid", userGroupID);
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
     * This function will get all group managed by this userId
     *
     * @param userId
     * @return
     */
    public List<String> getGroupNameOfUser(String userId) {
        EntityManagerFactory entityManagerFactory = null;
        EntityManager entityManager = null;
        List<String> lstGroups= new ArrayList<String>();
        GroupAdapter groupAdapter = new GroupAdapter();
        try {
            entityManagerFactory = Persistence.createEntityManagerFactory("JPA");
            entityManager = entityManagerFactory.createEntityManager();
            entityManager.getTransaction().begin();
            String queryString = "select distinct(u.groupId) from UserGroupDetail u where userId ='" + userId + "')";
            Query query = entityManager.createQuery(queryString);
            List<Long> listuDetails = (List<Long>) query.getResultList();
            for (int i = 0; i < listuDetails.size(); i++) {
                GroupUser groupUser  = groupAdapter.getGroupByGroupID(listuDetails.get(i));
                if (groupUser != null) {
                    String name = groupUser.getName();
                    lstGroups.add(name);
                }
            }
            entityManager.getTransaction().commit();

        } catch (Exception e) {
            EventLogManager.getInstance().log(e.getMessage());
        } finally {
            entityManager.close();
            entityManagerFactory.close();
        }
        return lstGroups;
    }
    
    public boolean removeUserFromGroup(long roleOfUser, long groupID, String deleteID) {
        boolean result = false;
        EntityManager entityManager = null;

        try {
            entityManager =EntityManagerFactorUtils.getCurrentEntityManagerFactory().createEntityManager();
            entityManager.getTransaction().begin();
            
            Query query = entityManager.createQuery("Delete from UserGroupDetail where groupid =:groupid and userId =:userId and roleId>:roleId");
            query.setParameter("groupid", groupID);
            query.setParameter("userId", deleteID);
            query.setParameter("roleId", roleOfUser);
            query.executeUpdate();
            
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
     * get user group by groupid, appId, list role
     * @param groupId
     * @param appId
     * @param lstRoles
     * @return
     */
    public List<UserGroupDetail> getUserGroupsByListRole(Long groupId, String appId, List<String> lstRoles) {
        List<UserGroupDetail> userResult = new ArrayList<UserGroupDetail>();        
        EntityManager entityManager = null;

        try {
            entityManager =EntityManagerFactorUtils.getCurrentEntityManagerFactory().createEntityManager();
            entityManager.getTransaction().begin();
            String roleCondition = "";
            long roleId;
            if (lstRoles != null && lstRoles.size() > 0) {
                roleId = Long.parseLong(lstRoles.get(0));
                if (lstRoles.size() == 1) {                                      
                    roleCondition = " and g.roleId=" + roleId;
                } else {
                    roleCondition = " and (g.roleId=" + roleId;
                    for (int i = 1; i < lstRoles.size(); i++) {
                        roleId = Long.parseLong(lstRoles.get(i));
                        roleCondition += " or g.roleId=" + roleId;
                    }                    
                    roleCondition += ")";
                }
            }
            
            String queryString = "select distinct g from UserGroupDetail AS g,DeviceRegister AS d where g.userId =d.userId and g.groupId ='" + groupId + "' and d.appId ='" + appId + "'" + roleCondition;
            Query query = entityManager.createQuery(queryString);            
            userResult =(List<UserGroupDetail>) query.getResultList();                                         
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            System.err.println(e.getMessage());
            EventLogManager.getInstance().log(e.getMessage());
        } finally {
            entityManager.close();            
        }

        return userResult;
    }
    
}
