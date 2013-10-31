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
public class MessageTemplate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)   
    private Long messageId;
    private String title;
    private String message;
    
   public MessageTemplate (String title, String message)
   {
       this.title = title;
       this.message = message;
   }
   
   public MessageTemplate() {
   }

    public Long getId() {
        return messageId;
    }

    public void setId(Long id) {
        this.messageId = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }                 
}
