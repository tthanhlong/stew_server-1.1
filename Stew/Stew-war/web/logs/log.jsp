<%-- 
    Document   : user
    Created on : Aug 28, 2013, 9:21:28 AM
    Author     : tthanhlong
--%>

<%@page import="java.text.DateFormat"%>
<%@page import="java.util.Date"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="jp.co.ncdc.stew.Adapters.AppsAdapter"%>
<%@page import="jp.co.ncdc.stew.Entities.Apps"%>
<%@page import="jp.co.ncdc.stew.APIs.model.LogModel"%>
<%@page import="jp.co.ncdc.stew.Controllers.LoggingController"%>
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
        <link rel="stylesheet" type="text/css" href="../Datepicker/tcal.css" />
	<script type="text/javascript" src="../Datepicker/tcal.js"></script> 
        <link rel="stylesheet" href="../css/stew.css"/>
        <%
            User user = (User) session.getAttribute(StewConstant.USER_SESSION);
            String userId = "";
            boolean isSupperAdmin = false;
            if(user != null){
                userId = user.getEmail(); 
                isSupperAdmin = (Boolean) session.getAttribute(StewConstant.IS_SUPPER);
            }
            
            String pageString = request.getParameter("page");
            int pageNumber = 0;
            try {
                pageNumber = Integer.parseInt(pageString);
            } catch (Exception e) {
            }
            String appId = request.getParameter("appId");
            if (appId == null || "-1".equals(appId))
                appId = "";            
           String logLevel = request.getParameter("level");
            if (logLevel == null || "-1".equals(logLevel))
                logLevel = "";
            String keySearch = request.getParameter("key");
            if (keySearch == null)
                keySearch = "";
            String fromDateString = request.getParameter("fromDate");
            if (fromDateString == null)
                fromDateString = "";
            String toDateString = request.getParameter("toDate");
            if (toDateString == null)
                toDateString = "";
            String []lstLevel = {StewConstant.ERR_LOG_TYPE_STRING, StewConstant.EVENT_LOG_TYPE_STRING};
            
            LoggingController loggingController = new LoggingController();
            DataModel logs = loggingController.getLogFilter(pageNumber, userId, isSupperAdmin, appId, logLevel, fromDateString, toDateString, keySearch);
            
            AppsAdapter appsAdapter = new AppsAdapter();
            List<Apps> lstApps = appsAdapter.getAllApps();
        %>
        <script type="text/javascript">
            $(document).ready(function() {
                $('#message-menu').removeClass("has-hover");            
                $('#log-menu').addClass("selected-menu");
                $('#download-log').attr("disabled", true);
                $('.icon-check').click(function(){
                    if ($(this).is(':checked')) {
                        $(this).parent().parent().addClass("select-tr");
                    }else{
                        $(this).parent().parent().removeClass("select-tr");
                    }
                    if($('.select-tr').length !== 0){
                        $('#download-log').attr("disabled", false);
                    }else{
                        $('#download-log').attr("disabled", true);
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
                    if($('.select-tr').length !== 0){
                        $('#download-log').attr("disabled", false);
                    }else{
                        $('#download-log').attr("disabled", true);
                    }
                });
                
                $('#search-log').click(function(){                       
                    var appId = $('#app-selectbox').val();
                    var logLevel = $('#level-selectbox :selected').val();
                    var fromDate = $('#from-date-picker').val();
                    var toDate = $('#to-date-picker').val();
                    var keySearch = $('#key-search').val();                                                                
                    if (f_tcalParseDate(fromDate,'m/d/Y') > f_tcalParseDate(toDate,'m/d/Y'))
                        alert("<%=LanguageManager.getInstance().getText("invalidDate", request)%>");
                    else
                        window.location.href = "log.jsp?appId=" + appId + "&level=" + logLevel + "&fromDate=" + fromDate + "&toDate=" + toDate + "&key=" + keySearch;                
                });
                
                $('#download-log1').click(function(){                    
                    var download_ids = new Array();                   
                    var index = 0;
                    
                    $(".select-tr").each(function(){
                        download_ids[index] = $(this).find(".icon-check").val();                          
                        index++;
                    });                    
                    if(confirm("<%=LanguageManager.getInstance().getText("confirmDownLoadCSV", request)%>")) {
                        $.ajax({
                            type: "POST",
                            dataType: "application/octet-stream",
                            async: false,
                            url: "ExportDataAction",
                            beforeSend: function(){
                                //before send
                            },
                            complete: function(){
                                //complete
                            },
                            data: {                                
                                "downloadIds": download_ids.join(),
                                "type": DOWNLOAD_LOG_CSV
                            },
                            success: function(data) {
                                if (data.status === STATUS_CODE_OK) {
                                   window.location.href = "log.jsp";
                                }
                            },
                            error: function(data) {
                                console.log(data);
                                //alert("error get record.");
                            }
                        });
                    }
                })
                
                
                $('#submit_form').click(function () {
                
                       var f = $(this).parents('form').attr('id');
                       $('#' + f).submit();
                });
            });
            
            /*
            * SUBMIT FORM
            */
            function doSubmit(){                                                
                var download_ids = new Array();                   
                var form = $("#exportdata");
                var index = 0;

                $(".select-tr").each(function(){
                    download_ids[index] = $(this).find(".icon-check").val();                          
                    index++;
                });                               
                if(confirm("<%=LanguageManager.getInstance().getText("confirmDownLoadCSV", request)%>")) {
                    var listID = $("<input>").attr("type", "hidden").attr("name", "downloadIds").attr("value", download_ids.join());
                    form.append(listID);
                    
                    return true;
                } else {                        
                    return false;                                
                }
            }   
        </script>
        <title><%=LanguageManager.getInstance().getText("logMenu", request)%></title>
    </head>
    <body>
        <div class="page_content">
            <jsp:include page="../common/header.jsp" />
            <div class="content">
                <div class="content_header">
                    <%=LanguageManager.getInstance().getText("logList", request)%>
                </div>
                <table class="filter_header">
                    <tr>
                        <td style="width: 20%">&nbsp;</td>
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
                            <select id="level-selectbox">
                                <option value="-1" selected="selected"><%=LanguageManager.getInstance().getText("filterByLogLevel", request)%></option>
                                <%
                                    if(lstLevel != null && lstLevel.length > 0){
                                        int count = lstLevel.length;
                                        for(int i = 0; i < count; i++){
                                            if (!"".equals(logLevel) && lstLevel[i].equals(logLevel)) {
                                        %>                                            
                                                <option selected="selected" value="<%=lstLevel[i]%>"><%=lstLevel[i]%></option>
                                        <%
                                            } else {
                                        %>
                                                <option value="<%=lstLevel[i]%>"><%=lstLevel[i]%></option>
                                        <%   
                                            }
                                        }
                                    }
                                %>
                            </select>
                        </td>
                        <td style="width: 10%">
                            <input type="text" id="from-date-picker" name="fromDate" value="<%=fromDateString%>"class="tcal"/>
                        </td>
                        <td>
                            <div align="center"><%=LanguageManager.getInstance().getText("toSymbol", request)%></div>
                        </td>
                        <td style="width: 10%">                            
                            <input type="text" id="to-date-picker" name="toDate" value="<%=toDateString%>"class="tcal"/>
                        </td>
                    </tr>
                    <tr>
                        <td colspan="3">&nbsp;</td>
                        <td colspan="3" class="search_td">
                            <div class="search_div">
                                <input value="<%=(keySearch == null || keySearch == "")?"":keySearch%>" id="key-search" placeholder="<%=LanguageManager.getInstance().getText("searchHolder", request)%>" type="text">
                                <span id="search-log" class="img-search"></span>
                            </div>
                        </td>
                    </tr>
                </table>
                <div class="user_list" align="center">
                    <table class="table_content">
                        <thead>
                            <tr class="tr-header">
                                <th style="width: 5%"><input type="checkbox" class="icon-check-all"/></th>
                                <th style="width: 25%"><%=LanguageManager.getInstance().getText("appName", request)%></th>
                                <th style="width: 15%"><%=LanguageManager.getInstance().getText("account", request)%></th>
                                <th style="width: 15%"><%=LanguageManager.getInstance().getText("logLevel", request)%></th>
                                <th style="width: 15%"><%=LanguageManager.getInstance().getText("logMessage", request)%></th>
                                <th style="width: 15%"><%=LanguageManager.getInstance().getText("logDate", request)%></th>
                            </tr>
                        </thead>
                        <tbody class="table_list">
                            <%
                                if (logs.getListItems() != null) {
                                    DateFormat df = new SimpleDateFormat("yyyy.MM.dd");
                                    int count = logs.getListItems().size();
                                    int totalPage = logs.getTotal();                                    
                                    if (logs.getListItems() != null && count > 0) {
                                        for (int i = 0; i < count; i++) {
                                            Object[] lstAllLogs = (Object[]) logs.getListItems().get(i);
                            %>
                            <tr>
                                <td class="check-icon-td"><input value="<%=lstAllLogs[0]%>" class="icon-check" type="checkbox"/></td>
                                <td id='appName'><%=lstAllLogs[1]%></td>
                                <td class="long-text"><%=lstAllLogs[2]%></td>
                                <td><%=lstAllLogs[3]%></td>
                                <td class="long-text"><%=lstAllLogs[4]%></td>
                                <td><%=df.format(lstAllLogs[5])%></td>
                            </tr>
                            <%
                                    }
                                }
                            %>
                        </tbody>
                        <tfoot>
                            <tr class="tr-header">
                                <td colspan="4" align="left"> &nbsp;</td>
                                <td colspan="2" align="right">
                                    <div id="paging">
                                        <%
                                            if (totalPage > 1 && totalPage > pageNumber) {
                                                if (pageNumber > 0) {
                                        %>
                                        <a class="page-first" href="log.jsp?page=0&appId=<%=appId%>&level=<%=logLevel%>&fromDate=<%=fromDateString%>&toDate=<%=toDateString%>&key=<%=keySearch%>"></a>
                                        <a class="page-prev" href="log.jsp?page=<%=pageNumber - 1%>&appId=<%=appId%>&level=<%=logLevel%>&fromDate=<%=fromDateString%>&toDate=<%=toDateString%>&key=<%=keySearch%>"></a>
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
                                        <a class="page-next" href="log.jsp?page=<%=pageNumber + 1%>&appId=<%=appId%>&level=<%=logLevel%>&fromDate=<%=fromDateString%>&toDate=<%=toDateString%>&key=<%=keySearch%>"></a>
                                        <a class="page-last" href="log.jsp?page=<%=totalPage - 1%>&appId=<%=appId%>&level=<%=logLevel%>&fromDate=<%=fromDateString%>&toDate=<%=toDateString%>&key=<%=keySearch%>"></a>
                                            <%
                                            } else {
                                            %>
                                        <a class="page-next" href="#"></a>
                                        <a class="page-last" href="#"></a>
                                            <%                                            }
                                                    }
                                                }
                                            %>
                                    </div>
                                </td>
                            </tr>
                        </tfoot>
                    </table>
                    <form id="exportdata" onsubmit="return doSubmit();" action="ExportDataAction" style="padding-top: 20px" method="POST">                        
                        <input type="submit" id="download-log" class="download-btn" value=""/>                        
                    </form>
                </div>   
            </div>
        </div>
    </body>
</html>
