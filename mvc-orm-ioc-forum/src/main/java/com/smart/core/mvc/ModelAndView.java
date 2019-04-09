package com.smart.core.mvc;

import java.util.HashMap;
import java.util.Map;

public class ModelAndView {
    private Map<String,Object> map = new HashMap<>();
    private String viewName;
    public ModelAndView(){
    }
    public ModelAndView(String viewName){
        this.viewName=viewName;
    }
    public void setViewName(String viewName){
        this.viewName=viewName;
    }

    public String getViewName() {
        return viewName;
    }

    public void addObject(String name, Object value){
        map.put(name,value);
    }
    public Map<String,Object> getMap(){
        return map;
    }
}
