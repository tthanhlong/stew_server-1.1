<%-- 
    Document   : logout
    Created on : Sep 27, 2013, 10:55:28 AM
    Author     : vcnduong
--%>

<%@page import="jp.co.ncdc.stew.Utils.StewConstant"%>
<%@page import="jp.co.ncdc.stew.Entities.User"%>
<%@page import="jp.co.ncdc.stew.Managers.LanguageManager"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Stew Administrator</title>
        <link rel="stylesheet" href="css/stew.css"/>
        <script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery-1.9.1.min.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/js/common.js"></script>
        <script type="text/javascript">
            <%
                User a = (User) session.getAttribute(StewConstant.USER_SESSION);
                String email ="";
                if(a!=null){
                    email = a.getEmail();
                }
            %>
            var email = "<%=email%>";
            $(document).ready(function() {      
                if(email !== ""){
                    $.ajax({
                        type: "POST",
                        dataType: "json",
                        url: "<%=request.getContextPath()%>/user/UserAction",                   
                        data: {
                            "email":email,
                            "type": LOGOUT
                        }
                    });
                }   
            });
        </script>
    </head>
    <body id="login">
        <div class="login_body">
            <div class="login_logo"></div>
            <div class="Login_content">
                <div><%=LanguageManager.getInstance().getText("logoutMsg", request)%></div>
                <div class="login_again"><a href="login.jsp"><%=LanguageManager.getInstance().getText("reLogin", request)%></a></div>
            </div>
        </div>
    </body>
</html>
