package com.smart.core.ioc;

import com.smart.core.ioc.annotations.Autowired;
import com.smart.core.orm.transaction.TransactionProxy;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ComponentFactory {
    public static List<Object> createComponentAndReturnControllerList(String packageName){
        try {
            ClassUtil.getClasses(packageName);
        }catch (Exception ex){
            ex.printStackTrace();
        }

        Map<String,Object> repositoryMap = new HashMap<>(16);
        ClassUtil.repositoryMap.forEach((k,v)->{
            try {
                repositoryMap.put(k, v.newInstance());
            }catch (Exception ex){
                ex.printStackTrace();
            }
        });

        ClassUtil.repositoryMap.forEach((k,v)->{
            try {
                Method[] methods = v.getMethods();
                for(Method method:methods){
                    if(method.isAnnotationPresent(Autowired.class)){
                        String simpleName = (method.getParameterTypes()[0]).getSimpleName();
                        Object parameterObj = repositoryMap.get(simpleName);
                        Object obj = repositoryMap.get(k);
                        method.invoke(obj,parameterObj);
                    }
                }
            }catch (Exception ex){
                ex.printStackTrace();
            }
        });

        Map<String,Object> serviceMap = new HashMap<>(16);
        Map<String,String> serviceInterfaceMap = new HashMap<>(16);
        ClassUtil.serviceMap.forEach((k,v)->{
            try {
                Object obj = v.newInstance();
                serviceInterfaceMap.put(v.getInterfaces()[0].getSimpleName(),k);
                autowired(v,obj,repositoryMap);
                serviceMap.put(k,proxy(obj));
            }catch (Exception ex){
                ex.printStackTrace();
            }
        });

        List<Object> list = new ArrayList<>(10);
        ClassUtil.controllerMap.forEach((k,v)->{
            try {
                //System.out.println(k);
                Object obj = v.newInstance();
                autowired(v,obj,serviceMap,serviceInterfaceMap);
                list.add(obj);//维持着web层对象的引用 进而维持着其他层对象的引用
            }catch (Exception ex){
                ex.printStackTrace();
            }
        });
        return list;
    }

    private static void autowired(Class clz,Object obj,Map<String,Object> map){
        try {
            Method[] methods = clz.getDeclaredMethods();
            for(Method method:methods){
                if(method.isAnnotationPresent(Autowired.class)){
                    String simpleName = (method.getParameterTypes()[0]).getSimpleName();
                    Object parameterObj = map.get(simpleName);
                    method.invoke(obj,parameterObj);
                }
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    private static void autowired(Class clz,Object obj,Map<String,Object> map,Map<String,String> map2){
        try {
            Method[] methods = clz.getDeclaredMethods();
            for(Method method:methods){
                if(method.isAnnotationPresent(Autowired.class)){
                    String simpleName = (method.getParameterTypes()[0]).getSimpleName();
                    Object parameterObj = map.get(map2.get(simpleName));
                    method.invoke(obj,parameterObj);
                }
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    private static Object proxy(Object obj){
        TransactionProxy proxy = new TransactionProxy();
        //ForumService forumService2 = (ForumService) proxy.newProxyInstance(forumService);
        return proxy.newProxyInstance(obj);
    }

}
