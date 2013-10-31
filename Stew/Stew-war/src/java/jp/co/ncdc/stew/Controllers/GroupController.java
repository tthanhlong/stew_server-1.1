/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jp.co.ncdc.stew.Controllers;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import jp.co.ncdc.stew.APIs.model.DataModel;
import jp.co.ncdc.stew.Adapters.GroupAdapter;
import jp.co.ncdc.stew.Adapters.GroupAppDetailAdapter;
import jp.co.ncdc.stew.Adapters.UserGroupDetailAdapter;
import jp.co.ncdc.stew.Entities.GroupUser;
import jp.co.ncdc.stew.Entities.UserGroupDetail;
import jp.co.ncdc.stew.Utils.StewConstant;

/**
 *
 * @author tthanhlong
 */
public class GroupController {

    GroupAdapter groupAdapter = new GroupAdapter();

    /**
     * get all group users
     *
     * @return
     */
    public List<GroupUser> getGroups() {

        List<GroupUser> result = groupAdapter.getGroupUsers();

        return result;
    }

    /**
     * get group users with paging number
     *
     * @param pageNumber
     * @return
     */
    public DataModel getGroupUserByPageNumber(int pageNumber) {
        DataModel result = new DataModel();

        List<GroupUser> lstAllGroupUsers = groupAdapter.getGroupUsers();
        int totalRecords = lstAllGroupUsers.size();

        if (totalRecords > 0 && lstAllGroupUsers != null) {
            int totalPage = totalRecords / StewConstant.ITEM_PER_PAGE_MANAGE + 1;
            if (totalRecords % StewConstant.ITEM_PER_PAGE_MANAGE == 0) {
                totalPage = totalPage - 1;
            }
            if (totalRecords <= StewConstant.ITEM_PER_PAGE_MANAGE) {
                result.setListItems(lstAllGroupUsers);
            } else {
                List<GroupUser> lstGroupUsers = new LinkedList<GroupUser>();
                int getUsersFrom = pageNumber * StewConstant.ITEM_PER_PAGE_MANAGE;
                int getUsersTo = (getUsersFrom + StewConstant.ITEM_PER_PAGE_MANAGE) < totalRecords ? getUsersFrom + StewConstant.ITEM_PER_PAGE_MANAGE : totalRecords;

                for (int i = getUsersFrom; i < getUsersTo; i++) {
                    lstGroupUsers.add(lstAllGroupUsers.get(i));
                }

                result.setListItems(lstGroupUsers);
            }

            result.setStatus(StewConstant.STATUS_CODE_OK);
            result.setTotal(totalPage);
        }

        return result;
    }

    /**
     * get group user by id
     *
     * @param groupIDString
     * @return
     */
    public GroupUser getGroupByID(String groupIDString) {
        GroupUser groupUser = null;
        try {
            Long groupID = Long.parseLong(groupIDString);
            groupUser = groupAdapter.getGroupByGroupID(groupID);
        } catch (Exception e) {
        }

        return groupUser;
    }

    /**
     * 
     * @param userID
     * @return 
     */
    public List<GroupUser> getUserGroupDetailByUserIdWithManagerRole(String userID) {
        UserGroupDetailAdapter userGroupDetailAdapter = new UserGroupDetailAdapter();
        boolean isSuperAdmin = userGroupDetailAdapter.checkUserIsSupperAdmin(userID);
        List<GroupUser> lstGroupUsers = null;
        if (isSuperAdmin) {
            lstGroupUsers = getGroups();
        } else {
            lstGroupUsers = new LinkedList<GroupUser>();
            List<UserGroupDetail> lstUserGroupDetail = userGroupDetailAdapter.getUserGroupDetailByUserIdWithManagerRole(userID);

            if (lstUserGroupDetail != null) {
                int countUserGroupDetail = lstUserGroupDetail.size();
                for (int i = 0; i < countUserGroupDetail; i++) {
                    Long groupID = lstUserGroupDetail.get(i).getGroupId();
                    GroupUser groupUser = new GroupAdapter().getGroupByGroupID(groupID);
                    if (groupUser != null) {
                        lstGroupUsers.add(groupUser);
                    }
                }
            }
        }

        return lstGroupUsers;
    }

