<%-- 
    Document   : delete_user
    Created on : Aug 30, 2013, 2:27:57 PM
    Author     : tthanhlong
--%>

<%@page import="jp.co.ncdc.stew.Managers.LanguageManager"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <script type="text/javascript" src="../js/jquery-1.9.1.min.js"></script>
        <script type="text/javascript" src="../js/common.js"></script>
        <link rel="stylesheet" href="../css/stew.css"/>
        <title><%=LanguageManager.getInstance().getText("deleteUserLabel", request)%></title>
        <%
            String email = request.getParameter("email");
        %>
        <script type="text/javascript">
            $(document).ready(function(){
                var flag = true;
                $('#delete-btn').click(function(){
                    if(flag){
                        $.ajax({
                            type: "POST",
                            dataType: "json",
                            url: "UserAction",
                            beforeSend: function(){
                                $('.yes-no-btn').attr("disabled", true).removeClass("has-hover");
                                flag = false;
                            },
                            data: {
                                "email": "<%=email%>",
                                "type": DELETE
                            },
                            success: function(data){
                                flag = true;
                                $('.yes-no-btn').attr("disabled", false).addClass("has-hover");
                                if(data.status === STATUS_CODE_OK){
                                    window.location.href = "user.jsp";
                                }else{
                                    $('#user-validate').html(" <%=LanguageManager.getInstance().getText("cannotDeleteUser", request)%>");
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
                <h4><%=LanguageManager.getInstance().getText("confirmDeleteUser", request)%> <i><%=email%></i>?</h4>
                <input id="delete-btn" type="button" class="yes-no-btn has-hover" value="<%=LanguageManager.getInstance().getText("yesBtn", request)%>"/>
                <input onclick="{window.location='user.jsp'}" type="button" class="yes-no-btn has-hover" value="<%=LanguageManager.getInstance().getText("btnBack", request)%>"/>
                <div class="validate" id="user-validate"></div>
            </div>
        </div>
    </body>
</html>
