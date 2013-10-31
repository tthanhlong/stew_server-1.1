/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jp.co.ncdc.stew.Controllers;

import java.util.LinkedList;
import java.util.List;
import jp.co.ncdc.stew.APIs.model.DataModel;
import jp.co.ncdc.stew.Adapters.MessageAdapter;
import jp.co.ncdc.stew.Entities.Message;
import jp.co.ncdc.stew.Utils.StewConstant;

/**
 *
 * @author tquangthai
 */
public class MessageController {

    MessageAdapter messageController = new MessageAdapter();
    /**
     * get message with paging number, userId, manager role
     * @param pageNumber
     * @param userId
     * @param isSupperAdmin
     * @return 
     */
    public DataModel getMessagesByPageNumber(int pageNumber, String userId, boolean isSupperAdmin){
        MessageAdapter messageAdapter = new MessageAdapter();
        DataModel result = new DataModel();
        List<Message> lstAllMessages = messageAdapter.getMessages(userId, isSupperAdmin);
        int totalRecords = lstAllMessages.size();
        
        if (totalRecords > 0 && lstAllMessages != null) {
            int totalPage = totalRecords / StewConstant.ITEM_PER_PAGE_MANAGE + 1;
            if (totalRecords % StewConstant.ITEM_PER_PAGE_MANAGE == 0) {
                totalPage = totalPage - 1;
            }
            if (totalRecords <= StewConstant.ITEM_PER_PAGE_MANAGE) {
                result.setListItems(lstAllMessages);
            }else{
                List<Message> lstMessages = new LinkedList<Message>();
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
     * get message by title
     * @param title
     * @return 
     */
    public Message getMessageByTitle(String title){
        MessageAdapter messageAdapter = new MessageAdapter();
        Message result = messageAdapter.getMessageByTitle(title);        
        return result;
    }
    
    /**
     * get message by id
     * @param messageId
     * @return 
     */
    public Message getMessageByID(long id){
        MessageAdapter messageAdapter = new MessageAdapter();
        Message result = messageAdapter.getMessageByMessageID(id);        
        return result;
    }
    
    /**
     * get message by managerId
     * @param userId
     * @param isSupperAdmin
     * @return 
     */
    public List<Message> getMessages(String userId, boolean isSupperAdmin) {
        MessageAdapter messageAdapter = new MessageAdapter();
        List<Message> lstMessages = messageAdapter.getMessages(userId, isSupperAdmin);        
        return lstMessages;
    }
    
    /**
     * Get all messages from database
     * @return
     */
    public List<Message> getAllMessages(){
        MessageAdapter messageAdapter = new MessageAdapter();
        List<Message> lstAllMessages = messageAdapter.getMessages();
        return  lstAllMessages;
    }
    
    /**
     * get message with paging number, userId, manager role
     * @param pageNumber
     * @param userId
     * @param isSupperAdmin
     * @return 
     */
    public DataModel getMessagesFilterByPageNum(int pageNum, String userId, boolean isSupperAdmin, String appId, String templateName, String keySearch){
        MessageAdapter messageAdapter = new MessageAdapter();
        DataModel result = new DataModel();
        List<Message> lstAllMessages = messageAdapter.getMessagesFilter(userId, isSupperAdmin, appId, templateName, keySearch);
        int totalRecords = lstAllMessages.size();
        
        if (totalRecords > 0 && lstAllMessages != null) {
            int totalPage = totalRecords / StewConstant.ITEM_PER_PAGE_MANAGE + 1;
            if (totalRecords % StewConstant.ITEM_PER_PAGE_MANAGE == 0) {
                totalPage = totalPage - 1;
            }
            if (totalRecords <= StewConstant.ITEM_PER_PAGE_MANAGE) {
                result.setListItems(lstAllMessages);
            }else{
                List<Message> lstMessages = new LinkedList<Message>();
                int getMessagesFrom = pageNum * StewConstant.ITEM_PER_PAGE_MANAGE;
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
