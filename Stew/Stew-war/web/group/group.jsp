<%-- 
    Document   : user
    Created on : Aug 28, 2013, 9:21:28 AM
    Author     : tthanhlong
--%>


<%@page import="jp.co.ncdc.stew.Adapters.UserGroupDetailAdapter"%>
<%@page import="jp.co.ncdc.stew.Entities.User"%>
<%@page import="jp.co.ncdc.stew.APIs.model.DataModel"%>
<%@page import="jp.co.ncdc.stew.Controllers.GroupController"%>
<%@page import="jp.co.ncdc.stew.Entities.GroupUser"%>
<%@page import="jp.co.ncdc.stew.Managers.LanguageManager"%>
<%@page import="jp.co.ncdc.stew.Utils.StewConstant"%>
<%@page import="java.util.List"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <script type="text/javascript" src="../js/jquery-1.9.1.min.js"></script>
        <script type="text/javascript" src="../js/common.js"></script>
        <link rel="stylesheet" href="../css/stew.css"/>
        <script type="text/javascript">
            <%
                User a = (User) session.getAttribute(StewConstant.USER_SESSION);
                String pageStr = request.getParameter("page");
                String email = "";
                boolean isSuperAdmin = false;
                if (a != null) {
                    UserGroupDetailAdapter userGroupDetailAdapter = new UserGroupDetailAdapter();
                    email = a.getEmail();
                    isSuperAdmin = userGroupDetailAdapter.checkUserIsSupperAdmin(email);
                }
            %>
            var email = "<%=email%>";
            var page_num = parseInt("<%=pageStr%>") ? parseInt("<%=pageStr%>") : 0;
            $(document).ready(function() {
                var flag = <%=isSuperAdmin%>;
                if (!flag) {
                    $('.create-icon').hide();
                }
                $('.delete-icon').hide();
                var groupSearch = queryString()['group'];
                var key = queryString()['key'];
                
                $('#group-menu').addClass("selected-menu");
                //if search by group
                if (groupSearch !== null && groupSearch !== "" && groupSearch !== undefined) {
                    //if search by group and key
                    if (key !== null && key !== "" && key !== undefined) {
                        $('#text-search').val(decodeURIComponent(key));
                        getGroupOfUser(GET_LIST_BY_GROUP_AND_KEY, page_num, decodeURIComponent(groupSearch), decodeURIComponent(key));
                    }else{
                        //search by group only
                        getGroupOfUser(GET_LIST_BY_GROUP_AND_KEY, page_num, decodeURIComponent(groupSearch), "");
                    }
                }else{
                    //search by key only
                    if (key !== null && key !== "" && key !== undefined) {
                        $('#text-search').val(decodeURIComponent(key));
                        getGroupOfUser(GET_LIST_BY_GROUP_AND_KEY, page_num, "", decodeURIComponent(key));
                    }else{
                        //search all
                        getGroupOfUser(GET_LIST, page_num, "", "");
                    }
                }
                
                loadGroup(groupSearch);
                    
                $('.delete-icon').click(function(){
                    var delete_ids = new Array();
                    var index = 0;
                    
                    $(".select-tr").each(function(){
                        delete_ids[index] = $(this).find(".icon-check").attr("id");
                        index++;
                    });
                    
                    if(confirm("<%=LanguageManager.getInstance().getText("confirmDeleteGroup", request)%>")) {
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
                                "deleteids": delete_ids.join(),
                                "type": DELETE
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
                        if($('.select-tr').length != 0 && <%=isSuperAdmin%>){
                            $('.delete-icon').show();
                        }else{
                            $('.delete-icon').hide();
                        }
                    }
                });
            });
            /**
             * get group by user
             * @returns {undefined}
             */
            function getGroupOfUser(type, page_num, group, key) {
                $.ajax({
                    type: "GET",
                    dataType: "json",
                    url: "<%=request.getContextPath()%>/group/GroupAction",
                    beforeSend: function() {
                        $('input[type=button]').attr("disabled", true);
                    },
                    data: {
                        "userId": email,
                        "page": page_num,
                        "group": group,
                        "key": key,
                        "type": type
                    },
                    success: function(data) {
                        $('input[type=button]').attr("disabled", false);
                        $("#paging").empty();
                        $("#table_group").empty();
                        var addmore = "";
                        if (group !== "") {
                            addmore += "&group=" + group;
                        }
                        if (key !== "") {
                            addmore += "&key=" + key;
                        }
                        var page = generatePaging(page_num, data.total, "group.jsp", addmore);
                        if(data.total > 1){
                            $("#paging").append(page);
                        }
                        var total = parseInt(data.total);
                        if (total > page_num) {
                            $.each(data.items, function(index, value) {
                                index = (parseInt(index) + 1) + (page_num * <%=StewConstant.ITEM_PER_PAGE_MANAGE%>);
                                var edit = "";
                                if (<%=isSuperAdmin%>) {
                                    edit = "<a href='create_group.jsp?edit=" + value.groupId + "'><span class='edit-icon'><%=LanguageManager.getInstance().getText("editAction", request)%></span></a>";
                                }
                                var adduser = "<a href='add_users.jsp?id=" + value.groupId +"'><span class='add-user'><%=LanguageManager.getInstance().getText("btnAddUser", request)%></span></a>";
                                var action = edit + "&nbsp&nbsp&nbsp&nbsp&nbsp" + adduser
                                var checkbox = "<input class='icon-check' onclick='checkGroup(this)' type='checkbox' id='" + value.groupId + "'/>";
                                var groupName = "<a href='show_users.jsp?id=" + value.groupId + "'><span class='gruop-icon td_name'>" + value.name + "</span></a>";
                                $('#table_group').append('<tr><td class="check-icon-td">' + checkbox + '</td><td>'+index+'</td><td>' + groupName + '</td><td><span class="td_des">' + value.description + '</span></td><td>' + action + '</td></tr>');
                            });                           
                        } else {
                            if (total === 0) {
                                $("#table_group").append("<tr> <td colspan='5' class='no_result'><%=LanguageManager.getInstance().getText("noResult", request)%></td></tr>");
                            } else {
                                if (page_num > 0) {
                                    page_num = page_num - 1;
                                    getGroupOfUser(type, page_num, group, key);
                                }
                            }
                        }
                    },
                    error: function() {
                        $('input[type=button]').attr("disabled", false);
                    }
                });
            }
                     
            function checkGroup(selected){
                if ($(selected).is(':checked')) {
                    $(selected).parent().parent().addClass("select-tr");
                }else{
                    $(selected).parent().parent().removeClass("select-tr");
                }
                if($('.select-tr').length != 0 && <%=isSuperAdmin%>){
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
            
            /**
             * 
             * @returns {undefined}
             */
            function loadGroup(groupName) {
                $.ajax({
                    type: "GET",
                    dataType: "json",
                    url: "<%=request.getContextPath()%>/group/GroupAction",
                    data: {
                        "userId": email,
                        "type": GET_ALL_DISTINCT_LIST
                    },
                    success: function(data) {
                        $.each(data.items, function(index, value) {
                            if (value.name === decodeURIComponent(groupName)) {
                                $("#group-distinct").append('<option selected="selected" value="' + value.name + '">' + value.name + '</option>');
                            }else{
                                $("#group-distinct").append('<option value="' + value.name + '">' + value.name + '</option>');
                            }
                        });
                    }
                });
            }
            
            function searchGroup(){
                var group = $('#group-distinct').val();
                var keySearch = $('#text-search').val();
                
                getGroupOfUser(GET_LIST_BY_GROUP_AND_KEY, 0, group, keySearch);
            }
                
        </script>
        <title>Group management</title>
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
                            <select id="group-distinct">
                                <option value=""><%=LanguageManager.getInstance().getText("chkSelectGroup", request)%></option>
                            </select>
                        </td>
                        <td class="search_td">
                            <div class="search_div">
                                <input placeholder="<%=LanguageManager.getInstance().getText("searchHolder", request)%>" type="text" id="text-search">
                                <input type="button" onclick="searchGroup()" class="search-button">                                            
                            </div>
                        </td>
                    </tr>
                </table>
                <div class="user_list">
                    <table class="table_content">
                        <thead>
                            <tr class="tr-header">
                                <th style="width: 5%"><input class="icon-check-all" type="checkbox" id="checkAllGroup"/></th>
                                <th style="width: 5%"><%=LanguageManager.getInstance().getText("thNumber", request)%></th>
                                <th style="width: 18%"><%=LanguageManager.getInstance().getText("groupName", request)%></th>
                                <th style="width: 40%"><%=LanguageManager.getInstance().getText("description", request)%></th>
                                <th><%=LanguageManager.getInstance().getText("option", request)%></th>
                            </tr>
                        </thead>
                        <tbody id="table_group" class="table_list">

                        </tbody>
                        <tfoot>
                            <tr class="tr-header">
                                <td colspan="4" align="left"> <span class="create-icon"><a href="create_group.jsp"><%=LanguageManager.getInstance().getText("create", request)%></a></span></td>
                                <td align="right"><div id="paging"></div></td>
                            </tr>
                        </tfoot>
                    </table>
                </div>
            </div>
        </div>
    </body>
</html>
