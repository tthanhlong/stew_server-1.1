/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jp.co.ncdc.stew.Servlet;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import jp.co.ncdc.stew.Adapters.MessageTemplateAdapter;
import jp.co.ncdc.stew.Entities.MessageTemplate;
import jp.co.ncdc.stew.Utils.StewConstant;

/**
 *
 * @author tquangthai
 */
public class MessageTemplateServlet extends HttpServlet {

    /**
     * Handles the ajax request from jsp pages
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
        //processRequest(request, response);
        request.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        String result = "";
        
        String typeString = request.getParameter("type");
        String id = request.getParameter("messageId");
        String title = request.getParameter("title");
        String message = request.getParameter("message");        
        String groups = request.getParameter("groups");
        String deleteIds = request.getParameter("deleteids");
        String delims = "[,]";
        
        String[] lstGroups = {};
        if (groups != null && !"".equals(groups)) {
            lstGroups = groups.split(delims);
        }
        
        String[] lstDeleteIds = {};
        if (deleteIds != null && !"".equals(deleteIds)) {
            lstDeleteIds = deleteIds.split(delims);
        }
        
        int type = 0;
        long messageId = 0;
        try {
            type = Integer.parseInt(typeString);
            messageId = Long.parseLong(id);
        } catch (Exception e) {
        }
        
        MessageTemplateAdapter messageAdapter = new MessageTemplateAdapter();        
        MessageTemplate messageOb = messageAdapter.getMessageByMessageID(messageId);
        
        switch(type){
            case StewConstant.CREATE:
                if (messageOb != null) {
                    result = "{\"status\": "+ StewConstant.EXISTED_MESSAGE +"}";                   
                }else{                    
                    MessageTemplate newMessage = new MessageTemplate();
                    newMessage.setTitle(title);
                    newMessage.setMessage(message);                    

                    if(messageAdapter.addMessage(newMessage)){                        
                        result = "{\"status\": "+ StewConstant.STATUS_CODE_OK +"}";
                    }else{
                        result = "{\"status\": "+ StewConstant.EXISTED_MESSAGE +"}";                        
                    }
                }
                break;
            case StewConstant.EDIT:
                //code for edit user
                if (messageOb != null) {                    
                    messageOb.setTitle(title);
                    messageOb.setMessage(message);                    
                    
                    if (messageAdapter.updateMessage(messageOb)) {                        
                        result = "{\"status\": "+ StewConstant.STATUS_CODE_OK +"}";
                    }
                }else{
                    result = "{\"status\": "+ StewConstant.STATUS_CODE_NOT_FOUND +"}";
                }
                break;
            case StewConstant.DELETE:
                int countDeleteId = lstDeleteIds.length;
                for (int i = 0; i < countDeleteId; i++) {
                    long deleteId = 0;
                    try {                        
                        deleteId = Long.parseLong(lstDeleteIds[i]);
                    } catch (Exception e) {
                    }
                    if (deleteId != 0) {
                        messageAdapter.deleteMessage(deleteId);
                    }
                }
                result = "{\"status\": " + StewConstant.STATUS_CODE_OK + "}";                
                break;     
            case StewConstant.DELETE_TEMPLATE_BY_ID:            
                try {
                    messageAdapter.deleteMessage(messageId);                
                    result = "{\"status\": " + StewConstant.STATUS_CODE_OK + "}";
                } catch (Exception e) {
                    result = "{\"status\": " + StewConstant.STATUS_CODE_NOT_FOUND + "}";
                }                
                break;
            default: break;
        }                   
        out.write(result);
    }

}
