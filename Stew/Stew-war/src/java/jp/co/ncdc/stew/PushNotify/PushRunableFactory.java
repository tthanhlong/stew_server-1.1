/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jp.co.ncdc.stew.PushNotify;

/**
 *
 * @author: Anh Le
 * @created: August 27, 2013 
 * 
 **/
public class PushRunableFactory {
    
    public static Thread createPushRunableInstancce(AbstractPush abstractPush)
    {
         return  new Thread(new PushRunable(abstractPush), abstractPush.getJobName());
    }
}
