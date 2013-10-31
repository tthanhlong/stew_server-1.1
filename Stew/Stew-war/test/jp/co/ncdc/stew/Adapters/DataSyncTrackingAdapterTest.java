/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jp.co.ncdc.stew.Adapters;

import jp.co.ncdc.stew.APIs.model.Status;
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
public class DataSyncTrackingAdapterTest {

    public DataSyncTrackingAdapterTest() {
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
     * Test of getTableNameForSync method, of class DataSyncTrackingAdapter.
     */
    @Test
    public void testGetTableNameForSync_1() {
        System.out.println("getTableNameForSync");
        String userToken = "token1";
        DataSyncTrackingAdapter instance = new DataSyncTrackingAdapter();
        String expResult = "table1, table2";
        Status result = instance.getTableNameForSync(userToken);
        assertEquals(expResult, result.getResult());
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }

    /**
     * Test of getTableNameForSync method, of class DataSyncTrackingAdapter.
     */
    @Test
    public void testGetTableNameForSync_2() {
        System.out.println("getTableNameForSync");
        String userToken = "token3";
        DataSyncTrackingAdapter instance = new DataSyncTrackingAdapter();
        String expResult = null;
        Status result = instance.getTableNameForSync(userToken);
        assertEquals(expResult, result.getResult());
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }
}