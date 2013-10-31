<%-- 
    Document   : send_message
    Created on : Sep 4, 2013, 9:24:21 AM
    Author     : tthanhlong
--%>

<%@page import="jp.co.ncdc.stew.APIs.model.DataModel"%>
<%@page import="java.util.Map"%>
<%@page import="jp.co.ncdc.stew.Adapters.GroupAdapter"%>
<%@page import="jp.co.ncdc.stew.Adapters.UserGroupDetailAdapter"%>
<%@page import="java.util.ArrayList"%>
<%@page import="jp.co.ncdc.stew.Entities.MessageSent"%>
<%@page import="jp.co.ncdc.stew.Controllers.MessageSentController"%>
<%@page import="jp.co.ncdc.stew.Entities.Apps"%>
<%@page import="jp.co.ncdc.stew.Adapters.AppsAdapter"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.Date"%>
<%@page import="jp.co.ncdc.stew.Entities.User"%>
<%@page import="jp.co.ncdc.stew.Utils.StewConstant"%>
<%@page import="jp.co.ncdc.stew.Entities.MessageTemplate"%>
<%@page import="jp.co.ncdc.stew.Controllers.MessageTemplateController"%>
<%@page import="jp.co.ncdc.stew.Entities.Message"%>
<%@page import="jp.co.ncdc.stew.Entities.GroupUser"%>
<%@page import="jp.co.ncdc.stew.Controllers.MessageController"%>
<%@page import="jp.co.ncdc.stew.Controllers.GroupController"%>
<%@page import="jp.co.ncdc.stew.Managers.LanguageManager"%>
<%@page import="java.util.List"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <script type="text/javascript" src="../js/jquery-1.9.1.min.js"></script>
        <script type="text/javascript" src="../js/common.js"></script>
        <link rel="stylesheet" type="text/css" href="../Datepicker/tcal.css" />
	<script type="text/javascript" src="../Datepicker/tcal.js"></script> 
        <title><%=LanguageManager.getInstance().getText("sendMessageLabel", request)%></title>
        <%
            
            String messageIdString = request.getParameter("messageId");
            String pageString = request.getParameter("page");            
            long messageId = 0;
            int pageNumber = 0;
            try{
                messageId = Long.parseLong(messageIdString);
                pageNumber = Integer.parseInt(pageString);
            }catch (Exception e) {                
            }                            
            
            String status = request.getParameter("status");
            if (status == null || "-1".equals(status))
                status = "";
            String keySearch = request.getParameter("key");
            if (keySearch == null)
                keySearch = "";
            String []lstStatus = {StewConstant.SUCCESS_SEND, StewConstant.WAITING_SEND, StewConstant.FAIL_SEND};
            
            User user = (User) session.getAttribute(StewConstant.USER_SESSION);
            String userId = "";
            boolean isSupperAdmin = false;
            if(user != null){
                userId = user.getEmail(); 
                isSupperAdmin = (Boolean) session.getAttribute(StewConstant.IS_SUPPER);
            }
                        
            MessageController messageController = new MessageController();
            MessageSentController messageSentController = new MessageSentController();
            Message message = messageController.getMessageByID(messageId);            
            UserGroupDetailAdapter userGroupAdapter = new UserGroupDetailAdapter();
            GroupAdapter groupAdapter = new GroupAdapter();
            List<Long> lstGroupIds;
            if (isSupperAdmin)
                lstGroupIds = groupAdapter.getAllgroupIds();
            else
                lstGroupIds = userGroupAdapter.getGroupByManagerId(userId);                                 
                        
            DataModel messageSents = messageSentController.getLogMessagesFilterByPageNum(pageNumber, messageId, lstGroupIds, message.getSentToApp(), isSupperAdmin, status, keySearch);
        %>
        <script type="text/javascript">
            $(document).ready(function(){
                $('#sendMessage-menu').removeClass("has-hover");
                $('#sendMessage-menu').addClass("selected-menu");                                                                                                               
                
                $('#search-message').click(function(){                       
                    var status = $('#status-selectbox').val();
                    var messageId = <%=messageIdString%>;
                    var keySearch = $('#key-search').val();                    
                    window.location.href = "view_send_message.jsp?messageId=" + messageId + "&status=" + status + "&key=" + keySearch;
                });
            });                 
        </script>
    </head>
    <body>               
        <div class="page_content">
            <jsp:include page="../common/header.jsp" />
            <div class="content">
                <div class="content_header">
                    <%=LanguageManager.getInstance().getText("sentMessageDetailLabel", request)%>
                </div>
                <table class="table_content create_user_content">
                    <tr class="group_name_box">
                        <td class="td_lable"><%=LanguageManager.getInstance().getText("sendToAppLabel", request)%></td>
                        <td><%=message.getSentToApp()%></td>                            
                    </tr>
                    <tr class="group_name_box">
                        <td class="td_lable"><%=LanguageManager.getInstance().getText("sendToGroupLabel", request)%></td>
                        <td><%=message.getSentToGroup()%></td>
                    </tr>
                    <tr class="group_name_box">
                        <td class="td_lable"><%=LanguageManager.getInstance().getText("messageTitleLabel", request)%></td>                        
                        <td><%=message.getTitle()%></td>
                    </tr>
                    <tr class="group_name_box">
                        <td class="td_lable"><%=LanguageManager.getInstance().getText("messageContentLabel", request)%></td>
                        <td>
                            <textarea disabled="disabled" class="txt-area-description" id="message-content" rows="5" cols="25" placeholder="<%=message.getMessage()%>"></textarea>
                        </td>
                    </tr>                                       
                </table>
                <table class="filter_header">
                    <tr>    
                        <td style="width: 50%"></td>                                                
                        <td>
                            <select id="status-selectbox">
                                <option value="-1" selected="selected"><%=LanguageManager.getInstance().getText("filterByStatus", request)%></option>
                                <%
                                    if(lstStatus != null && lstStatus.length > 0){
                                        int count = lstStatus.length;
                                        for(int i = 0; i < count; i++){
                                            if (!"".equals(status) && lstStatus[i].equals(status)) {
                                        %>                                            
                                                <option selected="selected" value="<%=lstStatus[i]%>"><%=lstStatus[i]%></option>
                                        <%
                                            } else {
                                        %>
                                                <option value="<%=lstStatus[i]%>"><%=lstStatus[i]%></option>
                                        <%   
                                            }
                                        }
                                    }
                                %>
                            </select>
                        </td>                                              
                        <td colspan="2" class="search_td">
                            <div class="search_div">
                                <input value="<%=(keySearch == null || keySearch == "")?"":keySearch%>" id="key-search" placeholder="<%=LanguageManager.getInstance().getText("searchHolder", request)%>" type="text">
                                <span id="search-message" class="img-search"></span>
                            </div>
                        </td>
                    </tr>                    
                </table>
                <div class="user_list">
                    <table class="table_content">
                        <thead>
                            <tr class="tr-header">                                
                                <th style="width: 25%"><%=LanguageManager.getInstance().getText("thSendToUser", request)%></th>
                                <th style="width: 15%"><%=LanguageManager.getInstance().getText("thSendDate", request)%></th>
                                <th style="width: 15%"><%=LanguageManager.getInstance().getText("thStatus", request)%></th>                                
                            </tr>
                        </thead>
                        <tbody class="table_list">
                            <%
                                if (messageSents.getListItems() != null) {
                                    int count = messageSents.getListItems().size();
                                    int totalPage = messageSents.getTotal();
                                    List<MessageSent> lstAllMessages = messageSents.getListItems();
                                    if (lstAllMessages != null && count > 0) {
                                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd");
                                        for (int i = 0; i < count; i++) {
                            %>
                            <tr>                                
                                <td><%=lstAllMessages.get(i).getUserId()%></td>
                                <td><%=dateFormat.format(message.getScheduleSend())%></td>
                                <td><%=lstAllMessages.get(i).getStatus()%></td>                                                                
                            </tr>
                            <%
                                    }
                                }
                            %>
                        </tbody>
                        <tfoot>
                            <tr class="tr-header">                                
                                <td colspan="3" align="right">
                                    <div id="paging">
                                        <%
                                            if (totalPage > 1 && totalPage > pageNumber) {
                                                if (pageNumber > 0) {
                                        %>
                                        <a class="page-first" href="view_send_message.jsp?messageId=<%=messageIdString%>&page=0&staus=<%=status%>&key=<%=keySearch%>"></a>
                                        <a class="page-prev" href="view_send_message.jsp?messageId=<%=messageIdString%>&page=<%=pageNumber - 1%>&staus=<%=status%>&key=<%=keySearch%>"></a>
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
                                        <a class="page-next" href="view_send_message.jsp?messageId=<%=messageIdString%>&page=<%=pageNumber + 1%>&staus=<%=status%>&key=<%=keySearch%>"></a>
                                        <a class="page-last" href="view_send_message.jsp?messageId=<%=messageIdString%>&page=<%=totalPage - 1%>&staus=<%=status%>&key=<%=keySearch%>"></a>
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
