/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jp.co.ncdc.stew.PushNotify;

/**
 * @Class Name: AbstractPush.java
 * @author: Anh Le
 * @created: August 27, 2013
 * @brief: This abstract class is base for each push notify platform
 */
public abstract class AbstractPush {

    /**
     * get name of service
     *
     * @return
     */
    public abstract String getJobName();

    /**
     * get interval wake up of service push notification
     *
     * @return
     */
    public abstract long getIntervalWakeUp();

    /**
     *
     * @param appID
     * @param deviceToken
     * @param certificatePath
     * @param certificatePassword
     * @param message
     * @return
     */
    public abstract Boolean pushMessage(String deviceToken, String message, String certificatePath, String certificatePassword);
}
