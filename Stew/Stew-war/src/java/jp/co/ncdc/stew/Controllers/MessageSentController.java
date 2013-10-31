/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jp.co.ncdc.stew.Controllers;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import jp.co.ncdc.stew.APIs.model.DataModel;
import jp.co.ncdc.stew.Adapters.MessageSentAdapter;
import jp.co.ncdc.stew.Entities.MessageSent;
import jp.co.ncdc.stew.Utils.StewConstant;

/**
 *
 * @author tquangthai
 */
public class MessageSentController {
    /**
     * get message with paging number
     * @param pageNumber
     * @return 
     */
    public DataModel getMessagesByPageNumber(int pageNumber){
        MessageSentAdapter messageAdapter = new MessageSentAdapter();
        DataModel result = new DataModel();
        List<MessageSent> lstAllMessages = messageAdapter.getMessages();
        int totalRecords = lstAllMessages.size();
        
        if (totalRecords > 0 && lstAllMessages != null) {
            int totalPage = totalRecords / StewConstant.ITEM_PER_PAGE_MANAGE + 1;
            if (totalRecords % StewConstant.ITEM_PER_PAGE_MANAGE == 0) {
                totalPage = totalPage - 1;
            }
            if (totalRecords <= StewConstant.ITEM_PER_PAGE_MANAGE) {
                result.setListItems(lstAllMessages);
            }else{
                List<MessageSent> lstMessages = new LinkedList<MessageSent>();
                int getMessagesFrom = pageNumber * StewConstant.ITEM_PER_PAGE_MANAGE;
                int getMessagesTo = (getMessagesFrom + StewConstant.ITEM_PER_PAGE_MANAGE) < totalRecords ? getMessagesFrom + StewConstant.ITEM_PER_PAGE_MANAGE : totalRecords;
                
                for (int i = getMessagesFrom; i < getMessagesTo; i++) {
                    lstMessages.add(lstAllMessages.get(i));
                }
                
                result.setListItems(lstMessages);
            }
            
            result.setStatus(StewConstant.STATUS_CODE_OK);
            result.setTotal(totalPage);
        }
        
        return result;
    }        
    
    /**
     * get message with paging number
     * @param pageNumber
     * @return 
     */
    public DataModel getLogMessagesByPageNumber(int pageNumber, Long messageId, List<Long> lstGroupId, String appId, boolean isSupperAdmin){
        MessageSentAdapter messageAdapter = new MessageSentAdapter();
        DataModel result = new DataModel();
        List<MessageSent> lstAllMessages;        
        lstAllMessages = new ArrayList<MessageSent>(messageAdapter.getLogMessageSent(messageId, lstGroupId, appId, isSupperAdmin).values());
        int totalRecords = lstAllMessages.size();
        
        if (totalRecords > 0 && lstAllMessages != null) {
            int totalPage = totalRecords / StewConstant.ITEM_PER_PAGE_MANAGE + 1;
            if (totalRecords % StewConstant.ITEM_PER_PAGE_MANAGE == 0) {
                totalPage = totalPage - 1;
            }
            if (totalRecords <= StewConstant.ITEM_PER_PAGE_MANAGE) {
                result.setListItems(lstAllMessages);
            }else{
                List<MessageSent> lstMessages = new LinkedList<MessageSent>();
                int getMessagesFrom = pageNumber * StewConstant.ITEM_PER_PAGE_MANAGE;
                int getMessagesTo = (getMessagesFrom + StewConstant.ITEM_PER_PAGE_MANAGE) < totalRecords ? getMessagesFrom + StewConstant.ITEM_PER_PAGE_MANAGE : totalRecords;
                
                for (int i = getMessagesFrom; i < getMessagesTo; i++) {
                    lstMessages.add(lstAllMessages.get(i));
                }
                
                result.setListItems(lstMessages);
            }
            
            result.setStatus(StewConstant.STATUS_CODE_OK);
            result.setTotal(totalPage);
        }
        
        return result;
    }
    
    /**
     * get message by id
     * @param messageId
     * @return 
     */
    public MessageSent getMessageByID(long id){
        MessageSentAdapter messageAdapter = new MessageSentAdapter();
        MessageSent result = messageAdapter.getMessageSentByMessageSentID(id);        
        return result;
    }
    
    /**
     * Get all messages sent from database
     * @return
     */
    public List<MessageSent> getAllMessages(){
        MessageSentAdapter messageAdapter = new MessageSentAdapter();
        List<MessageSent> lstAllMessages = messageAdapter.getMessages();
        return  lstAllMessages;
    }
    
    public Map<String, MessageSent> getLogMessageSent(Long messageId, List<Long> lstGroupId, String appId, boolean isSupperAdmin){
        MessageSentAdapter messageSentAdapter = new MessageSentAdapter();
        return messageSentAdapter.getLogMessageSent(messageId, lstGroupId, appId, isSupperAdmin);
    }
    
    /**
     * get message with paging number
     * @param pageNumber
     * @param messageId
     * @param lstGroupId
     * @param appId
     * @param isSupperAdmin
     * @param status
     * @param keySearch
     * @return 
     */
    public DataModel getLogMessagesFilterByPageNum(int pageNumber, Long messageId, List<Long> lstGroupId, String appId, boolean isSupperAdmin, String status, String keySearch){
        MessageSentAdapter messageAdapter = new MessageSentAdapter();
        DataModel result = new DataModel();
        List<MessageSent> lstAllMessages;        
        lstAllMessages = new ArrayList<MessageSent>(messageAdapter.getLogMessageSentFilter(messageId, lstGroupId, appId, isSupperAdmin, status, keySearch).values());
        int totalRecords = lstAllMessages.size();
        
        if (totalRecords > 0 && lstAllMessages != null) {
            int totalPage = totalRecords / StewConstant.ITEM_PER_PAGE_MANAGE + 1;
            if (totalRecords % StewConstant.ITEM_PER_PAGE_MANAGE == 0) {
                totalPage = totalPage - 1;
            }
            if (totalRecords <= StewConstant.ITEM_PER_PAGE_MANAGE) {
                result.setListItems(lstAllMessages);
            }else{
                List<MessageSent> lstMessages = new LinkedList<MessageSent>();
                int getMessagesFrom = pageNumber * StewConstant.ITEM_PER_PAGE_MANAGE;
                int getMessagesTo = (getMessagesFrom + StewConstant.ITEM_PER_PAGE_MANAGE) < totalRecords ? getMessagesFrom + StewConstant.ITEM_PER_PAGE_MANAGE : totalRecords;
                
                for (int i = getMessagesFrom; i < getMessagesTo; i++) {
                    lstMessages.add(lstAllMessages.get(i));
                }
                
                result.setListItems(lstMessages);
            }
            
            result.setStatus(StewConstant.STATUS_CODE_OK);
            result.setTotal(totalPage);
        }
        
        return result;
    }    
}
