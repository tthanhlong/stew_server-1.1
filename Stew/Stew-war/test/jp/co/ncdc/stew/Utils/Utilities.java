/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jp.co.ncdc.stew.Utils;

import jp.co.ncdc.stew.Utils.StewConstant;
import java.util.Date;
import jp.co.ncdc.stew.Adapters.AppsAdapter;
import jp.co.ncdc.stew.Adapters.DataSyncTrackingAdapter;
import jp.co.ncdc.stew.Adapters.DeviceRegisterAdapter;
import jp.co.ncdc.stew.Adapters.GroupAdapter;
import jp.co.ncdc.stew.Adapters.GroupAppDetailAdapter;
import jp.co.ncdc.stew.Adapters.LogAdapter;
import jp.co.ncdc.stew.Adapters.MessageAdapter;
import jp.co.ncdc.stew.Adapters.MessageTemplateAdapter;
import jp.co.ncdc.stew.Adapters.MessageSentAdapter;
import jp.co.ncdc.stew.Adapters.RoleAdapter;
import jp.co.ncdc.stew.Adapters.UserAdapter;
import jp.co.ncdc.stew.Adapters.UserAppAdapter;
import jp.co.ncdc.stew.Adapters.UserGroupDetailAdapter;
import jp.co.ncdc.stew.Entities.Apps;
import jp.co.ncdc.stew.Entities.DataSyncTracking;
import jp.co.ncdc.stew.Entities.DeviceRegister;
import jp.co.ncdc.stew.Entities.GroupAppDetail;
import jp.co.ncdc.stew.Entities.GroupUser;
import jp.co.ncdc.stew.Entities.Log;
import jp.co.ncdc.stew.Entities.Message;
import jp.co.ncdc.stew.Entities.MessageTemplate;
import jp.co.ncdc.stew.Entities.MessageSent;
import jp.co.ncdc.stew.Entities.Role;
import jp.co.ncdc.stew.Entities.User;
import jp.co.ncdc.stew.Entities.UserApp;
import jp.co.ncdc.stew.Entities.UserGroupDetail;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author tquangthai
 */
public class Utilities {
    
