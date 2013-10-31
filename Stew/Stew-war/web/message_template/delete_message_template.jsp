<%-- 
    Document   : delete_message
    Created on : Sep 3, 2013, 5:53:05 PM
    Author     : tthanhlong
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <script type="text/javascript" src="../js/jquery-1.9.1.min.js"></script>
        <script type="text/javascript" src="../js/common.js"></script>
        <link rel="stylesheet" href="../css/stew.css"/>
        <title>Delete message</title>
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
                var flag = true;
                $('#delete-btn').click(function(){
                    if(flag){
                        $.ajax({
                            type: "POST",
                            dataType: "json",
                            url: "MessageTemplateAction",
                            beforeSend: function(){
                                $('.yes-no-btn').attr("disabled", true).removeClass("has-hover");
                                flag = false;
                            },
                            data: {
                                "messageId": "<%=messageId%>",
                                "type": DELETE
                            },
                            success: function(data){
                                flag = true;
                                $('.yes-no-btn').attr("disabled", false).addClass("has-hover");
                                if(data.status === STATUS_CODE_OK){
                                    window.location.href = "message_template.jsp";
                                }else{
                                    $('#user-validate').html(" Cannot delete message!");
                                }
                            },
                            error: function (){
                                flag = true;
                                $('.yes-no-btn').attr("disabled", false).addClass("has-hover");
                          }
                     });
                    }
                });
            });
        </script>
    </head>
    <body>
        <div class="page_content">      
            <div class="delete_comfirm">
                <h4>Are you sure you want to delete message <i><%=messageId%></i>?</h4>
                <input id="delete-btn" type="button" class="yes-no-btn has-hover" value="Yes"/>
                <input onclick="{window.location='message_template.jsp'}" type="button" class="yes-no-btn has-hover" value="Back"/>
                <div class="validate" id="message-validate"></div>
            </div>
        </div>
    </body>
</html>
