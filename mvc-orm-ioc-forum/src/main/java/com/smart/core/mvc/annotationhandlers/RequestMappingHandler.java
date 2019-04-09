package com.smart.core.mvc.annotationhandlers;

import com.smart.core.mvc.annotations.RequestMapping;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

public class RequestMappingHandler {

    private PathVariableHandler pathVariableHandler = new PathVariableHandler();

    public  void process(Map<UrlAndHttpMethodObject,RequestMappingHelperObject> map, Object o, List<PathVariableHandlerHelperObject> helperObjects){
        Class<?> cl = o.getClass();
        for(Method method:cl.getDeclaredMethods()){
            RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
            if(requestMapping!=null){
                //map.put(requestMapping.value(),new RequestMappingHelperObject(method,cl,requestMapping.value(),requestMapping.method()));
                map.put(new UrlAndHttpMethodObject(requestMapping.value(),requestMapping.method()), new RequestMappingHelperObject(method, method.getParameters(), cl, o, requestMapping.method()));
                PathVariableHandlerHelperObject helperObject = pathVariableHandler.process(method,requestMapping.value());
                if(helperObject!=null){
                    helperObjects.add(helperObject);
                }
            }
        }
    }
}
