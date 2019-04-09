package com.smart.core.mvc.annotationhandlers;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

public class RequestMappingHelperObject {//封装@RequestMethod注解的属性 和 有@RequestMethod注解的方法的信息
    private Method method;
    private Class<?> cl;
    private Object object;
    private String httpMethod;
    private Parameter[] parameters;


    public RequestMappingHelperObject(Method method, Parameter[] parameters, Class<?> cl, Object o, String httpMethod) {
        this.method = method;
        this.parameters=parameters;
        this.cl = cl;
        this.object=o;
        this.httpMethod=httpMethod;
    }


    public Object getObject() {
        return object;
    }

    public Method getMethod() {
        return method;
    }

    public Class<?> getCl() {
        return cl;
    }


    public String getHttpMethod() {
        return httpMethod;
    }

    public Parameter[] getParameters() {
        return parameters;
    }
}
