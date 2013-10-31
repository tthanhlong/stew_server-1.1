/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jp.co.ncdc.stew.APIs;

import java.sql.SQLException;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import jp.co.ncdc.stew.APIs.model.Authentication;
import jp.co.ncdc.stew.APIs.model.CheckAppModel;
import jp.co.ncdc.stew.APIs.model.ClientAppData;
import jp.co.ncdc.stew.APIs.model.LogLevel;
import jp.co.ncdc.stew.APIs.model.Logging;
import jp.co.ncdc.stew.APIs.model.Login;
import jp.co.ncdc.stew.APIs.model.Logout;
import jp.co.ncdc.stew.APIs.model.PostDataModel;
import jp.co.ncdc.stew.APIs.model.RegisterInfo;
import jp.co.ncdc.stew.APIs.model.Status;
import jp.co.ncdc.stew.APIs.model.TableGetDataRequest;
import jp.co.ncdc.stew.APIs.model.TableGetDataResponse;
import jp.co.ncdc.stew.Controllers.AppsController;
import jp.co.ncdc.stew.Controllers.DataSyncTrackingControllers;
import jp.co.ncdc.stew.Controllers.DeviceRegisterController;
import jp.co.ncdc.stew.Controllers.LoggingController;
import jp.co.ncdc.stew.Controllers.UserController;
import jp.co.ncdc.stew.Utils.APIResponse;
import jp.co.ncdc.stew.Utils.StewConfig;
import jp.co.ncdc.stew.Utils.StewConstant;

/**
 * @name: StewService.java
 * @create: Aug 21, 2013
 * @version 1.0
 * @brief: This class is implementation for APIs
 */
@Path("/api")
public class StewService {

    /**
     * @brief: This is Authentication web service
     * @param username
     * @param password
     * @return
     */
    @POST
    @Path("/Login")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response login(Login loginInfo) {
        if (loginInfo != null) {
            Authentication result = new UserController().authenticateUser(loginInfo.getUsername(), loginInfo.getPassword(), loginInfo.getAppId(), loginInfo.getDeviceUDID(), loginInfo.getIsPush());
            return Response.status(StewConstant.STATUS_CODE_OK).entity(result).build();
        } else {
            return Response.status(StewConstant.STATUS_CODE_NOT_FOUND).entity("Request not found").build();
        }
    }

    /**
     * @brief: This is Authentication web service
     * @param username
     * @param password
     * @return
     */
    @POST
    @Path("/Logout")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response logout(Logout logout) {
        if (logout != null) {
            Status result = new UserController().logoutAndRemoveUserSession(logout.getToken());
            return Response.status(StewConstant.STATUS_CODE_OK).entity(result).build();
        } else {
            return Response.status(StewConstant.STATUS_CODE_NOT_FOUND).entity("Request not found").build();
        }
    }

    /**
     * @brief: This is Logging web service
     * @param token
     * @param typeString
     * @param message
     * @return
     */
    @POST
    @Path("/Log")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response log(Logging logInfo) {
        if (logInfo != null) {
            Status result = new LoggingController().logMessage(logInfo.getToken(), logInfo.getType(), logInfo.getMessage(), logInfo.getAppId(), logInfo.getDeviceUDID());
            return Response.status(StewConstant.STATUS_CODE_OK).entity(result).build();
        } else {
            return Response.status(StewConstant.STATUS_CODE_NOT_FOUND).entity("Request not found").build();
        }
    }

    /**
     * @brief: This is device registration
     * @param registerInfo
     * @return
     */
    @POST
    @Path("/DeviceRegistration")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response registerDevice(RegisterInfo registerInfo) {
        if (registerInfo != null) {
            Status result = new DeviceRegisterController().registerDevice(registerInfo.getDeviceToken(), registerInfo.getAppId(), registerInfo.getDeviceUDID());
            return Response.status(StewConstant.STATUS_CODE_OK).entity(result).build();
        } else {
            return Response.status(StewConstant.STATUS_CODE_NOT_FOUND).entity("Request not found").build();
        }
    }

