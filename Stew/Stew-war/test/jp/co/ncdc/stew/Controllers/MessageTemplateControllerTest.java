/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jp.co.ncdc.stew.Controllers;

import jp.co.ncdc.stew.Controllers.MessageTemplateController;
import java.util.List;
import jp.co.ncdc.stew.APIs.model.DataModel;
import jp.co.ncdc.stew.Entities.MessageTemplate;
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
public class MessageTemplateControllerTest {
    
    public MessageTemplateControllerTest() {
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
     * Test of getMessagesByPageNumber method, of class MessageTemplateController.
     */
    @Test
    public void testGetMessagesByPageNumber() {
        System.out.println("getMessagesByPageNumber");
        int pageNumber = 0;
        int listSize = 3;
        int totalPage = 2;
        
        MessageTemplateController instance = new MessageTemplateController();        
        DataModel result = instance.getMessagesByPageNumber(pageNumber);
        assertEquals(listSize, result.getListItems().size());
        assertEquals(totalPage, result.getTotal());
        assertEquals(StewConstant.STATUS_CODE_OK, result.getStatus());
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }
    
    /**
     * Test of getMessageByTitle method, of class MessageTemplateController.
     */
    @Test
    public void testGetMessageByTitle() {
        System.out.println("getMessageByTitle");
        String title = "title1";
        MessageTemplateController instance = new MessageTemplateController();
        String expResult = title;
        MessageTemplate result = instance.getMessageByTitle(title);
        assertEquals(expResult, result.getTitle());
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }

    /**
     * Test of getMessageByID method, of class MessageTemplateController.
     */
    @Test
    public void testGetMessageByID() {
        System.out.println("getMessageByID");
        long id = 1;
        MessageTemplateController instance = new MessageTemplateController();
        MessageTemplate expResult = new MessageTemplate();
        expResult.setId(id);
        expResult.setTitle("title1");
        expResult.setMessage("message1");
        MessageTemplate result = instance.getMessageByID(id);
        assertEquals(expResult.getId(), result.getId());
        assertEquals(expResult.getTitle(), result.getTitle());
        assertEquals(expResult.getMessage(), result.getMessage());
        // TODO review the generated test code and remove the default call to fail.        
        //fail("The test case is a prototype.");
    }

    /**
     * Test of getAllMessages method, of class MessageTemplateController.
     */
    @Test
    public void testGetAllMessages() {
        System.out.println("getAllMessages");
        MessageTemplateController instance = new MessageTemplateController();
        int expResult = 4;
        List result = instance.getAllMessages();
        assertEquals(expResult, result.size());
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }
}