/*
 * Copyright (c) Stew. All Rights Reserved.
 */
package jp.co.ncdc.stew.PushNotify;
import java.util.LinkedList;
import jp.co.ncdc.stew.Utils.StewConfig;


/**
 * @Class Name: PushNotifyWorker.java
 * @author: Anh Le
 * @created: August 27, 2013 
 * @brief: This class is implementation for push notification process processor. 
 */
public class PushNotifyWorker {

    // Create instance PushNotifyWorker
    private static PushNotifyWorker instance;
    public static PushNotifyWorker getInstance() {
        if (instance == null) {
            instance = new PushNotifyWorker();
        }
        return instance;
    } 
    
    private int numberOfPushNotifyService=1;
    //The list Amazon queue messages: store messages get from Amazon Queue
    private LinkedList<Thread> listPushNotifyServices;
    private Boolean isActive=false;
        
    /**
     * Init
     */
    private PushNotifyWorker()
    {
        listPushNotifyServices=new LinkedList<Thread>(); 
        numberOfPushNotifyService= StewConfig.getInstance().numberOfPushNotifyService;
      
    } 
         
    
    /**
     * Start processing PushNotifyService, this process will run as background 
     */
    public void Start() {
        if (!isActive) {
            System.err.println("Start PushNotifyServices");          
            int serviceCounter = 0;  
            while (serviceCounter < numberOfPushNotifyService) {
                serviceCounter++;  
                //Create push worker instance for iOS push             
                Thread pushNotifyService=PushRunableFactory.createPushRunableInstancce(new PushiOS());
                pushNotifyService.start();
                listPushNotifyServices.add(pushNotifyService);                                          
            }
            isActive = true;
        }
    } 
    
    /**
     * Stop processing PushNotifyService
     */
    public void Stop()
    {
        if (isActive) {
            System.out.println("Stop PushNotifyServices");
            for(Thread pushNotifyService :listPushNotifyServices)
                pushNotifyService.stop();
            isActive=false;
        }
    }
    
    /*
     * Checking PushNotifyService status
     */
    public Boolean isRunning()
    {
       if(isActive )
           return true;
       else
           return false;
    }
} 

