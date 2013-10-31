<%-- 
    Document   : send_message
    Created on : Sep 4, 2013, 9:24:21 AM
    Author     : tthanhlong
--%>

<%@page import="jp.co.ncdc.stew.Entities.Role"%>
<%@page import="jp.co.ncdc.stew.Adapters.RoleAdapter"%>
<%@page import="jp.co.ncdc.stew.Entities.Apps"%>
<%@page import="jp.co.ncdc.stew.Adapters.AppsAdapter"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.Date"%>
<%@page import="jp.co.ncdc.stew.Entities.User"%>
<%@page import="jp.co.ncdc.stew.Utils.StewConstant"%>
<%@page import="jp.co.ncdc.stew.Entities.MessageTemplate"%>
<%@page import="jp.co.ncdc.stew.Controllers.MessageTemplateController"%>
<%@page import="jp.co.ncdc.stew.Entities.Message"%>
<%@page import="jp.co.ncdc.stew.Entities.GroupUser"%>
<%@page import="jp.co.ncdc.stew.Controllers.MessageController"%>
<%@page import="jp.co.ncdc.stew.Controllers.GroupController"%>
<%@page import="jp.co.ncdc.stew.Managers.LanguageManager"%>
<%@page import="java.util.List"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <script type="text/javascript" src="../js/jquery-1.9.1.min.js"></script>
        <script type="text/javascript" src="../js/common.js"></script>
        <link rel="stylesheet" type="text/css" href="../Datepicker/tcal.css" />
	<script type="text/javascript" src="../Datepicker/tcal.js"></script> 
        <title><%=LanguageManager.getInstance().getText("sendMessageLabel", request)%></title>
        <%
            User user = (User) session.getAttribute(StewConstant.USER_SESSION);
            String userId = "";
            boolean isSupperAdmin = false;
            if(user != null){
                userId = user.getEmail(); 
                isSupperAdmin = (Boolean) session.getAttribute(StewConstant.IS_SUPPER);
            }
            
            GroupController groupController = new GroupController();
            MessageTemplateController messageController = new MessageTemplateController();                        
            List<GroupUser> lstGroups = groupController.getUserGroupDetailByUserIdWithManagerRole(userId);
            
            RoleAdapter roleAdapt = new RoleAdapter();
            List<Role> lstRoles = roleAdapt.getRoles();
            
            List<MessageTemplate> lstMessages = messageController.getAllMessages();
            AppsAdapter appsAdapter = new AppsAdapter();
            List<Apps> lstApps = appsAdapter.getAppsByManagerId(userId, isSupperAdmin);
        %>
        <script type="text/javascript">
            $(document).ready(function(){
                $('#sendMessage-menu').removeClass("has-hover");
                $('#sendMessage-menu').addClass("selected-menu");
                var flag = true;
                
                $('#message').change(function(){
                    var messageContent = $(this).find(":selected").attr("lang");
                    $('#message-content').val(messageContent);
                });                                
                                
                $('#sendMessage').click(function(){
                    $('#app-validate').html("");                
                    $('#group-validate').html("");
                    $('#message-validate').html("");
                    $('#hour-validate').html("");
                    $('#minute-validate').html("");
                    var appId = $('#app').val();                    
                    var messageID = $('#message').val();                   
                    var messageContent = $('#message-content').val();                   
                    var title = $('#message :selected').text();
                    var sendType = $('input[name=sendType]:radio:checked').val();
                    var sendDate = $('#datepicker').val();                    
                    var sendHour = $('#sendHour').val();
                    var sendMinute = $('#sendMinute').val();                                        
                    var groups = new Array();
                    var roles = new Array();
                    var index = 0;       
                    $(".new-group-role").each(function(){
                        groups[index] = $(this).find(".group").val();
                        roles[index] = $(this).find(".role").val();
                        index++;
                    });                    
                                        
                    if(flag){
                        if (isRequired(appId)) {                            
                            if (checkGroupValidate(groups)) {
                                if(parseInt(messageID) >= 0){
                                    $.ajax({
                                        type: "POST",
                                        dataType: "json",
                                        url: "../message/MessageAction",
                                        beforeSend: function(){
                                            $('input[type=button]').attr("disabled", true);
                                            flag = false;
                                        },
                                        data: {
                                            "appId": appId,
                                            "messageId": messageID,          
                                            "title": title,
                                            "messageContent": messageContent,                                            
                                            "roles": roles.join(),
                                            "groups": groups.join(),
                                            "sendType": sendType,
                                            "sendDate": sendDate,
                                            "sendHour": sendHour,
                                            "sendMinute": sendMinute,
                                            "type": SEND_MESSAGE
                                        },
                                        success: function(data){
                                            flag = true;
                                           $('input[type=button]').attr("disabled", false);
                                            if (data.status === STATUS_CODE_NOT_FOUND) {
                                                alert("<%=LanguageManager.getInstance().getText("groupValidate", request)%>");
                                            }else if(data.status === STATUS_CODE_OK) {
                                                window.location.href = "../message/message.jsp";
                                            }
                                        },
                                        error: function (){
                                            flag = true;
                                            $('input[type=button]').attr("disabled", false);
                                      }
                                 });
                                }else{
                                    $('#message-validate').html(" <%=LanguageManager.getInstance().getText("messageValidate", request)%>");
                                }
                            }else{
                                $('#group-validate').html(" <%=LanguageManager.getInstance().getText("groupValidate", request)%>");
                            }
                        }else {
                            $('#app-validate').html(" <%=LanguageManager.getInstance().getText("appValidate", request)%>");
                        }
                        
                    }
                });
                                
                
            });   
            function editTemplate() {
                if ($('#message').val() !== null)
                    window.location.href="../message_template/edit_message_template.jsp?messageId="+$('#message').val();                    
            }
            
            //Handle click on plus icon
            function addGroupRole(addIcon){
                $('#list-group-role').append("<tr class='new-group-role'>"+$('#list-group-role > tbody > tr:first').html()+"</tr>");
                $(addIcon).hide();
            }
            
            //Handle click on minus icon
            function removeGroupRole(removeIcon){
                if ($('.new-group-role').length > 1) {
                    $(removeIcon).parent().parent().remove();
                    $('#list-group-role > tbody > tr:last').find(".plus-icon").show();
                }
            }
            
            //Get role for user by group id
            function getRoleForUserByGroupID(groupID){
                $(groupID).parent().parent().find(".role").attr("disabled", false);                                                                              
            }
        </script>
    </head>
    <body>                  
        <div class="page_content">
            <jsp:include page="../common/header.jsp" />
            <div class="content">
                <div class="content_header">
                    <%=LanguageManager.getInstance().getText("sendMessageLabel", request)%>
                </div>
                <table class="table_content create_user_content">
                    <tr class="group_name_box">
                        <td class="td_lable"><%=LanguageManager.getInstance().getText("selectAppLabel", request)%>:<span class="validate">(*)</span></td>
                        <td>
                            <select name="app" id="app">
                                <option value="-1" disabled="disabled" selected="selected">--<%=LanguageManager.getInstance().getText("selectApp", request)%>--</option>
                                <%
                                    if(lstApps != null && lstApps.size() > 0){
                                        int count = lstApps.size();
                                        for(int i = 0; i < count; i++){
                                        %>
                                            <option value="<%=lstApps.get(i).getAppId()%>"><%=lstApps.get(i).getAppName()%></option>
                                        <%
                                        }
                                    }
                                %>
                            </select><span class="validate" id="app-validate"></span>
                        </td>
                    </tr>
                    <tr>
                        <td class="td_lable"><%=LanguageManager.getInstance().getText("thGroupRole", request)%>:<span class="validate">(*)</span></td>
                        <td>
                            <table id="list-group-role">
                                <tr class="hidden-group-role" style="display: none">
                                    <td>
                                        <select class="group" onchange="getRoleForUserByGroupID(this);">
                                            <option value="-1" selected="selected" disabled="disabled">--<%=LanguageManager.getInstance().getText("chkSelectGroup", request)%>--</option>
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
                                            <option value="-1" selected="selected">--<%=LanguageManager.getInstance().getText("chkSelectRole", request)%>--</option>
                                            <%
                                                if (lstRoles != null && lstRoles.size() > 0) {
                                                    int countRole = lstRoles.size();
                                                    for (int i = 0; i < countRole; i++) {
                                            %>
                                                <option value="<%=lstRoles.get(i).getRoleId()%>"><%=lstRoles.get(i).getRoleName()%></option>
                                            <%
                                                    }
                                                }
                                            %>
                                        </select>
                                        <span class="minus-icon" onclick="removeGroupRole(this)"></span>
                                        <span class="plus-icon" onclick="addGroupRole(this);"></span>
                                    </td>
                                </tr>
                                <tr class="new-group-role">
                                    <td>
                                        <select class="group" onchange="getRoleForUserByGroupID(this);">
                                            <option value="-1" selected="selected" disabled="disabled">--<%=LanguageManager.getInstance().getText("chkSelectGroup", request)%>--</option>
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
                                            <option value="-1" selected="selected">--<%=LanguageManager.getInstance().getText("chkSelectRole", request)%>--</option>
                                            <%
                                                if (lstRoles != null && lstRoles.size() > 0) {
                                                    int countRole = lstRoles.size();
                                                    for (int i = 0; i < countRole; i++) {
                                            %>
                                                <option value="<%=lstRoles.get(i).getRoleId()%>"><%=lstRoles.get(i).getRoleName()%></option>
                                            <%
                                                    }
                                                }
                                            %>
                                        </select>
                                        <span class="minus-icon" onclick="removeGroupRole(this)"></span>
                                        <span class="plus-icon" onclick="addGroupRole(this);"></span>
                                    </td>
                                </tr>
                            </table>
                            <span class="validate" id="group-role-validate"></span>
                        </td>
                    </tr>
                    <tr class="group_name_box">
                        <td class="td_lable"><%=LanguageManager.getInstance().getText("selectTemplateLabel", request)%>:<span class="validate">(*)</span></td>
                        <td>
                            <select name="message" id="message">
                                <option value="-1" disabled="disabled" selected="selected">--<%=LanguageManager.getInstance().getText("selectAMessage", request)%>--</option>
                                <%
                                    if(lstMessages != null && lstMessages.size() > 0){
                                        int count = lstMessages.size();
                                        for(int i = 0; i < count; i++){
                                        %>
                                        <option lang="<%=lstMessages.get(i).getMessage()%>" value="<%=lstMessages.get(i).getId()%>"><%=lstMessages.get(i).getTitle()%></option>
                                        <%
                                        }
                                    }
                                %>
                            </select><span class="validate" id="message-validate"></span>                            
                            <span class="create-icon"><a href="../message_template/create_message_template.jsp"><%=LanguageManager.getInstance().getText("add", request)%></a></span>&nbsp;&nbsp;&nbsp
                            <a title="<%=LanguageManager.getInstance().getText("editAction", request)%>" class="action" onclick="editTemplate()"><span class='edit-icon'><%=LanguageManager.getInstance().getText("editAction", request)%></span></a>                            
                        </td>
                    </tr>
                    <tr class="group_name_box">
                        <td class="td_lable"><%=LanguageManager.getInstance().getText("messageContentLabel", request)%>:</td>
                        <td>
                            <textarea class="txt-area-description" id="message-content" rows="5" cols="25" placeholder="<%=LanguageManager.getInstance().getText("messageContentTextArea", request)%>"></textarea>
                        </td>
                    </tr>
                    <tr class="group_name_box">
                        <td class="td_lable"><%=LanguageManager.getInstance().getText("sendDateLabel", request)%>:<span class="validate">(*)</span></td>
                        <td>
                            <table>
                                <tr>
                                    <td style="width: 30px"><INPUT style="width: 20px" TYPE="radio" NAME="sendType" VALUE="1" checked="checked"></td>
                                    <td><%=LanguageManager.getInstance().getText("sendNowLabel", request)%></td>
                                </tr>
                                <tr>
                                    <td style="width: 30px"><INPUT style="width: 20px" TYPE="radio" NAME="sendType" VALUE="2"></td>
                                    <td>
                                        <%SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");%>
                                        <input style="width: 170px" type="text" id="datepicker" name="sendDate" class="tcal" value="<%=dateFormat.format(new Date())%>"/>
                                    </td>
                                    <td>                                        
                                        <select name="sendHour" id="sendHour" style="width: 60px">
                                            <option value="-1" disabled="disabled">--<%=LanguageManager.getInstance().getText("selectAHour", request)%>--</option>
                                            <option selected="selected" value="0">0</option>                                            
                                            <%                                                
                                                for(int i = 1; i < 24; i++){
                                                %>
                                                    <option value="<%=i%>"><%=i%></option>
                                                <%
                                                }                                                
                                            %>
                                        </select><span class="validate" id="hour-validate"></span>
                                    </td>
                                    <td>&nbsp;&nbsp;&nbsp;:</td>
                                    <td>                                        
                                        <select name="sendMinute" id="sendMinute" style="width: 60px">
                                            <option value="-1" disabled="disabled">--<%=LanguageManager.getInstance().getText("selectAMinute", request)%>--</option>
                                            <option selected="selected" value="0">0</option>
                                            <%                                                                                                                               
                                                for(int i = 1; i < 60; i++){
                                                %>
                                                    <option value="<%=i%>"><%=i%></option>
                                                <%
                                                }                                                
                                            %>
                                        </select><span class="validate" id="minute-validate"></span>
                                    </td>
                                </tr>                                
                            </table>                        
                        </td>                        
                    </tr>                                        
                    <tr>
                        <td>&nbsp;</td>
                        <td>                  
                            <div class="button_group">
                                <input type="button" class="cancel-btn" onclick="{window.location = '../message/message.jsp'}"/>
                                <input type="button" id="sendMessage" class="send-btn"/>
                            </div>
                        </td>
                    </tr>
                </table>                
            </div>
        </div>
    </body>
</html>
