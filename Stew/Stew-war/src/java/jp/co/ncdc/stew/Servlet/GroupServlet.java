/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jp.co.ncdc.stew.Servlet;

import com.google.gson.Gson;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import jp.co.ncdc.stew.APIs.model.DataModel;
import jp.co.ncdc.stew.APIs.model.UserModel;
import jp.co.ncdc.stew.Adapters.GroupAdapter;
import jp.co.ncdc.stew.Adapters.RoleAdapter;
import jp.co.ncdc.stew.Adapters.UserAdapter;
import jp.co.ncdc.stew.Adapters.UserGroupDetailAdapter;
import jp.co.ncdc.stew.Controllers.GroupController;
import jp.co.ncdc.stew.Entities.GroupUser;
import jp.co.ncdc.stew.Entities.Role;
import jp.co.ncdc.stew.Entities.UserGroupDetail;
import jp.co.ncdc.stew.Servlet.model.GroupList;
import jp.co.ncdc.stew.Servlet.model.Status;
import jp.co.ncdc.stew.Servlet.model.UserAndRole;
import jp.co.ncdc.stew.Servlet.model.ServletModel;
import jp.co.ncdc.stew.Utils.StewConstant;
import jp.co.ncdc.stew.Utils.StewUtils;

/**
 *
 * @author tthanhlong
 */
public class GroupServlet extends HttpServlet {

    GroupAdapter groupAdapter = new GroupAdapter();
    GroupController groupController = new GroupController();
    UserGroupDetailAdapter userGroupDetailAdapter = new UserGroupDetailAdapter();
    Gson gson = new Gson();

    /**
     * Handles the HTTP
     * <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        String result = "";

        String typeString = request.getParameter("type");
        String groupName = request.getParameter("groupName");
        String groupIDString = request.getParameter("groupID");
        String description = request.getParameter("description");
        String pageStr = request.getParameter("page");
        String deleteIds = request.getParameter("deleteids");
        String email = request.getParameter("email");
        String role = request.getParameter("role");
        String key = request.getParameter("key");
        String userIds = request.getParameter("userids");
        int page = 0;
        int type = 0;
        try {
            type = Integer.parseInt(typeString);
            page = Integer.parseInt(pageStr);
        } catch (Exception e) {
        }
        
        long groupID = -1;
        try {
            groupID = Long.parseLong(groupIDString);
        } catch (Exception e) {
        }
        GroupUser groupUser = groupAdapter.getGroupByGroupID(groupID);
        String delims = "[,]";
        String[] lstDeleteIds = {};
        if (deleteIds != null && !"".equals(deleteIds)) {
            lstDeleteIds = deleteIds.split(delims);
        }
        switch (type) {
            case StewConstant.CREATE:
                //create group
                result = createGroup(groupName, description, email);
                break;
            case StewConstant.EDIT:
                //edit group
                if (groupUser != null) {
                    groupUser.setName(groupName);
                    groupUser.setDescription(description);
                    result = updateGroup(groupUser);
                }else{
                    result = gson.toJson(new Status(StewConstant.STATUS_CODE_NOT_FOUND, ""));
                }                
                break;
            case StewConstant.DELETE:
                //delete group
                result = deleteGroup(lstDeleteIds);
                break;
            case StewConstant.GET_USER_BYGROUP:
                //create group
                result = getUserByGroupId(groupUser, page, role, key);
                break;
            case StewConstant.GET_REMAINING_USERS:
                //create group
                result = getRemainingUsersOfGroup(email, groupID, page, key);
                break;
            case StewConstant.REMOVE_USER_FROM_GROUP:
                //create group
                result = removeUserFromGroup(email, groupID, lstDeleteIds);
                break;
            case StewConstant.ADD_USER_TO_GROUP:
                //create group
                String[] lstUserIds = {};
                if (userIds != null && !"".equals(userIds)) {
                    lstUserIds = userIds.split(delims);
                }
                result = addUsersToGroup(groupID, lstUserIds, role);
                break;
            default:
                break;
        }

        out.write(result);
    }

    /**
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        String result = "";

        String typeString = request.getParameter("type");
        String pageStr = request.getParameter("page");
        String email = request.getParameter("userId");
        String group = request.getParameter("group");
        String keySearch = request.getParameter("key");
        int type = 0;
        int page = 0;
        try {
            type = Integer.parseInt(typeString);
            page = Integer.parseInt(pageStr);
        } catch (Exception e) {
        }
        switch (type) {
            case StewConstant.GET_LIST:
                result = getListGroupByUser(email, page, false);
                break;
            case StewConstant.GET_LIST_BY_GROUP_AND_KEY:
                result = getListGroupByUserAndGroupAndKey(email, page, false, group, keySearch);
                break;
            case StewConstant.GET_ALL_LIST:
                result = getListGroupByUser(email, page, true);
                break;
            case StewConstant.GET_ALL_DISTINCT_LIST:
                result = getAllGroupUserByEmail(email);
                break;
            case StewConstant.GET_ROLE_OF_GROUP:
                result = getRolesForGroup();
                break;
            case StewConstant.GET_ROLE_USER_OF_GROUP:
                result = getRolesOfUserForGroup(email, group);
                break;
            default:
                break;
        }
        out.write(result);
    }

    /**
     * get list group by user id
     *
     * @param email
     * @return
     */
    public String getListGroupByUser(String email, int page, boolean isGetAll) {
        String result;
        GroupList groupList = new GroupList();
        List<GroupUser> listGroups = new ArrayList<GroupUser>();
        groupList.setTotal(0);
        groupList.setItems(null);
        groupList.setStatus(StewConstant.STATUS_CODE_NOT_FOUND);
        if (email != null && !"".equals(email)) {
            if (userGroupDetailAdapter.checkUserIsSupperAdmin(email)) {
                listGroups = groupAdapter.getGroupUsers();
            } else {
                List<Long> listGroupId = userGroupDetailAdapter.getGroupByManagerId(email);
                listGroups = groupController.getUserGroupByListIds(listGroupId);
            }

            int total = StewUtils.getTotalPage(StewConstant.ITEM_PER_PAGE_MANAGE, listGroups.size());
            groupList.setTotal(total);
            groupList.setStatus(StewConstant.STATUS_CODE_OK);

            if (isGetAll) {
                groupList.setItems(listGroups);
            } else {
                int from = StewConstant.ITEM_PER_PAGE_MANAGE * page;
                int to = Math.min(listGroups.size(), StewConstant.ITEM_PER_PAGE_MANAGE * (page + 1));
                groupList.setItems(listGroups.subList(from, to));
            }

        }
        result = gson.toJson(groupList);
        return result;
    }
    
