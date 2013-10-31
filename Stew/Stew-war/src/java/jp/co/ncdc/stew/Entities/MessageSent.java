/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jp.co.ncdc.stew.Entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 *
 * @author vcnduong
 */
@Entity
public class MessageSent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long messageSentId;
    private Long messageId;    
    private Long groupId;
    private String userId;
    private String appId;
    
    private String status;

    public MessageSent() {
    }

    public MessageSent(Long messageId, String userId, String status, String appId, Long groupId) {
        this.messageId = messageId;
        this.userId = userId;
        this.status = status;
        this.appId = appId;
        this.groupId = groupId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getMessageId() {
        return messageId;
    }

    public void setMessageId(Long messageId) {
        this.messageId = messageId;
    }

    public Long getMessageSentId() {
        return messageSentId;
    }

    public void setMessageSentId(Long messageSentId) {
        this.messageSentId = messageSentId;
    }

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }
    
    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }
}
