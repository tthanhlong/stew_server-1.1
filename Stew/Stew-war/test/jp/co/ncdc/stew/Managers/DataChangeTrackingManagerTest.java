/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jp.co.ncdc.stew.Managers;

import java.util.ArrayList;
import java.util.List;
import jp.co.ncdc.stew.APIs.model.TrackData;
import jp.co.ncdc.stew.APIs.model.TrackTable;
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
public class DataChangeTrackingManagerTest {
    
    List<TrackData> lstTrackData = new ArrayList<TrackData>();
    TrackData trackData1 = new TrackData();
    TrackData trackData2 = new TrackData();
    TrackData trackData3 = new TrackData();            
    TrackData trackData5 = new TrackData();
    List<TrackTable> lstTrackTable1 = new ArrayList<TrackTable>();
    List<TrackTable> lstTrackTable2 = new ArrayList<TrackTable>();
    List<TrackTable> lstTrackTable3 = new ArrayList<TrackTable>();
    TrackTable trackTable1 = new TrackTable();
    TrackTable trackTable2 = new TrackTable();
    TrackTable trackTable3 = new TrackTable();
    TrackTable trackTable4 = new TrackTable();
    TrackTable trackTable5 = new TrackTable();    
    
    void InitData() {
        List<String> l1 = new ArrayList();
        l1.add("1");
        l1.add("2");
        l1.add("3");
        List<String> l2 = new ArrayList();
        l2.add("4");        
        List<String> l3 = new ArrayList();
        l3.add("5");
        l3.add("6");        
        trackTable1.setTableName("table1");
        trackTable1.setInsert(l1);
        trackTable1.setUpdate(l2);
        trackTable1.setDelete(l3);
        /*
         * trackTable1 info:
         * tableName: table1
         * listInsert: "1", "2", "3"
         * listUpdate: "4"
         * listDelete: "5", "6"
         */
        
        trackTable2.setTableName("table2");
        /*
         * trackTable2 info:
         * tableName: table2
         * listInsert:
         * listUpdate:
         * listDelete:
         */
        
        trackTable3.setTableName("table1");
        trackTable3.setUpdate(l2);
        List<String> l4 = new ArrayList();
        l4.add("4");
        trackTable3.setUpdate(l4);
        /*
         * trackTable3 info:
         * tableName: table1
         * listInsert:
         * listUpdate: "4"
         * listDelete:
         */
        
        trackTable4.setTableName("table4");
        /*
         * trackTable4 info:
         * tableName: table4
         * listInsert:
         * listUpdate:
         * listDelete:
         */
        
        trackTable5.setTableName("table1");
        List<String> l5 = new ArrayList();
        l5.add("1");
        l5.add("2");
        trackTable5.setInsert(l5);
        /*
         * trackTable5 info:
         * tableName: table1
         * listInsert: "1", "2"
         * listUpdate:
         * listDelete:
         */
        
        trackData1.setAppId("appId1");
        trackData1.setUserId("userId1");
        trackData1.setDeviceUDID("deviceUDID1");
        lstTrackTable1.add(trackTable1);
        lstTrackTable1.add(trackTable2);
        trackData1.setTrackDatas(lstTrackTable1);
        /*
         * trackData1 info:
         * appId1
         * uuserId1
         * deviceUDID1
         * listTrackTable: trackTable1, trackTable2
         */
        
        trackData2.setAppId("appId1");
        trackData2.setUserId("userId1");
        trackData2.setDeviceUDID("deviceUDID2");
        lstTrackTable2.add(trackTable3);
        lstTrackTable2.add(trackTable4);
        trackData2.setTrackDatas(lstTrackTable2);
        /*
         * trackData2 info:
         * appId1
         * userId1
         * deviceUDID2
         * listTrackTable: rtachTable3, trackTable4
         */
        
        trackData3.setAppId("appId1");
        trackData3.setUserId("userId1");
        trackData3.setDeviceUDID("deviceUDID3");
        lstTrackTable3.add(trackTable5);        
        trackData3.setTrackDatas(lstTrackTable3);
        /*
         * trackData3 info:
         * appId1
         * userId1
         * deviceUDID3
         * listTrackTable: trackTable5
         */
        
        lstTrackData.add(trackData1);
        lstTrackData.add(trackData2);
        lstTrackData.add(trackData3);
        lstTrackData.add(trackData5);
    }
        
    public DataChangeTrackingManagerTest() {
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
     * Test of getInstance method, of class DataChangeTrackingManager.
     */
//    @Test
//    public void testGetInstance() {
//        System.out.println("getInstance");
//        DataChangeTrackingManager expResult = null;
//        DataChangeTrackingManager result = DataChangeTrackingManager.getInstance();
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }

    /**
     * Test of UpdateDeviceRegister method, of class DataChangeTrackingManager.
     */
//    @Test
//    public void testUpdateDeviceRegister() {
//        System.out.println("UpdateDeviceRegister");
//        String appId = "";
//        String deviceUDID = "";
//        String userId = "";
//        DataChangeTrackingManager instance = null;
//        instance.UpdateDeviceRegister(appId, deviceUDID, userId);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }

