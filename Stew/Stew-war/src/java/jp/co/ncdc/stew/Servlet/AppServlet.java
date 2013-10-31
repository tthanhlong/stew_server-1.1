/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jp.co.ncdc.stew.Servlet;

import com.google.gson.Gson;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import jp.co.ncdc.stew.Adapters.AppsAdapter;
import jp.co.ncdc.stew.Adapters.GroupAppDetailAdapter;
import jp.co.ncdc.stew.Adapters.UserGroupDetailAdapter;
import jp.co.ncdc.stew.Controllers.UserAppController;
import jp.co.ncdc.stew.Entities.Apps;
import jp.co.ncdc.stew.Entities.GroupAppDetail;
import jp.co.ncdc.stew.Entities.User;
import jp.co.ncdc.stew.Servlet.model.GroupApp;
import jp.co.ncdc.stew.Servlet.model.ServletModel;
import jp.co.ncdc.stew.Servlet.model.Status;
import jp.co.ncdc.stew.Servlet.model.UserAppModel;
import jp.co.ncdc.stew.Utils.StewConfig;
import jp.co.ncdc.stew.Utils.StewConstant;
import jp.co.ncdc.stew.Utils.StewUtils;

/**
 *
 * @author vcnduong
 */
public class AppServlet extends HttpServlet {

    Gson gson = new Gson();
    UserGroupDetailAdapter userGroupDetailAdapter = new UserGroupDetailAdapter();
    GroupAppDetailAdapter groupAppDetailAdapter = new GroupAppDetailAdapter();
    UserAppController userAppController = new UserAppController();
    AppsAdapter adapter = new AppsAdapter();
    StewConfig stewConfig = StewConfig.getInstance();

    /**
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        String result = "";

        String typeString = request.getParameter("type");
        String pageStr = request.getParameter("page");
        String email = request.getParameter("userId");
        HttpSession session = request.getSession();
        User a = (User) session.getAttribute(StewConstant.USER_SESSION);
        if (a != null && email.equals(a.getEmail())) {
            int type = 0;
            int page = 0;
            try {
                type = Integer.parseInt(typeString);
            } catch (Exception e) {
            }
            try {
                page = Integer.parseInt(pageStr);
            } catch (Exception e) {
            }
            switch (type) {
                case StewConstant.GET_LIST:
                    result = getListAppByUser(email, page);
                    break;
                default:
                    break;
            }
        } else {
            Status status = new Status(StewConstant.STATUS_CODE_NOT_FOUND, StewConstant.ERR_WRONG_USER);
            result = gson.toJson(status);
        }
        out.write(result);
    }

    /**
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        String result = "";
        String typeString = request.getParameter("type");
        String appName = request.getParameter("appName");
        String appId = request.getParameter("appId");
        String pathCert = request.getParameter("pathCert");
        String passCert = request.getParameter("passCert");
        String email = request.getParameter("userId");
        String groups = request.getParameter("groups");
        String pageStr = request.getParameter("page");
        HttpSession session = request.getSession();
        User a = (User) session.getAttribute(StewConstant.USER_SESSION);
        Apps apps = adapter.getAppByAppID(appId);
        Status status = new Status(StewConstant.STATUS_CODE_NOT_FOUND, "");
        if (a != null && email.equals(a.getEmail())) {
            int type = 0;
            int page = 0;
            try {
                type = Integer.parseInt(typeString);
            } catch (Exception e) {
            }
            try {
                page = Integer.parseInt(pageStr);
            } catch (Exception e) {
            }
            switch (type) {
                case StewConstant.CREATE:
                    if (apps == null) {
                        status = createApps(appId, appName, pathCert, passCert, email, groups);
                    } else {
                        status = new Status(StewConstant.STATUS_CODE_NOT_FOUND, StewConstant.ERR_EXIST_APP);
                    }
                    result = gson.toJson(status);
                    break;
                case StewConstant.EDIT:
                    if (apps != null) {
                        String oldPath = stewConfig.APNSCertificatePath + apps.getCertificatePath();
                        apps.setAppName(appName);
                        apps.setCertificatePath(pathCert);
                        apps.setCertificatePassword(passCert);
                        status = editApps(apps, groups, oldPath);
                    } else {
                        status = new Status(StewConstant.STATUS_CODE_NOT_FOUND, StewConstant.ERR_NOT_EXIST_APP);
                    }
                    result = gson.toJson(status);
                    break;
                case StewConstant.VIEW:
                    result = getUserApp(appId, email, page);
                    break;
                case StewConstant.DELETE:
                    result = deleteApp();
                    break;
                default:
                    break;
            }
        } else {
            status = new Status(StewConstant.STATUS_CODE_NOT_FOUND, StewConstant.ERR_WRONG_USER);
        }
        out.write(result);
    }

    /**
     * get list app by user login
     *
     * @param email
     * @param page
     * @return
     */
    private String getListAppByUser(String email, int page) {
        String result;
        ServletModel servletModel = new ServletModel();
        List<GroupApp> listApps = new ArrayList<GroupApp>();
        servletModel.setTotal(0);
        servletModel.setLstApp(null);
        servletModel.setStatus(StewConstant.STATUS_CODE_NOT_FOUND);
        if (email != null && !"".equals(email)) {
            List<Apps> apps = null;
            if (userGroupDetailAdapter.checkUserIsSupperAdmin(email)) {
                apps = adapter.getAllApps();
            } else {
                apps = adapter.getAppByAdmin(email);
            }
            int from = StewConstant.ITEM_PER_PAGE_MANAGE * page;
            int to = Math.min(apps.size(), StewConstant.ITEM_PER_PAGE_MANAGE * (page + 1));
            int total = StewUtils.getTotalPage(StewConstant.ITEM_PER_PAGE_MANAGE, apps.size());
            servletModel.setStatus(StewConstant.STATUS_CODE_OK);
            servletModel.setTotal(total);
            listApps = adapter.getGroupAppOfApps(apps.subList(from, to), email);
            servletModel.setLstApp(listApps);
        }
        result = gson.toJson(servletModel);
        return result;
    }

