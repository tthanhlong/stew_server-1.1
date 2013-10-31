/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jp.co.ncdc.stew.Adapters;

import jp.co.ncdc.stew.Adapters.UserGroupDetailAdapter;
import java.util.List;
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
public class UserGroupDetailAdapterTest {
    
    public UserGroupDetailAdapterTest() {
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
     * Test of addUserGroup method, of class UserGroupDetailAdapter.
     */
    @Test
    public void testAddUserGroup_1() {
        System.out.println("addUserGroup");        
        long groupId = 2;
        long roleId = 2;
        UserGroupDetail userGroupDetail = new UserGroupDetail(groupId, "user4@tma.com", roleId);
        UserGroupDetailAdapter instance = new UserGroupDetailAdapter();
        boolean expResult = true;
        boolean result = instance.addUserGroup(userGroupDetail);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }

    /**
     * Test of getUserGroupByEmail method, of class UserGroupDetailAdapter.
     */
    @Test
    public void testGetUserGroupByEmail_1() {
        System.out.println("getUserGroupByEmail");
        String email = "user1@tma.com";
        UserGroupDetailAdapter instance = new UserGroupDetailAdapter();
        int size = 2;
        List result = instance.getUserGroupByEmail(email);
        assertEquals(size, result.size());
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }

    /**
     * Test of getUserGroupByGroupID method, of class UserGroupDetailAdapter.
     */
    @Test
    public void testGetUserGroupByGroupID_1() {
        System.out.println("getUserGroupByGroupID");
        long groupID = 1;
        UserGroupDetailAdapter instance = new UserGroupDetailAdapter();
        int size = 4;
        List result = instance.getUserGroupByGroupID(groupID);
        assertEquals(size, result.size());
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }

    /**
     * Test of deleteUserGroupByEmailAndGroupID method, of class UserGroupDetailAdapter.
     */
    @Test
    public void testDeleteUserGroupByEmailAndGroupID_1() {
        System.out.println("deleteUserGroupByEmailAndGroupID");
        String email = "user4@tma.com";
        long groupID = 2;
        UserGroupDetailAdapter instance = new UserGroupDetailAdapter();
        boolean expResult = true;
        boolean result = instance.deleteUserGroupByEmailAndGroupID(email, groupID);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }
    
    /**
     * Test of deleteUserGroupByEmail method, of class UserGroupDetailAdapter.
     */
    @Test
    public void testDeleteUserGroupByEmail_1() {
        System.out.println("deleteUserGroupByEmail");
        String email = "user4@tma.com";
        UserGroupDetailAdapter instance = new UserGroupDetailAdapter();
        boolean expResult = true;
        boolean result = instance.deleteUserGroupByEmail(email);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }

    /**
     * Test of deleteUserGroupByGroupID method, of class UserGroupDetailAdapter.
     */
    @Test
    public void testDeleteUserGroupByGroupID_1() {
        System.out.println("deleteUserGroupByGroupID");
        long groupID = 2;
        UserGroupDetailAdapter instance = new UserGroupDetailAdapter();
        boolean expResult = true;
        boolean result = instance.deleteUserGroupByGroupID(groupID);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }  
}