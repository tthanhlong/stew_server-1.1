<%-- 
    Document   : edit_user
    Created on : Aug 29, 2013, 9:34:28 AM
    Author     : tthanhlong
--%>


<%@page import="jp.co.ncdc.stew.Utils.StewConstant"%>
<%@page import="jp.co.ncdc.stew.Entities.UserGroupDetail"%>
<%@page import="jp.co.ncdc.stew.Entities.GroupUser"%>
<%@page import="jp.co.ncdc.stew.Entities.Role"%>
<%@page import="java.util.List"%>
<%@page import="jp.co.ncdc.stew.Entities.User"%>
<%@page import="jp.co.ncdc.stew.Controllers.GroupController"%>
<%@page import="jp.co.ncdc.stew.Controllers.UserController"%>
<%@page import="jp.co.ncdc.stew.Managers.LanguageManager"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <script type="text/javascript" src="../js/jquery-1.9.1.min.js"></script>
        <script type="text/javascript" src="../js/common.js"></script>
        <title><%=LanguageManager.getInstance().getText("editUserLabel", request)%></title>
        <%
            User a = (User) session.getAttribute(StewConstant.USER_SESSION);
            String emailLogin ="";
            if(a!=null){
                emailLogin = a.getEmail();
            }
            String email = request.getParameter("email");
            UserController userController = new UserController();
            GroupController groupController = new GroupController();
            List<Role> lstAllRoles = userController.getRoles();
            List<GroupUser> lstAllGroups = groupController.getGroups();
            
            User user = userController.getUserByEmail(email);
            List<GroupUser> lstGroups = groupController.getUserGroupDetailByUserIdWithManagerRole(emailLogin);
            
            List<UserGroupDetail> lstGroupDetails = userController.getUserGroupDetail(email);
            int countGroupDetail = lstGroupDetails.size();
            boolean flag = false;
            if(user != null){
                flag = true;
            }
        %>
        <%!
            public String getRoleName(long roleID, List<Role> allRoles){
                String roleName = "";
                int countRole = allRoles.size();
                if (allRoles != null && countRole != 0) {
                    for (int i = 0; i < countRole; i++) {
                        if (allRoles.get(i).getRoleId() == roleID) {
                            roleName = allRoles.get(i).getRoleName();
                            break;
                        }
                    }
                }
                
                return roleName;
            }
            
            public String getGroupName(long groupID, List<GroupUser> groups){
                String groupName = "";
                int countGroup = groups.size();
                if(groups != null && countGroup != 0){
                    for(int i = 0; i < countGroup; i++){
                        if(groups.get(i).getGroupId() == groupID){
                            groupName = groups.get(i).getName();
                            break;
                        }
                    }
                }
                
                return groupName;
            }
            
            public boolean checkGroupAuthorized(long groupID, List<GroupUser> groups){
                boolean result = false;
                int countGroup = groups.size();
                if (groups != null && countGroup != 0) {
                    for (int i = 0; i < countGroup; i++) {
                        if (groups.get(i).getGroupId().equals(groupID)) {
                            result = true;
                            break;
                        }
                    }
                }
                return result;
            }
        %>
        <script type="text/javascript">
            $(document).ready(function() {
                $('#user-menu').removeClass("has-hover");
                $('#user-menu').addClass("selected-menu");
                var flag = true;
                $('#saveUser').click(function() {
                    if (flag) {
                        $('#password-validate').html("");
                        $('#user-validate').html("");
                        $('#role-validate').html("");
                        $('#last-name-validate').html("");
                        $('#first-name-validate').html("");
                        $('#group-role-validate').html("");

                        var email = $('#email').val();
                        var lastname = $('#lastname').val();
                        var firstname = $('#firstname').val();
                        var pwd = $('#password').val();
                        var confirmPwd = $('#re-password').val();
                        var groups = new Array();
                        var roles = new Array();
                        var index = 0;
                        
                        $(".new-group-role").each(function(){
                            groups[index] = $(this).find(".group").val();
                            roles[index] = $(this).find(".role").val();
                            index++;
                        });
                        
//                        console.log("groups" + groups);
//                        console.log("roles" + roles);
                        if (checkGroupValidate(groups) && checkDuplicate(groups)){
                            if (checkGroupValidate(roles)) {
                                if (isValidEmail(email)) {
                                    if (isRequired(lastname)) {
                                        if (isRequired(firstname)) {
                                            if (isValidPassword(pwd, confirmPwd)) {
                                                $.ajax({
                                                    type: "POST",
                                                    dataType: "json",
                                                    url: "UserAction",
                                                    beforeSend: function(){
                                                        flag = false;
                                                    },
                                                    complete: function(){
                                                        flag = true;
                                                    },
                                                    data: {
                                                        "email": email,
                                                        "pwd": pwd,
                                                        "lastname": lastname,
                                                        "firstname": firstname,
                                                        "roles": roles.join(),
                                                        "groups": groups.join(),
                                                        "type": EDIT
                                                    },
                                                    success: function(data) {
                                                        if (data.status === STATUS_CODE_OK) {
                                                            window.location.href = "user.jsp";
                                                        }
                                                    },
                                                    error: function() {
                                                        alert("error create user");
                                                    }
                                                });
                                            } else {
                                                $('#password-validate').html(" <%=LanguageManager.getInstance().getText("passwordInvalid", request)%>");
                                            }
                                        } else {
                                            $('#first-name-validate').html(" <%=LanguageManager.getInstance().getText("requiredField", request)%>");
                                        }
                                    } else {
                                        $('#last-name-validate').html(" <%=LanguageManager.getInstance().getText("requiredField", request)%>");
                                    }
                                } else {
                                    $('#user-validate').html(" <%=LanguageManager.getInstance().getText("emailInvalid", request)%>");
                                }
                            }else{
                                $('#group-role-validate').html(" <%=LanguageManager.getInstance().getText("roleInvalid", request)%>");
                            }
                        }else{
                            $('#group-role-validate').html(" <%=LanguageManager.getInstance().getText("groupInvalid", request)%>");
                        }
                    }
                });
            });
            
            //Handle click on plus icon
            function addGroupRole(addIcon){
                $('#list-group-role').append("<tr class='new-group-role group-role-enable'>"+$('#list-group-role > tbody > tr:first').html()+"</tr>");
                $(addIcon).hide();
            }
            
            //Handle click on minus icon
            function removeGroupRole(removeIcon){
                if ($('.group-role-enable').length > 1) {
                    $(removeIcon).parent().parent().remove();
                    $('.group-role-enable:last').find(".plus-icon").show();
                }
            }
            //Get role for user by group id
            function getRoleForUserByGroupID(groupID){
                $(groupID).parent().parent().find(".role").children().remove();
                $(groupID).parent().parent().find(".role").append('<option value="-1">--<%=LanguageManager.getInstance().getText("chkSelectRole", request)%>--</option>');
                
                var groupId = $(groupID).val();
                $.ajax({
                    type: "POST",
                    dataType: "json",
                    url: "UserAction",
                    beforeSend: function(){
                        $(groupID).parent().parent().find(".role").attr("disabled", true);
                    },
                    complete: function(){
                        $(groupID).parent().parent().find(".role").attr("disabled", false);
                    },
                    data: {
                        "email": "<%=emailLogin%>",
                        "groupID": groupId,
                        "type": GET_ROLE_BY_USER_AND_GROUP
                    },
                    success: function(data) {
                        if (data.status === STATUS_CODE_OK) {
                           var roleOfGroup = $(groupID).parent().parent().find(".role");
                           if (data.roles.length > 0) {
                                $.each(data.roles, function (i, item) {
                                    console.log("id: " + item.roleId + ", roleName: " + item.roleName);
                                    $(roleOfGroup).append('<option value="'+ item.roleId +'">' + item.roleName + '</option>')
                                });
                            }
                        }
                    },
                    error: function() {
                        alert("error get role by group and user");
                    }
                });
            }
            
            function deleteUser(email){
                if (confirm("<%=LanguageManager.getInstance().getText("confirmDelete", request)%>")) {
                        $.ajax({
                            type: "POST",
                            dataType: "json",
                            url: "UserAction",
                            beforeSend: function() {
                                //before send
                            },
                            complete: function() {
                                //complete
                            },
                            data: {
                                "email": email,
                                "type": DELETE_USER_BY_EMAIL
                            },
                            success: function(data) {
                                if (data.status === STATUS_CODE_OK) {
                                    window.location.href = "user.jsp";
                                }else{
                                    alert("<%=LanguageManager.getInstance().getText("errorDeleteUser", request)%>");
                                }
                            },
                            error: function() {
                                //error
                            }
                        });
                    }
            }
        </script>
    </head>
    <body>
        <div class="page_content">
             <jsp:include page="../common/header.jsp" />
             <div class="content">
                <div class="content_header">
                    <%=LanguageManager.getInstance().getText("editUserLabel", request)%>
                </div>  
                <table class="table_content create_user_content">
                    <tr class="group_name_box">
                        <td class="td_lable"><%=LanguageManager.getInstance().getText("account", request)%>:<span class="validate">(*)</span></td>
                        <td><input disabled="disabled" class="user-info" type="text" required="true" id="email" value="<%=flag?user.getEmail():""%>"/><span class="validate" id="user-validate"></span></td>
                    </tr>
                    <tr class="group_name_box">
                        <td class="td_lable"><%=LanguageManager.getInstance().getText("tdLastName", request)%>:<span class="validate">(*)</span></td>
                        <td><input class="user-info" type="text" required="true" id="lastname" value="<%=flag?user.getLastName():""%>"/><span class="validate" id="last-name-validate"></span></td>
                    </tr>
                    <tr class="group_name_box">
                        <td class="td_lable"><%=LanguageManager.getInstance().getText("tdFirstName", request)%>:<span class="validate">(*)</span></td>
                        <td><input class="user-info" type="text" required="true" id="firstname" value="<%=flag?user.getFirstName():""%>"/><span class="validate" id="first-name-validate"></span></td>
                    </tr>
                    <tr class="group_name_box">
                        <td class="td_lable"><%=LanguageManager.getInstance().getText("tdPassword", request)%>:<span class="validate">(*)</span></td>
                        <td><input class="user-info" type="password" required="true" id="password" value="<%=flag?user.getPasswordHash():""%>"/><span class="validate" id="password-validate"></span></td>
                    </tr>
                    <tr class="group_name_box">
                        <td class="td_lable"><%=LanguageManager.getInstance().getText("tdConfirmPassword", request)%>:<span class="validate">(*)</span></td>
                        <td><input class="user-info" type="password" required="true" id="re-password" value="<%=flag?user.getPasswordHash():""%>"/></td>
                    </tr>
                    <tr>
                        <td class="td_lable"><%=LanguageManager.getInstance().getText("thGroupRole", request)%>:<span class="validate">(*)</span></td>
                        <td>
                            <table id="list-group-role">
                                <tr class="hidden-group-role" style="display: none">
                                    <td>
                                        <select class="group" onchange="getRoleForUserByGroupID(this);">
                                            <option value="-1">--<%=LanguageManager.getInstance().getText("chkSelectGroup", request)%>--</option>
                                            <%
                                                if (lstGroups != null && lstGroups.size() > 0) {
                                                    int countGroup = lstGroups.size();
                                                    for (int i = 0; i < countGroup; i++) {
                                            %>
                                                <option value="<%=lstGroups.get(i).getGroupId()%>"><%=lstGroups.get(i).getName()%></option>
                                            <%
                                                    }
                                                }
                                            %>
                                        </select>
                                    </td>
                                    <td>
                                        <select class="role" disabled="disabled">
                                            <option value="-1">--<%=LanguageManager.getInstance().getText("chkSelectRole", request)%>--</option>
                                        </select>
                                        <span class="minus-icon" onclick="removeGroupRole(this)"></span>
                                        <span class="plus-icon" onclick="addGroupRole(this);"></span>
                                    </td>
                                </tr>
                                <%
                                    if(lstGroupDetails != null && countGroupDetail != 0){
                                        for(int k = 0; k < countGroupDetail; k++){
                                            if(checkGroupAuthorized(lstGroupDetails.get(k).getGroupId(), lstGroups)){
                                                %>
                                                <tr class="new-group-role group-role-enable">
                                                    <td>
                                                        <select class="group" onchange="getRoleForUserByGroupID(this);">
                                                            <option value="-1">--<%=LanguageManager.getInstance().getText("chkSelectGroup", request)%>--</option>
                                                            <%
                                                                int countGroup = lstGroups.size();
                                                                if (lstGroups != null && lstGroups.size() > 0) {    
                                                                    for (int i = 0; i < countGroup; i++) {
                                                                        if(lstGroups.get(i).getGroupId() == lstGroupDetails.get(k).getGroupId()){
                                                                            %>
                                                                            <option value="<%=lstGroups.get(i).getGroupId()%>" selected><%=lstGroups.get(i).getName()%></option>
                                                                            <%
                                                                        }else{
                                                                            %>
                                                                            <option value="<%=lstGroups.get(i).getGroupId()%>"><%=lstGroups.get(i).getName()%></option>
                                                                            <%
                                                                        }
                                                                    }
                                                                }
                                                            %>
                                                        </select>
                                                    </td>
                                                    <td>
                                                        <select class="role" disabled="disabled">
                                                            <option value="<%=lstGroupDetails.get(k).getRoleId()%>"><%=getRoleName(lstGroupDetails.get(k).getRoleId(), lstAllRoles)%></option>
                                                        </select>
                                                        <span class="minus-icon" onclick="removeGroupRole(this)"></span>
                                                        <%
                                                            if(countGroup > 1 && k != countGroupDetail - 1){
                                                            %>
                                                                <span class="plus-icon" onclick="addGroupRole(this);" style="display: none"></span>
                                                            <%
                                                            }else{
                                                            %>
                                                                <span class="plus-icon" onclick="addGroupRole(this);"></span>
                                                            <%
                                                        }
                                                        %>
                                                    </td>
                                                </tr>
                                                <%
                                            }else{
                                                %>
                                                <tr class="new-group-role group-role-disable">
                                                    <td>
                                                        <select class="group" disabled="disabled">
                                                            <option value="<%=lstGroupDetails.get(k).getGroupId()%>"><%=getGroupName(lstGroupDetails.get(k).getGroupId(), lstAllGroups)%></option>
                                                        </select>
                                                    </td>
                                                    <td>
                                                        <select class="role" disabled="disabled">
                                                            <option value="<%=lstGroupDetails.get(k).getRoleId()%>"><%=getRoleName(lstGroupDetails.get(k).getRoleId(), lstAllRoles)%></option>
                                                        </select>
                                                    </td>
                                                </tr>
                                                <%
                                            }
                                        }
                                    }
                                %>
                                </table>
                            <span class="validate" id="group-role-validate"></span>
                        </td>
                    </tr>
                    <tr>
                        <td>&nbsp;</td>
                        <td>                  
                            <div class="button_group">
                                <input type="button" class="cancel-btn" onclick="{window.location = 'user.jsp'}"/>
                                <input type="button" id="saveUser" class="save-btn"/>
                            </div>
                            <div>
                                <input type="button" class="delete-btn" onclick="deleteUser('<%=email%>');">
                            </div>
                        </td>
                    </tr>
                </table>
            </div>
        </div>
    </body>
</html>
