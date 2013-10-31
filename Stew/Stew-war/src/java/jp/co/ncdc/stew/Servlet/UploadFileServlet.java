/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jp.co.ncdc.stew.Servlet;

import com.google.gson.Gson;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import jp.co.ncdc.stew.Servlet.model.Status;
import jp.co.ncdc.stew.Utils.StewConfig;
import jp.co.ncdc.stew.Utils.StewConstant;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FileUtils;

/**
 *
 * @author vcnduong
 */
public class UploadFileServlet extends HttpServlet {

    Gson gson = new Gson();
    StewConfig stewConfig = StewConfig.getInstance();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        String result = "";
        Status status = new Status(StewConstant.STATUS_CODE_NOT_FOUND, "");        
        boolean isMultipartContent = ServletFileUpload.isMultipartContent(request);
        if (!isMultipartContent) {
            out.write(result);
            return;
        }
        FileItemFactory factory = new DiskFileItemFactory();
        ServletFileUpload upload = new ServletFileUpload(factory);
        List<FileItem> fields = null;
        try {
            fields = upload.parseRequest(request);
        } catch (FileUploadException ex) {
            Logger.getLogger(UploadFileServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
        Iterator<FileItem> it = fields.iterator();
        while (it.hasNext()) {
            FileItem fileItem = it.next();
            boolean isFormField = fileItem.isFormField();
            if (!isFormField) {
                String filePath = stewConfig.APNSCertificatePath;
                String pre = new Date().getTime() + "";
                String extension = "";
                int i = fileItem.getName().lastIndexOf('.');
                if (i > 0) {
                    extension = fileItem.getName().substring(i + 1);
                }
                String fileName = pre +"." + extension;            
                File fileToCreate = new File(filePath, fileName);
                try {
                    fileItem.write(fileToCreate);
                    status = new Status(StewConstant.STATUS_CODE_OK, fileName);       

                } catch (Exception ex) {
                    Logger.getLogger(UploadFileServlet.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        result = gson.toJson(status);
        out.write(result);
    }
}
