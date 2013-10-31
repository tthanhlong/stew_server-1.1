/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jp.co.ncdc.stew.Servlet;

import com.google.gson.Gson;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import jp.co.ncdc.stew.Adapters.RoleAdapter;
import jp.co.ncdc.stew.Adapters.UserAdapter;
import jp.co.ncdc.stew.Adapters.UserGroupDetailAdapter;
import jp.co.ncdc.stew.Entities.Role;
import jp.co.ncdc.stew.Entities.User;
import jp.co.ncdc.stew.Entities.UserGroupDetail;
import jp.co.ncdc.stew.Servlet.model.Status;
import jp.co.ncdc.stew.Utils.CipherUtils;
import jp.co.ncdc.stew.Utils.StewConstant;

/**
 *
 * @author tthanhlong
 */
public class UserServlet extends HttpServlet {

    UserAdapter userAdapter = new UserAdapter();
    UserGroupDetailAdapter userGroupDetailAdapter = new UserGroupDetailAdapter();
    int okStatus = StewConstant.STATUS_CODE_OK;
    int error = StewConstant.STATUS_CODE_NOT_FOUND;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        String result = "";

        String typeString = request.getParameter("type");
        String email = request.getParameter("email");
        String password = request.getParameter("pwd");
        String firstName = request.getParameter("firstname");
        String lastName = request.getParameter("lastname");
        String roles = request.getParameter("roles");
        String groups = request.getParameter("groups");
        String groupIDString = request.getParameter("groupID");
        String deleteIds = request.getParameter("deleteids");
        String userIds = request.getParameter("userids");
        String delims = "[,]";

        String[] lstGroups = {};
        if (groups != null && !"".equals(groups)) {
            lstGroups = groups.split(delims);
        }
        String[] lstRoles = {};
        if (roles != null && !"".equals(roles)) {
            lstRoles = roles.split(delims);
        }
        String[] lstDeleteIds = {};
        if (deleteIds != null && !"".equals(deleteIds)) {
            lstDeleteIds = deleteIds.split(delims);
        }
        String[] lstUserIds = {};
        if (userIds != null && !"".equals(userIds)) {
            lstUserIds = userIds.split(delims);
        }
        int countGroup = lstGroups.length;
        
        int type = 0;
        try {
            type = Integer.parseInt(typeString);
        } catch (Exception e) {
        }
        User user = userAdapter.getUserByEmail(email);