    public String getListGroupByUserAndGroupAndKey(String email, int page, boolean isGetAll, String group, String key){
        String result;
        GroupList groupList = new GroupList();
        List<GroupUser> listGroups = new ArrayList<GroupUser>();
        groupList.setTotal(0);
        groupList.setItems(null);
        groupList.setStatus(StewConstant.STATUS_CODE_NOT_FOUND);
        if (email != null && !"".equals(email)) {
            if (userGroupDetailAdapter.checkUserIsSupperAdmin(email)) {
                listGroups = groupAdapter.getGroupUsers();
            } else {
                List<Long> listGroupId = userGroupDetailAdapter.getGroupByManagerId(email);
                listGroups = groupController.getUserGroupByListIds(listGroupId);
            }
            
            List<GroupUser> lstGroupUsers = new ArrayList<GroupUser>();
            for (GroupUser groupUser : listGroups) {
                if (!group.equals("") && groupUser.getName().equals(group) && groupUser.getName().toLowerCase().indexOf(key.toLowerCase()) != -1) {
                    lstGroupUsers.add(groupUser);
                }else if(group.equals("") && groupUser.getName().toLowerCase().indexOf(key.toLowerCase()) != -1){
                    lstGroupUsers.add(groupUser);
                }
            }

            int total = StewUtils.getTotalPage(StewConstant.ITEM_PER_PAGE_MANAGE, lstGroupUsers.size());
            groupList.setTotal(total);
            groupList.setStatus(StewConstant.STATUS_CODE_OK);

            if (isGetAll) {
                groupList.setItems(lstGroupUsers);
            } else {
                int from = StewConstant.ITEM_PER_PAGE_MANAGE * page;
                int to = Math.min(lstGroupUsers.size(), StewConstant.ITEM_PER_PAGE_MANAGE * (page + 1));
                groupList.setItems(lstGroupUsers.subList(from, to));
            }

        }
        result = gson.toJson(groupList);
        return result;
    }
    /**
     * get list group by user id
     *
     * @param email
     * @return
     */
    public String getAllGroupUserByEmail(String email) {
        String result;
        GroupList groupList = new GroupList();
        List<GroupUser> listGroups = new ArrayList<GroupUser>();
        groupList.setTotal(0);
        groupList.setItems(null);
        groupList.setStatus(StewConstant.STATUS_CODE_NOT_FOUND);
        if (email != null && !"".equals(email)) {
            if (userGroupDetailAdapter.checkUserIsSupperAdmin(email)) {
                listGroups = groupAdapter.getGroupUsers();
            } else {
                List<Long> listGroupId = userGroupDetailAdapter.getGroupByManagerId(email);
                listGroups = groupController.getUserGroupByListIds(listGroupId);
            }
            
            Map<String, GroupUser> mapGroupUsers = new HashMap<String, GroupUser>();
            
            for (GroupUser groupUser:listGroups) {
                if (groupUser != null)
                    mapGroupUsers.put(groupUser.getName(), groupUser);
            }
            
            List<GroupUser> lstNewGroupUsers = new ArrayList<GroupUser>(mapGroupUsers.values());
            
            int total = lstNewGroupUsers.size();
            groupList.setTotal(total);
            groupList.setStatus(StewConstant.STATUS_CODE_OK);

            groupList.setItems(lstNewGroupUsers);
        }
        result = gson.toJson(groupList);
        return result;
    }