    /**
     *
     * @param listGroupId
     * @return
     */
    public List<GroupUser> getUserGroupByListIds(List<Long> listGroupId) {
        List<GroupUser> listGroups = new ArrayList<GroupUser>();
        for (int i = 0; i < listGroupId.size(); i++) {
            GroupUser groupUser = groupAdapter.getGroupByGroupID(listGroupId.get(i));
            if (groupUser != null) {
                listGroups.add(groupUser);
            }
        }
        return listGroups;
    }

    
    public boolean deleteGroupByIDs(String[] lstDeleteIds) {
        boolean result = false;
        
        int countDeleteId = lstDeleteIds.length;
        for (int i = 0; i < countDeleteId; i++) {
            String deleteID = lstDeleteIds[i];
            if (deleteID != null && !deleteID.equals("")) {
                long deleteIDLong = 0;
                try {
                    deleteIDLong = Long.parseLong(deleteID);
                } catch (Exception e) {
                }
                
                //delete from group table
                GroupAdapter groupAdapter = new GroupAdapter();
                groupAdapter.deleteGroup(deleteIDLong);
                
                //delete from user group detail
                UserGroupDetailAdapter userGroupDetailAdapter = new UserGroupDetailAdapter();
                userGroupDetailAdapter.deleteUserGroupByGroupID(deleteIDLong);
                
                //delete from group app detail
                GroupAppDetailAdapter groupAppDetailAdapter = new GroupAppDetailAdapter();
                groupAppDetailAdapter.deleteGroupAppDetailByGroupID(deleteIDLong);
                
            }
        }
        result = true;
        return result;
    }

    public boolean removeUsersFromGroup(String email, long groupID, String[] lstDeleteIds) {
        boolean result = false;
        
        int countDeleteId = lstDeleteIds.length;
        UserGroupDetailAdapter userGroupDetailAdapter = new UserGroupDetailAdapter();
        if (userGroupDetailAdapter.checkUserIsSupperAdmin(email)) {
            for (int i = 0; i < countDeleteId; i++) {
                String deleteID = lstDeleteIds[i];
                if (deleteID != null && !deleteID.equals("")) {
                    //delete from user group detail
                    userGroupDetailAdapter.deleteUserGroupByEmailAndGroupID(deleteID, groupID);
                }
            }
            result = true;
        }else{
            UserGroupDetail userGroupDetail = userGroupDetailAdapter.getUserGroupByEmailAndGroup(email, String.valueOf(groupID));
            long roleOfUser = StewConstant.ROLE_USER;
            if (userGroupDetail != null) {
                roleOfUser = userGroupDetail.getRoleId();
            }
            for (int i = 0; i < countDeleteId; i++) {
                String deleteID = lstDeleteIds[i];
                if (deleteID != null && !deleteID.equals("")) {
                    //delete from user group detail
                    userGroupDetailAdapter.removeUserFromGroup(roleOfUser, groupID, deleteID);
                }
            }
            result = true;
        }
        return result;
    }

    public boolean addUsersToGroup(long groupID, String[] lstUserIds, String role) {
        boolean result = false;
        
        int countUserId = lstUserIds.length;
        UserGroupDetailAdapter userGroupDetailAdapter = new UserGroupDetailAdapter();
        for (int i = 0; i < countUserId; i++) {
            String userId = lstUserIds[i];
            if (!userId.equals("") && !userId.equals(null)) {
                //add a new record to group detail
                UserGroupDetail userGroupDetail = new UserGroupDetail(groupID, userId, Long.parseLong(role));
                userGroupDetailAdapter.addUserGroup(userGroupDetail);
            }
        }
        result = true;
        return result;
    }
}
