/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jp.co.ncdc.stew.Adapters;

import jp.co.ncdc.stew.Adapters.RoleAdapter;
import java.util.List;
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
public class RoleAdapterTest {
    
    public RoleAdapterTest() {
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
     * Test of addRole method, of class RoleAdapter.
     */
    @Test
    public void testAddRole_1() {
        System.out.println("addRole");
        Role role = new Role("Dev", "This is dev role");        
        RoleAdapter instance = new RoleAdapter();
        boolean expResult = true;
        boolean result = instance.addRole(role);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }

    /**
     * Test of getRoles method, of class RoleAdapter.
     */
    @Test
    public void testGetRoles_1() {
        System.out.println("getRoles");
        RoleAdapter instance = new RoleAdapter();
        int expResult = 5;
        List result = instance.getRoles();
        assertEquals(expResult, result.size());
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }
}