    /**
     *
     * @param appId
     * @param name
     * @param certPath
     * @param CertPass
     * @param group
     * @return
     */
    public Status createApps(String appId, String name, String certPath, String CertPass, String email, String groups) {
        Status status;
        String pre = "_" + new Date().getTime();
        String newCertPath = appId + pre + StewConstant.CERT_EXT;
        File oldfile = new File(stewConfig.APNSCertificatePath + certPath);
        File newfile = new File(stewConfig.APNSCertificatePath + newCertPath);
        Apps apps;
        if (oldfile.renameTo(newfile)) {
            apps = new Apps(appId, name, newCertPath, CertPass);
        } else {
            apps = new Apps(appId, name, certPath, CertPass);
        }
        apps.setCreateDate(new Date());
        apps.setAdminId(email);
        if (adapter.addApps(apps)) {
            addGroupAppByApp(groups, appId);
            status = new Status(StewConstant.STATUS_CODE_OK, "");
        } else {
            status = new Status(StewConstant.STATUS_CODE_NOT_FOUND, "");
        }
        return status;
    }

    /**
     *
     * @param appId
     * @param name
     * @param certPath
     * @param CertPass
     * @param groups
     * @return
     */
    public Status editApps(Apps apps, String groups, String oldPath) {
        Status status;
        if (adapter.updateApps(apps)) {
            groupAppDetailAdapter.deleteGroupAppDetailByApp(apps.getAppId());
            addGroupAppByApp(groups, apps.getAppId());
            status = new Status(StewConstant.STATUS_CODE_OK, "");
            File oldfile = new File(oldPath);
            oldfile.delete();
        } else {
            status = new Status(StewConstant.STATUS_CODE_NOT_FOUND, "");
        }
        return status;
    }

    /**
     *
     * @param groups
     * @param appId
     * @return
     */
    private boolean addGroupAppByApp(String groups, String appId) {
        if (groups != null) {
            String[] groupArr = groups.split(",");
            for (int i = 0; i < groupArr.length; i++) {
                if (!"".equals(groupArr[i])) {
                    Long groupId = new Long(0);
                    try {
                        groupId = Long.parseLong(groupArr[i]);
                        GroupAppDetail groupApp = new GroupAppDetail(appId, groupId);
                        groupAppDetailAdapter.addGroupAppDetail(groupApp);
                    } catch (Exception e) {
                        return false;
                    }
                }
            }
        }
        return false;
    }

    /**
     *
     * @param appId
     * @param email
     * @param page
     * @return
     */
    private String getUserApp(String appId, String email, int page) {
        String result;
        ServletModel servletModel = new ServletModel();
        servletModel.setTotal(0);
        servletModel.setLstUserApp(null);
        servletModel.setStatus(StewConstant.STATUS_CODE_NOT_FOUND);
        if (email != null && !"".equals(email)) {
            List<UserAppModel> listUserAppModels = userAppController.getUserOfAppAndGroup(appId, email);
            int from = StewConstant.ITEM_PER_PAGE_MANAGE * page;
            int to = Math.min(listUserAppModels.size(), StewConstant.ITEM_PER_PAGE_MANAGE * (page + 1));
            int total = StewUtils.getTotalPage(StewConstant.ITEM_PER_PAGE_MANAGE, listUserAppModels.size());
            servletModel.setStatus(StewConstant.STATUS_CODE_OK);
            servletModel.setTotal(total);
            servletModel.setLstUserApp(listUserAppModels.subList(from, to));
        }
        result = gson.toJson(servletModel);
        return result;
    }
    
    /**
     *
     * @param name
     * @param description
     * @return
     */
    public String deleteApp() {
        String result;
        Status status= new Status(StewConstant.STATUS_CODE_NOT_FOUND, "");        
        result = gson.toJson(status);
        return result;
    }
}
