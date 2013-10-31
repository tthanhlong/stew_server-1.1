/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jp.co.ncdc.stew.Controllers;

import jp.co.ncdc.stew.Controllers.UserController;
import java.util.List;
import jp.co.ncdc.stew.APIs.model.Authentication;
import jp.co.ncdc.stew.APIs.model.DataModel;
import jp.co.ncdc.stew.Entities.User;
import jp.co.ncdc.stew.Utils.StewConstant;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author tthanhlong
 */
public class UserControllerTest {
    
    public UserControllerTest() {
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
     * Test of authenticateUser method, of class UserController.
     */
    @Test
    public void testAuthenticateUser() {
        System.out.println("authenticateUser");
        String userName = "user1@tma.com";
        String password = "123";
        UserController instance = new UserController();
//        Authentication expResult = null;
//        Authentication result = instance.authenticateUser(userName, password);
//        assertEquals(StewConstant.OK_STATUS, result.status);
        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
    }

    /**
     * Test of getUsersByPageNumber method, of class UserController.
     */
//    @Test
//    public void testGetUsersByPageNumber() {
//        System.out.println("getUsersByPageNumber");
//        int pageNumber = 0;
//        UserController instance = new UserController();
////        DataModel expResult = null;
//        DataModel result = instance.getUsersByPageNumber(pageNumber);
//        assertNotSame(0, result.getTotal());
//        // TODO review the generated test code and remove the default call to fail.
////        fail("The test case is a prototype.");
//    }

    /**
     * Test of getUsersByPageNumberAndGroupID method, of class UserController.
     */
    @Test
    public void testGetUsersByPageNumberAndGroupID() {
        System.out.println("getUsersByPageNumberAndGroupID");
        int pageNumber = 0;
        String groupIDString = "1";
        UserController instance = new UserController();
//        DataModel expResult = null;
        DataModel result = instance.getUsersByPageNumberAndGroupID(pageNumber, groupIDString);
        assertNotSame(0, result.getTotal());
        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
    }

    /**
     * Test of getRoles method, of class UserController.
     */
    @Test
    public void testGetRoles() {
        System.out.println("getRoles");
        UserController instance = new UserController();
        List expResult = null;
        List result = instance.getRoles();
        assertNotSame(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
    }

    /**
     * Test of getUserByEmail method, of class UserController.
     */
    @Test
    public void testGetUserByEmail() {
        System.out.println("getUserByEmail");
        String email = "user1@tma.com";
        UserController instance = new UserController();
        User expResult = null;
        User result = instance.getUserByEmail(email);
        assertNotSame(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
    }

    /**
     * Test of getUserGroupDetail method, of class UserController.
     */
    @Test
    public void testGetUserGroupDetail() {
        System.out.println("getUserGroupDetail");
        String email = "user1@tma.com";
        UserController instance = new UserController();
//        List expResult = null;
        List result = instance.getUserGroupDetail(email);
        assertNotSame(0, result.size());
        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
    }
}