/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jp.co.ncdc.stew.Adapters;

import jp.co.ncdc.stew.Adapters.GroupAdapter;
import java.util.List;
import jp.co.ncdc.stew.Entities.GroupUser;
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
public class GroupAdapterTest {
    
    public GroupAdapterTest() {
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
     * Test of addGroup method, of class GroupAdapter.
     */
    @Test
    public void testAddGroup_1() {
        System.out.println("addGroup");
        GroupUser groupUser = new GroupUser();
        groupUser.setName("group3");
        groupUser.setDescription("This is group 3");
        GroupAdapter instance = new GroupAdapter();
        boolean expResult = true;
        boolean result = instance.addGroup(groupUser);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }

    /**
     * Test of updateGroup method, of class GroupAdapter.
     */
    @Test
    public void testUpdateGroup_1() {
        System.out.println("updateGroup");
        GroupUser groupUser = new GroupUser();
        long id = 2;
        groupUser.setGroupId(id);
        groupUser.setName("group02");
        groupUser.setDescription("This is group 02");
        GroupAdapter instance = new GroupAdapter();
        boolean expResult = true;
        boolean result = instance.updateGroup(groupUser);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }

    /**
     * Test of deleteGroup method, of class GroupAdapter.
     */
    @Test
    public void testDeleteGroup_1() {
        System.out.println("deleteGroup");
        long groupId = 3;
        GroupAdapter instance = new GroupAdapter();
        boolean expResult = true;
        boolean result = instance.deleteGroup(groupId);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }

    /**
     * Test of getGroupByGroupID method, of class GroupAdapter.
     */
    @Test
    public void testGetGroupByGroupID_1() {
        System.out.println("getGroupByGroupID");
        long groupId = 1;
        GroupUser expResult = new GroupUser();
        expResult.setGroupId(groupId);
        GroupAdapter instance = new GroupAdapter();        
        GroupUser result = instance.getGroupByGroupID(groupId);
        assertEquals(expResult.getGroupId(), result.getGroupId());
        assertEquals("group1", result.getName());
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }

    /**
     * Test of getGroupByGroupName method, of class GroupAdapter.
     */
    @Test
    public void testGetGroupByGroupName_1() {
        System.out.println("getGroupByGroupName");
        String groupName = "group1";
        GroupAdapter instance = new GroupAdapter();        
        GroupUser result = instance.getGroupByGroupName(groupName);
        assertEquals(groupName, result.getName());
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }
    
    /**
     * Test of getGroupUsers method, of class GroupAdapter.
     */
    @Test
    public void testGetGroupUsers_1() {
        System.out.println("getGroupUsers");
        GroupAdapter instance = new GroupAdapter();
        int size = 2;
        List result = instance.getGroupUsers();
        assertEquals(size, result.size());
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }    
}