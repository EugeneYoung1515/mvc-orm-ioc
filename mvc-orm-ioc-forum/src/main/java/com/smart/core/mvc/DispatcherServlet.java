package com.smart.core.mvc;

import com.smart.core.mvc.annotationhandlers.PathVariableHandlerHelperObject;
import com.smart.core.mvc.annotationhandlers.RequestMappingHelperObject;
import com.smart.core.mvc.annotationhandlers.ResponseBodyHandler;
import com.smart.core.mvc.annotationhandlers.UrlAndHttpMethodObject;
import com.smart.core.mvc.annotations.PathVariable;
import com.smart.core.mvc.annotations.RequestParam;
import com.smart.core.mvc.annotations.ResponseBody;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;

@WebServlet(urlPatterns = "/")//这里把所有的拦截了 下面还是能转发到url-pattern对应的servlet
public class DispatcherServlet extends HttpServlet {
    private Map<UrlAndHttpMethodObject,RequestMappingHelperObject> urlMappings;
    private List<PathVariableHandlerHelperObject> helperObjects;
    private List<String> formToBean;

    @Override
    public void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if(req.getMethod().equals("PATCH")){
            doPatch(req,resp);
        }else{
            super.service(req,resp);
        }
    }

    public void doPatch(HttpServletRequest httpServletRequest,HttpServletResponse httpServletResponse) throws ServletException,IOException{
        doGet(httpServletRequest,httpServletResponse);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void init(){//写在这里就没有多线程问题
        urlMappings = (Map<UrlAndHttpMethodObject,RequestMappingHelperObject>)getServletContext().getAttribute("UrlMappings");
        helperObjects=(List<PathVariableHandlerHelperObject>)getServletContext().getAttribute("PathVariables");

        formToBean=new ArrayList<>();
        formToBean.add("Board");
        formToBean.add("Topic");
        formToBean.add("Post");
        formToBean.add("User");
        formToBean.add("MainPost");
    }

    @Override
    public void doGet(HttpServletRequest httpServletRequest,HttpServletResponse httpServletResponse) throws ServletException, IOException {

        run(httpServletRequest,httpServletResponse);
    }

    @Override
    public void doPost(HttpServletRequest httpServletRequest,HttpServletResponse httpServletResponse) throws ServletException, IOException{
        doGet(httpServletRequest,httpServletResponse);
    }

    @Override
    public void doPut(HttpServletRequest httpServletRequest,HttpServletResponse httpServletResponse) throws ServletException, IOException{
        doGet(httpServletRequest,httpServletResponse);
    }

    @Override
    public void doDelete(HttpServletRequest httpServletRequest,HttpServletResponse httpServletResponse) throws ServletException, IOException{
        doGet(httpServletRequest,httpServletResponse);
    }

    private void run(HttpServletRequest httpServletRequest,HttpServletResponse httpServletResponse) throws IOException{
        //执行顺序 run -> run2 -> showView -> showView2和methodInvoke

        //含不含Pathvariable
        //都不能一个url 两个http方法
        String requestURI = httpServletRequest.getRequestURI();
        String contextPath = httpServletRequest.getContextPath();
        String contextRelativePath =requestURI.substring(contextPath.length());
        httpServletRequest.setAttribute("@@@contextRelativePath@@@",contextRelativePath);
        run2(httpServletRequest,httpServletResponse,contextRelativePath);
    }

    private void run2(HttpServletRequest httpServletRequest,HttpServletResponse httpServletResponse,String contextRelativePath) throws IOException{
        boolean trueIsNoNext = showView(httpServletRequest, httpServletResponse, contextRelativePath);
        if (trueIsNoNext){
            return;
        }
        for (PathVariableHandlerHelperObject helperObject : helperObjects) {
            if (match(contextRelativePath.split("/"), helperObject.getSplitResult(), helperObject.getTrueIsEL())) {
                //匹配2 下面有个匹配1
                httpServletRequest.setAttribute("@@@helperObject@@@", helperObject);
                trueIsNoNext=showView(httpServletRequest, httpServletResponse, helperObject.getUrl());
                if (trueIsNoNext){
                    return;
                }else{
                    httpServletResponse.sendError(404);
                }
                break;
            }
        }
        httpServletResponse.sendError(404);

    }

    private Boolean match(String[] url,String[] urlPattern,boolean[] trueIsEL){
        if(url.length!=urlPattern.length){
            return false;
        }
        for(int i=0;i<url.length;i++){
            //if(!url[i].equals(urlPattern[i])&&trueIsEL[i]){}
            if(!url[i].equals(urlPattern[i])&& (!trueIsEL[i])){
                return false;
            }
        }
        return true;
    }

    public Object methodInvoke(Method method,Parameter[] parameters,Object o,HttpServletRequest httpServletRequest,HttpServletResponse httpServletResponse) throws Exception{
        Object[] methodParameters = new Object[parameters.length];

        String contextRelativePath=null;
        String[] splitResult=null;
        PathVariableHandlerHelperObject helperObject=null;
        Map<String,Integer> map=null;

        helperObject = (PathVariableHandlerHelperObject)httpServletRequest.getAttribute("@@@helperObject@@@");
        if(helperObject!=null){
            map= helperObject.getMap();

            contextRelativePath = (String)httpServletRequest.getAttribute("@@@contextRelativePath@@@");
            splitResult= contextRelativePath.split("/");
        }

        for(int i=0;i<parameters.length;i++){

            if(formToBean.contains(parameters[i].getType().getSimpleName())){
                methodParameters[i]=formToBean(parameters[i].getType(),httpServletRequest);
            }

            if(parameters[i].getType().equals(HttpServletRequest.class)){
                methodParameters[i]=httpServletRequest;
                continue;
            }
            if(parameters[i].getType().equals(HttpServletResponse.class)){
                methodParameters[i]=httpServletResponse;
                continue;
            }
            if(parameters[i].getAnnotation(PathVariable.class)!=null ){//&& map!=null
                String str = splitResult[map.get(parameters[i].getName())];
                if(parameters[i].getType().equals(Integer.class)){
                    if(str==null){
                        methodParameters[i]=null;
                    }else{
                        methodParameters[i]=Integer.valueOf(str);
                    }
                }else{
                    methodParameters[i] = str;
                }
                continue;
            }
            if(parameters[i].getAnnotation(RequestParam.class)!=null ){//&& map!=null
                String requestParam = httpServletRequest.getParameter(parameters[i].getAnnotation(RequestParam.class).value());
                if(parameters[i].getType().equals(Integer.class)){
                    if(requestParam==null){
                        methodParameters[i]=null;
                    }else{
                        methodParameters[i]=Integer.valueOf(requestParam);
                    }
                }else{
                    methodParameters[i] = requestParam;
                }
                continue;
            }
            if(parameters[i].getType().equals(HttpSession.class)){
                methodParameters[i]=httpServletRequest.getSession();
            }
        }
        return method.invoke(o, methodParameters);
    }

    private boolean showView(HttpServletRequest httpServletRequest,HttpServletResponse httpServletResponse,String contextRelativePath) throws IOException{//这个丢出异常没用
        String html = contextRelativePath.replace(".html","").replace(".jpg","").replace(".json","");
        System.out.println(html);

        RequestMappingHelperObject requestMappingObject = urlMappings.get(new UrlAndHttpMethodObject(html,httpServletRequest.getMethod()));
        //匹配1
        boolean trueIsNoNext = false;
        if(requestMappingObject!=null){
            String httpMethod = httpServletRequest.getMethod();
            if (httpMethod.equals(requestMappingObject.getHttpMethod())) {//这个if其实可以不用
                trueIsNoNext=true;
                Method method = requestMappingObject.getMethod();
                Parameter[] parameters = requestMappingObject.getParameters();
                Class<?> cl = requestMappingObject.getCl();
                Object o = requestMappingObject.getObject();
                try {
                    Class<?> methodReturnType = method.getReturnType();
                    if(methodReturnType.equals(ModelAndView.class)){
                        //根据放返回值类型分

                        //ModelAndView modelAndView = (ModelAndView) method.invoke(cl.cast(getServletContext().getAttribute(cl.getName())),
                        //2ModelAndView modelAndView = (ModelAndView) method.invoke(cl.cast(o), httpServletRequest, httpServletResponse);
                        ModelAndView modelAndView = (ModelAndView) methodInvoke(method,parameters,cl.cast(o), httpServletRequest, httpServletResponse);
                        Map<String, Object> map = modelAndView.getMap();
                        map.forEach((kk, vv) -> {
                            httpServletRequest.setAttribute(kk, vv);
                        });
                        String view = modelAndView.getViewName();
                        showView2(view,httpServletRequest,httpServletResponse);
                    }else if (methodReturnType.equals(String.class)){
                        String view = (String) methodInvoke(method,parameters,cl.cast(o), httpServletRequest, httpServletResponse);
                        showView2(view,httpServletRequest,httpServletResponse);
                    }else{
                        if(!methodReturnType.equals(void.class) && method.getAnnotation(ResponseBody.class)!=null){
                            //Object obj = method.invoke(cl.cast(o), httpServletRequest, httpServletResponse);
                            Object obj = methodInvoke(method,parameters,cl.cast(o), httpServletRequest, httpServletResponse);
                            //httpServletResponse.setCharacterEncoding("UTF-8");//这里要编码不然发出的json中文显示出来是乱码
                            //这里不设置了 在CharapterEncodingFilter哪里设置响应的编码
                            Writer writer = httpServletResponse.getWriter();
                            writer.write(new ResponseBodyHandler().process(obj));
                        }else{
                            methodInvoke(method,parameters,cl.cast(o), httpServletRequest, httpServletResponse);
                        }
                    }
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        }
        return trueIsNoNext;
    }

    private void showView2(String view,HttpServletRequest httpServletRequest,HttpServletResponse httpServletResponse) throws ServletException,IOException{
        String viewPath;
        if(view.indexOf("redirect:/")>-1 ){
            //根据视图名分

            view=view.replace("redirect:","");
            httpServletResponse.sendRedirect(httpServletRequest.getContextPath()+view);
        }else if(view.indexOf("redirect:")>-1 && view.indexOf(httpServletRequest.getContextPath())>-1){
            httpServletResponse.sendRedirect(view.replace("redirect:",""));
        }else {
            if (view.indexOf("forward:/") > -1) {//这里可以加上一个转发到其他控制器方法 调用上面的showview 不过这样就和重定向一样
                //原来servlet 就能转发到 一个servlet对应的url 交给一个servlet处理
                //就要一遍看web-inf／jsp下有没有符合的
                //另一边看RequestMapping中url有没有符合的
                //下面input那里实现了

                view = view.replace("forward:/", "");

            }
            if (view.indexOf(".jsp") > -1) {
                viewPath = "/" + view;//写这个是为了处理 不在"/WEB-INF/jsp/"下的 是在/WEB-INF下的 jsp
            }else {
                viewPath = "/WEB-INF/jsp/" + view + ".jsp";

                InputStream inputStream = httpServletRequest.getServletContext().getResourceAsStream(viewPath);
                if(inputStream==null){//等于null 就是没有这个资源 那就尝试调用其他控制器的方法
                    //这里可能还需要加一个条件 最早的没有replace的view 包含 "forward:/"才处理
                    run2(httpServletRequest,httpServletResponse,"/"+view);
                    return;
                }

            }
            requestDispatcherForward(viewPath, httpServletRequest, httpServletResponse);
        }
    }


    private void requestDispatcherForward(String viewPath,HttpServletRequest httpServletRequest,
                                         HttpServletResponse httpServletResponse) throws ServletException, IOException{
        RequestDispatcher requestDispatcher = httpServletRequest.getRequestDispatcher(viewPath);
        requestDispatcher.forward(httpServletRequest, httpServletResponse);
    }


    /*
    private Object formToBean(Class<?> cl,HttpServletRequest httpServletRequest) throws Exception{
        Object o = cl.newInstance();
        Field[] fields = cl.getDeclaredFields();
        String param;
        for (Field field:fields){
            field.setAccessible(true);
            if(field.getType().equals(String.class)){
                //System.out.println(field.getName());
                if(field.getName().equals("postText")){
                    param=httpServletRequest.getParameter("mainPost.postText");
                }else {
                    param = httpServletRequest.getParameter(field.getName());
                }
                field.set(o,param);
            }else if(field.getType().equals(int.class)){
                param = httpServletRequest.getParameter(field.getName());
                if(param!=null){
                    field.set(o,Integer.parseInt(param));
                }
            }else {
                //Object obj = field.getType().newInstance();
                if(formToBean.contains(field.getType().getSimpleName())){
                    Object obj = formToBean(field.getType(),httpServletRequest);
                    field.set(o,obj);
                }
            }
        }
        return o;
    }
    */


    //两处问题
    //1.type错
    //2.拿不到posttext
    //原因 继承和Field类

    /*
    private Object formToBean(Class<?> cl,HttpServletRequest httpServletRequest,String... fieldName) throws Exception{
        Object o = cl.newInstance();
        Field[] fields = cl.getDeclaredFields();
        String param;
        String prefixFieldName = "";
        if(fieldName.length > 0){
            prefixFieldName = fieldName[0]+".";
        }
        for (Field field:fields){
            field.setAccessible(true);
            if(field.getType().equals(String.class)){
                if(field.get(o)==null) {//这个可能会有问题
                    param = httpServletRequest.getParameter(prefixFieldName + field.getName());
                    field.set(o, param);
                }
            }else if(field.getType().equals(int.class)){
                param = httpServletRequest.getParameter(prefixFieldName+field.getName());
                if(param!=null){
                    field.set(o,Integer.parseInt(param));
                }
            }else {
                //1if(formToBean.contains(field.getType().getSimpleName())){
                //1System.out.println((prefixFieldName+field.getName()+"."+"kk"));

                    Enumeration<String> enumeration = httpServletRequest.getParameterNames();
                    while (enumeration.hasMoreElements()){
                        if(enumeration.nextElement().contains(prefixFieldName+field.getName()+".")){
                            System.out.println((prefixFieldName+field.getName()+"."+"kk"));
                            Object obj = formToBean(field.getType(),httpServletRequest,prefixFieldName+field.getName());
                            field.set(o,obj);
                            break;
                        }
                    }
                    //1Object obj = formToBean(field.getType(),httpServletRequest,prefixFieldName+field.getName());
                    //1field.set(o,obj);
                //1}//有formToBean这个List在还是耦合的
                //1处是可以的
            }
        }
        return o;
    }
    */

    private Object formToBean(Class<?> cl,HttpServletRequest httpServletRequest,String... fieldName) throws Exception{
        Object o = cl.newInstance();
        Method[] methods = cl.getMethods();
        String param;
        String prefixFieldName = "";
        if(fieldName.length > 0){
            prefixFieldName = fieldName[0]+".";
        }
        for(Method method:methods){
            String methodName = method.getName();
            if(methodName.startsWith("set")){
                String field = methodName.substring(3,4).toLowerCase()+methodName.substring(4);
                //System.out.println(field+"method");
                if(method.getParameterTypes()[0].equals(String.class)){
                    param = httpServletRequest.getParameter(prefixFieldName + field);
                    if(param!=null&& !param.equals("")){
                        method.invoke(o,param);
                    }
                }else if(method.getParameterTypes()[0].equals(int.class)){
                    param = httpServletRequest.getParameter(prefixFieldName + field);
                    if(param!=null&& !param.equals("")){
                        method.invoke(o,Integer.parseInt(param));
                    }
                }else {
                    Enumeration<String> enumeration = httpServletRequest.getParameterNames();
                    while (enumeration.hasMoreElements()){
                        if(enumeration.nextElement().contains(prefixFieldName+field+".")){
                            Object obj = formToBean(method.getParameterTypes()[0],httpServletRequest,prefixFieldName+field);
                            method.invoke(o,obj);
                            break;
                        }
                    }

                }
            }
        }
        return o;
    }
}
//不会有多线程问题的AppListener
//2.这个类的init()方法
//3.请求属性
//4.对实例变量只读
//5.没有实例变量

//用一个对象封装多个返回值在多个方法之间传递
