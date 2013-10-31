<%-- 
    Document   : create_message
    Created on : Aug 30, 2013, 4:17:28 PM
    Author     : tthanhlong
--%>

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
        <title>Create message</title>
        <script type="text/javascript">
            $(document).ready(function(){
                $('#message-menu').removeClass("has-hover");
                $('#message-menu').addClass("selected-menu");
                var flag = true;
                $('#saveMessage').click(function(){  
                    if(flag){
                        $('#title-validate').html("");
                        $('#message-validate').html("");                        

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
                                        "title": title,
                                        "message": message,                                        
                                        "type": CREATE
                                    },
                                    success: function(data){
                                        flag = true;
                                       $('input[type=button]').attr("disabled", false);
                                        if (data.status === EXISTED_MESSAGE) {
                                            $('#title-validate').html(" Message is existed!");
                                        }else if(data.status === STATUS_CODE_OK) {
                                            window.location.href = "message_template.jsp";
                                        }
                                    },
                                    error: function (){
                                        alert("error");
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
        </script>
    </head>
    <body>               
         <div class="page_content">
            <jsp:include page="../common/header.jsp" />            
            <div class="content">
                <div class="content_header">
                    <%=LanguageManager.getInstance().getText("createTemplateDetail", request)%>
                </div> 
                <table class="table_content create_user_content">
                    <tr class="group_name_box">
                        <td class="td_lable"><%=LanguageManager.getInstance().getText("thTitle", request)%>:<span class="validate">(*)</span></td>
                        <td><input class="user-info" type="text" required="true" id="title"/><span class="validate" id="title-validate"></span></td>
                    </tr>
                    <tr class="group_name_box">
                        <td class="td_lable"><%=LanguageManager.getInstance().getText("messageContentLabel", request)%>:<span class="validate">(*)</span></td>
                        <td>
                            <textarea class="txt-area-description" required="true" id="message" rows="5" cols="25"></textarea><span class="validate" id="message-validate"></span>
                        </td>    
                    </tr>                       
                    <tr>
                        <td>&nbsp;</td>
                        <td>                  
                            <div class="button_group">
                                <input type="button" class="cancel-btn" onclick="{window.location = 'message_template.jsp'}"/>
                                <input type="button" id="saveMessage" class="save-btn"/>
                            </div>
                        </td>
                    </tr>
                </table>
            </div>
        </div>
    </body>
</html>
