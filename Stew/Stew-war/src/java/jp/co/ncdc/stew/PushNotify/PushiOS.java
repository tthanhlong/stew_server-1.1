/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jp.co.ncdc.stew.PushNotify;

import com.notnoop.apns.APNS;
import com.notnoop.apns.ApnsService;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.SocketAddress;
import java.util.Date;
import java.util.Map;
import jp.co.ncdc.stew.Managers.EventLogManager;
import jp.co.ncdc.stew.Utils.StewConfig;

/**
 * @Class Name: NotifyMessageQueueManager.java
 * @author: Anh Le
 * @created: August 27, 2013
 *
 */
public class PushiOS extends AbstractPush {

    private long interval = 1000;// miliseconds
    private String name = "_iOSPush";

    @Override
    public String getJobName() {
        return name;
    }

    @Override
    public long getIntervalWakeUp() {
        return interval;
    }

    @Override
    public Boolean pushMessage(String deviceToken, String message, String certificatePath, String certificatePassword) {
        //Setup the connection
        try {
            ApnsService service =
                    APNS.newService()
                    .withCert(certificatePath, certificatePassword)
                    .withSandboxDestination()
                    .build();
            //Create and send the message
            String payload = APNS.newPayload().alertBody(message).build();
            service.push(deviceToken, payload);
            //To query the feedback service for inactive devices:
            Map<String, Date> inactiveDevices = service.getInactiveDevices();
            for (String token : inactiveDevices.keySet()) {
                //Date inactiveAsOf = inactiveDevices.get(token);
                if (token.equals(deviceToken)) {
                    return false;
                }
            }
            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}
