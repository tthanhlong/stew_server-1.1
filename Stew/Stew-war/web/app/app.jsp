<%-- 
    Document   : app
    Created on : Oct 3, 2013, 2:45:31 PM
    Author     : vcnduong
--%>

<%@page import="jp.co.ncdc.stew.Entities.User"%>
<%@page import="jp.co.ncdc.stew.Utils.StewConstant"%>
<%@page import="jp.co.ncdc.stew.Managers.LanguageManager"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <script type="text/javascript" src="../js/jquery-1.9.1.min.js"></script>
        <script type="text/javascript" src="../js/common.js"></script>
        <link rel="stylesheet" href="../css/stew.css"/>
        <title>Manage Application</title>
        <script type="text/javascript">
            <%
                User a = (User) session.getAttribute(StewConstant.USER_SESSION);
                String pageStr = request.getParameter("page");
                String email = "";
                if (a != null) {
                    email = a.getEmail();
                }
            %>
            var email = "<%=email%>";
            var page_num = parseInt("<%=pageStr%>") ? parseInt("<%=pageStr%>") : 0;
            $(document).ready(function() {
                $('#app-menu').addClass("selected-menu");
                $('.delete-icon').hide();
                getAppOfUser();
                $('.icon-check').click(function(){
                    if ($(this).is(':checked')) {
                        $(this).parent().parent().addClass("select-tr");
                    }else{
                        $(this).parent().parent().removeClass("select-tr");
                    }
                    if($('.select-tr').length != 0){
                        $('.delete-icon').show();
                    }else{
                        $('.delete-icon').hide();
                    }
                    if($('.select-tr').length === $('.icon-check').length){
                        $('.icon-check-all').prop('checked', true);
                    }else{
                        $('.icon-check-all').prop('checked', false);
                    }
                });
                
                $('.icon-check-all').click(function(){
                    if ($(this).is(':checked')) {
                        $('.icon-check').prop('checked', true);
                        $('.icon-check').parent().parent().addClass("select-tr");
                    }else{
                        $('.icon-check').prop('checked', false);
                        $('.icon-check').parent().parent().removeClass("select-tr");
                    }
                    if($('.select-tr').length != 0){
                        $('.delete-icon').show();
                    }else{
                        $('.delete-icon').hide();
                    }
                });
            });
            
            function getAppOfUser() {
                $.ajax({
                    type: "GET",
                    dataType: "json",
                    url: "<%=request.getContextPath()%>/app/AppAction",
                    beforeSend: function() {
                        $('input[type=button]').attr("disabled", true);
                    },
                    data: {
                        "userId": email,
                        "page": page_num,
                        "type": GET_LIST
                    },
                    success: function(data) {
                        $('input[type=button]').attr("disabled", false);
                        $("#paging").empty();
                        $("#table_app").empty();
                        var page = generatePaging(page_num, data.total, "app.jsp");
                        $("#paging").append(page);
                        var total = parseInt(data.total);
                        if (total > page_num) {
                            $.each(data.lstApp, function(index, value) {
                                var checkbox = "<input type='checkbox' onclick='checkApp(this)' id='cb+" + value.appID + "' class='icon-check'/>";
                                var action = "<a href='view_app.jsp?edit=" + value.appID + "'><span class='view-icon'><%=LanguageManager.getInstance().getText("viewAction", request)%></span></a>";//                               
                                $('#table_app').append('<tr><td class="check-icon-td">' + checkbox + '</td><td>'+value.appName+'</td><td>' + value.appID + '</td><td><span class="td_des">' + value.groups + '</span></td><td>' + value.createDate + '</td><td>' + action + '</td></tr>');
                            });

                        } else {
                            if (total === 0) {
                                $("#table_app").append("<tr> <td colspan='6' class='no_result'><%=LanguageManager.getInstance().getText("noResult", request)%></td></tr>");
                            } else {
                                if (page_num > 0) {
                                    page_num = page_num - 1;
                                    getAppOfUser();
                                }
                            }
                        }
                    },
                    error: function() {
                        $('input[type=button]').attr("disabled", false);
                    }
                });
            }
              function checkApp(selected){
                    if ($(selected).is(':checked')) {
                        $(selected).parent().parent().addClass("select-tr");
                    }else{
                        $(selected).parent().parent().removeClass("select-tr");
                    }
                    if($('.select-tr').length != 0){
                        $('.delete-icon').show();
                    }else{
                        $('.delete-icon').hide();
                    }
                    if($('.select-tr').length === $('.icon-check').length){
                        $('.icon-check-all').prop('checked', true);
                    }else{
                        $('.icon-check-all').prop('checked', false);
                    }
                }
        </script>
    </head>
    <body>    
        <div class="page_content">
            <jsp:include page="../common/header.jsp" />   
            <div class="content">
                <div class="content_header">
                    <%=LanguageManager.getInstance().getText("groupList", request)%>
                </div>   
                <table class="filter_header">
                    <tr>
                        <td class="delete-icon-td">
                            <span class="delete-icon"><%=LanguageManager.getInstance().getText("delete", request)%></span>
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
                                <th style="width: 5%"><input type="checkbox" class="icon-check-all" id="checkAllApp"/></th>
                                <th style="width: 15%"><%=LanguageManager.getInstance().getText("thName", request)%></a></th>
                                <th style="width: 25%"><%=LanguageManager.getInstance().getText("thAppId", request)%></th>
                                <th style="width: 25%"><%=LanguageManager.getInstance().getText("thGroup", request)%></th>
                                <th style="width: 15%"><%=LanguageManager.getInstance().getText("createDate", request)%></th>
                                <th><%=LanguageManager.getInstance().getText("thOption", request)%></th>
                            </tr>
                        </thead>
                        <tbody id="table_app" class="table_list">

                        </tbody>
                        <tfoot>
                        <td colspan="3" align="left"> <span class="create-icon"><a href="create_app.jsp"><%=LanguageManager.getInstance().getText("create", request)%></a></span></td>
                        <td colspan="3" align="right"><div id="paging"></div></td>
                        </tfoot>
                    </table>
                </div>
            </div>
        </div>
    </body>
</html>
