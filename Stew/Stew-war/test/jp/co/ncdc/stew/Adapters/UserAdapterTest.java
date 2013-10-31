/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jp.co.ncdc.stew.Adapters;

import jp.co.ncdc.stew.Adapters.UserAdapter;
import java.util.List;
import jp.co.ncdc.stew.Entities.User;
import jp.co.ncdc.stew.Utils.CipherUtils;
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
public class UserAdapterTest {
    
    public UserAdapterTest() {
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
     * Test of addUser method, of class UserAdapter.
     */
    @Test
    public void testAddUser_1() {
        System.out.println("addUser");
        User user = new User("1234", "user5@tma.com");
        UserAdapter instance = new UserAdapter();
        boolean expResult = true;
        boolean result = instance.addUser(user);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }

    /**
     * Test of getUser method, of class UserAdapter.
     */
    @Test
    public void testGetUser_1() {
        System.out.println("getUser");
        String userName = "user1@tma.com";
        String password = "123456";
        UserAdapter instance = new UserAdapter();
        User result = instance.getUser(userName, password);
        assertEquals(userName, result.getEmail());        
        assertEquals(CipherUtils.encrypt(password), result.getPasswordHash());
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }

    /**
     * Test of updateUser method, of class UserAdapter.
     */
    @Test
    public void testUpdateUser_1() {
        System.out.println("updateUser");
        User user = new User("123456", "user5@tma.com");
        UserAdapter instance = new UserAdapter();
        boolean expResult = true;
        boolean result = instance.updateUser(user);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }

    /**
     * Test of deleteUser method, of class UserAdapter.
     */
    @Test
    public void testDeleteUser_1() {
        System.out.println("deleteUser");
        String userName = "user5@tma.com";
        UserAdapter instance = new UserAdapter();
        boolean expResult = true;
        boolean result = instance.deleteUser(userName);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }

    /**
     * Test of getUsers method, of class UserAdapter.
     */
    @Test
    public void testGetUsers_1() {
        System.out.println("getUsers");
        UserAdapter instance = new UserAdapter();
        int size = 4;
        List result = instance.getUsers();
        assertEquals(size, result.size());
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }   
    /**
     * Test of getUserByEmail method, of class UserAdapter.
     */
    @Test
    public void testGetUserByEmail_1() {
        System.out.println("getUserByEmail");
        String email = "user1@tma.com";
        UserAdapter instance = new UserAdapter();
        User expResult = new User("123456", "user1@tma.com");
        User result = instance.getUserByEmail(email);
        assertEquals(expResult.getEmail(), result.getEmail());        
        assertEquals(CipherUtils.encrypt(expResult.getPasswordHash()), result.getPasswordHash());
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }
}