    /**
     *
     * @param name
     * @param description
     * @return
     */
    public String createGroup(String name, String description, String email) {
        String result;
        Status status;
        GroupUser newGroupUser = new GroupUser(name, description);
        long newGroupID = groupAdapter.getGroupIDAfterInsertGroup(newGroupUser);
        if (newGroupID != -1) {
            boolean isSuperAdmin = userGroupDetailAdapter.checkUserIsSupperAdmin(email);
            if (!isSuperAdmin) {
                UserGroupDetail userGroupDetail = new UserGroupDetail(newGroupID, email, StewConstant.ROLE_ADMIN);
                userGroupDetailAdapter.addUserGroup(userGroupDetail);
            }
            status = new Status(StewConstant.STATUS_CODE_OK, "");
        } else {
            status = new Status(StewConstant.STATUS_CODE_NOT_FOUND, "");
        }
        result = gson.toJson(status);
        return result;
    }

    /**
     *
     * @param name
     * @param description
     * @return
     */
    public String updateGroup(GroupUser groupUser) {
        String result;
        Status status;
        if (groupUser != null) {
            if (groupAdapter.updateGroup(groupUser)) {
                status = new Status(StewConstant.STATUS_CODE_OK, "");
            } else {
                status = new Status(StewConstant.STATUS_CODE_NOT_FOUND, "");
            }
        } else {
            status = new Status(StewConstant.STATUS_CODE_NOT_FOUND, "");
        }
        result = gson.toJson(status);
        return result;
    }

    /**
     *
     * @param name
     * @param description
     * @return
     */
    public String deleteGroup(String[] lstDelete) {
        String result;
        Status status;
        if (lstDelete.length == 0) {
            status = new Status(StewConstant.STATUS_CODE_NOT_FOUND, "");
        } else {
            if (groupController.deleteGroupByIDs(lstDelete)) {
                status = new Status(StewConstant.STATUS_CODE_OK, "");
            } else {
                status = new Status(StewConstant.STATUS_CODE_NOT_FOUND, "");
            }
        }
        result = gson.toJson(status);
        return result;
    }

    /**
     *
     * @param groupUser
     * @return
     */
    public String getUserByGroupId(GroupUser groupUser, int page, String role, String key) {
        String result;
        ServletModel userOfGroup = new ServletModel();
        List<UserAndRole> listUser = new ArrayList<UserAndRole>();
        userOfGroup.setStatus(StewConstant.STATUS_CODE_NOT_FOUND);
        if (groupUser != null) {
            listUser = userGroupDetailAdapter.getUserByGroupId(groupUser.getGroupId(), role, key);
            int from = StewConstant.ITEM_PER_PAGE_MANAGE * page;
            int to = Math.min(listUser.size(), StewConstant.ITEM_PER_PAGE_MANAGE * (page + 1));
            int total = StewUtils.getTotalPage(StewConstant.ITEM_PER_PAGE_MANAGE, listUser.size());
            userOfGroup.setStatus(StewConstant.STATUS_CODE_OK);
            userOfGroup.setTotal(total);
            userOfGroup.setGroupName(groupUser.getName());
            userOfGroup.setItems(listUser.subList(from, to));
        }
        result = gson.toJson(userOfGroup);
        return result;
    }

