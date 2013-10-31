<%-- 
    Document   : login
    Created on : Sep 27, 2013, 10:55:28 AM
    Author     : vcnduong
--%>

<%@page import="jp.co.ncdc.stew.Managers.LanguageManager"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Login to stew</title>
        <link rel="stylesheet" href="css/stew.css"/>
        <script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery-1.9.1.min.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/js/common.js"></script>
        <script type="text/javascript">
            $(document).ready(function() {
                $('#loginBtn').click(function() {
                    var email = $('#email').val();
                    var pwd = $('#password').val();
                    $.ajax({
                        type: "POST",
                        dataType: "json",
                        url: "<%=request.getContextPath()%>/user/UserAction",
                        beforeSend: function() {
                            $('input[type=button]').attr("disabled", true);
                        },
                        data: {
                            "email": email,
                            "pwd": pwd,
                            "type": LOGIN
                        },
                        success: function(data) {
                            if (data.status === STATUS_CODE_OK) {
                                window.location.href = "<%=request.getContextPath()%>/user/user.jsp";
                            } else {
                                //alert(data.errCode);
                                if (data.errCode === "ERR001") {
                                    alert("<%=LanguageManager.getInstance().getText("ERR001", request)%>")
                                }
                                if (data.errCode === "ERR002") {
                                    alert("<%=LanguageManager.getInstance().getText("ERR002", request)%>")
                                }
                                if (data.errCode === "ERR003") {
                                    alert("<%=LanguageManager.getInstance().getText("ERR003", request)%>")
                                }
                            }
                            $('input[type=button]').attr("disabled", false);
                        },
                        error: function() {
                            $('input[type=button]').attr("disabled", false);
                        }
                    });
                });
                $('#password').keypress(function (e) {
                    if (e.which == 13) {
                        $('#loginBtn').trigger("click");
                    }
                });
            });
            
        </script>
    </head>
    <body id="login">
        <div class="login_header">
            <%=LanguageManager.getInstance().getText("loginLbl", request)%>
        </div>
        <div class="login_body">
            <div class="login_logo"></div>
            <div class="Login_content">
                <table>
                    <tr>
                        <td class="tbl_table"><%=LanguageManager.getInstance().getText("account", request)%></td>
                        <td><input type="text" id="email" value="stew@test.com"/></td>
                    </tr>
                    <tr>
                        <td class="tbl_table"><%=LanguageManager.getInstance().getText("password", request)%></td>
                        <td><input type="password" id="password" value="123456"/></td>
                    </tr>
                    <tr>
                        <td></td>
                        <td><input class="button_login" id="loginBtn" type="button" value="<%=LanguageManager.getInstance().getText("loginLbl", request)%>"/></td>
                    </tr>
                </table>
            </div>
        </div>
    </body>
</html>
