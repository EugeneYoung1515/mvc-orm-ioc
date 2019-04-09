package com.smart.core.mvc.annotationhandlers;

import java.util.Objects;

public class UrlAndHttpMethodObject {//这个类是为了作为Map的key 放@RequestMapping上的url和httpMethod
    private String url;
    private String httpMethod;

    public UrlAndHttpMethodObject(String url,String httpMethod){
        this.url=url;
        this.httpMethod=httpMethod;
    }

    public String getUrl() {
        return url;
    }

    public String getHttpMethod() {
        return httpMethod;
    }

    @Override
    public boolean equals(Object obj) {
        if(this==obj)return true;
        if(obj==null)return false;
        //if(!(obj instanceof UrlAndHttpMethodObject))return false;//这句可以去掉 不允许有子类的 可以换成
        if(getClass()!=obj.getClass())return false;
        UrlAndHttpMethodObject other=(UrlAndHttpMethodObject)obj;
        return Objects.equals(url,other.getUrl())&&Objects.equals(httpMethod,other.getHttpMethod());
    }

    @Override
    public int hashCode() {
        return Objects.hash(url,httpMethod);
    }//重写equals方法和hashcode方法是为了给map的查找使用
}
