/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jp.co.ncdc.stew.Servlet;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import jp.co.ncdc.stew.APIs.model.LogModel;
import jp.co.ncdc.stew.Adapters.AppsAdapter;
import jp.co.ncdc.stew.Adapters.LogAdapter;
import jp.co.ncdc.stew.Entities.Log;
import jp.co.ncdc.stew.Managers.LanguageManager;
import jp.co.ncdc.stew.Utils.StewConstant;

/**
 *
 * @author tquangthai
 */
public class ExportDataServlet extends HttpServlet {

    /**
     * Processes requests for both HTTP
     * <code>GET</code> and
     * <code>POST</code> methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        try {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet ExportDataServlet</title>");            
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet ExportDataServlet at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        } finally {            
            out.close();
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP
     * <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP
     * <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {                        
        request.setCharacterEncoding("UTF-8");
        //response.setContentType("application/json");
        response.setContentType("application/octet-stream");
        response.setHeader("content-disposition","filename=abc.csv");        
        
        String result = "";        
        String downloadIds = request.getParameter("downloadIds");        
        String delims = "[,]";                        
        String[] lstDownloadIds = {};
        try {
            PrintWriter out =  new PrintWriter(new OutputStreamWriter(response.getOutputStream(), "UTF-8"), true);
            if (downloadIds != null && !"".equals(downloadIds)) {
                lstDownloadIds = downloadIds.split(delims);
            }
            LogAdapter logAdapter = new LogAdapter();     
            AppsAdapter appAdapter = new AppsAdapter();
            Log log;
            out.append(LanguageManager.getInstance().getText("appName", request));
            out.append("," + LanguageManager.getInstance().getText("account", request));
            out.append("," + LanguageManager.getInstance().getText("logLevel", request));
            out.append("," + LanguageManager.getInstance().getText("logMessage", request));
            out.append("," + LanguageManager.getInstance().getText("logDate", request) + "\n");            
            for (int i = 0; i < lstDownloadIds.length; i++) {
                long logId = 0;
                String appName = "";
                logId = Long.parseLong(lstDownloadIds[i]);                                
                log = logAdapter.getLogByLogId(logId);
                appName = appAdapter.getAppByAppID(log.getAppId()).getAppName();
                out.append(appName);
                out.append("," + log.getUserId());
                out.append("," + log.getType());
                out.append("," + log.getMessageLog());
                out.append("," + log.getTimeLog());
                out.append("\n");
            }
            out.flush();
            out.close();
        } catch (Exception e) {
        }


        //PrintWriter out = response.getWriter();                                       
//        
//        switch(type){            
//            case StewConstant.DOWNLOAD_LOG_CSV:            
//                try {                    
//                    result = "{\"status\": " + StewConstant.STATUS_CODE_OK + "}";
//                } catch (Exception e) {
//                    result = "{\"status\": " + StewConstant.STATUS_CODE_NOT_FOUND + "}";
//                }                
//                break;
//            default: break;
//        }
//        out.write(result);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
}
