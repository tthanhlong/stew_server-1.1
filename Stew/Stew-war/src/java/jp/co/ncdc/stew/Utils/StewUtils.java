/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jp.co.ncdc.stew.Utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 *
 * @author tthanhlong
 */
public class StewUtils {

    private static StewUtils instance;

    /**
     *
     * @return
     */
    public static StewUtils getInstance() {
        if (instance == null) {
            instance = new StewUtils();
        }
        return instance;
    }

    /**
     *
     * @param value
     * @return
     * @throws NoSuchAlgorithmException
     */
    public static String SHA1Encrypt(String value) throws NoSuchAlgorithmException {

        MessageDigest mDigest = MessageDigest.getInstance("SHA1");
        byte[] result = mDigest.digest(value.getBytes());
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < result.length; i++) {
            sb.append(Integer.toString((result[i] & 0xff) + 0x100, 16).substring(1));
        }

        return sb.toString();
    }

    /**
     *
     * @return
     */
    public static synchronized String getUUID() {
        String uuid = UUID.randomUUID().toString();
        return findAndReplace(uuid, "-", "");
    }

    /**
     *
     * @param orig
     * @param sub
     * @param rep
     * @return
     */
    public static String findAndReplace(String orig, String sub, String rep) {

        StringBuffer out = new StringBuffer();
        int index = 0;
        int oldIndex = index;
        while (index != -1) {
            index = orig.indexOf(sub, index);
            if (index != -1) {
                out.append(orig.substring(oldIndex, index));
                index += sub.length();
                oldIndex = index;
                out.append(rep);
            } else {
                out.append(orig.substring(oldIndex));
            }
        }

        return out.toString();
    }

    /**
     *
     * @return
     */
    public static Long getUniqueID() {
        Date now = new Date();
        return now.getTime();
    }

    /**
     * get total page number
     *
     *
     * @author vcnduong
     * @param itemPerPage
     * @return
     */
    public static int getTotalPage(int itemPerPage, int allItem) {
        int result = 0;
        if ((allItem % itemPerPage) == 0) {
            result = allItem / itemPerPage;
        } else {
            result = ((int) allItem / itemPerPage) + 1;
        }
        return result;
    }

    /**
     *
     * @param date
     * @param format
     * @return
     */
    public static String convertDateToString(Date date, String format) {
        if (date != null) {
            DateFormat df = new SimpleDateFormat(format);
            return df.format(date);
        } else {
            return "";
        }
    }

    /**
     *
     * @param list
     * @return
     */
    public static String convertListStringToString(List<String> list) {
        String result = "";
        for (int i = 0; i < list.size(); i++) {
            result += list.get(i);
            if (i != list.size() - 1) {
                result += ",";
            }
        }
        return result;
    }

    /**
     * 
     * @param list
     * @param s
     * @return 
     */
    public static boolean isListContainStr(List<String> list, String s) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).equals(s)) {
                return true;
            }
        }
        return false;
    }
}
