/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jp.co.ncdc.stew.Utils;

import jp.co.ncdc.stew.Utils.StewUtils;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author vcnduong
 */
public class StewUtilsTest {
    
    public StewUtilsTest() {
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
     * Test of getInstance method, of class StewUtils.
     */
    @Test
    public void testGetInstance() {
        System.out.println("getInstance");
        StewUtils expResult = null;
        StewUtils result = StewUtils.getInstance();
        assertNotSame(expResult, result);
    }

    /**
     * Test of SHA1Encrypt method, of class StewUtils.
     */
    @Test
    public void testSHA1Encrypt() throws Exception {
        System.out.println("SHA1Encrypt");
        String value = "123456";
        String expResult = "7c4a8d09ca3762af61e59520943dc26494f8941b";
        String result = StewUtils.SHA1Encrypt(value);
        assertEquals(expResult, result);
    }

    /**
     * Test of getUUID method, of class StewUtils.
     */
    @Test
    public void testGetUUID() {
        System.out.println("getUUID");
        String expResult = "";
        String result = StewUtils.getUUID();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of findAndReplace method, of class StewUtils.
     */
    @Test
    public void testFindAndReplace() {
        System.out.println("findAndReplace");
        String orig = "";
        String sub = "";
        String rep = "";
        String expResult = "";
        String result = StewUtils.findAndReplace(orig, sub, rep);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
}