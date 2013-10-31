/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jp.co.ncdc.stew.Controllers;

import java.util.Date;
import jp.co.ncdc.stew.APIs.model.Authentication;
import jp.co.ncdc.stew.Adapters.UserAppAdapter;
import jp.co.ncdc.stew.Adapters.UserSessionAdapter;
import jp.co.ncdc.stew.Entities.UserApp;
import jp.co.ncdc.stew.Entities.UserSession;
import jp.co.ncdc.stew.Managers.DataChangeTrackingManager;
import jp.co.ncdc.stew.Utils.APIResponse;
import jp.co.ncdc.stew.Utils.StewConfig;
import jp.co.ncdc.stew.Utils.StewConstant;
import jp.co.ncdc.stew.Utils.StewUtils;

/**
 *
 * @author vcnduong
 */
public class UserSessionController {
    UserAppAdapter userAppAdapter = new UserAppAdapter();

    /**
     * create new userToken
     *
     * @param appId
     * @param deviceUDID
     * @param userId
     * @return
     */
    public Authentication createUserSession(String appId, String deviceUDID, String userId) {
        Authentication result = new Authentication();
        UserSession userSession = new UserSession();
        userSession.setAppId(appId);
        userSession.setDeviceUDID(deviceUDID);
        userSession.setUserId(userId);
        Date now = new Date();
        userSession.setLoginDate(now);

        //set default token age
        int defaultTokenAge = StewConfig.getInstance().defaulTokenAge;
        long newExpiryDateLong = now.getTime() + defaultTokenAge * 3600 * 1000;
        userSession.setTokenExpireDate(new Date(newExpiryDateLong));

        //generate token
        String newToken = StewUtils.getUUID();
        //set token to user session
        userSession.setUserToken(newToken);
        //add user session to database
        UserSessionAdapter adapter = new UserSessionAdapter();
        if (adapter.addUserSession(userSession)) {
            result = new APIResponse().authenticateResponse(StewConstant.OK_STATUS, userSession.getUserToken(), "");
            //create singerton list tracking
            DataChangeTrackingManager.getInstance().UpdateDeviceRegister(appId, deviceUDID, userId);
        } else {
            result = new APIResponse().authenticateResponse(StewConstant.ERR_STATUS, "", StewConstant.ERR_SAVE_USERSESSION);
        }
        if(userAppAdapter.getAllUserAppByAppIdAndUserId(appId, userId) == null){
            UserApp userApp = new UserApp();
            userApp.setAppId(appId);
            userApp.setUserId(userId);
            userApp.setStatus(StewConstant.ACTIVE);
            userApp.setDateModified(now);
            userAppAdapter.addUserApp(userApp);
        }
        return result;
    }
}
