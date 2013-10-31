/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jp.co.ncdc.stew.Controllers;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import jp.co.ncdc.stew.APIs.model.Authentication;
import jp.co.ncdc.stew.APIs.model.DataModel;
import jp.co.ncdc.stew.APIs.model.Status;
import jp.co.ncdc.stew.APIs.model.UserModel;
import jp.co.ncdc.stew.Adapters.GroupAdapter;
import jp.co.ncdc.stew.Adapters.RoleAdapter;
import jp.co.ncdc.stew.Adapters.UserAdapter;
import jp.co.ncdc.stew.Adapters.UserGroupDetailAdapter;
import jp.co.ncdc.stew.Adapters.UserSessionAdapter;
import jp.co.ncdc.stew.Entities.DeviceRegister;
import jp.co.ncdc.stew.Entities.Role;
import jp.co.ncdc.stew.Entities.User;
import jp.co.ncdc.stew.Entities.UserGroupDetail;
import jp.co.ncdc.stew.Entities.UserSession;
import jp.co.ncdc.stew.Utils.APIResponse;
import jp.co.ncdc.stew.Utils.StewConstant;

/**
 * @name: LoggingController.java
 * @create: Aug 21, 2013
 * @version 1.0
 * @brief: This class is implementation for User Controller
 */
public class UserController {

    UserAdapter userController = new UserAdapter();

    /**
     * @brief: This method is process of authentication
     *
     * @param userName
     * @param password
     * @param appId
     * @param deviceUDID
     * @return
     */
    public Authentication authenticateUser(String userName, String password, String appId, String deviceUDID, String isPush) {
        Authentication result = new Authentication();
        //if required parameters are not existed: return error
        if (userName == null || "".equals(userName) || appId == null || "".equals(appId) || deviceUDID == null || "".equals(deviceUDID) || isPush == null || "".equals(isPush)) {
            result = new APIResponse().authenticateResponse(StewConstant.ERR_STATUS, "", StewConstant.ERR_REQUIRED);
        } else {
            User user = userController.getUser(userName, password);
            //if user is not existed: return error
            if (user == null) {
                result = new APIResponse().authenticateResponse(StewConstant.ERR_STATUS, "", StewConstant.ERR_USER_NOT_EXIST);
            } else {
                String userId = user.getEmail();
                UserSessionController controller = new UserSessionController();
                //get or update device register by logininfo
                DeviceRegisterController deviceRegisterController = new DeviceRegisterController();
                DeviceRegister deviceRegister = deviceRegisterController.updateRegisterDevice(userId, appId, deviceUDID);
                //check device want to use push notifixation must register first
                if (StewConstant.REQUIRE_PUSH.equals(isPush.toLowerCase())) {
                    // is required push check deviceToken registerd
                    if(deviceRegister ==null){
                        return new APIResponse().authenticateResponse(StewConstant.ERR_STATUS, "", StewConstant.DEVICE_NOTYET_REGISTER);
                    }else{
                        if (deviceRegister.getDeviceToken() != null || !"".equals(deviceRegister.getDeviceToken())) {
                            //create user session
                            result = controller.createUserSession(appId, deviceUDID, userId);
                        } else {
                            //return error not device not yet register
                            result = new APIResponse().authenticateResponse(StewConstant.ERR_STATUS, "", StewConstant.DEVICE_NOTYET_REGISTER);
                        }
                    }
                } else {
                    //create user session not required push
                    result = controller.createUserSession(appId, deviceUDID, userId);
                }
            }
        }
        return result;
    }
    
    /**
     * Get all user by user email
     * @param email
     * @param groupID
     * @return 
     */
    public List<UserModel> getAllUserByEmail(String email, long groupID, String keySearch){
        UserGroupDetailAdapter userGroupDetailAdapter = new UserGroupDetailAdapter();
        boolean isSuperAdmin = userGroupDetailAdapter.checkUserIsSupperAdmin(email);
        
        List<UserModel> lstAllUserModels = null;
        
        if (isSuperAdmin) {
            lstAllUserModels =  userController.getUserInformationByGroupID(groupID, keySearch);
        }else{
            lstAllUserModels = userController.getUserInformationManageByGroupID(email, groupID, keySearch);
        }
        
        return lstAllUserModels;
    }
    /**
     * Get all users with paging number
     *
     * @param pageNumber
     * @return
     */
    public DataModel getUsersByPageNumber(int pageNumber, String email) {
        List<UserModel> lstAllUserModels = getAllUserByEmail(email, -1, "");
        DataModel result = new DataModel();
        int totalRecords = lstAllUserModels.size();

        if (!lstAllUserModels.isEmpty() && totalRecords > 0) {
            int totalPage = totalRecords / StewConstant.ITEM_PER_PAGE_MANAGE + 1;
            if (totalRecords % StewConstant.ITEM_PER_PAGE_MANAGE == 0) {
                totalPage = totalPage - 1;
            }
            if (totalRecords <= StewConstant.ITEM_PER_PAGE_MANAGE) {
                result.setListItems(lstAllUserModels);
            } else {
                List<UserModel> lstUserModels = new LinkedList<UserModel>();
                int getUsersFrom = pageNumber * StewConstant.ITEM_PER_PAGE_MANAGE;
                int getUsersTo = (getUsersFrom + StewConstant.ITEM_PER_PAGE_MANAGE) < totalRecords ? getUsersFrom + StewConstant.ITEM_PER_PAGE_MANAGE : totalRecords;

                for (int i = getUsersFrom; i < getUsersTo; i++) {
                    lstUserModels.add(lstAllUserModels.get(i));
                }

                result.setListItems(lstUserModels);
            }

            result.setStatus(StewConstant.STATUS_CODE_OK);
            result.setTotal(totalPage);
        }

        return result;
    }

