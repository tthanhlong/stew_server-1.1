/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jp.co.ncdc.stew.APIs.model;

import java.util.List;
import jp.co.ncdc.stew.Entities.MessageTemplate;

/**
 *
 * @author tquangthai
 */
public class MessageModel {
    private int status;
    private int total;
    private List<MessageTemplate> messages;
    
    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }
    
    public List<MessageTemplate> getMessages() {
        return messages;        
    }

    public void setMessages(List<MessageTemplate> messages) {
        this.messages = messages;
    }

    public MessageModel(int status, int total, List<MessageTemplate> messages) {
        this.status = status;
        this.total = total;
        this.messages = messages;    
    }

    public MessageModel() {
    }    
}