    /**
     * Test of saveToDatabase method, of class DataChangeTrackingManager.
     */
//    @Test
//    public void testSaveToDatabase() {
//        System.out.println("saveToDatabase");
//        DataChangeTrackingManager instance = null;
//        instance.saveToDatabase();
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }

    /**
     * Test of loadFromDatabase method, of class DataChangeTrackingManager.
     */
//    @Test
//    public void testLoadFromDatabase() {
//        System.out.println("loadFromDatabase");
//        DataChangeTrackingManager instance = null;
//        instance.loadFromDatabase();
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }

    /**
     * Test of addDataToTable method, of class DataChangeTrackingManager.
     */
//    @Test
//    public void testAddDataToTable() {
//        System.out.println("addDataToTable");
//        TrackTable trackTable = null;
//        String recordId = "";
//        int action = 0;
//        DataChangeTrackingManager instance = null;
//        instance.addDataToTable(trackTable, recordId, action);
//        // TODO review the generated test code and remove the default call to fail.
//        //fail("The test case is a prototype.");
//    }

    /**
     * Test of cleanTrackTable method, of class DataChangeTrackingManager.
     */
//    @Test
//    public void testCleanTrackTable() {
//        System.out.println("cleanTrackTable");
//        TrackTable desTable = null;
//        TrackTable sourceTable = null;
//        DataChangeTrackingManager instance = null;
//        TrackTable expResult = null;
//        TrackTable result = instance.cleanTrackTable(desTable, sourceTable);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        //fail("The test case is a prototype.");
//    }

    /**
     * Test of cleanTableData method, of class DataChangeTrackingManager.
     */
//    @Test
//    public void testCleanTableData() {
//        System.out.println("cleanTableData");
//        List<TrackTable> trackingData = null;
//        TrackTable trackTable = null;
//        DataChangeTrackingManager instance = null;
//        List expResult = null;
//        List result = instance.cleanTableData(trackingData, trackTable);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        //fail("The test case is a prototype.");
//    }

    /**
     * Test of cleanTrackingData method, of class DataChangeTrackingManager.
     */
//    @Test
//    public void testCleanTrackingData_4args_1() {
//        System.out.println("cleanTrackingData");
//        String appId = "";
//        String deviceUDID = "";
//        String userId = "";
//        List<TrackTable> lstTrackTable = null;
//        DataChangeTrackingManager instance = null;
//        List expResult = null;
//        List result = instance.cleanTrackingData(appId, deviceUDID, userId, lstTrackTable);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }

    /**
     * Test of getTrackingData method, of class DataChangeTrackingManager.
     */
    @Test
    public void testGetTrackingData_1() {
        System.out.println("getTrackingData");
        String appId = "appId1";
        String deviceUDID = "deviceUDID1";
        String userId = "user1@tma.com.vn";
        DataChangeTrackingManager instance = DataChangeTrackingManager.getInstance();
        int expResult = 2;
        List result = instance.getTrackingData(appId, deviceUDID, userId);
        assertEquals(expResult, result.size());
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }
//
//    /**
//     * Test of trackDataChange method, of class DataChangeTrackingManager.
//     */
    @Test
    public void testTrackDataChange_1() {
        InitData();
        System.out.println("trackDataChange");
        String appId = "appId1";
        String deviceUDID = "deviceUDID2";
        String userId = "user1@tma.com.vn";
        String tableName = "Department";
        String recordId = "4";
        int action = StewConstant.CREATE;
        DataChangeTrackingManager instance = DataChangeTrackingManager.getInstance();
        int expResult = 2;
        instance.trackDataChange(appId, deviceUDID, userId, tableName, recordId, action);        
        assertEquals(expResult, instance.getLstTrackData().get(0).getTrackDatas().get(0).getInsert().size());
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }
//    
//    /**
//     * Test of trackDataChange method, of class DataChangeTrackingManager.
//     */
    @Test
    public void testLoadFromDatabase_1() {
        
        DataChangeTrackingManager instance = DataChangeTrackingManager.getInstance();
        int expResult = 2;        
        assertEquals(expResult, instance.getLstTrackData().size());        
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }
    
        /**
     * Test of cleanTrackingData method, of class DataChangeTrackingManager.
     */
    @Test
    public void testCleanTrackingData_1() {
        System.out.println("cleanTrackingData");
        String appId = "appId1";
        String deviceUDID = "deviceUDID1";
        String userId = "user1@tma.com.vn";
        TrackTable trackTable = new TrackTable();                
        List<String> l1 = new ArrayList();
        l1.add("2");        
        trackTable.setTableName("Department1");        
        trackTable.setDelete(l1);
        
        DataChangeTrackingManager instance = DataChangeTrackingManager.getInstance();
        int expResult = 0;
        instance.cleanTrackingData(appId, deviceUDID, userId, trackTable);
        assertEquals(expResult, instance.getLstTrackData().get(0).getTrackDatas().get(1).getDelete().size());
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }
}