        switch (type) {
            case StewConstant.LOGIN:
                result = doLogin(user, password, request);
                break;
            case StewConstant.LOGOUT:
                result = doLogout(email, request);
                break;
            case StewConstant.CREATE:
                if (user != null) {
                    result = "{\"status\": " + StewConstant.EXISTED_USER + "}";
                } else {
                    User newUser = new User();
                    newUser.setEmail(email);
                    newUser.setPasswordHash(password);
                    newUser.setCreateDate(new Date());
                    newUser.setFirstName(firstName);
                    newUser.setLastName(lastName);

                    if (userAdapter.addUser(newUser)) {
                        for (int i = 0; i < countGroup; i++) {
                            String group = lstGroups[i];
                            String role = lstRoles[i];
                            if (!"".equals(group) && !"".equals(role)) {
                                long groupID = Long.parseLong(group);
                                long roleID = Long.parseLong(role);
                                
                                UserGroupDetail userGroupDetail = new UserGroupDetail();
                                userGroupDetail.setUserId(email);
                                userGroupDetail.setRoleId(roleID);
                                userGroupDetail.setGroupId(groupID);
                                
                                userGroupDetailAdapter.addUserGroup(userGroupDetail);
                            }
                        }
                        result = "{\"status\": " + StewConstant.STATUS_CODE_OK + "}";
                    } else {
                        result = "{\"status\": " + StewConstant.EXISTED_USER + "}";
                    }
                }
                break;
            case StewConstant.EDIT:
                //code for edit user
                if (user != null) {
                    String currentPwd = user.getPasswordHash();
                    if (password == null ? currentPwd != null : !password.equals(currentPwd)) {
                        user.setPasswordHash(password);
                    } else {
                        user.setPasswordHash(CipherUtils.decrypt(currentPwd));
                    }
                    user.setCreateDate(new Date());
                    user.setFirstName(firstName);
                    user.setLastName(lastName);

                    if (userAdapter.updateUser(user) && userGroupDetailAdapter.deleteUserGroupByEmail(email)) {
                        for (int i = 0; i < countGroup; i++) {
                            String group = lstGroups[i];
                            String role = lstRoles[i];
                            if (!"".equals(group) && !"".equals(role)) {
                                long groupID = Long.parseLong(group);
                                long roleID = Long.parseLong(role);
                                
                                UserGroupDetail userGroupDetail = new UserGroupDetail();
                                userGroupDetail.setUserId(email);
                                userGroupDetail.setRoleId(roleID);
                                userGroupDetail.setGroupId(groupID);
                                
                                userGroupDetailAdapter.addUserGroup(userGroupDetail);
                            }
                        }
                    }
                    result = "{\"status\": " + StewConstant.STATUS_CODE_OK + "}";
                } else {
                    result = "{\"status\": " + StewConstant.STATUS_CODE_NOT_FOUND + "}";
                }
                break;
            case StewConstant.DELETE:
                int countDeleteId = lstDeleteIds.length;
                for (int i = 0; i < countDeleteId; i++) {
                    String emailSelected = lstUserIds[i];
                    String deleteID = lstDeleteIds[i];
                    if (deleteID != null && !deleteID.equals("")) {
                        UserGroupDetail userGroupDetail = userGroupDetailAdapter.getUserGroupDetailByID(Long.parseLong(deleteID));
                        if (userGroupDetail != null) {
                            List<UserGroupDetail> lstUserGroupDetailsByEmail = userGroupDetailAdapter.getUserGroupByEmail(emailSelected);

                            int count = lstUserGroupDetailsByEmail.size();
                            if (count == 1) {
                                userAdapter.deleteUser(emailSelected);
                            }
                            userGroupDetailAdapter.deleteUserByUserGroupID(Long.parseLong(deleteID));
                        }
                    }
                }
                result = "{\"status\": " + StewConstant.STATUS_CODE_OK + "}";
                
                break;
            case StewConstant.DELETE_USER_GROUP:
                try {
                    Long groupID = Long.parseLong(groupIDString);
                    if (userGroupDetailAdapter.deleteUserGroupByEmailAndGroupID(email, groupID)) {
                        result = "{\"status\": " + StewConstant.STATUS_CODE_OK + "}";
                    } else {
                        result = "{\"status\": " + StewConstant.STATUS_CODE_NOT_FOUND + "}";
                    }
                } catch (Exception e) {
                    result = "{\"status\": " + StewConstant.STATUS_CODE_NOT_FOUND + "}";
                }
                break;
            case StewConstant.GET_ROLE_BY_USER_AND_GROUP:
                try {
                    UserGroupDetailAdapter userGroupDetailAdapter = new UserGroupDetailAdapter();
                    boolean isSuperAdmin = userGroupDetailAdapter.checkUserIsSupperAdmin(email);
                    List<Role> lstRolesOfGroup = null;
                    RoleAdapter roleAdapter = new RoleAdapter();
                    if (isSuperAdmin) {
                        //is super admin, load all roles except super admin
                        lstRolesOfGroup = roleAdapter.getRolesLower(StewConstant.ROLE_SUPPER);

                    }else{
                        //not super admin, load all lower roles
                        UserGroupDetail userGroupDetail = userGroupDetailAdapter.getUserGroupByEmailAndGroup(email, groupIDString);
                        long roleID = userGroupDetail.getRoleId();
                        lstRolesOfGroup = roleAdapter.getRolesLower(roleID);

                    }
                    String lstRolesString = new Gson().toJson(lstRolesOfGroup);
                    
                    result = "{\"status\": " + StewConstant.STATUS_CODE_OK + ", \"roles\": "+ lstRolesString +"}";
                } catch (Exception e) {
                    result = "{\"status\": " + StewConstant.STATUS_CODE_NOT_FOUND + "}";
                }
                
                break;
            case StewConstant.DELETE_USER_BY_EMAIL:
                //delete user from edit user screeen
                try {
                    userGroupDetailAdapter.deleteUserGroupByEmail(email);
                    userAdapter.deleteUser(email);
                    result = "{\"status\": " + StewConstant.STATUS_CODE_OK + "}";
                } catch (Exception e) {
                    result = "{\"status\": " + StewConstant.STATUS_CODE_NOT_FOUND + "}";
                }
                
                break;
            default:
                break;
        }
        out.write(result);
    }

    /**
     * function login
     *
     * @param user
     * @return
     */
    public String doLogin(User user, String pass, HttpServletRequest request) {
        HttpSession session = request.getSession();
        String result = "";
        Status status = new Status();
        String hashPass = null;
        if (pass != null) {
            hashPass = CipherUtils.encrypt(pass);
        }
        if (user != null) {
            boolean isSuperAdmin = userGroupDetailAdapter.checkUserIsSupperAdmin(user.getEmail());
            if (hashPass != null && hashPass.equals(user.getPasswordHash())) {
                if (isSuperAdmin || userGroupDetailAdapter.getGroupByManagerId(user.getEmail()).size() > 0) {
                    session.setAttribute(StewConstant.USER_SESSION, user);
                    session.setAttribute(StewConstant.IS_SUPPER, isSuperAdmin);
                    status = new Status(okStatus, StewConstant.OK_STATUS);
                } else {
                    status = new Status(error, StewConstant.ERR_WRONG_ROLE);
                }
            } else {
                status = new Status(error, StewConstant.ERR_WRONG_PASS);
            }
        } else {
            status = new Status(error, StewConstant.ERR_WRONG_USER);
        }
        Gson gson = new Gson();
        result = gson.toJson(status);
        return result;
    }

    /**
     *
     * @param user
     * @param request
     * @return
     */
    public String doLogout(String userId, HttpServletRequest request) {
        String result;
        Status status;
        HttpSession session = request.getSession();
        User a = (User) session.getAttribute(StewConstant.USER_SESSION);
        if (a != null && userId.equals(a.getEmail())) {
            session.setAttribute(StewConstant.USER_SESSION, null);
            status = new Status(okStatus, StewConstant.OK_STATUS);
        } else {
            status = new Status(error, StewConstant.ERR_STATUS);
        }
        Gson gson = new Gson();
        result = gson.toJson(status);
        return result;
    }
}
