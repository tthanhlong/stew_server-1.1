/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package jp.co.ncdc.stew.Controllers;

import java.util.LinkedList;
import java.util.List;
import jp.co.ncdc.stew.APIs.model.DataModel;
import jp.co.ncdc.stew.Adapters.MessageTemplateAdapter;
import jp.co.ncdc.stew.Entities.MessageTemplate;
import jp.co.ncdc.stew.Utils.StewConstant;

/**
 * @name: MessageTemplateController.java
 * @create: Aug 30, 2013
 * @version 1.0
 * @brief: This class is implementation for MessageTemplate Controller
*/
public class MessageTemplateController {
    MessageTemplateAdapter messageController = new MessageTemplateAdapter();
    /**
     * get message with paging number
     * @param pageNumber
     * @return 
     */
    public DataModel getMessagesByPageNumber(int pageNumber){
        MessageTemplateAdapter messageAdapter = new MessageTemplateAdapter();
        DataModel result = new DataModel();
        List<MessageTemplate> lstAllMessages = messageAdapter.getMessages();
        int totalRecords = lstAllMessages.size();
        
        if (totalRecords > 0 && lstAllMessages != null) {
            int totalPage = totalRecords / StewConstant.ITEM_PER_PAGE_MANAGE + 1;
            if (totalRecords % StewConstant.ITEM_PER_PAGE_MANAGE == 0) {
                totalPage = totalPage - 1;
            }
            if (totalRecords <= StewConstant.ITEM_PER_PAGE_MANAGE) {
                result.setListItems(lstAllMessages);
            }else{
                List<MessageTemplate> lstMessages = new LinkedList<MessageTemplate>();
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
    public MessageTemplate getMessageByTitle(String title){
        MessageTemplateAdapter messageAdapter = new MessageTemplateAdapter();
        MessageTemplate result = messageAdapter.getMessageByTitle(title);        
        return result;
    }
    
    /**
     * get message by id
     * @param messageId
     * @return 
     */
    public MessageTemplate getMessageByID(long id){
        MessageTemplateAdapter messageAdapter = new MessageTemplateAdapter();
        MessageTemplate result = messageAdapter.getMessageByMessageID(id);        
        return result;
    }
    
    /**
     * Get all messages from database
     * @return
     */
    public List<MessageTemplate> getAllMessages(){
        MessageTemplateAdapter messageAdapter = new MessageTemplateAdapter();
        List<MessageTemplate> lstAllMessages = messageAdapter.getMessages();
        return  lstAllMessages;
    }
}
