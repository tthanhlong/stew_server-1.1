/*
 * Copyright (c) Stew. All Rights Reserved.
 */
package jp.co.ncdc.stew.PushNotify;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import jp.co.ncdc.stew.APIs.model.NotificationMessage;
import jp.co.ncdc.stew.Adapters.DeviceRegisterAdapter;
import jp.co.ncdc.stew.Adapters.MessageAdapter;
import jp.co.ncdc.stew.Adapters.MessageTemplateAdapter;
import jp.co.ncdc.stew.Adapters.MessageSentAdapter;
import jp.co.ncdc.stew.Entities.DeviceRegister;
import jp.co.ncdc.stew.Entities.Message;
import jp.co.ncdc.stew.Entities.MessageTemplate;
import jp.co.ncdc.stew.Entities.MessageSent;
import jp.co.ncdc.stew.Utils.StewConstant;

/**
 * @Class Name: NotifyMessageQueueManager.java
 * @author: Anh Le
 * @created: August 27, 2013 
 * @brief: This class is implementation for Queue storage message
 */

public class NotifyMessageQueueManager {
    
    // Instance
    private static NotifyMessageQueueManager instance;

    public static NotifyMessageQueueManager getInstance() {
        if (instance == null) {
            instance = new NotifyMessageQueueManager();
        }
        return instance;
    }
    
    private LinkedList<NotificationMessage> listMessageNotify;
    
    private NotifyMessageQueueManager()
    {
        listMessageNotify=new LinkedList<NotificationMessage>();
        //loadMessageFromDatabase();
    }
  
      /**
     * Get the MessageTemplate to push notify
     * @return MessageTemplate
     */
    public synchronized NotificationMessage getMessage()
    {
        // Get queue message by schedule
        long currentTime=new Date().getTime();
        for(NotificationMessage notificationMessage:listMessageNotify)
        {
            if(notificationMessage!=null)
                if(currentTime>=notificationMessage.getScheduleSend().getTime())
                {
                    return notificationMessage;
                }
        }   
        return null;
    } 
    
 
    /**
     * Add MessageTemplate to list message storage
     * @param queueMessage 
     */
    public synchronized void addMessage(NotificationMessage queueMessage) {
        if (!checkMessageExist(queueMessage)) {
            listMessageNotify.addFirst(queueMessage);
            if (!PushNotifyWorker.getInstance().isRunning()) {
                 PushNotifyWorker.getInstance().Start();
            }
        }
    }
    
    private Boolean checkMessageExist(NotificationMessage queueMessage) {
        Boolean isExist = false;
        for (NotificationMessage message : listMessageNotify) {
            if (message != null) {
                if (message.getAppId().equals(queueMessage.getAppId()) && message.getUserID().equals(queueMessage.getUserID()) && message.getDeviceToken().equals(queueMessage.getDeviceToken()) && message.getId() == queueMessage.getId()) {
                    isExist = true;
                    break;
                }
            }
        }
        return isExist;
    }
    
    /**
     * Delete MessageTemplate 
     * @param queueMessage 
     */
    public synchronized void deleteMessage(NotificationMessage queueMessage)
    {
        listMessageNotify.remove(queueMessage);
       
    }
    
    
     /**
     * load MessageTemplate from database for the first run
     */
    private synchronized void loadMessageFromDatabase() {
        //TODO
        MessageSentAdapter messageSentAdapter = new MessageSentAdapter();
        List<MessageSent> allMessageSents = messageSentAdapter.getMessagesWaiting();
        MessageAdapter messageAdapter = new MessageAdapter();
        DeviceRegisterAdapter deviceRegisterAdapter = new DeviceRegisterAdapter();
        int count = allMessageSents.size();
        if (allMessageSents != null && count > 0) {
            for (int i = 0; i < count; i++) {
                Long messageID = allMessageSents.get(i).getMessageId();
                Message selectedMessage = messageAdapter.getMessageByMessageID(messageID);
                String messageContent = "";
                String userId = allMessageSents.get(i).getUserId();
                if (selectedMessage != null) {
                    messageContent = selectedMessage.getMessage();
                }



                List<DeviceRegister> allDevices = deviceRegisterAdapter.getDeviceRegister(userId,allMessageSents.get(i).getAppId());
                int countDevices = allDevices.size();
                if (allDevices != null && countDevices > 0) {
                    for (int k = 0; k < countDevices; k++) {
                        String appID = allDevices.get(k).getAppId();
                        String deviceToken = allDevices.get(k).getDeviceToken();
                        if (deviceToken != null) {
                            NotificationMessage notificationMessage = new NotificationMessage();
                            notificationMessage.setAppId(appID);
                            notificationMessage.setDeviceToken(deviceToken);
                            notificationMessage.setMessage(messageContent);
                            notificationMessage.setStatus(allMessageSents.get(i).getStatus());
                            notificationMessage.setUserID(userId);
                            notificationMessage.setId(allMessageSents.get(i).getMessageId());
                            notificationMessage.setScheduleSend(selectedMessage.getScheduleSend());
                            NotifyMessageQueueManager.getInstance().addMessage(notificationMessage);
                        }
                    }
                }

            }
        }
    }
    
    // Check queue message
    public synchronized Boolean  isEmpty()
    {
       if(listMessageNotify.size()>0)
           return false;
       else
           return true;
    }

}