    /**
     * @brief: This is API for get log level
     * @return
     */
    @POST
    @Path("/GetLogLevel")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getLogLevel() {
        LogLevel result = new LogLevel();
        result.setStatus(StewConstant.OK_STATUS);
        result.setLogLevel(StewConfig.getInstance().logLevel);
        return Response.status(StewConstant.STATUS_CODE_OK).entity(result).build();
    }

    /**
     *
     * @param deviceToken
     * @return
     */
    @GET
    @Path("/getTableNameForSync/{token}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getTableNameForSync(@PathParam("token") String userToken) {
        if (userToken != null) {
            Status result = new DataSyncTrackingControllers().getTableNameForSync(userToken);
            return Response.status(StewConstant.STATUS_CODE_OK).entity(result).build();
        } else {
            return Response.status(StewConstant.STATUS_CODE_NOT_FOUND).entity("Request not found").build();
        }
    }

    /**
     * @brief: Create app data web service
     * @param clientAppData
     * @return
     * @throws SQLException
     */
    @POST
    @Path("/CreateAppData")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createAppData(ClientAppData clientAppData) throws SQLException {
        if (clientAppData != null) {
            AppsController appsController = new AppsController();
            Status result = appsController.createDatabase(clientAppData);
            return Response.status(StewConstant.STATUS_CODE_OK).entity(result).build();
        } else {
            return Response.status(StewConstant.STATUS_CODE_NOT_FOUND).entity("Request not found").build();
        }
    }

    /**
     * @brief API get data for table
     * @param tableSyncRequest
     * @return
     */
    @POST
    @Path("/GetDataForTable")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getDataForTable(TableGetDataRequest request) throws SQLException {
        if (request != null) {
            AppsController appsController = new AppsController();
            TableGetDataResponse result = appsController.getDataForTable(request.getToken(),request.getTableName(),request.getMaxRecord(),request.getPrimaryKey(), false);
            return Response.status(StewConstant.STATUS_CODE_OK).entity(result).build();
        } else {
            return Response.status(StewConstant.STATUS_CODE_NOT_FOUND).entity("Request not found").build();
        }
    }
    
    /**
     * @brief API get all data for table if the first time sync
     * @param tableSyncRequest
     * @return
     */
    @POST
    @Path("/GetAllDataForTable")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllDataForTable(TableGetDataRequest request) throws SQLException {
        if (request != null) {
            AppsController appsController = new AppsController();
            TableGetDataResponse result = appsController.getDataForTable(request.getToken(),request.getTableName(),request.getMaxRecord(),request.getPrimaryKey(), true);
            return Response.status(StewConstant.STATUS_CODE_OK).entity(result).build();
        } else {
            return Response.status(StewConstant.STATUS_CODE_NOT_FOUND).entity("Request not found").build();
        }
    }
    
    /**
     * This is API post data to table
     * @param postModel
     * @return
     * @throws SQLException 
     */
    @POST
    @Path("/PostDataForTable")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response postDataForTable(PostDataModel postModel) throws SQLException {
        if (postModel != null) {
            AppsController appsController = new AppsController();
            Status result = appsController.postDataToTable(postModel);
            return Response.status(StewConstant.STATUS_CODE_OK).entity(result).build();
        } else {
            return Response.status(StewConstant.STATUS_CODE_NOT_FOUND).entity("Request not found").build();
        }
    }
    /**
     * This is API post data to table
     * @param postModel
     * @return
     * @throws SQLException 
     */
    @POST
    @Path("/CheckAppData")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response checkAppData(CheckAppModel checkAppModel){
        Status result = null;
        AppsController appsController = new AppsController();
        if (checkAppModel != null && !checkAppModel.equals("")) {
            String token = checkAppModel.getToken();
            if (appsController.checkAppDataExist(token)) {
                result = new APIResponse().loggingResponse(StewConstant.ERR_STATUS, "");
            } else {
                result = new APIResponse().loggingResponse(StewConstant.OK_STATUS, "");
            }
        } else {
            result = new APIResponse().loggingResponse(StewConstant.ERR_STATUS, "Request not found");
        }
        
        return Response.status(StewConstant.STATUS_CODE_OK).entity(result).build();
    }
}