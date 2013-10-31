<%-- 
    Document   : create_app
    Created on : Oct 2, 2013, 10:35:42 AM
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
        <title>Cerate App</title>
        <script type="text/javascript">
            <%
                User a = (User) session.getAttribute(StewConstant.USER_SESSION);
                String email = "";
                if (a != null) {
                    email = a.getEmail();
                }
                String edit = request.getParameter("edit");
                String appId = "";

                if (edit != null) {
                    appId = edit;
                }
            %>
            var appId = "<%=appId%>";
            var email = "<%=email%>";
            $(document).ready(function() {
                $('#app-menu').addClass("selected-menu");
                loadGroup();
            });

            /**
             * 
             * @returns {undefined}  
             */
            var allGroups = [];
            var numGroup = 0;
            var remain = [];
            var selectedGroups = "";
            function createEditGroupSubmit() {
                getGroupsString();
                var type = appId != "" ? EDIT : CREATE;
                var appName = $('#app_name').val();
                var appID = $('#app_id').val();
                if (appName != "" && appID != "") {
                    $.ajax({
                        type: "POST",
                        dataType: "json",
                        url: "AppAction",
                        beforeSend: function() {
                            $('input[type=button]').attr("disabled", true);
                        },
                        data: {
                            "appId": appID,
                            "appName": appName,
                            "pathCert": $("#Certificate_path").html(),
                            "passCert": $('#cert_pass').val(),
                            "groups": selectedGroups,
                            "userId": email,
                            "type": type
                        },
                        success: function(data) {
                            $('input[type=button]').attr("disabled", false);
                            if (data.status === STATUS_CODE_OK) {
                                window.location.href = "app.jsp";
                            } else {
                                alert("save error");
                            }
                        },
                        error: function() {
                            $('input[type=button]').attr("disabled", false);
                        }
                    });
                } else {
                    $('#group-name-validate').html(" Group name is invalid!");
                }
            }
            //Handle click on plus icon
            function addGroup(addIcon) {
                var length =$('#list-group-role').find("select").length;
                if (length < numGroup) {
                    $('#list-group-role').append("<div class='new-group-role'>" + $('.new-group-role').html() + "</div>");                 
                    $(addIcon).hide();
                    if(length < numGroup -1){
                        $('#list-group-role > .new-group-role:last').find(".plus-icon").show();
                    }
                } else {
                    $('#list-group-role > .new-group-role:last').find(".plus-icon").hide();
                }
                getGroupsString();
            }

            //Handle click on minus icon
            function removeGroup(removeIcon) {
                if ($('.new-group-role').length > 1) {
                    $(removeIcon).parent().remove();
                    $('#list-group-role > .new-group-role:last').find(".plus-icon").show();
                }
                getGroupsString();
            }
            /**
             * 
             * @returns {undefined}
             */
            function loadGroup() {
                $.ajax({
                    type: "GET",
                    dataType: "json",
                    url: "<%=request.getContextPath()%>/group/GroupAction",
                    data: {
                        "userId": email,
                        "type": GET_ALL_LIST
                    },
                    success: function(data) {
                        numGroup = data.items.length;
                        $.each(data.items, function(index, value) {
                            $("select").append('<option value="' + value.groupId + '">' + value.name + '</option>');
                            var idValue = value.groupId + ":" + value.name;
                            allGroups.push(idValue);
                        });
                    }
                });
            }
            /**
             * 
             * @returns {undefined}
             */
            function bindGroupOnView() {
                $("#list-group-role select").each(function() {
                    var firstId = $(this).find("option:selected").val();
                    var firstText = $(this).find("option:selected").text();
                    $(this).empty();
//                    if(firstId != null && firstId != ""){
//                        $(this).append('<option value="' + firstId + '">' + firstText + '</option>');
//                    }
                    for(var i=0; i< allGroups.length; i++){
                        $(this).append('<option value="' + allGroups[i].split(":")[0] + '">' + allGroups[i].split(":")[1] + '</option>');
                    }
                });
            }
           
            //get selected group
            function getGroupsString() {
                selectedGroups = "";
                $("#list-group-role option:selected").each(function() {
                    selectedGroups += $(this).val() + ",";
                    var idValue = $(this).val() + ":" + $(this).text();
                    allGroups = $.grep(allGroups, function( a ) {
                        return a !== idValue;
                    });
                });
                bindGroupOnView();
            }
            //upload file on change
            function uploadFile() {
                $('#upload-form').submit();
                return false;
            }
            //on submit
            function uploadFileSubmit() {
                $('#upload-form').ajaxSubmit({
                    dataType: 'json',
                    semantic: true,
                    success: function(data) {
                        if (data.status == STATUS_CODE_OK) {
                            $("#Certificate_path").html(data.errCode);
                        }
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
                    <span id="page_nane"><%=LanguageManager.getInstance().getText("createApp", request)%></span>                    
                </div> 
                <table class="table_content pleft30">
                    <tr class="group_name_box">
                        <td class="td_lable"><label class="required"><%=LanguageManager.getInstance().getText("appName", request)%></label></td>
                        <td><input id="app_name" type="text"/><label class="group-name-validate"></label></td>
                    </tr>
                    <tr class="group_name_box">
                        <td class="td_lable"><label class="required"><%=LanguageManager.getInstance().getText("appId", request)%></label></td>
                        <td><input id="app_id" type="text"/><label class="group-name-validate"></label></td>
                    </tr>
                    <tr class="group_name_box">
                        <td class="td_lable"><label><%=LanguageManager.getInstance().getText("uploadCetificate", request)%></label></td>
                        <td>
                            <form method="post" action="uploadAction" id="upload-form" enctype="multipart/form-data" onsubmit="uploadFileSubmit();
                return false;">
                                <input type="file" name="fileName" onchange="uploadFile()"/><label class="group-name-validate"></label>
                            </form>
                            <label id="Certificate_path" class="hidden"></label>
                        </td>
                    </tr>
                    <tr class="group_name_box">
                        <td class="td_lable"><label><%=LanguageManager.getInstance().getText("cetificatePass", request)%></label></td>
                        <td><input id="cert_pass" type="text"/><label class="group-name-validate"></label></td>
                    </tr>
                    <tr class="group_description_box">
                        <td class="td_lable"><label class="required"><%=LanguageManager.getInstance().getText("thGroup", request)%></label></td>
                        <td id="list-group-role">
                            <div class="new-group-role">
                                <select class="group"></select>
                                <span class="minus-icon" onclick="removeGroup(this);"></span>
                                <span class="plus-icon" onclick="addGroup(this);"></span>
                            </div>
                        </td>
                    </tr>              
                    <tr>
                        <td></td>
                        <td>
                            <div class="button_group">                                
                                <input type="button" class="btn_cancel" value="<%=LanguageManager.getInstance().getText("btnCancel", request)%>" onclick="{
                    window.location = 'app.jsp'
                }"/>
                                <input type="button" class="save-btn" onclick="createEditGroupSubmit();" value="<%=LanguageManager.getInstance().getText("btnSave", request)%>"/>
                            </div>
                        </td>
                    </tr>
                </table>
            </div>
        </div>
    </body>
</html>
<script type="text/javascript" src="../js/jquery.form.js"></script> 