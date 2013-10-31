/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jp.co.ncdc.stew.Adapters;

import jp.co.ncdc.stew.Adapters.MessageTemplateAdapter;
import java.util.List;
import jp.co.ncdc.stew.Entities.MessageTemplate;
import jp.co.ncdc.stew.Entities.Role;
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
public class MessageTemplateAdapterTest {
    
    public MessageTemplateAdapterTest() {
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
     * Test of addMessage method, of class MessageTemplateAdapter.
     */
    /*@Test
    public void initData() {
        System.out.println("initData");
        MessageTemplateAdapter instance = new MessageTemplateAdapter();
        MessageTemplate message;
        message = new MessageTemplate("title1", "message1");
        instance.addMessage(message);                
        message = new MessageTemplate("title2", "message2");
        instance.addMessage(message);                
        message = new MessageTemplate("title3", "message3");
        instance.addMessage(message);                
        message = new MessageTemplate("title4", "message4");
        instance.addMessage(message);     
        
        RoleAdapter roleAdapter = new RoleAdapter();
        Role role;
        role = new Role("admin", "system admin");
        roleAdapter.addRole(role);
        role = new Role("user", "system user");
        roleAdapter.addRole(role);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }*/
    
    /**
     * Test of addMessage method, of class MessageTemplateAdapter.
     */
    @Test
    public void testAddMessage_1() {
        System.out.println("addMessage");
        MessageTemplate message = new MessageTemplate();
        message.setTitle("title5");
        message.setMessage("message5");        
        MessageTemplateAdapter instance = new MessageTemplateAdapter();
        boolean expResult = true;
        boolean result = instance.addMessage(message);        
        assertEquals(expResult, result);        
        // TODO review the generated test code and remove the default call to fail.        
    }
           
        /**
     * Test of updateMessage method, of class MessageTemplateAdapter.
     */
    @Test
    public void testUpdateMessage_1() {
        System.out.println("updateMessage");
        MessageTemplate message = new MessageTemplate();
        long id = 5;
        message.setId(id);
        message.setTitle("title05");
        message.setMessage("message05");
        MessageTemplateAdapter instance = new MessageTemplateAdapter();
        boolean expResult = true;
        boolean result = instance.updateMessage(message);        
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }

    /**
     * Test of deleteMessage method, of class MessageTemplateAdapter.
     */
    @Test
    public void testDeleteMessage_1() {
        System.out.println("deleteMessage");
        long messageId = 5;
        MessageTemplateAdapter instance = new MessageTemplateAdapter();
        boolean expResult = true;
        boolean result = instance.deleteMessage(messageId);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }

    /**
     * Test of getMessageByMessageID method, of class MessageTemplateAdapter.
     */
    @Test
    public void testGetMessageByMessageID_1() {
        System.out.println("getMessageByMessageID");
        long messageId = 1;
        MessageTemplateAdapter instance = new MessageTemplateAdapter();
        MessageTemplate expResult = new MessageTemplate();
        expResult.setMessage("message1");
        expResult.setTitle("title1");
        expResult.setId(messageId);
        MessageTemplate result = instance.getMessageByMessageID(messageId);
        if (result != null) {
            assertEquals(expResult.getId(), result.getId());
            assertEquals(expResult.getMessage(), result.getMessage());
            assertEquals(expResult.getTitle(), result.getTitle());
        }else 
            fail("null result.");
        // TODO review the generated test code and remove the default call to fail.        
    }

    /**
     * Test of getMessageByTitle method, of class MessageTemplateAdapter.
     */
    @Test
    public void testGetMessageByTitle_1() {
        System.out.println("getMessageByTitle");
        String title = "title1";
        long id = 1;
        MessageTemplateAdapter instance = new MessageTemplateAdapter();
        MessageTemplate expResult = new MessageTemplate();
        expResult.setId(id);
        expResult.setTitle("title1");
        expResult.setMessage("message1");
        MessageTemplate result = instance.getMessageByTitle(title);
        if (result != null) {
            assertEquals(expResult.getId(), result.getId());
            assertEquals(expResult.getTitle(), result.getTitle());
            assertEquals(expResult.getMessage(), result.getMessage());
        }else        
            fail("null result");
        // TODO review the generated test code and remove the default call to fail.        
    }

    /**
     * Test of getMessages method, of class MessageTemplateAdapter.
     */
    @Test
    public void testGetMessages_1() {
        System.out.println("getMessages");
        MessageTemplateAdapter instance = new MessageTemplateAdapter();
        int expResult = 4;
        List result = instance.getMessages();
        assertEquals(expResult, result.size());
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }
}