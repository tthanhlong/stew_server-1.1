/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jp.co.ncdc.stew.Adapters;

import java.util.ArrayList;
import jp.co.ncdc.stew.Adapters.MessageSentAdapter;
import java.util.List;
import jp.co.ncdc.stew.Entities.MessageSent;
import jp.co.ncdc.stew.Utils.StewConstant;
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
public class MessageSentAdapterTest {
    
    public MessageSentAdapterTest() {
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

    /**
     * Test of addMessage method, of class MessageSentAdapter.
     */
    @Test
    public void testAddMessage_1() {
        System.out.println("addMessage");
        MessageSent messageSent;      
        long messageId;
        MessageSentAdapter instance = new MessageSentAdapter();
        
        messageId = 1;
        messageSent = new MessageSent(messageId, "user1@tma.com", StewConstant.WAITING_SEND, null, null);         
        boolean expResult = true;
        boolean result = instance.addMessage(messageSent);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }

    /**
     * Test of updateMessage method, of class MessageSentAdapter.
     */
    @Test
    public void testUpdateMessage_1() {
        System.out.println("updateMessage");
        MessageSent messageSent = new MessageSent();
        long sentId = 5;
        long messageId = 2;
        messageSent.setMessageSentId(sentId);
        messageSent.setMessageId(messageId);
        messageSent.setUserId("user4@tma.com");
        messageSent.setStatus(StewConstant.SUCCESS_SEND);
        MessageSentAdapter instance = new MessageSentAdapter();
        boolean expResult = true;
        boolean result = instance.updateMessage(messageSent);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }

    /**
     * Test of deleteMessageSent method, of class MessageSentAdapter.
     */
    @Test
    public void testDeleteMessageSent_1() {
        System.out.println("deleteMessageSent");
        long messageId = 5;
        MessageSentAdapter instance = new MessageSentAdapter();
        boolean expResult = true;
        boolean result = instance.deleteMessageSent(messageId);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }

    /**
     * Test of getMessageSentByMessageSentID method, of class MessageSentAdapter.
     */
    @Test
    public void testGetMessageSentByMessageSentID_1() {
        System.out.println("getMessageSentByMessageSentID");
        long messageSentId = 1;
        MessageSentAdapter instance = new MessageSentAdapter();
        MessageSent expResult = new MessageSent();
                expResult.setMessageSentId(messageSentId);
        MessageSent result = instance.getMessageSentByMessageSentID(messageSentId);
        assertEquals(expResult.getMessageSentId(), result.getMessageSentId());
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }

    /**
     * Test of getMessages method, of class MessageSentAdapter.
     */
    @Test
    public void testGetMessages_1() {
        System.out.println("getMessages");
        MessageSentAdapter instance = new MessageSentAdapter();
        int size = 4;
        List result = instance.getMessages();
        assertEquals(size, result.size());
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }

    /**
     * Test of getMessageSentByMessageID method, of class MessageSentAdapter.
     */
    /*@Test
    public void testGetMessageSentByMessageID() {
        System.out.println("getMessageSentByMessageID");
        Long messageId = null;
        MessageSentAdapter instance = new MessageSentAdapter();
        MessageSent expResult = null;
        MessageSent result = instance.getMessageSentByMessageID(messageId);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }*/

    /**
     * Test of getMessagesWaiting method, of class MessageSentAdapter.
     */
    @Test
    public void testGetMessagesWaiting_1() {
        System.out.println("getMessagesWaiting");
        MessageSentAdapter instance = new MessageSentAdapter();
        int size = 2;
        List result = instance.getMessagesWaiting();
        assertEquals(size, result.size());
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }
    
    /**
     * Test of addMessage method, of class MessageSentAdapter.
     */
    @Test
    public void testAddMessageSent_1() {
        System.out.println("addMessageSent");
        MessageSent messageSent;      
        long messageId;
        MessageSentAdapter instance = new MessageSentAdapter();
        
        messageId = 1;
        messageSent = new MessageSent(messageId, "user1@tma.com", StewConstant.WAITING_SEND, null, null);         
        long expResult = 6;
        long result = instance.addMessageSent(messageSent);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }
    
    /**
     * Test of getMessageId method, of class MessageSentAdapter.
     */
    @Test
    public void testGetMessageId_1() {
        System.out.println("getMessageId");         
        long groupId;
        MessageSentAdapter instance = new MessageSentAdapter();
        
        groupId = 2;       
        int expResult = 2;
        int result = instance.getMessageId(groupId).size();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }
    
    /**
     * Test of getLogMessageSent method, of class MessageSentAdapter.
     */
    @Test
    public void testGetLogMessageSent_1() {
        System.out.println("getLogMessageSent");         
        long groupId1 = 1;
        long groupId2 = 2;
        long messageId = 1;        
        List<Long> lstGroupId = new ArrayList<Long>();
        lstGroupId.add(groupId1);
        lstGroupId.add(groupId2);
        
        MessageSentAdapter instance = new MessageSentAdapter();
                
        int expResult = 2;
        int result = instance.getLogMessageSent(messageId, lstGroupId, "appId1", false).size();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }
    
    /**
     * Test of getLogMessageSent method, of class MessageSentAdapter.
     */
    @Test
    public void testGetLogMessageSent_2() {
        System.out.println("getLogMessageSent");         
        long groupId1 = 1;
        long groupId2 = 2;
        long messageId = 1;        
        List<Long> lstGroupId = new ArrayList<Long>();
        lstGroupId.add(groupId1);
        lstGroupId.add(groupId2);
        
        MessageSentAdapter instance = new MessageSentAdapter();
                
        int expResult = 3;
        int result = instance.getLogMessageSent(messageId, lstGroupId, "appId1", true).size();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }
    
    /**
     * Test of getMessagesSendToday method, of class MessageSentAdapter.
     */
    @Test
    public void testGetMessagesSendToday_1() {
        System.out.println("getMessagesSendToday");                 
        long messageId1 = 1;       
        long messageId2 = 2;
        List<Long> lstMessageId = new ArrayList<Long>();
        lstMessageId.add(messageId1);
        lstMessageId.add(messageId2);        
        MessageSentAdapter instance = new MessageSentAdapter();
                
        int expResult = 3;
        int result = instance.getMessagesSendToday(lstMessageId).size();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }
    
    /**
     * Test of updateMessage method, of class MessageSentAdapter.
     */
    @Test
    public void testUpdateMessageSentStatus_1() {
        System.out.println("updateMessageSentStatus");
        MessageSent messageSent = new MessageSent();        
        long messageId = 2;
        messageSent.setAppId("appId1");
        messageSent.setMessageId(messageId);
        messageSent.setUserId("user4@tma.com");
        messageSent.setStatus(StewConstant.SUCCESS_SEND);
        MessageSentAdapter instance = new MessageSentAdapter();
        boolean expResult = true;
        boolean result = instance.updateMessage(messageSent);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }
    
}