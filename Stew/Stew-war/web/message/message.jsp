<%-- 
    Document   : message
    Created on : Oct 2, 2013, 11:47:14 AM
    Author     : tquangthai
--%>

<%@page import="jp.co.ncdc.stew.Entities.Apps"%>
<%@page import="jp.co.ncdc.stew.Adapters.AppsAdapter"%>
<%@page import="jp.co.ncdc.stew.Controllers.MessageTemplateController"%>
<%@page import="jp.co.ncdc.stew.Entities.MessageTemplate"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="jp.co.ncdc.stew.Entities.User"%>
<%@page import="jp.co.ncdc.stew.Utils.StewConstant"%>
<%@page import="jp.co.ncdc.stew.Entities.Message"%>
<%@page import="java.util.List"%>
<%@page import="jp.co.ncdc.stew.APIs.model.DataModel"%>
<%@page import="jp.co.ncdc.stew.Controllers.MessageController"%>
<%@page import="jp.co.ncdc.stew.Managers.LanguageManager"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <script type="text/javascript" src="../js/jquery-1.9.1.min.js"></script>
        <link rel="stylesheet" href="../css/stew.css"/>
        <script type="text/javascript">
            $(document).ready(function(){
                $('#sendMessage-menu').removeClass("has-hover");
                $('#sendMessage-menu').addClass("selected-menu");
                
                $('#search-message').click(function(){                       
                    var appId = $('#app-selectbox').val();
                    var templateName = "";
                    if ($('#template-selectbox').val() != "-1")
                        templateName = $('#template-selectbox :selected').text();
                    var keySearch = $('#key-search').val();
                    window.location.href = "message.jsp?appId=" + appId + "&template=" + templateName + "&key=" + keySearch;                
                });            
            });                        
        </script>
        <title><%=LanguageManager.getInstance().getText("messageManagement", request)%></title>
    </head>
    <body>        
        <%
            User user = (User) session.getAttribute(StewConstant.USER_SESSION);
            String userId = "";
            boolean isSupperAdmin = false;
            if(user != null){
                userId = user.getEmail(); 
                isSupperAdmin = (Boolean) session.getAttribute(StewConstant.IS_SUPPER);
            }
            
            String pageString = request.getParameter("page");            
            String appId = request.getParameter("appId");
            if (appId == null || "-1".equals(appId))
                appId = "";            
            String templateName = request.getParameter("template");
            if (templateName == null || "-1".equals(templateName))
                templateName = "";
            String keySearch = request.getParameter("key");
            if (keySearch == null)
                keySearch = "";
            
            int pageNumber = 0;
            try{
                pageNumber = Integer.parseInt(pageString);
            }catch (Exception e){
                
            }
            
            MessageController messageController = new MessageController();
            DataModel messages = messageController.getMessagesFilterByPageNum(pageNumber, userId, isSupperAdmin, appId, templateName, keySearch);
            MessageTemplateController messageTemplateController = new MessageTemplateController();
            List<MessageTemplate> lstTemplates = messageTemplateController.getAllMessages();
            AppsAdapter appsAdapter = new AppsAdapter();
            List<Apps> lstApps = appsAdapter.getAppsByManagerId(userId, isSupperAdmin);//.getAllApps();
        %>
        <div class="page_content">
            <jsp:include page="../common/header.jsp" />
            <div class="content">
                <div class="content_header">
                    <%=LanguageManager.getInstance().getText("manageMessage", request)%>
                </div>
                <table class="filter_header">
                    <tr>    
                        <td style="width: 25%"></td>                        
                        <td>
                            <select id="app-selectbox">
                                <option value="-1" selected="selected"><%=LanguageManager.getInstance().getText("filterByApp", request)%></option>
                                <%
                                    if(lstApps != null && lstApps.size() > 0){
                                        int count = lstApps.size();
                                        for(int i = 0; i < count; i++){
                                            if (!"".equals(appId) && lstApps.get(i).getAppId().equals(appId)) {
                                        %>
                                            <option selected="selected" value="<%=lstApps.get(i).getAppId()%>"><%=lstApps.get(i).getAppName()%></option>
                                        <%
                                            }else {
                                        %>
                                            <option value="<%=lstApps.get(i).getAppId()%>"><%=lstApps.get(i).getAppName()%></option>
                                        <%
                                            }
                                        }
                                    }
                                %>
                            </select>
                        </td>
                        <td>
                            <select id="template-selectbox">
                                <option value="-1" selected="selected"><%=LanguageManager.getInstance().getText("filterByTemplateName", request)%></option>
                                <%
                                    if(lstTemplates != null && lstTemplates.size() > 0){
                                        int count = lstTemplates.size();
                                        for(int i = 0; i < count; i++){
                                            if (!"".equals(templateName) && lstTemplates.get(i).getTitle().equals(templateName)) {
                                        %>                                            
                                                <option selected="selected" lang="<%=lstTemplates.get(i).getMessage()%>" value="<%=lstTemplates.get(i).getId()%>"><%=lstTemplates.get(i).getTitle()%></option>
                                        <%
                                            } else {
                                        %>
                                                <option lang="<%=lstTemplates.get(i).getMessage()%>" value="<%=lstTemplates.get(i).getId()%>"><%=lstTemplates.get(i).getTitle()%></option>
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
                            <th style="width: 5%"><%=LanguageManager.getInstance().getText("thId", request)%></th>                            
                            <th style="width: 38%"><%=LanguageManager.getInstance().getText("thMessageContent", request)%></th>                            
                            <th style="width: 15%"><%=LanguageManager.getInstance().getText("thApp", request)%></th>
                            <th style="width: 20%"><%=LanguageManager.getInstance().getText("thSentTo", request)%></th>
                            <th style="width: 14%"><%=LanguageManager.getInstance().getText("thCreateDate", request)%></th>
                            <th style="width: 8%"><%=LanguageManager.getInstance().getText("thOption", request)%></th>                            
                        </tr>
                    </thead>
                    <tbody class="table_list">
                        <%
                            if(messages.getListItems() != null){
                                int count = messages.getListItems().size();
                                int totalPage = messages.getTotal();
                                List<Message> lstAllMessages = messages.getListItems();                                
                                if(count > 0 && lstAllMessages != null){
                                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd");
                                    for(int i = 0; i < count ; i++){
                                    %>
                                    <tr>                                        
                                        <td><%=lstAllMessages.get(i).getMessageId()%></td>
                                        <td class="long-text"><%=lstAllMessages.get(i).getMessage()%></td>                                        
                                        <td><%=lstAllMessages.get(i).getSentToApp()%></td>
                                        <td><%=lstAllMessages.get(i).getSentToGroup()%></td>
                                        <td><%=dateFormat.format(lstAllMessages.get(i).getCreateMessage())%></td>
                                        <td>                                                                                        
                                            <a title="<%=LanguageManager.getInstance().getText("viewAction", request)%>" class="action" href="view_send_message.jsp?messageId=<%=lstAllMessages.get(i).getMessageId()%>"><img  width="30px" src="../image/icon/icon_review.png"/></a>
                                        </td>
                                    </tr>
                                    <%
                                    }
                                }
                        %>
                    </tbody>
                    <tfoot>
                            <tr class="tr-header">
                                <td colspan="3" align="left"><span class="create-icon"><a href="send_message.jsp"><%=LanguageManager.getInstance().getText("create", request)%></a></span></td>
                                <td colspan="3" align="right">
                                    <div id="paging">
                                        <%
                                            if (totalPage > 1 && totalPage > pageNumber) {
                                                if (pageNumber > 0) {
                                        %>
                                        <a class="page-first" href="message.jsp?page=0&appId=<%=appId%>&template=<%=templateName%>&key=<%=keySearch%>"></a>
                                        <a class="page-prev" href="message.jsp?page=<%=pageNumber - 1%>&appId=<%=appId%>&template=<%=templateName%>&key=<%=keySearch%>"></a>
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
                                        <a class="page-next" href="message.jsp?page=<%=pageNumber + 1%>&appId=<%=appId%>&template=<%=templateName%>&key=<%=keySearch%>"></a>
                                        <a class="page-last" href="message.jsp?page=<%=totalPage - 1%>&appId=<%=appId%>&template=<%=templateName%>&key=<%=keySearch%>"></a>
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
                                                            <td colspan="3" align="left"><span class="create-icon"><a href="send_message.jsp"><%=LanguageManager.getInstance().getText("create", request)%></a></span></td>
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
