/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jp.co.ncdc.stew.APIs.model;

import jp.co.ncdc.stew.APIs.model.DataModel;
import java.util.List;
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
public class DataModelTest {
    
    public DataModelTest() {
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
     * Test of getStatus method, of class DataModel.
     */
    @Test
    public void testGetStatus() {
        System.out.println("getStatus");
        DataModel instance = new DataModel();
        int expResult = 0;
        int result = instance.getStatus();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setStatus method, of class DataModel.
     */
    @Test
    public void testSetStatus() {
        System.out.println("setStatus");
        int status = 0;
        DataModel instance = new DataModel();
        instance.setStatus(status);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getTotal method, of class DataModel.
     */
    @Test
    public void testGetTotal() {
        System.out.println("getTotal");
        DataModel instance = new DataModel();
        int expResult = 0;
        int result = instance.getTotal();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setTotal method, of class DataModel.
     */
    @Test
    public void testSetTotal() {
        System.out.println("setTotal");
        int total = 0;
        DataModel instance = new DataModel();
        instance.setTotal(total);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getListItems method, of class DataModel.
     */
    @Test
    public void testGetListItems() {
        System.out.println("getListItems");
        DataModel instance = new DataModel();
        List expResult = null;
        List result = instance.getListItems();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setListItems method, of class DataModel.
     */
    @Test
    public void testSetListItems() {
        System.out.println("setListItems");
        List listItems = null;
        DataModel instance = new DataModel();
        instance.setListItems(listItems);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
}