<%-- 
    Document   : message
    Created on : Aug 28, 2013, 9:21:54 AM
    Author     : tthanhlong
--%>

<%@page import="jp.co.ncdc.stew.Entities.MessageTemplate"%>
<%@page import="jp.co.ncdc.stew.APIs.model.DataModel"%>
<%@page import="jp.co.ncdc.stew.Controllers.MessageTemplateController"%>
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
            $(document).ready(function(){
                $('#message-menu').removeClass("has-hover");
                $('#message-menu').addClass("selected-menu");
                $('.delete-icon').hide();
                
                $('.icon-check').click(function(){
                    if ($(this).is(':checked')) {
                        $(this).parent().parent().addClass("select-tr");
                    }else{
                        $(this).parent().parent().removeClass("select-tr");
                    }
                    if($('.select-tr').length !== 0){
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
                
                $('.delete-icon').click(function(){
                    var delete_ids = new Array();                   
                    var index = 0;
                    
                    $(".select-tr").each(function(){
                        delete_ids[index] = $(this).find(".icon-check").val();                        
                        index++;
                    });
                    
                    if(confirm("<%=LanguageManager.getInstance().getText("confirmDeleteTemplate", request)%>")) {
                        $.ajax({
                            type: "POST",
                            dataType: "json",
                            url: "MessageTemplateAction",
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
                                   window.location.href = "message_template.jsp";
                                }
                            },
                            error: function() {
                                alert("error get record.");
                            }
                        });
                    }
                })
            });
        </script>
        <title><%=LanguageManager.getInstance().getText("messageManagement", request)%></title>
    </head>
    <body>        
        <%
            String pageString = request.getParameter("page");
            int pageNumber = 0;
            try{
                pageNumber = Integer.parseInt(pageString);
            }catch (Exception e){
                
            }
            MessageTemplateController messageController = new MessageTemplateController();
            DataModel messages = messageController.getMessagesByPageNumber(pageNumber);
        %>
        <div class="page_content">
            <jsp:include page="../common/header.jsp" />
            <div class="content">
                <div class="content_header">
                    <%=LanguageManager.getInstance().getText("messageTemplateList", request)%>
                </div>
                <table class="filter_header">
                    <tr>
                        <td class="delete-icon-td">
                            <span class="delete-icon"><%=LanguageManager.getInstance().getText("delete", request)%></span>
                        </td>                        
                    </tr>
                </table>
                <div class="user_list">
                    <table class="table_content">
                        <thead>
                            <tr class="tr-header">
                                <th style="width: 5%"><input type="checkbox" class="icon-check-all"/></th>
                                <th style="width: 5%"><%=LanguageManager.getInstance().getText("thNumber", request)%></th>
                                <th style="width: 25%"><%=LanguageManager.getInstance().getText("thTitle", request)%></th>
                                <th style="width: 50%"><%=LanguageManager.getInstance().getText("messageMenu", request)%></th>                            
                                <th style="width: 20%"><%=LanguageManager.getInstance().getText("thOption", request)%></th>
                            </tr>
                        </thead>
                        <tbody class="table_list">
                            <%
                                if(messages.getListItems() != null){
                                    int count = messages.getListItems().size();
                                    int totalPage = messages.getTotal();
                                    List<MessageTemplate> lstAllMessages = messages.getListItems();
                                    if(count > 0 && lstAllMessages != null){
                                        for(int i = 0; i < count ; i++){
                                        %>
                                        <tr>
                                            <td class="check-icon-td"><input value="<%=lstAllMessages.get(i).getId()%>" class="icon-check" type="checkbox"/></td>
                                            <td><%=lstAllMessages.get(i).getId()%></td>
                                            <td><%=lstAllMessages.get(i).getTitle()%></td>                                        
                                            <td class="long-text"><%=lstAllMessages.get(i).getMessage()%></td>
                                            <td class="edit-btn">
                                                <a title="<%=LanguageManager.getInstance().getText("editAction", request)%>" class="action" href="edit_message_template.jsp?messageId=<%=lstAllMessages.get(i).getId()%>"><span class='edit-icon'><%=LanguageManager.getInstance().getText("editAction", request)%></span></a>                                                
                                            </td>
                                                                                                                                                
                                        </tr>
                                        <%
                                        }
                                    }
                            %>
                        </tbody>
                        <tfoot>
                            <tr class="tr-header">
                                <td colspan="3" align="left"> <span class="create-icon"><a href="create_message_template.jsp"><%=LanguageManager.getInstance().getText("add", request)%></a></span></td>
                                <td colspan="2" align="right">
                                    <div id="paging">
                                        <%
                                            if (totalPage > 1 && totalPage > pageNumber) {
                                                if (pageNumber > 0) {
                                        %>
                                        <a class="page-first" href="message_template.jsp?page=0"></a>
                                        <a class="page-prev" href="message_template.jsp?page=<%=pageNumber - 1%>"></a>
                                            <%
                                            } else {
                                            %>
                                        <a class="page-first" href="#"></a>
                                        <a class="page-prev" href="#"></a>
                                            <%                                    }
                                            %>
                                        <a class="current">page <%=pageNumber + 1%> of <%=totalPage%></a>
                                            <%
                                                if (pageNumber < totalPage - 1) {
                                            %>
                                        <a class="page-next" href="message_template.jsp?page=<%=pageNumber + 1%>"></a>
                                        <a class="page-last" href="message_template.jsp?page=<%=totalPage - 1%>"></a>
                                            <%
                                            } else {
                                            %>
                                        <a class="page-next" href="#"></a>
                                        <a class="page-last" href="#"></a>
                                            <%                                            }
                                                    }
                                                }else{
                                                    %>
                                                    </tbody>
                                                    <tfoot>
                                                        <tr class="tr-header">
                                                            <td colspan="3" align="left"><span class="create-icon"><a href="create_message_template.jsp"><%=LanguageManager.getInstance().getText("add", request)%></a></span></td>
                                                            <td colspan="2" align="right"></td>
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
