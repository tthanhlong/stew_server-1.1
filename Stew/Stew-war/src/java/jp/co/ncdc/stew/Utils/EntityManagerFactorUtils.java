/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jp.co.ncdc.stew.Utils;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author tquangthai
 */
public class EntityManagerFactorUtils {      
    private static EntityManagerFactory entityManagerFactory=null;
    public static EntityManagerFactory getCurrentEntityManagerFactory()
    {
        if (entityManagerFactory == null) {
            entityManagerFactory = Persistence.createEntityManagerFactory("JPA");
        }
        return entityManagerFactory;
    }
}
