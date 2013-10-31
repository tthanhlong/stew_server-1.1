<%-- 
    Document   : create_user
    Created on : Aug 29, 2013, 9:34:37 AM
    Author     : tthanhlong
--%>

<%@page import="jp.co.ncdc.stew.Utils.StewConstant"%>
<%@page import="jp.co.ncdc.stew.Entities.User"%>
<%@page import="jp.co.ncdc.stew.Managers.LanguageManager"%>
<%@page import="jp.co.ncdc.stew.Entities.Role"%>
<%@page import="jp.co.ncdc.stew.APIs.model.DataModel"%>
<%@page import="jp.co.ncdc.stew.Controllers.UserController"%>
<%@page import="jp.co.ncdc.stew.Entities.GroupUser"%>
<%@page import="jp.co.ncdc.stew.Controllers.GroupController"%>
<%@page import="java.util.List"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <script type="text/javascript" src="../js/jquery-1.9.1.min.js"></script>
        <script type="text/javascript" src="../js/common.js"></script>
        <title>Show users</title>
        <script type="text/javascript">
            <%
                String groupID = request.getParameter("id");
                String pageStr = request.getParameter("page");
                User a = (User) session.getAttribute(StewConstant.USER_SESSION);
                String email = "";
                if (a != null) {
                    email = a.getEmail();
                }
            %>
            var groupID = "<%=groupID%>";
            var page_num = parseInt("<%=pageStr%>") ? parseInt("<%=pageStr%>") : 0;
            $(document).ready(function() {
                $('.delete-icon').hide();
                $('#group-menu').addClass("selected-menu");
                var roleSearch = queryString()['role'];
                var key = queryString()['key'];
                if (roleSearch !== null && roleSearch !== "" && roleSearch !== undefined) {
                    //filter with role
                    if (key !== null && key !== "" && key !== undefined) {
                        //search by key and role
                        $('#text-search').val(decodeURIComponent(key));
                        getUsersOfGroup(page_num, roleSearch, decodeURIComponent(key));
                    }else{
                        //filter by role only
                        getUsersOfGroup(page_num, roleSearch, "");
                    }
                }else{
                    if (key !== null && key !== "" && key !== undefined) {
                        //search by key only
                        $('#text-search').val(decodeURIComponent(key));
                        getUsersOfGroup(page_num, "", decodeURIComponent(key));
                    }else{
                        //get all
                        getUsersOfGroup(page_num, "", "");
                    }
                }
                getRoles(roleSearch);
                $(document).on("click",".icon-check",function(){
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
                    if ($('.icon-check').length !== 0) {
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
                    }
                });
                
                $('.delete-icon').click(function() {
                    var delete_ids = new Array();
                    var index = 0;

                    $(".select-tr").each(function() {
                        delete_ids[index] = $(this).find(".icon-check").attr("id");
                        index++;
                    });

                    if (confirm("<%=LanguageManager.getInstance().getText("confirmRemoveUser", request)%>")) {
                        $.ajax({
                            type: "POST",
                            dataType: "json",
                            url: "<%=request.getContextPath()%>/group/GroupAction",
                            beforeSend: function() {
                                //before send
                            },
                            complete: function() {
                                //complete
                            },
                            data: {
                                "deleteids": delete_ids.join(),
                                "groupID": "<%=groupID%>",
                                "email": "<%=email%>",
                                "type": REMOVE_USER_FROM_GROUP
                            },
                            success: function(data) {
                                if (data.status === STATUS_CODE_OK) {
                                    getUsersOfGroup(0, roleSearch, key);
                                }
                            },
                            error: function() {
                                alert("error get role by group and user");
                            }
                        });
                    }
                });
            });
            
            /**
             * 
             */
            function getUsersOfGroup(page_num, role, key) {
                if (groupID != "null") {
                    $.ajax({
                        type: "POST",
                        dataType: "json",
                        url: "<%=request.getContextPath()%>/group/GroupAction",
                        beforeSend: function() {
                            $('input[type=button]').attr("disabled", true);
                        },
                        data: {
                            "page": page_num,
                            "groupID": groupID,
                            "role": role,
                            "key": key,
                            "type": GET_USER_BYGROUP
                        },
                        complete: function() {
                            //complete
                            $('.delete-icon').hide();
                            $('.icon-check-all').prop('checked', false);
                        },
                        success: function(data) {
                            $('input[type=button]').attr("disabled", false);
                            $("#paging").empty();
                            $("#table_groupuser").empty();
                            if (data != null) {
//                                var page = generatePaging(page_num, data.total, "group.jsp");
                                var addmore = "";
                                if (role !== "") {
                                    addmore += "&role=" + role;
                                }
                                if (key !== "") {
                                    addmore += "&key=" + key;
                                }
                                var page = generatePagingShowUser(page_num, data.total, "show_users.jsp?id=" + <%=groupID%>, addmore);
                                if(data.total > 1){
                                    $("#paging").append(page);
                                }
                                $("#groupName").html(data.groupName);
                                var total = parseInt(data.total);
                                if (total > page_num) {
                                    if (data.items !== null) {
                                        $.each(data.items, function(index, value) {
                                            index = (parseInt(index) + 1) + (page_num * <%=StewConstant.ITEM_PER_PAGE_MANAGE%>);
                                            var checkbox = "<input class='icon-check' type='checkbox' lang='" + value.roleName + "' id='" + value.email + "' onclick='checkGroupUser(this)'/>";
                                            var roleName = "<span>" + value.roleName + "</span></a>";
                                            $('#table_groupuser').append('<tr><td class="check-icon-td">' + checkbox + '</td><td>'+value.email+'</td><td>' + roleName + '</td><td><span class="td_des">' + value.firstName + ' ' + value.lastName + '</span></td><td>' + value.createDate + '</td></tr>');
                                        });
                                    }
                                } else {
                                    if (total === 0) {
                                        $("#table_groupuser").append("<tr> <td colspan='5' class='no_result'><%=LanguageManager.getInstance().getText("noResult", request)%></td></tr>");
                                    } else {
                                        if (page_num > 0) {
                                            page_num = page_num - 1;
                                            getUsersOfGroup(page_num, role, key);
                                        }
                                    }
                                }
                            }
                        },
                        error: function() {
                            $('input[type=button]').attr("disabled", false);
                        }
                    });
                }
            }
            function checkGroupUser(selected){
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
            
            function getRoles(role){
                $.ajax({
                    type: "GET",
                    dataType: "json",
                    url: "<%=request.getContextPath()%>/group/GroupAction",
                    beforeSend: function() {
                        //before send
                    },
                    complete: function() {
                        //complete
                    },
                    data: {
                        "type": GET_ROLE_OF_GROUP
                    },
                    success: function(data) {
                        if (data.status === STATUS_CODE_OK) {
                            $.each(data.listItems, function(index, value) {
                                if (value.roleId === role) {
                                    $("#role-group").append('<option selected="selected" value="' + value.roleId + '">' + value.roleName + '</option>');
                                }else{
                                    $("#role-group").append('<option value="' + value.roleId + '">' + value.roleName + '</option>');
                                }
                            });
                        }
                    },
                    error: function() {
                        alert("error get role by group and user");
                    }
                });
            }
            
            function searchUserOfGroup(){
                var role = $('#role-group').val();
                var keySearch = $('#text-search').val();
                
                getUsersOfGroup(0, role, keySearch);
            }
        </script>
    </head>
    <body>

        <div class="page_content">
            <jsp:include page="../common/header.jsp" />
            <div class="content">
                <div class="content_header">
                    <%=LanguageManager.getInstance().getText("groupInfo", request)%>
                </div>
                <div class="group_user_top_label">
                    <div class="group_name"><%=LanguageManager.getInstance().getText("groupName", request)%>:<span id="groupName"></span></div> 
                    <div class=""lable_group_name><%=LanguageManager.getInstance().getText("groupUserLabel", request)%></div>
                </div>
                <table class="filter_header">
                    <tr>
                        <td class="delete-icon-td">
                            <span class="delete-icon"><%=LanguageManager.getInstance().getText("delete", request)%></span>
                        </td>
                        <td class="dropdown-list">
                            <select id="role-group">
                                <option value=""><%=LanguageManager.getInstance().getText("chkSelectRole", request)%></option>
                            </select>
                        </td>
                        <td class="search_td">
                            <div class="search_div">
                                <input placeholder="<%=LanguageManager.getInstance().getText("searchHolder", request)%>" type="text" id="text-search">
                                <input type="button" onclick="searchUserOfGroup()" class="search-button">                                            
                            </div>
                        </td>
                    </tr>
                </table>

                <table class="table_content">
                    <thead>
                        <tr class="tr-header">
                            <th style="width: 5%"><input type="checkbox" class="icon-check-all"/></th>
                            <th style="width: 25%"><%=LanguageManager.getInstance().getText("thEmail", request)%></th>
                            <th style="width: 20%"><%=LanguageManager.getInstance().getText("thRole", request)%></th>
                            <th style="width: 15%"><%=LanguageManager.getInstance().getText("thUserName", request)%></th>
                            <th style="width: 20%"><%=LanguageManager.getInstance().getText("createDate", request)%></th>
                        </tr>
                    </thead>
                    <tbody id="table_groupuser" class="table_list">                       
                    </tbody>
                    <tfoot>
                        <tr class="tr-header">
                            <td colspan="4" align="left"> <span class="create-icon"><a href="add_users.jsp?id=<%=groupID%>"><%=LanguageManager.getInstance().getText("create", request)%></a></span></td>
                            <td align="right"><div id="paging"></div></td>
                        </tr>
                    </tfoot>
                </table>
            </div>
        </div>
    </body>
</html>
