<%-- 
    Document   : user
    Created on : Aug 28, 2013, 9:21:28 AM
    Author     : tthanhlong
--%>

<%@page import="java.text.DateFormat"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="jp.co.ncdc.stew.Entities.GroupUser"%>
<%@page import="jp.co.ncdc.stew.Controllers.GroupController"%>
<%@page import="jp.co.ncdc.stew.Entities.User"%>
<%@page import="jp.co.ncdc.stew.APIs.model.UserModel"%>
<%@page import="jp.co.ncdc.stew.Utils.StewConstant"%>
<%@page import="jp.co.ncdc.stew.Entities.Role"%>
<%@page import="jp.co.ncdc.stew.APIs.model.DataModel"%>
<%@page import="jp.co.ncdc.stew.Controllers.UserController"%>
<%@page import="jp.co.ncdc.stew.Managers.LanguageManager"%>
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
            $(document).ready(function() {
                $('#user-menu').removeClass("has-hover");
                $('#user-menu').addClass("selected-menu");
                $('.delete-icon').hide();

                $('.icon-check').click(function() {
                    if ($(this).is(':checked')) {
                        $(this).parent().parent().addClass("select-tr");
                    } else {
                        $(this).parent().parent().removeClass("select-tr");
                    }
                    if ($('.select-tr').length != 0) {
                        $('.delete-icon').show();
                    } else {
                        $('.delete-icon').hide();
                    }
                    if ($('.select-tr').length === $('.icon-check').length) {
                        $('.icon-check-all').prop('checked', true);
                    } else {
                        $('.icon-check-all').prop('checked', false);
                    }
                });

                $('.icon-check-all').click(function() {
                    if ($('.icon-check').length !== 0) {
                        if ($(this).is(':checked')) {
                            $('.icon-check').prop('checked', true);
                            $('.icon-check').parent().parent().addClass("select-tr");
                        } else {
                            $('.icon-check').prop('checked', false);
                            $('.icon-check').parent().parent().removeClass("select-tr");
                        }
                        if ($('.select-tr').length != 0) {
                            $('.delete-icon').show();
                        } else {
                            $('.delete-icon').hide();
                        }
                    }
                });

                $('.delete-icon').click(function() {
                    var delete_ids = new Array();
                    var user_delete = new Array();
                    var index = 0;

                    $(".select-tr").each(function() {
                        delete_ids[index] = $(this).find(".icon-check").val();
                        user_delete[index] = $(this).find(".email-selected").html();
                        index++;
                    });

                    if (confirm("<%=LanguageManager.getInstance().getText("confirmDelete", request)%>")) {
                        $.ajax({
                            type: "POST",
                            dataType: "json",
                            url: "UserAction",
                            beforeSend: function() {
                                //before send
                            },
                            complete: function() {
                                //complete
                            },
                            data: {
                                "userids": user_delete.join(),
                                "deleteids": delete_ids.join(),
                                "type": DELETE
                            },
                            success: function(data) {
                                if (data.status === STATUS_CODE_OK) {
                                    window.location.href = "user.jsp";
                                }
                            },
                            error: function() {
                                alert("error get role by group and user");
                            }
                        });
                    }
                });

                $('#search-user').click(function() {
                    var keySearch = $('#key-search').val();
                    var groupID = $('#group-selectbox').val();

                    if (groupID == "-1") {
                        window.location.href = "user.jsp?key=" + keySearch;
                    } else {
                        window.location.href = "user.jsp?group=" + groupID + "&key=" + keySearch;
                    }
                })
            });
        </script>
        <title><%=LanguageManager.getInstance().getText("userLabel", request)%></title>
    </head>
    <body>
        <%
            User a = (User) session.getAttribute(StewConstant.USER_SESSION);
            String email = "";
            if (a != null) {
                email = a.getEmail();
            }

            String pageString = request.getParameter("page");
            String groupID = request.getParameter("group");
            String keySearch = request.getParameter("key");
            long groupIDLong = 0;
            int pageNumber = 0;
            try {
                pageNumber = Integer.parseInt(pageString);
            } catch (Exception e) {
            }
            try {
                groupIDLong = Long.parseLong(groupID);
            } catch (Exception e) {
            }
            UserController userController = new UserController();
            DataModel users = null;
            if (groupIDLong == 0 && (keySearch == null || keySearch == "")) {
                //load all users
                users = userController.getUsersByPageNumber(pageNumber, email);
            } else if (groupIDLong == 0) {
                //if search by key search only
                users = userController.getUsersByPageNumberAndKeySearch(pageNumber, email, keySearch);
            } else if (keySearch == null || keySearch == "") {
                //search by groupId only
                users = userController.getUsersByPageNumberAndGroupID(pageNumber, email, groupIDLong);
            } else {
                users = userController.getUsersByPageNumberAndGroupIDAndKeySearch(pageNumber, email, groupIDLong, keySearch);
            }

            GroupController groupController = new GroupController();
            List<GroupUser> lstGroupUsers = groupController.getUserGroupDetailByUserIdWithManagerRole(email);
        %>
        <div class="page_content">
            <jsp:include page="../common/header.jsp" />
            <div class="content">
                <div class="content_header">
                    <%=LanguageManager.getInstance().getText("userList", request)%>
                </div>
                <table class="filter_header">
                    <tr>
                        <td class="delete-icon-td">
                            <span class="delete-icon"><%=LanguageManager.getInstance().getText("delete", request)%></span>
                        </td>
                        <td class="dropdown-list">
                            <select id="group-selectbox">
                                <option value="-1"><%=LanguageManager.getInstance().getText("chkSelectGroup", request)%></option>
                                <%
                                    if (lstGroupUsers != null) {
                                        int countGroupUser = lstGroupUsers.size();
                                        for (int j = 0; j < countGroupUser; j++) {
                                            System.out.println("before check" + groupID);
                                            if (groupIDLong != 0 && lstGroupUsers.get(j).getGroupId() == groupIDLong) {
                                                System.out.println("inside check");
                                %>
                                <option selected="selected" value="<%=lstGroupUsers.get(j).getGroupId()%>"><%=lstGroupUsers.get(j).getName()%></option>
                                <%
                                } else {
                                %>
                                <option value="<%=lstGroupUsers.get(j).getGroupId()%>"><%=lstGroupUsers.get(j).getName()%></option>
                                <%
                                            }
                                        }
                                    }
                                %>
                            </select>
                        </td>
                        <td class="search_td">
                            <div class="search_div">
                                <input value="<%=(keySearch == null || keySearch == "") ? "" : keySearch%>" id="key-search" placeholder="<%=LanguageManager.getInstance().getText("searchHolder", request)%>" type="text">
                                <span id="search-user" class="img-search"></span>
                            </div>
                        </td>
                    </tr>
                </table>
                <div class="user_list">
                    <table class="table_content">
                        <thead>
                            <tr class="tr-header">
                                <th style="width: 5%"><input type="checkbox" class="icon-check-all"/></th>
                                <th style="width: 25%"><%=LanguageManager.getInstance().getText("account", request)%></th>
                                <th style="width: 15%"><%=LanguageManager.getInstance().getText("thGroup", request)%></th>
                                <th style="width: 15%"><%=LanguageManager.getInstance().getText("thRole", request)%></th>
                                <th style="width: 15%"><%=LanguageManager.getInstance().getText("thUserName", request)%></th>
                                <th style="width: 15%"><%=LanguageManager.getInstance().getText("thCreateDate", request)%></th>
                                <th><%=LanguageManager.getInstance().getText("action", request)%></th>
                            </tr>
                        </thead>
                        <tbody class="table_list">
                            <%
                                if (users.getListItems() != null && !users.getListItems().isEmpty()) {
                                    DateFormat df = new SimpleDateFormat("yyyy.MM.dd");
                                    int count = users.getListItems().size();
                                    int totalPage = users.getTotal();
                                    if (users.getListItems() != null && count > 0) {
                                        for (int i = 0; i < count; i++) {
                                            Object[] lstAllUsers = (Object[]) users.getListItems().get(i);
                            %>
                            <tr>
                                <td class="check-icon-td"><input value="<%=lstAllUsers[0]%>" class="icon-check" type="checkbox"/></td>
                                <td class="email-selected long-text"><%=lstAllUsers[1]%></td>
                                <td><%=lstAllUsers[2]%></td>
                                <td><%=lstAllUsers[3]%></td>
                                <td><%=lstAllUsers[4] + " " + lstAllUsers[5]%></td>
                                <td><%=df.format(lstAllUsers[6])%></td>

                                <td class="edit-btn">
                                    <a title="<%=LanguageManager.getInstance().getText("editAction", request)%>" class="action" href="edit_user.jsp?email=<%=lstAllUsers[1]%>"><span class='edit-icon'><%=LanguageManager.getInstance().getText("editAction", request)%></span></a>
                                </td>
                            </tr>
                            <%
                            }
                                }else{
                                    %>
                                <tr>
                                    <td colspan="7" class='no_result'><%=LanguageManager.getInstance().getText("noResult", request)%></td>
                                <tr>
                                    <%
                                }
                            %>
                        </tbody>
                        <tfoot>
                            <tr class="tr-header">
                                <td colspan="4" align="left"> <span class="create-icon"><a href="create_user.jsp"><%=LanguageManager.getInstance().getText("create", request)%></a></span></td>
                                <td colspan="3" align="right">
                                    <div id="paging">
                                        <%
                                            if (totalPage > 1 && totalPage > pageNumber) {
                                                if (pageNumber > 0) {
                                                    if (groupID == null) {
                                        %>
                                        <a class="page-first" href="user.jsp?page=0&key=<%=(keySearch == null) ? "" : keySearch%>"></a>
                                        <a class="page-prev" href="user.jsp?page=<%=pageNumber - 1%>&key=<%=(keySearch == null) ? "" : keySearch%>"></a>
                                        <%
                                        } else {
                                        %>
                                        <a class="page-first" href="user.jsp?page=0&group=<%=groupID%>&key=<%=(keySearch == null) ? "" : keySearch%>"></a>
                                        <a class="page-prev" href="user.jsp?page=<%=pageNumber - 1%>&group=<%=groupID%>&key=<%=(keySearch == null) ? "" : keySearch%>"></a>
                                        <%
                                            }
                                        } else {
                                        %>
                                        <a class="page-first"></a>
                                        <a class="page-prev"></a>
                                        <%                                    }
                                        %>
                                        <a class="current">page <%=pageNumber + 1%> of <%=totalPage%></a>
                                        <%
                                            if (pageNumber < totalPage - 1) {
                                                if (groupID == null) {
                                        %>
                                        <a class="page-next" href="user.jsp?page=<%=pageNumber + 1%>&key=<%=(keySearch == null) ? "" : keySearch%>"></a>
                                        <a class="page-last" href="user.jsp?page=<%=totalPage - 1%>&key=<%=(keySearch == null) ? "" : keySearch%>"></a>
                                        <%
                                        } else {
                                        %>
                                        <a class="page-next" href="user.jsp?page=<%=pageNumber + 1%>&group=<%=groupID%>&key=<%=(keySearch == null) ? "" : keySearch%>"></a>
                                        <a class="page-last" href="user.jsp?page=<%=totalPage - 1%>&group=<%=groupID%>&key=<%=(keySearch == null) ? "" : keySearch%>"></a>
                                        <%
                                            }
                                        } else {
                                        %>
                                        <a class="page-next"></a>
                                        <a class="page-last"></a>
                                        <%                                            }
                                            }
                                        } else {
                                        %>
                                        </tbody>
                                        <tfoot>
                                            <tr class="tr-header">
                                                <td colspan="4" align="left"> <span class="create-icon"><a href="create_user.jsp"><%=LanguageManager.getInstance().getText("create", request)%></a></span></td>
                                                <td colspan="3" align="right"></td>
                                                <%
                                                    }
                                                %>
                                    </div>
                                </td>
                            </tr>
                        </tfoot>
                    </table>
                </div>   
            </div>
        </div>
    </body>
</html>
