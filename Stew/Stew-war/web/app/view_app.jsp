<%-- 
    Document   : view_app
    Created on : Oct 6, 2013, 9:15:16 AM
    Author     : vcnduong
--%>

<%@page import="jp.co.ncdc.stew.Adapters.GroupAppDetailAdapter"%>
<%@page import="jp.co.ncdc.stew.Controllers.UserAppController"%>
<%@page import="java.util.List"%>
<%@page import="jp.co.ncdc.stew.Entities.Apps"%>
<%@page import="jp.co.ncdc.stew.Adapters.AppsAdapter"%>
<%@page import="jp.co.ncdc.stew.Managers.LanguageManager"%>
<%@page import="jp.co.ncdc.stew.Entities.User"%>
<%@page import="jp.co.ncdc.stew.Utils.StewConstant"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <script type="text/javascript" src="../js/jquery-1.9.1.min.js"></script>
        <script type="text/javascript" src="../js/common.js"></script>
        <title>View application information</title>
        <script type="text/javascript">
            <%
                User a = (User) session.getAttribute(StewConstant.USER_SESSION);
                String pageStr = request.getParameter("page");
                AppsAdapter adapter = new AppsAdapter();
                GroupAppDetailAdapter gada = new GroupAppDetailAdapter();
                String email = "";
                String name = "";
                String group = "";
                if (a != null) {
                    email = a.getEmail();
                }
                String edit = request.getParameter("edit");
                String appId = "";
                if (edit != null) {
                    appId = edit;
                    Apps apps = adapter.getAppByAppID(appId);
                    if (apps != null) {
                        name = apps.getAppName();
                        List<String> groups = gada.getListGroupNameOfApp(appId);
                        for (int i = 0; i < groups.size(); i++) {
                            group = groups.get(i) + "<br>";
                        }
                    }
                }
            %>
            var appId = "<%=appId%>";
            var email = "<%=email%>";
            var page_num = parseInt("<%=pageStr%>") ? parseInt("<%=pageStr%>") : 0;
            $(document).ready(function() {
                $('#app-menu').addClass("selected-menu");
                loadAppInfo();
                loadUserOfApp();
            });
            //load app info
            function loadAppInfo() {
                if (appId != "") {
                    $('#app_name').html("<%=name%>");
                    $('#app_id').html(appId);
                    $('#app_group').html("<%=group%>");
                }
            }
            //load user
            function loadUserOfApp() {
                $.ajax({
                    type: "POST",
                    dataType: "json",
                    url: "AppAction",
                    beforeSend: function() {
                        $('input[type=button]').attr("disabled", true);
                    },
                    data: {
                        "userId": email,
                        "appId": appId,
                        "page": page_num,
                        "type": VIEW
                    },
                    success: function(data) {
                        $('input[type=button]').attr("disabled", false);
                        $("#paging").empty();
                        $("#table_app").empty();
                        var page = generatePaging(page_num, data.total, "view_app.jsp","&&edit="+appId);
                        $("#paging").append(page);
                        var total = parseInt(data.total);
                        if (total > page_num) {
                            $.each(data.lstUserApp, function(index, value) {
                                $('#table_app').append('<tr><td>' + value.groups + '</td><td>' + value.userId + '</td><td><span>' + value.dateModified + '</span></td><td>' + value.status + '</td></tr>');
                            });
                        } else {
                            if (total === 0) {
                                $("#table_app").append("<tr> <td colspan='4' class='no_result'><%=LanguageManager.getInstance().getText("noResult", request)%></td></tr>");
                            } else {
                                if (page_num > 0) {
                                    page_num = page_num - 1;
                                    loadUserOfApp();
                                }
                            }
                        }
                    },
                    error: function() {
                        $('input[type=button]').attr("disabled", false);
                    }
                });
            }
        </script>

    </head>
    <body>
        <div class="page_content">
            <jsp:include page="../common/header.jsp" />            
            <div class="content">
                <div class="content_header">
                    <span id="page_nane"><%=LanguageManager.getInstance().getText("appinfo", request)%></span>                    
                </div> 
                <table class="table_content pleft30">
                    <tr class="group_name_box">
                        <td class="td_lable" style="width: 200px;"><label><%=LanguageManager.getInstance().getText("appName", request)%></label></td>
                        <td><label id="app_name"></label></td>
                    </tr>
                    <tr class="group_name_box">
                        <td class="td_lable"><label><%=LanguageManager.getInstance().getText("appId", request)%></label></td>
                        <td><label id="app_id"></label></td>
                    </tr>
                    <tr class="group_name_box">
                        <td class="td_lable"><label><%=LanguageManager.getInstance().getText("thGroup", request)%></label></td>
                        <td><label id="app_group"></label></td>
                    </tr>   
                </table>
                <table class="filter_header">
                    <tr>
                        <td style="width: 200px;"></td>
                        <td class="dropdown-list">
                            <select></select>
                        </td>
                        <td class="dropdown-list">
                            <select></select>
                        </td>
                        <td class="search_td">
                            <div class="search_div">
                                <input placeholder="<%=LanguageManager.getInstance().getText("searchHolder", request)%>" type="text">
                                <input type="button" class="search-button">                                            
                            </div>
                        </td>
                    </tr>
                </table>       
                <div class="user_list">
                    <table class="table_content">
                        <thead>
                            <tr>
                                <th style="width: 20%"><%=LanguageManager.getInstance().getText("thGroup", request)%></th>
                                <th style="width: 25%"><%=LanguageManager.getInstance().getText("userMenu", request)%></th>
                                <th style="width: 25%"><%=LanguageManager.getInstance().getText("dateModified", request)%></th>
                                <th style="width: 15%"><%=LanguageManager.getInstance().getText("state", request)%></th>
                            </tr>
                        </thead>
                        <tbody id="table_app" class="table_list">

                        </tbody>
                        <tfoot>
                        <td colspan="2" align="left"></td>
                        <td colspan="2" align="right"><div id="paging"></div></td>
                        </tfoot>
                    </table>
                </div>
            </div>
        </div>
    </body>
</html>
