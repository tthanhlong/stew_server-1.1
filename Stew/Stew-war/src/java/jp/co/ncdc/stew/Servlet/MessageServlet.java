/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jp.co.ncdc.stew.Servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import jp.co.ncdc.stew.APIs.model.NotificationMessage;
import jp.co.ncdc.stew.Adapters.DeviceRegisterAdapter;
import jp.co.ncdc.stew.Adapters.GroupAdapter;
import jp.co.ncdc.stew.Adapters.MessageAdapter;
import jp.co.ncdc.stew.Adapters.MessageSentAdapter;
import jp.co.ncdc.stew.Adapters.UserGroupDetailAdapter;
import jp.co.ncdc.stew.Entities.DeviceRegister;
import jp.co.ncdc.stew.Entities.GroupUser;
import jp.co.ncdc.stew.Entities.Message;
import jp.co.ncdc.stew.Entities.MessageSent;
import jp.co.ncdc.stew.Entities.UserGroupDetail;
import jp.co.ncdc.stew.PushNotify.NotifyMessageQueueManager;
import jp.co.ncdc.stew.Utils.StewConstant;
import org.hibernate.tool.hbm2x.StringUtils;

/**
 *
 * @author tquangthai
 */
public class MessageServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        request.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        String result = "";
        int size = 2;
        
        String typeString = request.getParameter("type");
        String appId = request.getParameter("appId");     
        String title = request.getParameter("title");
        String messageContent = request.getParameter("messageContent");   
        String groups = request.getParameter("groups");
        String roles = request.getParameter("roles");        
        String sendTypeString = request.getParameter("sendType");
        String sendDateString = request.getParameter("sendDate");
        String sendHourString = request.getParameter("sendHour");
        StringUtils.leftPad(sendHourString, size, "0");                     
        String sendMinuteSrting = request.getParameter("sendMinute");
        StringUtils.leftPad(sendMinuteSrting, size, "0");
        String sendDateTimeString = sendDateString + " " + sendHourString + ":" + sendMinuteSrting;
        String delims = "[,]";
        Map<String, List<String>> mapGroupRole = new HashMap<String, List<String>>();
        
        String[] lstGroups = {};
        if (groups != null && !"".equals(groups)) {
            lstGroups = groups.split(delims);
        }
        String[] lstRoles = {};
        if (roles != null && !"".equals(roles)) {
            lstRoles = roles.split(delims);
        }
        
        for (int i = 0; i < lstGroups.length; i++) {
            if (!mapGroupRole.containsKey(lstGroups[i]) ) {
                List list = new ArrayList( );
                list.add(lstRoles[i]);
                mapGroupRole.put(lstGroups[i], list);
            }
            else {
                List list = (List) mapGroupRole.get(lstGroups[i]);
                list.add(lstRoles[i]);
            }
        }
        
        int type = 0;
        int sendType = 0;
        try {
            type = Integer.parseInt(typeString);        
            sendType = Integer.parseInt(sendTypeString);
        } catch (Exception e) {
        }
        
        //Get Message tempalte to get content
        MessageAdapter messageAdapter = new MessageAdapter();        
        DeviceRegisterAdapter deviceRegisterAdapter = new DeviceRegisterAdapter();
        
        switch(type){
            case StewConstant.CREATE:                   
                    Message newMessageCreate = new Message();
                    newMessageCreate.setTitle(title);
                    newMessageCreate.setMessage(messageContent);                    

                    if(messageAdapter.addMessage(newMessageCreate) > 0){                        
                        result = "{\"status\": "+ StewConstant.STATUS_CODE_OK +"}";
                    }else{
                        result = "{\"status\": "+ StewConstant.EXISTED_MESSAGE +"}";                        
                    }
                
                break;
            case StewConstant.SEND_MESSAGE:
                System.err.println("SEND_MESSAGE");
                int countGroup = mapGroupRole.size();//lstGroups.length;
                long newMessageId = 0;
                if (countGroup > 0 && groups != null && !"".equals(groups)) {
                    Message newMessage = new Message();                                        
                    newMessage.setSentToApp(appId);
                    newMessage.setTitle(title);
                    String sendToGroup = "";
                    GroupUser groupUser;
                    GroupAdapter groupAdapt = new GroupAdapter();                    
                    for (String groupIdString:mapGroupRole.keySet()) {
                        long groupId = 0;
                        try {
                            groupId = Long.parseLong(groupIdString);
                        } catch (Exception e){
                        }
                        if (groupId > 0) {
                            groupUser = groupAdapt.getGroupByGroupID(groupId);
                            if ("".equals(sendToGroup)) {                                
                                sendToGroup = groupUser.getName();
                            } else {
                                sendToGroup += ", " + groupUser.getName();
                            }
                        }
                    }
                    newMessage.setSentToGroup(sendToGroup);
                    newMessage.setMessage(messageContent);                     
                    Date currentDate = new Date();
                    Date sendDateTime = new Date();
                    newMessage.setCreateMessage(currentDate);
                    if (sendType == StewConstant.SEND_AFTER) {
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm");                        
                        try
                        {    
                            sendDateTime = simpleDateFormat.parse(sendDateTimeString);
                            if (sendDateTime.before(currentDate)) {
                                sendDateTime = currentDate;
                            }                                
                        }
                        catch (Exception e){                                 
                        }
                    }
                    newMessage.setScheduleSend(sendDateTime);
                    MessageAdapter newMessageAdapter = new MessageAdapter();
                    // Create new mwssage
                    newMessageId = newMessageAdapter.addMessage(newMessage);
                                    
                    for (Map.Entry<String, List<String>> entry : mapGroupRole.entrySet()) {
                        String groupIDString = entry.getKey();
                        List<String> lstSendRoles = entry.getValue();
                        if (!"".equals(groupIDString)) {
                            Long groupID;
                            try {
                                UserGroupDetailAdapter userGroupDetailAdapter = new UserGroupDetailAdapter();
                                groupID = Long.parseLong(groupIDString);                                
                                //remove all roles if list roles contain "-1" element (will send to all roles in group)
                                if (lstSendRoles.contains("-1"))
                                    lstSendRoles.removeAll(lstSendRoles);
                                List<UserGroupDetail> lstUserGroups = userGroupDetailAdapter.getUserGroupsByListRole(groupID, appId, lstSendRoles);
                                if (lstUserGroups.size() > 0) {
                                    for (UserGroupDetail userGroupDetail : lstUserGroups) {
                                        if (userGroupDetail != null) {
                                            String userId = userGroupDetail.getUserId();
                                            // add new message sent to MessageSent table
                                            MessageSentAdapter messageSentAdapter = new MessageSentAdapter();
                                            MessageSent messageSent = new MessageSent();
                                            messageSent.setAppId(appId);
                                            messageSent.setMessageId(newMessageId);
                                            messageSent.setGroupId(groupID);
                                            messageSent.setUserId(userId);
                                            messageSent.setStatus(StewConstant.WAITING_SEND);
                                            // Create new message send
                                            messageSentAdapter.addMessageSent(messageSent);

                                            System.err.println("user ID " + userId + "|" + appId);

                                            //Create new Queue notification message
                                            List<DeviceRegister> allDevices = deviceRegisterAdapter.getDeviceRegister(userId, appId);
                                            int countDevices = allDevices.size();
                                            System.err.println("allDevices " + countDevices);
                                            if (allDevices != null && countDevices > 0) {
                                                for (int k = 0; k < countDevices; k++) {
                                                    String appID = allDevices.get(k).getAppId();
                                                    String deviceToken = allDevices.get(k).getDeviceToken();
                                                    if (deviceToken != null) {
                                                        NotificationMessage notificationMessage = new NotificationMessage();
                                                        notificationMessage.setAppId(appID);
                                                        notificationMessage.setDeviceToken(deviceToken);
                                                        notificationMessage.setMessage(messageContent);
                                                        notificationMessage.setStatus(StewConstant.WAITING_SEND);
                                                        notificationMessage.setUserID(userId);
                                                        notificationMessage.setId(newMessageId);
                                                        notificationMessage.setScheduleSend(sendDateTime);
                                                        NotifyMessageQueueManager.getInstance().addMessage(notificationMessage);
                                                    }
                                                }
                                            }

                                        }
                                    } 
                                    
                                    
                                }                         
                                result = "{\"status\": "+ StewConstant.STATUS_CODE_OK +"}";
                                
                            } catch (Exception e) {
                            }
                        }else{
                            result = "{\"status\": "+ StewConstant.STATUS_CODE_NOT_FOUND +"}";
                        }
                    }
                }else{
                    result = "{\"status\": "+ StewConstant.STATUS_CODE_NOT_FOUND +"}";
                }
                break;
            case StewConstant.VIEW:   
                
                break;
            default: break;
        }                   
        out.write(result);
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
