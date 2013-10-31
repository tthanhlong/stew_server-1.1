/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jp.co.ncdc.stew.Controllers;

import jp.co.ncdc.stew.Controllers.GroupController;
import java.util.List;
import jp.co.ncdc.stew.APIs.model.DataModel;
import jp.co.ncdc.stew.Entities.GroupUser;
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
public class GroupControllerTest {
    
    public GroupControllerTest() {
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
     * Test of getGroups method, of class GroupController.
     */
    @Test
    public void testGetGroups() {
        System.out.println("getGroups");
        GroupController instance = new GroupController();
        int expResult = 2;
        List result = instance.getGroups();
        assertEquals(expResult, result.size());
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }

    /**
     * Test of getGroupUserByPageNumber method, of class GroupController.
     */
    @Test
    public void testGetGroupUserByPageNumber() {
        System.out.println("getGroupUserByPageNumber");
        int pageNumber = 0;
        GroupController instance = new GroupController();
        int size = 2;
        int totalPage = 1;
        
        DataModel result = instance.getGroupUserByPageNumber(pageNumber);        
        assertEquals(totalPage, result.getTotal());
        assertEquals(StewConstant.STATUS_CODE_OK, result.getStatus());
        assertEquals(size, result.getListItems().size());
        
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }

    /**
     * Test of getGroupByID method, of class GroupController.
     */
    @Test
    public void testGetGroupByID() {
        System.out.println("getGroupByID");
        String groupIDString = "1";
        long groupId = Long.parseLong(groupIDString);
        GroupController instance = new GroupController();
        GroupUser expResult = new GroupUser();
        expResult.setGroupId(groupId);
        expResult.setName("group1");
        expResult.setDescription("This is group 1");
        GroupUser result = instance.getGroupByID(groupIDString);
        assertEquals(expResult.getGroupId(), result.getGroupId());
        assertEquals(expResult.getName(), result.getName());
        assertEquals(expResult.getDescription(), result.getDescription());
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }
}