    /**
     * Get user by page number and search by key only
     * @param pageNumber
     * @param email
     * @param keySearch
     * @return 
     */
    public DataModel getUsersByPageNumberAndKeySearch(int pageNumber, String email, String keySearch){
        List<UserModel> allUsers = getAllUserByEmail(email, -1, keySearch);
        DataModel result = new DataModel();
        
        result.setStatus(StewConstant.STATUS_CODE_OK);
        if (allUsers.isEmpty() || allUsers.size()== 0) {
            result.setTotal(0);
            result.setListItems(allUsers);
        }else{            
            int totalRecords = allUsers.size();

            if (!allUsers.isEmpty() && totalRecords > 0) {
                int totalPage = totalRecords / StewConstant.ITEM_PER_PAGE_MANAGE + 1;
                if (totalRecords % StewConstant.ITEM_PER_PAGE_MANAGE == 0) {
                    totalPage = totalPage - 1;
                }
                if (totalRecords <= StewConstant.ITEM_PER_PAGE_MANAGE) {
                    result.setListItems(allUsers);
                } else {
                    List<UserModel> lstUserModels = new LinkedList<UserModel>();
                    int getUsersFrom = pageNumber * StewConstant.ITEM_PER_PAGE_MANAGE;
                    int getUsersTo = (getUsersFrom + StewConstant.ITEM_PER_PAGE_MANAGE) < totalRecords ? getUsersFrom + StewConstant.ITEM_PER_PAGE_MANAGE : totalRecords;

                    for (int i = getUsersFrom; i < getUsersTo; i++) {
                        lstUserModels.add(allUsers.get(i));
                    }

                    result.setListItems(lstUserModels);
                }

                result.setTotal(totalPage);
            }else{
                result.setTotal(0);
                result.setListItems(allUsers);
            }
        }
        
        return result;
    }
    
    /**
     * Get users by page number and search by groupId and key search
     * @param pageNumber
     * @param email
     * @param groupIDLong
     * @param keySearch
     * @return 
     */
    public DataModel getUsersByPageNumberAndGroupIDAndKeySearch(int pageNumber, String email, long groupIDLong, String keySearch){
        List<UserModel> allUsers = getAllUserByEmail(email, groupIDLong, keySearch);
        DataModel result = new DataModel();
        
        result.setStatus(StewConstant.STATUS_CODE_OK);
        if (allUsers.isEmpty() || allUsers.size()== 0) {
            result.setTotal(0);
            result.setListItems(allUsers);
        }else{
            int totalRecords = allUsers.size();

            if (!allUsers.isEmpty() && totalRecords > 0) {
                int totalPage = totalRecords / StewConstant.ITEM_PER_PAGE_MANAGE + 1;
                if (totalRecords % StewConstant.ITEM_PER_PAGE_MANAGE == 0) {
                    totalPage = totalPage - 1;
                }
                if (totalRecords <= StewConstant.ITEM_PER_PAGE_MANAGE) {
                    result.setListItems(allUsers);
                } else {
                    List<UserModel> lstUserModels = new LinkedList<UserModel>();
                    int getUsersFrom = pageNumber * StewConstant.ITEM_PER_PAGE_MANAGE;
                    int getUsersTo = (getUsersFrom + StewConstant.ITEM_PER_PAGE_MANAGE) < totalRecords ? getUsersFrom + StewConstant.ITEM_PER_PAGE_MANAGE : totalRecords;

                    for (int i = getUsersFrom; i < getUsersTo; i++) {
                        lstUserModels.add(allUsers.get(i));
                    }

                    result.setListItems(lstUserModels);
                }

                result.setTotal(totalPage);
            }else{
                result.setTotal(0);
                result.setListItems(allUsers);
            }
        }
        
        return result;
    }
    
