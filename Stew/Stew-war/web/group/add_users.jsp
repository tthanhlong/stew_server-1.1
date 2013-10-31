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
        <title>Add users</title>
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
                $('.function-btn').hide();
                $('#group-menu').addClass("selected-menu");
                var key = queryString()['key'];
                if (key !== null && key !== "" && key !== undefined) {
                    //search by key
                    $('#text-search').val(decodeURIComponent(key));
                    getUsersOfGroup(page_num, decodeURIComponent(key));
                }else{
                    //get all users
                    getUsersOfGroup(page_num, "");
                }
                getRoles();
                $(document).on("click",".icon-check",function(){
                    if ($(this).is(':checked')) {
                        $(this).parent().parent().addClass("select-tr");
                    }else{
                        $(this).parent().parent().removeClass("select-tr");
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
                            $('.function-btn').show();
                            $('.icon-check').prop('checked', true);
                            $('#role-group').attr("disabled", false);
                            $('.icon-check').parent().parent().addClass("select-tr");
                        }else{
                            $('.function-btn').hide();
                            $('.icon-check').prop('checked', false);
                            $('#role-group').attr("disabled", true);
                            $('.icon-check').parent().parent().removeClass("select-tr");
                        }
                    }
                });
                
                $('.add-to-group-btn').click(function(){
                    var role = $('#role-group').val();
                    if (role === "-1") {
                        alert("<%=LanguageManager.getInstance().getText("errorChooseRole", request)%>");
                    }else{
                        //add users to group
                        var user_ids = new Array();
                        var index = 0;

                        $(".select-tr").each(function(){
                            user_ids[index] = $(this).find(".icon-check").attr("id");
                            index++;
                        });
                        
                        if (confirm("<%=LanguageManager.getInstance().getText("confirmAddUser", request)%>")) {
                            $.ajax({
                                type: "POST",
                                dataType: "json",
                                url: "<%=request.getContextPath()%>/group/GroupAction",
                                beforeSend: function(){
                                    //before send
                                },
                                complete: function(){
                                    //complete
                                },
                                data: {
                                    "userids": user_ids.join(),
                                    "groupID": "<%=groupID%>",
                                    "role": role,
                                    "type": ADD_USER_TO_GROUP
                                },
                                success: function(data) {
                                    if (data.status === STATUS_CODE_OK) {
                                       window.location.href = "group.jsp";
                                    }
                                },
                                error: function() {
                                    alert("error get role by group and user");
                                }
                            });
                        }
                    }
                });
            });
            
            /**
             * 
             */
            function getUsersOfGroup(page_num, key) {
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
                            "email": "<%=email%>",
                            "key": key,
                            "type": GET_REMAINING_USERS
                        },
                        complete: function() {
                            //complete;
                            $('input[type=button]').attr("disabled", false);
                            $('.icon-check-all').prop('checked', false);
                        },
                        success: function(data) {
                            $("#paging").empty();
                            $("#table_groupuser").empty();
                            if (data != null) {
//                                var page = generatePaging(page_num, data.total, "group.jsp");
                                var addmore = "";
                                if (key !== "") {
                                    addmore += "&key=" + key;
                                }
                                var page = generatePagingShowUser(page_num, data.total, "add_users.jsp?id=" + <%=groupID%>, addmore);
                                if(data.total > 1){
                                    $("#paging").append(page);
                                }
                                $("#groupName").html(data.groupName);
                                var total = parseInt(data.total);
                                if (total > page_num) {
                                    if (data.listItems !== null) {
                                        $.each(data.listItems, function(index, value) {
                                            index = (parseInt(index) + 1) + (page_num * <%=StewConstant.ITEM_PER_PAGE_MANAGE%>);
                                            var checkbox = "<input class='icon-check' type='checkbox' id='" + value.email + "' onclick='checkGroupUser(this)'/>";
                                            $('#table_groupuser').append('<tr><td class="check-icon-td">' + checkbox + '</td><td>'+value.email+'</td><td><span class="td_des">' + value.firstName + ' ' + value.lastName + '</span></td><td>' + formatDate(value.dateCreated) + '</td></tr>');
                                        });
                                    }
                                } else {
                                    if (total === 0) {
                                        $("#table_groupuser").append("<tr> <td colspan='4' class='no_result'><%=LanguageManager.getInstance().getText("noResult", request)%></td></tr>");
                                    } else {
                                        if (page_num > 0) {
                                            page_num = page_num - 1;
                                            getUsersOfGroup(page_num, key);
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
                    $('.function-btn').show();
                    $('#role-group').attr("disabled", false);
                }else{
                    $('.function-btn').hide();
                    $('#role-group').attr("disabled", true);
                }
                if($('.select-tr').length === $('.icon-check').length){
                    $('.icon-check-all').prop('checked', true);
                }else{
                    $('.icon-check-all').prop('checked', false);
                }
            }
            
            function getRoles(){
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
                        "userId": "<%=email%>",
                        "group": "<%=groupID%>",
                        "type": GET_ROLE_USER_OF_GROUP
                    },
                    success: function(data) {
                        if (data.status === STATUS_CODE_OK) {
                            $.each(data.listItems, function(index, value) {
                                $("#role-group").append('<option value="' + value.roleId + '">' + value.roleName + '</option>');
                            });
                        }
                    },
                    error: function() {
                        alert("error get role by group and user");
                    }
                });
            }
            
            function searchUserOfGroup(){
                console.log("inside search group");
                var keySearch = $('#text-search').val();
                console.log(keySearch);
                
                getUsersOfGroup(0, keySearch);
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
                            &nbsp;
                        </td>
                        <td class="dropdown-list">
                            <select id="role-group" disabled="disabled">
                                <option value="-1"><%=LanguageManager.getInstance().getText("chkSelectRole", request)%></option>
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
                            <th style="width: 15%"><%=LanguageManager.getInstance().getText("thUserName", request)%></th>
                            <th style="width: 20%"><%=LanguageManager.getInstance().getText("createDate", request)%></th>
                        </tr>
                    </thead>
                    <tbody id="table_groupuser" class="table_list">                       
                    </tbody>
                    <tfoot>
                        <tr class="tr-header">
                            <td colspan="3" align="left">&nbsp;</td>
                            <td align="right"><div id="paging"></div></td>
                        </tr>
                    </tfoot>
                </table>
                <table align="center" class="function-btn">
                    <tr>
                        <td>
                            <div class="button_group">                                
                                <input type="button" class="cancel-btn" onclick="{window.location = 'group.jsp'}"/>
                                <input type="button" class="add-to-group-btn" onclick=""/>
                            </div>
                            <div>
                                <input type="button" style="display: none" class="delete-btn" onclick="">
                            </div>
                        </td>
                    </tr>
                </table>
            </div>
        </div>
    </body>
</html>
