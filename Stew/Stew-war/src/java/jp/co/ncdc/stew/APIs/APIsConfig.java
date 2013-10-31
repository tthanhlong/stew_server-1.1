/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jp.co.ncdc.stew.APIs;

import java.util.Set;
import java.util.HashSet;
import javax.ws.rs.core.Application;

/**
 * @name: APIsConfig
 * @brief: This class is definition of APIs
 * @create: Aug 21, 2013 
 * @version 1.0
 */
public class APIsConfig extends Application {

    private Set<Object> singletons = new HashSet<Object>();
    private Set<Class<?>> empty = new HashSet<Class<?>>();

    public APIsConfig() {
        singletons.add(new StewService());
    }

    @Override
    public Set<Class<?>> getClasses() {
        return empty;
    }

    @Override
    public Set<Object> getSingletons() {
        return singletons;
    }
}