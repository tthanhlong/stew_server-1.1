/*
 * Copyright (c) Stew. All Rights Reserved.
 */
package jp.co.ncdc.stew.PushNotify;

import java.util.logging.Level;
import java.util.logging.Logger;
import jp.co.ncdc.stew.APIs.model.NotificationMessage;
import jp.co.ncdc.stew.Adapters.AppsAdapter;
import jp.co.ncdc.stew.Adapters.MessageSentAdapter;
import jp.co.ncdc.stew.Entities.Apps;
import jp.co.ncdc.stew.Entities.MessageSent;
import jp.co.ncdc.stew.Utils.StewConfig;
import jp.co.ncdc.stew.Utils.StewConstant;

/**
 * @Class Name: PushRunable.java
 * @author: Anh Le
 * @created: August 27, 2013
 * @brief: This class is implementation for Runnable of push notify process. It
 * handles for process message from Queue
 */
public class PushRunable implements Runnable {

    private AbstractPush mobilePush;
    // Contructor

    public PushRunable(AbstractPush abstractPush) {
        this.mobilePush = abstractPush;
    }

    public PushRunable() {
    }

    // Start up runable
    @Override
    public void run() {
        while (true) {
            // Checking if List NotifyMessage is not empty, get the last message from list and process it
            if (!NotifyMessageQueueManager.getInstance().isEmpty()) {
                //Get message
                NotificationMessage queueMessage = NotifyMessageQueueManager.getInstance().getMessage();
                //Process message      
                if(queueMessage!=null)
                   processMessage(queueMessage);
            }
            try {
                Thread.sleep(mobilePush.getIntervalWakeUp());
            } catch (InterruptedException ex) {
                Logger.getLogger(PushRunable.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    /**
     * @brief: This method is used private to handle for processing each message
     * @param queueMessage
     */
    private void processMessage(NotificationMessage queueMessage) {
        // TODO
        // get message, appID, device token from queue message and process apple push here
        System.err.println("Process message: "+ queueMessage.getId());
        AppsAdapter adapter = new AppsAdapter();
        Apps apps = adapter.getAppByAppID(queueMessage.getAppId());
        String status = StewConstant.FAIL_SEND;
        MessageSentAdapter messageSentAdapter = new MessageSentAdapter();
        if (apps != null) {
            String certificatePath = StewConfig.getInstance().APNSCertificatePath + apps.getCertificatePath();
            String certificatePassword = apps.getCertificatePassword();
            Boolean result = mobilePush.pushMessage(queueMessage.getDeviceToken(), queueMessage.getMessage(), certificatePath, certificatePassword);
            //update status of message sent
            if (result) {
                status = StewConstant.SUCCESS_SEND;
            }
            else
               status = StewConstant.FAIL_SEND;
            
            //update message sent status
            NotifyMessageQueueManager.getInstance().deleteMessage(queueMessage);
            messageSentAdapter.updateMessageSentStatus(status,queueMessage.getId(),queueMessage.getAppId(),queueMessage.getUserId());

        }
     
    }
}
