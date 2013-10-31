/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jp.co.ncdc.stew.Controllers;

import jp.co.ncdc.stew.APIs.model.Status;
import jp.co.ncdc.stew.Adapters.DeviceRegisterAdapter;
import jp.co.ncdc.stew.Entities.DeviceRegister;
import jp.co.ncdc.stew.Utils.APIResponse;
import jp.co.ncdc.stew.Utils.StewConstant;

/**
 *
 * @author vcnduong
 */
public class DeviceRegisterController {

    /**
     * register device
     *
     * @author vcnduong
     * @param token
     * @param deviceToken
     * @param appId
     * @param deviceUDID
     * @return
     */
    public Status registerDevice(String deviceToken, String appId, String deviceUDID) {
        Status result = new Status();
        DeviceRegisterAdapter deviceRegisterAdapter = new DeviceRegisterAdapter();
        if (deviceToken == null || "".equals(deviceToken) || appId == null || "".equals(appId) || deviceUDID == null || "".equals(deviceUDID)) {
            result = new Status(StewConstant.ERR_STATUS, StewConstant.ERR_REQUIRED);
        } else {
            DeviceRegister register = deviceRegisterAdapter.getDeviceRegisterByDeviceToken(deviceToken);
            //check device token register or not
            if (register != null) {
                //if device token registered check device UDID and appID of this record
                if (deviceUDID.equals(register.getDeviceUDID()) && appId.equals(register.getAppId())) {
                    result = new Status(StewConstant.ERR_STATUS, StewConstant.ERR_DEVICE_ALREADY_REGISTERED);
                } else {
                    result = new Status(StewConstant.ERR_STATUS, StewConstant.ERR_DEVICE_TOKEN_INUSE);
                }
            } else {
                //if device token does not registered, create new device registered record
                DeviceRegister deviceRegister = new DeviceRegister();
                deviceRegister.setAppId(appId);
                deviceRegister.setDeviceToken(deviceToken);
                deviceRegister.setDeviceUDID(deviceUDID);
                //save new recode device registered to database
                if (deviceRegisterAdapter.registerDevice(deviceRegister)) {
                    result = new APIResponse().loggingResponse(StewConstant.OK_STATUS, "");
                } else {
                    //show error if cant save record to database
                    result = new Status(StewConstant.ERR_STATUS, StewConstant.ERR_SAVE_DEVICEREGISTER);
                }
            }
        }
        return result;
    }

    /**
     * update register device
     *
     * @param userId
     * @param appId
     * @param deviceUDID
     * @return
     */
    public DeviceRegister updateRegisterDevice(String userId, String appId, String deviceUDID) {
        //get device adapter
        DeviceRegisterAdapter adapter = new DeviceRegisterAdapter();

        //get device already registerd
        DeviceRegister current = adapter.getDeviceRegister(userId, appId, deviceUDID);
        if (current != null) {
            //if device already registered return this record
            return current;
        } else {
            //if device not yet register 
            //get device register record not yet has userId
            DeviceRegister deviceRegister = adapter.getDeviceRegister(null, appId, deviceUDID);
            if (deviceRegister != null) {
                //update user id if has the record and save record to database
                deviceRegister.setUserId(userId);
                adapter.updateDeviceRegister(deviceRegister);
                //return updated record
                return deviceRegister;
            } else {
                // if not yet has record with the deviceUDID and appId given
                // create new record and save to database
//                DeviceRegister newDeviceRegister = new DeviceRegister();
//                newDeviceRegister.setAppId(appId);
//                newDeviceRegister.setUserId(userId);
//                newDeviceRegister.setDeviceUDID(deviceUDID);
//                adapter.registerDevice(newDeviceRegister);
                //return new record
                return null;
            }

        }

    }
}
