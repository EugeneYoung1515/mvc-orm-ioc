package com.smart.core.orm.transaction;

import com.smart.core.orm.annotation.Transactional;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class TransactionProxy implements InvocationHandler {

    private Object obj;

    public Object newProxyInstance(Object obj){
        this.obj=obj;
        return Proxy.newProxyInstance(obj.getClass().getClassLoader(),obj.getClass().getInterfaces(),this);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable{
        Object returnObject=null;
        System.out.println("业务层代理");
        if(method.isAnnotationPresent(Transactional.class)){
            try {
                ConnectionManager.beginTransaction();
                System.out.println("开启事务");
                returnObject=method.invoke(obj,args);
                ConnectionManager.commit();
                System.out.println("提交事务");
            }catch (Exception e){
                ConnectionManager.rollback();
                System.out.println("回滚");
                e.printStackTrace();
            }
            finally{
                ConnectionManager.close();
            }
        }else {
            System.out.println(obj.getClass());
            returnObject=method.invoke(obj,args);
            ConnectionManager.close();
        }
        return returnObject;
    }
}
