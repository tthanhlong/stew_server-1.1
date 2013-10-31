<%-- 
    Document   : header
    Created on : Aug 28, 2013, 7:03:52 PM
    Author     : tthanhlong
--%>

<%@page import="jp.co.ncdc.stew.Entities.User"%>
<%@page import="jp.co.ncdc.stew.Utils.StewConstant"%>
<%@page import="jp.co.ncdc.stew.Managers.LanguageManager"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" href="../css/stew.css"/>
        <title>Stew Administrator Console</title>
        <%
            User a = (User) session.getAttribute(StewConstant.USER_SESSION);
            String email = "";
            if (a != null) {
                email = a.getEmail();
            }
        %>
        <script type="text/javascript">
            var email = "<%=email%>";
            $(document).ready(function() {
                if (email === "") {
                    window.location.href = "<%=request.getContextPath()%>/login.jsp";
                }
            });
        </script>
    </head>
    <body>     
        <div class="header-item">  
            <table>
                <tr>
                    <td class="nav_item" id="logo_image"><img src="../image/gui/navi_logo.png"></td>
                    <td class="nav_item" id="user-menu"><a href="../user/user.jsp"><%=LanguageManager.getInstance().getText("userMenu", request)%></a></td>
                    <td class="nav_item" id="group-menu"><a href="../group/group.jsp"><%=LanguageManager.getInstance().getText("groupMenu", request)%></a></td>
                    <td class="nav_item" id="app-menu"><a href="../app/app.jsp"><%=LanguageManager.getInstance().getText("appMenu", request)%></a></td>
                    <td class="nav_item" id="message-menu"><a href="../message_template/message_template.jsp"><%=LanguageManager.getInstance().getText("messageTemplateMenu", request)%></a></td>
                    <td class="nav_item" id="sendMessage-menu"><a href="../message/message.jsp"><%=LanguageManager.getInstance().getText("sendMessageMenu", request)%></a></td>
                    <td class="nav_item" id="log-menu"><a href="../logs/log.jsp"><%=LanguageManager.getInstance().getText("logMenu", request)%></a></td>
                    <td style="width: 20%;text-align: left"><div class="log_out"><a href="../logout.jsp"><%=LanguageManager.getInstance().getText("logout", request)%></a></div></td>
                </tr>
            </table>
        </div>	
    </body>
</html>
