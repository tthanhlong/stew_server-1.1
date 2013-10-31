/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jp.co.ncdc.stew.Controllers;

import java.util.Date;

/**
 *
 * @author tquangthai
 */
public class AsyncTask {
    
    private String taskName="";
    Thread task=null;
    public AsyncTask()
    {
        // Create Task name
        taskName="task_"+String.valueOf(new Date().getTime());
    } 
    
    /**
     * To do async task
     * @param abstractTask 
     */
    public void doAsyncTask(final AbstractTask abstractTask){ 
        Runnable runnable = new Runnable() {
            @Override 
            public void run() { 
                try { 
                      abstractTask.doTask();   
                   // Kill thread after complete task;
                   if(task!=null)
                       task.stop();
                } catch (Exception ex) { 
                    //Handle error which cannot be thrown back 
                } 
            } 
        }; 
        task =new Thread(runnable,taskName); 
        task.start();
    }
}

