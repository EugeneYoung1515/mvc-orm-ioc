package com.smart.core;

import com.smart.core.ioc.ComponentFactory;
import com.smart.core.mvc.annotationhandlers.PathVariableHandlerHelperObject;
import com.smart.core.mvc.annotationhandlers.RequestMappingHandler;
import com.smart.core.mvc.annotationhandlers.RequestMappingHelperObject;
import com.smart.core.mvc.annotationhandlers.UrlAndHttpMethodObject;
import com.smart.core.orm.transaction.ConnectionManager;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

@WebListener
public class ApplicationListener implements ServletContextListener {//这个类的方法不会有多线程问题
    private Map<UrlAndHttpMethodObject,RequestMappingHelperObject> methodMap = new HashMap<>();
    private RequestMappingHandler requestMappingHandler = new RequestMappingHandler();
    private ServletContext servletContext;
    private List<PathVariableHandlerHelperObject> helperObjects = new ArrayList<>();

    public void contextInitialized(ServletContextEvent servletContextEvent) {
        servletContext=servletContextEvent.getServletContext();

        /*
        UrlMappingRegister(new TestServlet());//new对象并登记url
        UrlMappingRegister(new TestServlet2());
        servletContext.setAttribute("UrlMappings",methodMap);
        servletContext.setAttribute("PathVariables",helperObjects);
        */

        String topPackageName="";//这里可能会有问题
        InputStream inputStream = getClass().getResourceAsStream("/setting.properties");
        Properties properties = new Properties();
        try {
            properties.load(inputStream);
            topPackageName = properties.getProperty("topPackageName");

            ConnectionManager.driver = properties.getProperty("jdbc.driverClassName");
            ConnectionManager.url = properties.getProperty("jdbc.url");
            ConnectionManager.user = properties.getProperty("jdbc.username");
            ConnectionManager.password = properties.getProperty("jdbc.password");
        }catch (IOException ex){
            ex.printStackTrace();
        }

        List<Object> controllerList = ComponentFactory.createComponentAndReturnControllerList(topPackageName);
        for(Object obj:controllerList){
            urlMappingRegister(obj);
        }
        servletContext.setAttribute("UrlMappings",methodMap);
        servletContext.setAttribute("PathVariables",helperObjects);
    }

    private void urlMappingRegister(Object o){
        //servletContext.setAttribute(o.getClass().getName(),o);
        requestMappingHandler.process(methodMap,o,helperObjects);//这样一个List表示对象o的所有有被注解方法
    }

    public void contextDestroyed(ServletContextEvent servletContextEvent) {
    }
}//注解处理器哪些对象 也要是单例 只new一次 或者是只有静态方法
