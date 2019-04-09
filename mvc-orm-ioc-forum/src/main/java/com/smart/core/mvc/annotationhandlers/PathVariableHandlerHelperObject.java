package com.smart.core.mvc.annotationhandlers;

import java.util.Map;

public class PathVariableHandlerHelperObject {//封装有@PathVariable
    private String[] splitResult;//1
    private boolean[] trueIsEL;//2  1和2放@RequestMapping中的url的模式
    private String url;//@RequestMapping中的url
    private Map<String,Integer> map;//@PathVariable注解的方法参数在@RequestMapping中的url的位置

    public PathVariableHandlerHelperObject(String[] splitResult,boolean[] trueIsEL,String url,Map<String,Integer> map){
        this.splitResult=splitResult;
        this.trueIsEL=trueIsEL;
        this.url = url;
        this.map=map;
    }

    public boolean[] getTrueIsEL() {
        return trueIsEL;
    }

    public String[] getSplitResult() {
        return splitResult;
    }

    public String getUrl() {
        return url;
    }

    public Map<String, Integer> getMap() {
        return map;
    }
}
