package jp.co.ncdc.stew.Utils;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author tthanhlong
 */
public class StewConstant {
    //public static final int DEFAULT_AGE_TOKEN = 30;

    public static final String OK_STATUS = "OK";
    public static final String FIRST = "FIRST";
    public static final String ERR_STATUS = "error";
    public static final String ERR_REQUIRED = "Input parameters are required";
    public static final String ERR_USER_NOT_EXIST = "User is not existed";
    public static final String ERR_SAVE_USERSESSION = "Problem occurs during saving user session.";
    public static final String ERR_DEL_USERSESSION = "Problem occurs during deletion user session.";
    public static final String ERR_TOKEN_NOT_EXIST = "Token is not existed";
    public static final String ERR_APPID_NOT_EXIST = "The App with id given is not existed";
    public static final String ERR_EXPIRE_TOKEN = "The token is expired";
    public static final String ERR_DEVICE_ALREADY_REGISTERED = "The device already registered for this device and app";
    public static final String ERR_DEVICE_TOKEN_INUSE = "The device token already registered by another device";
    public static final String ERR_SAVE_DEVICEREGISTER = "Problem occurs during saving device register.";
    public static final String DEVICE_NOTYET_REGISTER = "Please register you device before login.";
    public static final String ERR_DATABASE_NAME_EMPTY = "App database not defined.";
    public static final String TRACK_TABLE_EMPTY = "This table does not change.";
    public static final String ERR_TABLETRACK_NULL = "Can not get table tracking change of records";
    public static final String ERR_NOT_EXIST_RECORD = "Not exist any data with that token";
    public static final String ERR_LIST_RECORD_EMPTY = "Requested table is empty or does not exist";
    public static final int ERR_LOG_TYPE = 1;
    public static final int EVENT_LOG_TYPE = 2;
    public static final String ERR_APPID_DOES_NOT_MATCH = "AppID and UserID do not match";
    public static final int STATUS_CODE_NOT_FOUND = 404;
    public static final int EXISTED_USER = 1000;
    public static final int STATUS_CODE_OK = 200;
    public static final int ITEM_PER_PAGE_MANAGE = 10;
    public static final long DEFAULT_ROLE = 1;
    public static final int CREATE = 1;
    public static final int EDIT = 2;
    public static final int DELETE = 3;
    public static final int DELETE_USER_GROUP = 4;
    public static final int SEND_MESSAGE = 5;
    public static final int LOGIN = 6;
    public static final int LOGOUT = 7;
    public static final int GET_LIST = 8;
    public static final int GET_ROLE_BY_USER_AND_GROUP = 9;
    public static final int GET_USER_BYGROUP = 10;
    public static final int VIEW = 11;
    public static final int GET_ALL_LIST = 12;
    public static final int EXISTED_MESSAGE = 1000;
    public static final String WAITING_SEND = "Waiting";
    public static final String SUCCESS_SEND = "Success";
    public static final String FAIL_SEND = "Failure";
    public static final String DATE_FORMAT = "yyyy.MM.dd";
    public static final String REQUIRE_PUSH = "true";
    public static final String NO_PUSH = "false";
    public static final String APP_ID = "clientapp";
    public static final String BLOB_TYPE = "blob";
    public static final String MEDIUM_BLOB_TYPE = "mediumblob";
    public static final String VARCHAR_TYPE = "varchar";
    public static final String TEXT_TYPE = "text";
    public static final String BIGINT_TYPE = "bigint";
    public static final String UTF_8_COLLATION = " COLLATE utf8_unicode_ci DEFAULT NULL,";
    public static final String PRIMARY_KEY_ATTRIBUTE = "varchar(255) COLLATE utf8_unicode_ci NOT NULL,";
    public static final String NCDC_STEW_ID = "NCDC_STEW_USER_ID";
    //public static final String NCDC_STEW_ID = "userID";
    public static final String LAST_UPDATE = "LastUpdate";
    public static final String INSERT_TYPE = "I";
    public static final String UPDATE_TYPE = "U";
    public static final String DELETE_TYPE = "D";
    public static final long ROLE_SUPPER = 1;
    public static final long ROLE_ADMIN = 2;
    public static final long ROLE_MANAGER = 3;
    public static final long ROLE_USER = 4;
    public static final String USER_SESSION = "UserSession";
    public static final String IS_SUPPER = "isSupperAdmin";
    //Error code
    //login
    public static final String ERR_WRONG_ROLE = "ERR001";
    public static final String ERR_WRONG_PASS = "ERR002";
    public static final String ERR_WRONG_USER = "ERR003";
    public static final String ERR_EXIST_APP = "ERR004";
    public static final String ERR_NOT_EXIST_APP = "ERR005";
    public static final String ERR_NOT_EXIST_TABLE = "Table does not exist";
//    public static final int ITEM_PER_PAGE = 10;
    public static final int SEND_NOW = 1;
    public static final int SEND_AFTER = 2;
    public static final String ERR_LOG_TYPE_STRING = "ERROR";
    public static final String EVENT_LOG_TYPE_STRING = "MONITOR";
    public static final String CERT_EXT = ".p12";
    public static final String ACTIVE = "Active";
    public static final String ERR_CREATE_APP_CLIENT = "Cannot create app";
    public static final String ERR_POST_APP_CLIENT = "Cannot post data";
    public static final String ERR_TOKEN_EXPIRE = "Token is expire";
    
    public static final int DELETE_USER_BY_EMAIL = 13;
    public static final int GET_ALL_DISTINCT_LIST = 14;
    public static final int GET_LIST_BY_GROUP_AND_KEY = 15;
    public static final int REMOVE_USER_FROM_GROUP = 16;
    public static final int GET_ROLE_OF_GROUP = 17;
    public static final int GET_REMAINING_USERS = 18;
    public static final int GET_ROLE_USER_OF_GROUP = 19;
    
    public static final int DELETE_TEMPLATE_BY_ID = 20;
    public static final int DOWNLOAD_LOG_CSV = 21;
    public static final int ADD_USER_TO_GROUP = 22;
}
