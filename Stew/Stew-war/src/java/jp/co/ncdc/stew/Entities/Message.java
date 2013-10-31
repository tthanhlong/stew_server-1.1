/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jp.co.ncdc.stew.Entities;

import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Temporal;

/**
 *
 * @author vcnduong
 */
@Entity
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long messageId;
    private String title;    
    private String message;
    private String sentToGroup;
    private String sentToApp;
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date createMessage;
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date scheduleSend;

    public Message() {
    }

    public Long getMessageId() {
        return messageId;
    }

    public void setMessageId(Long messageId) {
        this.messageId = messageId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Date getCreateMessage() {
        return createMessage;
    }

    public void setCreateMessage(Date createMessage) {
        this.createMessage = createMessage;
    }

    public Date getScheduleSend() {
        return scheduleSend;
    }

    public void setScheduleSend(Date scheduleSend) {
        this.scheduleSend = scheduleSend;
    }
    
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSentToGroup() {
        return sentToGroup;
    }

    public void setSentToGroup(String sentToGroup) {
        this.sentToGroup = sentToGroup;
    }

    public String getSentToApp() {
        return sentToApp;
    }

    public void setSentToApp(String sentToApp) {
        this.sentToApp = sentToApp;
    }
}
