<%-- 
    Document   : create_user
    Created on : Aug 29, 2013, 9:34:37 AM
    Author     : tthanhlong
--%>

<%@page import="jp.co.ncdc.stew.Entities.User"%>
<%@page import="jp.co.ncdc.stew.Utils.StewConstant"%>
<%@page import="jp.co.ncdc.stew.Controllers.GroupController"%>
<%@page import="jp.co.ncdc.stew.Entities.GroupUser"%>
<%@page import="jp.co.ncdc.stew.Managers.LanguageManager"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <script type="text/javascript" src="../js/jquery-1.9.1.min.js"></script>
        <script type="text/javascript" src="../js/common.js"></script>
        <title>Create group</title>
        <script type="text/javascript">
            <%
                User a = (User) session.getAttribute(StewConstant.USER_SESSION);
                String email = "";
                if (a != null) {
                    email = a.getEmail();
                }
                String edit = request.getParameter("edit");
                String groupId = "";
                String name = "";
                String description = "";
                if (edit != null) {
                    groupId = edit;
                    GroupController groupController = new GroupController();
                    GroupUser groupUser = groupController.getGroupByID(groupId);
                    if (groupUser != null) {
                        name = groupUser.getName();
                        description = groupUser.getDescription();
                    }
                }
            %>
            var groupId = "<%=groupId%>";
            $(document).ready(function() {
                var editID = <%=edit%>;
                if (editID !== undefined && editID !== null && editID !== "") {
                    $('.delete-btn').show();
                }
                $('#group-menu').addClass("selected-menu");
                bindGroupData();
            });
            /**
             * 
             * @returns {undefined}
             */
            function bindGroupData() {
                if (groupId != "") {
                    $("#page_nane").html("<%=LanguageManager.getInstance().getText("editGroup", request)%>")
                    $('#group-name').val("<%=name%>");
                    $('#group-description').val("<%=description%>");
                }
            }
            /**
             * 
             * @returns {undefined}  
             */
            function createEditGroupSubmit() {
                var email = "<%=email%>";
                var groupName = $('#group-name').val();
                var description = $('#group-description').val();
                var type = groupId != "" ? EDIT : CREATE;
                if (isRequired(groupName)) {
                    $.ajax({
                        type: "POST",
                        dataType: "json",
                        url: "GroupAction",
                        beforeSend: function() {
                            $('input[type=button]').attr("disabled", true);
                        },
                        data: {
                            "groupID": groupId,
                            "groupName": groupName,
                            "description": description,
                            "type": type,
                            "email": email
                        },
                        success: function(data) {
                            $('input[type=button]').attr("disabled", false);
                            if (data.status === STATUS_CODE_OK) {
                                window.location.href = "group.jsp";
                            } else{
                                alert("save error");
                            }
                        },
                        error: function() {
                            $('input[type=button]').attr("disabled", false);
                        }
                    });
                } else {
                    $('#group-name-validate').html(" Group name is invalid!");
                }
            }
            //Handle click on plus icon
            function addGroupRole(addIcon){
                $('#list-group-role').append("<div class='new-group-role'>"+$('.new-group-role').html()+"</div>");
                //$(addIcon).hide();
            }
            
            //Handle click on minus icon
            function removeGroupRole(removeIcon){
                if ($('.new-group-role').length > 1) {
                    $(removeIcon).parent().remove();
                    $('#list-group-role > .new-group-role:last').find(".plus-icon").show();
                }
            }
            
            function deleteGroup(groupID){
                if(confirm("<%=LanguageManager.getInstance().getText("confirmDeleteGroup", request)%>")) {
                    $.ajax({
                        type: "POST",
                        dataType: "json",
                        url: "<%=request.getContextPath()%>/group/GroupAction",
                        beforeSend: function(){
                            //before send
                        },
                        complete: function(){
                            //complete
                        },
                        data: {
                            "deleteids": groupID,
                            "type": DELETE
                        },
                        success: function(data) {
                            if (data.status === STATUS_CODE_OK) {
                               window.location.href = "group.jsp";
                            }
                        },
                        error: function() {
                            alert("error get role by group and user");
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
                    <span id="page_nane"><%=LanguageManager.getInstance().getText("groupDetail", request)%></span>                    
                </div> 
                <table class="table_content pleft30">
                    <tr class="group_name_box">
                        <td class="td_lable"><label class="required"><%=LanguageManager.getInstance().getText("groupName", request)%></label></td>
                        <td><input id="group-name" type="text"/><label class="group-name-validate"></label></td>
                    </tr>
                    <tr class="group_description_box">
                        <td class="td_lable"><label><%=LanguageManager.getInstance().getText("description", request)%></label></td>
                        <td><textarea id="group-description" cols="8"></textarea></td>
                    </tr>
                    <tr>
                        <td></td>
                        <td>
                            <div class="button_group">                                
                                <input type="button" class="cancel-btn" onclick="{window.location = 'group.jsp'}"/>
                                <input type="button" class="save-btn" onclick="createEditGroupSubmit();"/>
                            </div>
                            <div>
                                <input type="button" style="display: none" class="delete-btn" onclick="deleteGroup('<%=groupId%>');">
                            </div>
                        </td>
                    </tr>
                </table>
            </div>
        </div>
    </body>
</html>