    private String removeUserFromGroup(String email, long groupID, String[] lstDeleteIds) {
        String result;
        Status status;
        if (lstDeleteIds.length == 0) {
            status = new Status(StewConstant.STATUS_CODE_NOT_FOUND, "");
        } else {
            if (groupController.removeUsersFromGroup(email, groupID, lstDeleteIds)) {
                status = new Status(StewConstant.STATUS_CODE_OK, "");
            } else {
                status = new Status(StewConstant.STATUS_CODE_NOT_FOUND, "");
            }
        }
        result = gson.toJson(status);
        return result;
    }

    private String getRolesForGroup() {
        String result;
        DataModel dataModel = new DataModel();
        
        dataModel.setStatus(StewConstant.STATUS_CODE_OK);
        List<Role> lstRoles = new RoleAdapter().getRolesForGroup();
        dataModel.setTotal(lstRoles.size());
        dataModel.setListItems(lstRoles);
        
        result = gson.toJson(dataModel);
        return result;
    }

    private String getRemainingUsersOfGroup(String email, long groupID, int page, String key) {
        String result;
        DataModel dataModel = new DataModel();
        
        
        UserAdapter userAdapter = new UserAdapter();
        List<Object> lstObjects = userAdapter.getRemainingUsersOfGroup(email, groupID, key);
        Map<String, Object> lstUserDistinct = new HashMap<String, Object>();
        for (Object userModel:lstObjects) {
            if (userModel != null){
                Object[] ojb = (Object[]) userModel;
                lstUserDistinct.put((String) ojb[0], userModel);
            }
        }
        
        int from = StewConstant.ITEM_PER_PAGE_MANAGE * page;
        int to = Math.min(lstUserDistinct.size(), StewConstant.ITEM_PER_PAGE_MANAGE * (page + 1));
        int total = StewUtils.getTotalPage(StewConstant.ITEM_PER_PAGE_MANAGE, lstUserDistinct.size());
        
        List<UserModel> lstAllUsers = new LinkedList<UserModel>();
        Collection<Object> userCollection = lstUserDistinct.values();
        Object[] userObjects = userCollection.toArray();
        int countUser = userCollection.size();
        for (int i = 0; i < countUser; i++) {
            UserModel userModel = new UserModel();
            try {
                Object[] item = (Object[]) userObjects[i];
                userModel.setEmail((String) item[0]);
                userModel.setFirstName((String) item[1]);
                userModel.setLastName((String) item[2]);
                userModel.setDateCreated((Date) item[3]);
            } catch (Exception e) {
            }
            lstAllUsers.add(userModel);            
        }
        
        dataModel.setStatus(StewConstant.STATUS_CODE_OK);
        dataModel.setTotal(total);
        dataModel.setListItems(lstAllUsers.subList(from, to));
        
        result = gson.toJson(dataModel);
        return result;
    }
    
//    public static void main(String[] args){
//        GroupServlet groupServlet = new GroupServlet();
//        groupServlet.getRemainingUsersOfGroup("tthanhlong@tma.com.vn", 1, 0, "");
//    }

    private String getRolesOfUserForGroup(String email, String group) {
        String result;
        DataModel dataModel = new DataModel();
        
        dataModel.setStatus(StewConstant.STATUS_CODE_OK);
        List<Role> lstRoles = new RoleAdapter().getRolesOfUserForGroup(email, group);
        dataModel.setTotal(lstRoles.size());
        dataModel.setListItems(lstRoles);
        
        result = gson.toJson(dataModel);
        return result;
    }

    private String addUsersToGroup(long groupID, String[] lstUserIds, String role) {
        String result;
        Status status;
        if (lstUserIds.length == 0) {
            status = new Status(StewConstant.STATUS_CODE_NOT_FOUND, "");
        } else {
            if (groupController.addUsersToGroup(groupID, lstUserIds, role)) {
                status = new Status(StewConstant.STATUS_CODE_OK, "");
            } else {
                status = new Status(StewConstant.STATUS_CODE_NOT_FOUND, "");
            }
        }
        result = gson.toJson(status);
        return result;
    }
}
