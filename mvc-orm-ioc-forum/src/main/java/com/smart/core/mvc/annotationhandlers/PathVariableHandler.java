package com.smart.core.mvc.annotationhandlers;

import com.smart.core.mvc.annotations.PathVariable;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.HashMap;
import java.util.Map;

public class PathVariableHandler {
    //private List<Integer> pathVariableLocation=new ArrayList<>();
    private Map<String,Integer> map;

    public PathVariableHandlerHelperObject process(Method method,String url){
        if (!hasPathVariable(method))return null;
        String[] splitResult = url.split("/");
        boolean[] isEL=new boolean[splitResult.length];
        for(int i = 0;i<splitResult.length;i++){
            if(splitResult[i].indexOf("{")>-1){
                isEL[i]=true;
                String str = splitResult[i].substring(1,splitResult[i].length()-1);
                map.put(str,i);
            }else {
                isEL[i]=false;
            }
        }
        return new PathVariableHandlerHelperObject(splitResult,isEL,url,map);
    }
    public boolean hasPathVariable(Method method){
        boolean hasPathVariable=false;
        Parameter[] parameters = method.getParameters();
        map = new HashMap<>();
        for(int i=0;i<parameters.length;i++){
            if(parameters[i].getAnnotation(PathVariable.class)!=null){
                //pathVariableLocation.add(i);
                map.put(parameters[i].getName(),-1);
                hasPathVariable=true;
            }
        }
        return hasPathVariable;
    }
}
