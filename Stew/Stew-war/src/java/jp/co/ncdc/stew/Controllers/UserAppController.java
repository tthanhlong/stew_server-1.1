/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jp.co.ncdc.stew.Controllers;

import java.util.ArrayList;
import java.util.List;
import jp.co.ncdc.stew.Adapters.GroupAdapter;
import jp.co.ncdc.stew.Adapters.UserAppAdapter;
import jp.co.ncdc.stew.Adapters.UserGroupDetailAdapter;
import jp.co.ncdc.stew.Entities.GroupUser;
import jp.co.ncdc.stew.Entities.User;
import jp.co.ncdc.stew.Entities.UserApp;
import jp.co.ncdc.stew.Entities.UserGroupDetail;
import jp.co.ncdc.stew.Servlet.model.UserAppModel;
import jp.co.ncdc.stew.Utils.StewUtils;

/**
 *
 * @author vcnduong
 */
public class UserAppController {

    UserAppAdapter userAppAdapter = new UserAppAdapter();
    UserGroupDetailAdapter userGroupDetailAdapter = new UserGroupDetailAdapter();
    GroupAdapter groupAdapter = new GroupAdapter();
    
    /**
     * 
     * @param appId
     * @return 
     */
    public List<UserAppModel> getUserOfAppAndGroup(String appId, String userId){
        List<UserAppModel>  result = new ArrayList<UserAppModel>();
        List<UserApp> listUserApps = userAppAdapter.getAllUserAppByAppId(appId);
        for(int i =0; i< listUserApps.size(); i++){
            List<String> groupNames = userGroupDetailAdapter.getGroupNameOfUser(userId);
            UserAppModel userAppModel= new UserAppModel(listUserApps.get(i), groupNames);
            result.add(userAppModel);
        }
        return result;
    }
}