    /**
     * Get users by page number and search by groupId only
     * @param pageNumber
     * @param email
     * @param groupIDLong
     * @return 
     */
    public DataModel getUsersByPageNumberAndGroupID(int pageNumber, String email, long groupIDLong){
        List<UserModel> lstAllUserModels = getAllUserByEmail(email, groupIDLong, "");
        DataModel result = new DataModel();
        
        int totalRecords = lstAllUserModels.size();

        if (!lstAllUserModels.isEmpty() && totalRecords > 0) {
            int totalPage = totalRecords / StewConstant.ITEM_PER_PAGE_MANAGE + 1;
            if (totalRecords % StewConstant.ITEM_PER_PAGE_MANAGE == 0) {
                totalPage = totalPage - 1;
            }
            if (totalRecords <= StewConstant.ITEM_PER_PAGE_MANAGE) {
                result.setListItems(lstAllUserModels);
            } else {
                List<UserModel> lstUserModels = new LinkedList<UserModel>();
                int getUsersFrom = pageNumber * StewConstant.ITEM_PER_PAGE_MANAGE;
                int getUsersTo = (getUsersFrom + StewConstant.ITEM_PER_PAGE_MANAGE) < totalRecords ? getUsersFrom + StewConstant.ITEM_PER_PAGE_MANAGE : totalRecords;

                for (int i = getUsersFrom; i < getUsersTo; i++) {
                    lstUserModels.add(lstAllUserModels.get(i));
                }

                result.setListItems(lstUserModels);
            }

            result.setStatus(StewConstant.STATUS_CODE_OK);
            result.setTotal(totalPage);
        }

        return result;        
    }
    /**
     * get user with paging number
     *
     * @param pageNumber
     * @return
     */
    public DataModel getUsersByPageNumberAndGroupID(int pageNumber, String groupIDString) {
        UserAdapter userAdapter = new UserAdapter();
        DataModel result = new DataModel();

        try {
            Long groupID = Long.parseLong(groupIDString);
            UserGroupDetailAdapter userGroupDetailAdapter = new UserGroupDetailAdapter();
            List<User> lstAllUsers = new LinkedList<User>();
            List<UserGroupDetail> lstUserGroupDetail = userGroupDetailAdapter.getUserGroupByGroupID(groupID);
            int count = lstUserGroupDetail.size();

            if (lstUserGroupDetail != null && count != 0) {
                for (int i = 0; i < count; i++) {
                    String selectedEmail = lstUserGroupDetail.get(i).getUserId();
                    User selectedUser = userAdapter.getUserByEmail(selectedEmail);
                    if (selectedUser != null) {
                        lstAllUsers.add(selectedUser);
                    }
                }
            }

            int totalRecords = lstAllUsers.size();

            if (totalRecords > 0 && lstAllUsers != null) {
                int totalPage = totalRecords / StewConstant.ITEM_PER_PAGE_MANAGE + 1;
                if (totalRecords % StewConstant.ITEM_PER_PAGE_MANAGE == 0) {
                    totalPage = totalPage - 1;
                }
                if (totalRecords <= StewConstant.ITEM_PER_PAGE_MANAGE) {
                    result.setListItems(lstAllUsers);
                } else {
                    List<User> lstUsers = new LinkedList<User>();
                    int getUsersFrom = pageNumber * StewConstant.ITEM_PER_PAGE_MANAGE;
                    int getUsersTo = (getUsersFrom + StewConstant.ITEM_PER_PAGE_MANAGE) < totalRecords ? getUsersFrom + StewConstant.ITEM_PER_PAGE_MANAGE : totalRecords;

                    for (int i = getUsersFrom; i < getUsersTo; i++) {
                        lstUsers.add(lstAllUsers.get(i));
                    }

                    result.setListItems(lstUsers);
                }

                result.setStatus(StewConstant.STATUS_CODE_OK);
                result.setTotal(totalPage);
            }

        } catch (Exception e) {
        }

        return result;
    }

    /**
     * get all roles
     *
     * @return
     */
    public List<Role> getRoles() {
        RoleAdapter roleAdapter = new RoleAdapter();
        List<Role> result = roleAdapter.getRoles();

        return result;
    }

    /**
     * get user by email
     *
     * @param email
     * @return
     */
    public User getUserByEmail(String email) {
        UserAdapter userAdapter = new UserAdapter();
        User result = userAdapter.getUserByEmail(email);

        return result;
    }

    /**
     * get list group by email
     *
     * @param email
     * @return
     */
    public List<UserGroupDetail> getUserGroupDetail(String email) {
        UserGroupDetailAdapter userGroupDetailAdapter = new UserGroupDetailAdapter();
        List<UserGroupDetail> result = userGroupDetailAdapter.getUserGroupByEmail(email);

        return result;
    }

    /**
     * @bref logout
     * @param userToken
     * @return
     */
    public Status logoutAndRemoveUserSession(String userToken) {
        Status result = new Status();
        UserSessionAdapter adapter = new UserSessionAdapter();
        if (adapter.deleteUserSession(userToken)) {
            result.setStatus(StewConstant.OK_STATUS);
        } else {
            result.setStatus(StewConstant.ERR_STATUS);
            result.setDescription(StewConstant.ERR_DEL_USERSESSION);
        }
        return result;
    }
}
