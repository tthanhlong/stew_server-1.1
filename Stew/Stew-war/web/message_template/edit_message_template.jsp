<%-- 
    Document   : edit_message
    Created on : Sep 3, 2013, 11:23:01 AM
    Author     : tthanhlong
--%>


<%@page import="jp.co.ncdc.stew.Entities.MessageTemplate"%>
<%@page import="jp.co.ncdc.stew.Managers.LanguageManager"%>
<%@page import="jp.co.ncdc.stew.Controllers.MessageTemplateController"%>
<%@page import="java.util.List"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <script type="text/javascript" src="../js/jquery-1.9.1.min.js"></script>
        <script type="text/javascript" src="../js/common.js"></script>
        <title>Edit message</title>
        <%
            String Id = request.getParameter("messageId");
            long messageId = 0;
            try{
                messageId = Long.parseLong(Id);
            }catch (Exception e) {
                
            }
        %>
        <script type="text/javascript">
            $(document).ready(function(){
                $('#message-menu').removeClass("has-hover");
                $('#message-menu').addClass("selected-menu");
                var flag = true;
                $('#saveMessage').click(function(){
                    if(flag){
                        $('#title-validate').html("");
                        $('#message-validate').html("");                        

                        var messageId = <%=messageId%>;
                        var title = $('#title').val();
                        var message = $('#message').val();                        
                        
                        if (title !== "") {
                            if(message !== ""){                                
                                $.ajax({
                                    type: "POST",
                                    dataType: "json",
                                    url: "MessageTemplateAction",
                                    beforeSend: function(){
                                        $('input[type=button]').attr("disabled", true);
                                        flag = false;
                                    },
                                    data: {
                                        "messageId": messageId,
                                        "title": title,
                                        "message": message,                                        
                                        "type": EDIT
                                    },
                                    success: function(data){
                                        flag = true;
                                       $('input[type=button]').attr("disabled", false);
                                        window.location.href = "message_template.jsp";
                                    },
                                    error: function (){
                                        flag = true;
                                       $('input[type=button]').attr("disabled", false);
                                    }
                                 });                                
                            }else{
                                $('#message-validate').html("<%=LanguageManager.getInstance().getText("messageBlank", request)%>");
                            }
                        }else{
                            $('#title-validate').html("<%=LanguageManager.getInstance().getText("titleInvalid", request)%>");
                        }
                    }
                });
            });
            function deleteTemplate(templateId){
                if (confirm("<%=LanguageManager.getInstance().getText("confirmDeleteTemplate", request)%>")) {
                        $.ajax({
                            type: "POST",
                            dataType: "json",
                            url: "MessageTemplateAction",
                            beforeSend: function() {
                                //before send
                            },
                            complete: function() {
                                //complete
                            },
                            data: {
                                "messageId": templateId,
                                "type": DELETE_TEMPLATE_BY_ID
                            },
                            success: function(data) {
                                if (data.status === STATUS_CODE_OK) {
                                    window.location.href = "message_template.jsp";
                                }else{
                                    alert("<%=LanguageManager.getInstance().getText("errorDeleteMessageTemplate", request)%>");
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
         <%
            
            MessageTemplateController messageController = new MessageTemplateController();            
            
            MessageTemplate message = messageController.getMessageByID(messageId);            
            boolean flag = false;
            if(message != null){
                flag = true;
            }
        %>
       <div class="page_content">
            <jsp:include page="../common/header.jsp" />            
            <div class="content">
                <div class="content_header">
                    <%=LanguageManager.getInstance().getText("editTemplateDetail", request)%>
                </div> 
                <table class="table_content create_user_content">                                                            
                    <tr class="group_name_box">
                        <td class="td_lable"><%=LanguageManager.getInstance().getText("thTitle", request)%>:<span class="validate">(*)</span></td>
                        <td><input class="user-info" type="text" required="true" id="title" value="<%=flag?message.getTitle():""%>"/><span class="validate" id="title-validate"></span></td>
                    </tr>
                    <tr class="group_name_box">
                        <td class="td_lable"><%=LanguageManager.getInstance().getText("messageContentLabel", request)%>:<span class="validate">(*)</span></td>                        
                        <td><textarea class="txt-area-description" type="message" id="message" rows="5" cols="25"><%=flag?message.getMessage():""%></textarea><span class="validate" id="message-validate"></span></td>
                    </tr>
                    <tr>
                        <td>&nbsp;</td>
                        <td>                  
                            <div class="button_group">
                                <input type="button" class="cancel-btn" onclick="{window.location = 'message_template.jsp'}"/>
                                <input type="button" id="saveMessage" class="save-btn"/>
                            </div>
                            <div>
                                <input type="button" class="delete-btn" onclick="deleteTemplate('<%=messageId%>');">
                            </div>
                        </td>
                    </tr>
                </table>                
            </div>
        </div>
    </body>
</html>