    public Utilities() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }
    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    // @Test
    // public void hello() {}
    @Test
    public void initData() {
        System.out.println("initData");
        
        Apps app = new Apps();
        AppsAdapter appsAdapter = new AppsAdapter(); 
        app.setAppId("appId1");
        app.setAppName("app1");
        appsAdapter.addApps(app);
        app = new Apps();
        app.setAppId("appId2");
        app.setAppName("app2");
        appsAdapter.addApps(app);
        
        UserAdapter userAdapter = new UserAdapter();
        User user;
        Date date = new Date();
        long roleId = 1;
        //String passwordHash, Long roleId, String email, String userToken, Date tokenExpireDate, Date loginDate
        user = new User("stew@test.com", "123456","","", new Date());
        userAdapter.addUser(user);
        user = new User("user2@tma.com", "123456","","", new Date());
        userAdapter.addUser(user);
        user = new User("user3@tma.com", "123456","","", new Date());
        userAdapter.addUser(user);
        user = new User("user4@tma.com", "123456","","", new Date());
        userAdapter.addUser(user);
        
        DeviceRegister deviceRegister = new DeviceRegister();
        DeviceRegisterAdapter deviceRegisterAdapt = new DeviceRegisterAdapter();
        deviceRegister.setAppId("appId1");
        deviceRegister.setUserId("user1@tma.com");
        deviceRegister.setDeviceUDID("deviceUDID1");
        deviceRegister.setDeviceToken("deviceToken1");
        deviceRegisterAdapt.registerDevice(deviceRegister);
        
        deviceRegister = new DeviceRegister();
        deviceRegister.setAppId("appId1");
        deviceRegister.setUserId("user2@tma.com");
        deviceRegister.setDeviceUDID("deviceUDID2");
        deviceRegister.setDeviceToken("deviceToken2");
        deviceRegisterAdapt.registerDevice(deviceRegister);
        
        deviceRegister = new DeviceRegister();
        deviceRegister.setAppId("appId1");
        deviceRegister.setUserId("user3@tma.com");
        deviceRegister.setDeviceUDID("deviceUDID3");
        deviceRegister.setDeviceToken("deviceToken3");
        deviceRegisterAdapt.registerDevice(deviceRegister);
        
        deviceRegister = new DeviceRegister();
        deviceRegister.setAppId("appId1");
        deviceRegister.setUserId("user4@tma.com");
        deviceRegister.setDeviceUDID("deviceUDID4");
        deviceRegister.setDeviceToken("deviceToken4");
        deviceRegisterAdapt.registerDevice(deviceRegister);
                        
        MessageTemplateAdapter instance = new MessageTemplateAdapter();
        MessageTemplate messageTemplate;
        messageTemplate = new MessageTemplate("title1", "message1");
        instance.addMessage(messageTemplate);                
        messageTemplate = new MessageTemplate("title2", "message2");
        instance.addMessage(messageTemplate);                
        messageTemplate = new MessageTemplate("title3", "message3");
        instance.addMessage(messageTemplate);                
        messageTemplate = new MessageTemplate("title4", "message4");
        instance.addMessage(messageTemplate);     
        
        RoleAdapter roleAdapter = new RoleAdapter();
        Role role;
        role = new Role("SupperAdmin", "system supper admin");
        roleAdapter.addRole(role);
        role = new Role("admin", "sytem admin");
        roleAdapter.addRole(role);
        role = new Role("manager", "sytem manager");
        roleAdapter.addRole(role);
        role = new Role("user", "system user");
        roleAdapter.addRole(role);
        
        GroupAdapter groupAdapter = new GroupAdapter();
        GroupUser group = new GroupUser();
        group.setName("group1");
        group.setDescription("This is group 1");
        groupAdapter.addGroup(group);
        group = new GroupUser();
        group.setName("group2");
        group.setDescription("This is group 2");
        groupAdapter.addGroup(group);     
        group = new GroupUser();
        group.setName("group3");
        group.setDescription("This is group 3");
        groupAdapter.addGroup(group);
        
        Log log = new Log();
        LogAdapter logAdapter = new LogAdapter();
        log.setAppId("appId1");
        log.setDeviceUDID("deviceUDID1");
        log.setMessageLog("this is message log 1");
        log.setUserId("user2@tma.com.vn");
        log.setType("ERROR");
        log.setTimeLog(date);
        logAdapter.addLog(log);
        
        log = new Log();
        log.setAppId("appId1");
        log.setDeviceUDID("deviceUDID1");
        log.setMessageLog("this is message log 2");
        log.setUserId("user3@tma.com.vn");
        log.setType("MONITOR");
        log.setTimeLog(date);
        logAdapter.addLog(log);
                
        log = new Log();
        log.setAppId("appId2");
        log.setDeviceUDID("deviceUDID1");
        log.setMessageLog("this is message log 3");
        log.setUserId("user3@tma.com.vn");
        log.setType("MONITOR");
        log.setTimeLog(date);
        logAdapter.addLog(log);
        
        log = new Log();
        log.setAppId("appId1");
        log.setDeviceUDID("deviceUDID1");
        log.setMessageLog("this is message log 4");
        log.setUserId("user4@tma.com.vn");
        log.setType("ERROR");
        log.setTimeLog(date);
        logAdapter.addLog(log);
        
        UserApp userApp = new UserApp();
        UserAppAdapter userAppAdapt = new UserAppAdapter();
        userApp.setAppId("appId1");
        userApp.setUserId("user1@tma.com.vn");
        userApp.setStatus("active");
        userApp.setDateModified(date);
        userAppAdapt.addUserApp(userApp);
        userApp = new UserApp();
        userApp.setAppId("appId1");
        userApp.setUserId("user2@tma.com.vn");
        userApp.setStatus("active");
        userApp.setDateModified(date);
        userAppAdapt.addUserApp(userApp);
        userApp = new UserApp();
        userApp.setAppId("appId1");
        userApp.setUserId("user3@tma.com.vn");
        userApp.setStatus("active");
        userApp.setDateModified(date);
        userAppAdapt.addUserApp(userApp);
        userApp = new UserApp();
        userApp.setAppId("appId1");
        userApp.setUserId("user4@tma.com.vn");
        userApp.setStatus("active");
        userApp.setDateModified(date);
        userAppAdapt.addUserApp(userApp);
        userApp = new UserApp();
        userApp.setAppId("appId2");
        userApp.setUserId("user1@tma.com.vn");
        userApp.setStatus("active");
        userApp.setDateModified(date);
        
        UserGroupDetail UGDetail;
        UserGroupDetailAdapter UGDAdapter = new UserGroupDetailAdapter();       
        long groupId1 = 1;
        long groupId2 = 2;
        long groupId3 = 3;        
        long role1 = 1;
        long role2 = 2;
        long role3 = 3;
        long role4 = 4;
        
        UGDetail = new UserGroupDetail(null, "stew@test.com", role1);
        UGDAdapter.addUserGroup(UGDetail);
        UGDetail = new UserGroupDetail(groupId2, "user2@tma.com", role2);
        UGDAdapter.addUserGroup(UGDetail);
        UGDetail = new UserGroupDetail(groupId3, "user2@tma.com", role4);
        UGDAdapter.addUserGroup(UGDetail);
        UGDetail = new UserGroupDetail(groupId1, "user3@tma.com", role4);
        UGDAdapter.addUserGroup(UGDetail);
        UGDetail = new UserGroupDetail(groupId2, "user3@tma.com", role2);
        UGDAdapter.addUserGroup(UGDetail);
        UGDetail = new UserGroupDetail(groupId3, "user3@tma.com", role3);
        UGDAdapter.addUserGroup(UGDetail);
        UGDetail = new UserGroupDetail(groupId2, "user4@tma.com", role2);
        UGDAdapter.addUserGroup(UGDetail);
        
        GroupAppDetail groupAppDetail;
        GroupAppDetailAdapter GADAdapter = new GroupAppDetailAdapter();
        groupAppDetail = new GroupAppDetail("appId1", groupId1);
        GADAdapter.addGroupAppDetail(groupAppDetail);
        groupAppDetail = new GroupAppDetail("appId1", groupId3);
        GADAdapter.addGroupAppDetail(groupAppDetail);
        groupAppDetail = new GroupAppDetail("appId2", groupId1);
        GADAdapter.addGroupAppDetail(groupAppDetail);
        groupAppDetail = new GroupAppDetail("appId2", groupId2);
        GADAdapter.addGroupAppDetail(groupAppDetail);
        groupAppDetail = new GroupAppDetail("appId2", groupId3);
        GADAdapter.addGroupAppDetail(groupAppDetail);
        
        Message message = new Message();
        MessageAdapter messageAdapt = new MessageAdapter();
        message.setMessage("message1");
        message.setSentToApp("appId1");
        message.setSentToGroup("group1");
        message.setTitle("title1");
        message.setCreateMessage(new Date());
        message.setScheduleSend(new Date());
        messageAdapt.addMessage(message);
        
        message = new Message();
        message.setMessage("message2");
        message.setSentToApp("appId1");
        message.setSentToGroup("group1,group2,group3");
        message.setTitle("title2");
        message.setCreateMessage(new Date());
        message.setScheduleSend(new Date());
        messageAdapt.addMessage(message);
                
        MessageSent messageSent;      
        long messageId;
        MessageSentAdapter messageSentAdapt = new MessageSentAdapter();
        
        messageId = 1;
        messageSent = new MessageSent(messageId, "user3@tma.com", StewConstant.WAITING_SEND, "appId1", groupId1);
        messageSentAdapt.addMessage(messageSent);
        //messageSent = new MessageSent(messageId, "user2@tma.com", StewConstant.SUCCESS_SEND, "appId1", groupId2);
        //messageSentAdapt.addMessage(messageSent);
        messageId = 2;
        messageSent = new MessageSent(messageId, "user3@tma.com", StewConstant.SUCCESS_SEND, "appId1", groupId1);
        messageSentAdapt.addMessage(messageSent);
        messageSent = new MessageSent(messageId, "user2@tma.com", StewConstant.FAIL_SEND, "appId1", groupId2);
        messageSentAdapt.addMessage(messageSent);       
        messageSent = new MessageSent(messageId, "user3@tma.com", StewConstant.SUCCESS_SEND, "appId1", groupId2);
        messageSentAdapt.addMessage(messageSent);       
        messageSent = new MessageSent(messageId, "user2@tma.com", StewConstant.FAIL_SEND, "appId1", groupId3);
        messageSentAdapt.addMessage(messageSent);       
        messageSent = new MessageSent(messageId, "user3@tma.com", StewConstant.SUCCESS_SEND, "appId1", groupId3);
        messageSentAdapt.addMessage(messageSent);       
        messageSent = new MessageSent(messageId, "user4@tma.com", StewConstant.FAIL_SEND, "appId1", groupId3);
        messageSentAdapt.addMessage(messageSent);               
        
        DataSyncTracking dataSync = new DataSyncTracking();  
        DataSyncTrackingAdapter dataSyncAdapt = new DataSyncTrackingAdapter();
        long appVersion = 1;
        dataSync.setDeviceUDID("deviceUDID1");
        dataSync.setAppId("appId1");
        dataSync.setUserId("user1@tma.com.vn");
        dataSync.setDataToSync_Metadata("[{\"tableName\":\"Department\",\"update\":[\"1\"],\"insert\":[\"3\",\"4\"],\"delete\":[\"2\"]},{\"tableName\":\"Department1\",\"update\":[\"1\"],\"insert\":[\"3\"],\"delete\":[\"2\"]}]");
        dataSync.setAppVersion(appVersion);
        dataSync.setTransactionId("1");
        dataSync.setSyncDate(date);
        dataSyncAdapt.addDataSyncTracking(dataSync);
        dataSync = new DataSyncTracking();
        dataSync.setDeviceUDID("deviceUDID2");
        dataSync.setAppId("appId1");
        dataSync.setUserId("user2@tma.com.vn");
        dataSync.setDataToSync_Metadata("[{\"tableName\":\"Department\",\"update\":[\"1\"],\"insert\":[\"3\"],\"delete\":[\"2\"]}]");
        dataSync.setAppVersion(appVersion);
        dataSync.setTransactionId("2");
        dataSync.setSyncDate(date);
        dataSyncAdapt.addDataSyncTracking(dataSync);
                                
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }
}