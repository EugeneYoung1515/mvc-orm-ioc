package com.smart.core.mvc.filter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter(filterName = "hiddenMethodFilter",urlPatterns = "/*",initParams = {
        @WebInitParam(name = "test", value = "hiddenMethodinit")})
public class HiddenMethodFilter implements Filter {

    private class MyHttpServletRequest extends HttpServletRequestWrapper{
        //private HttpServletRequest request;
        private String hM;
        public MyHttpServletRequest(HttpServletRequest request){
            super(request);
        }

        @Override
        public String getMethod(){
            return hM;
        }

        public void setHM(String HM){
            this.hM=HM;
        }

    }

    @Override
    public void init(FilterConfig config) throws ServletException{
        System.out.println(config.getInitParameter("test"));
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException{
        HttpServletRequest httpReq = (HttpServletRequest)req;
        HttpServletResponse httpRes =(HttpServletResponse)res;
        httpRes.setCharacterEncoding("UTF-8");
        if(httpReq.getMethod().equals("POST")){
            String hiddenMethod = httpReq.getParameter("_method");
            if(hiddenMethod!=null && hiddenMethod.length()>0){
                String upper = hiddenMethod.toUpperCase();
                if(upper.equals("PUT")||upper.equals("DELETE")||upper.equals("PATCH")){
                    MyHttpServletRequest myHttpReq = new MyHttpServletRequest(httpReq);
                    myHttpReq.setHM(upper);
                    chain.doFilter(myHttpReq,res);
                    return;
                }
            }
        }
        chain.doFilter(req,res);
    }

    @Override
    public void destroy(){}

}//模仿spring的HiddenMethodFilter写的
