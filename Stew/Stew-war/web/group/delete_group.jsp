<%-- 
    Document   : delete_user
    Created on : Aug 30, 2013, 2:27:57 PM
    Author     : tthanhlong
--%>

<%@page import="jp.co.ncdc.stew.Entities.GroupUser"%>
<%@page import="jp.co.ncdc.stew.Controllers.GroupController"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <script type="text/javascript" src="../js/jquery-1.9.1.min.js"></script>
        <script type="text/javascript" src="../js/common.js"></script>
        <link rel="stylesheet" href="../css/stew.css"/>
        <title>Delete group</title>
        <%
            String groupID = request.getParameter("id");
            GroupController groupController = new GroupController();
            GroupUser groupUser = groupController.getGroupByID(groupID);
            boolean flag = false;
            if (groupUser !=  null) {
                flag = true;
            }
        %>
        <script type="text/javascript">
            $(document).ready(function(){
                var flag = true;
                $('#delete-btn').click(function(){
                    if(flag){
                        $.ajax({
                            type: "POST",
                            dataType: "json",
                            url: "GroupAction",
                            beforeSend: function(){
                                $('.yes-no-btn').attr("disabled", true).removeClass("has-hover");
                                flag = false;
                            },
                            data: {
                                "groupID": "<%=groupID%>",
                                "type": DELETE
                            },
                            success: function(data){
                                flag = true;
                                $('.yes-no-btn').attr("disabled", false).addClass("has-hover");
                                if(data.status === STATUS_CODE_OK){
                                    window.location.href = "group.jsp";
                                }else{
                                    $('#group-validate').html(" Cannot delete group!");
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
                <h4>Are you sure you want to delete group <i><%=flag?groupUser.getName():""%></i>?</h4>
                <input id="delete-btn" type="button" class="yes-no-btn has-hover" value="Yes"/>
                <input onclick="{window.location='group.jsp'}" type="button" class="yes-no-btn has-hover" value="Back"/>
                <div class="validate" id="group-validate"></div>
            </div>
        </div>
    </body>
</html>
