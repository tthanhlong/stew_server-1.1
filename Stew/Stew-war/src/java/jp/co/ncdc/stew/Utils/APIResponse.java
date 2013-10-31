/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jp.co.ncdc.stew.Utils;

import jp.co.ncdc.stew.APIs.model.Authentication;
import jp.co.ncdc.stew.APIs.model.Status;

/**
 *
 * @author tthanhlong
 */
public class APIResponse {
    
    /**
     * @brief API response defined for authentication
     * @param status
     * @param userToken
     * @param description
     * @return 
     */
    public Authentication authenticateResponse(String status, String userToken, String description){
        Authentication result = new Authentication();
        
        if (status == null ? StewConstant.OK_STATUS == null : status.equals(StewConstant.OK_STATUS)) {
            result.setStatus(status);
            result.setUserTokens(userToken);            
        }else if(status == null ? StewConstant.ERR_STATUS == null : status.equals(StewConstant.ERR_STATUS)){
            result.setStatus(status);
            result.setDescription(description);
        }
        
        return result;
    }

    /**
     * @brief API response defined for logging
     * @param status
     * @param description
     * @return 
     */
    public Status loggingResponse(String status, String description) {
        Status result = new Status();
        
        if (status == null ? StewConstant.ERR_STATUS == null : status.equals(StewConstant.ERR_STATUS)) {
            result.setStatus(status);
            result.setDescription(description);
        }else if(status == null ? StewConstant.OK_STATUS == null : status.equals(StewConstant.OK_STATUS)){
            result.setStatus(status);
        }
        
        return result;
    }
}
