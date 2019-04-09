package com.smart.core.mvc.filter;


import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.IOException;

@WebFilter(filterName = "characterEncodingFilter",urlPatterns = "/*")
public class CharacterEncodingFilter implements Filter {

    private class EncodingWrapper extends HttpServletRequestWrapper{

        public EncodingWrapper(HttpServletRequest httpServletRequest){
            super(httpServletRequest);
        }

        @Override
        public String getParameter(String s){
            String param = getRequest().getParameter(s);
            if(param!=null){
                try{
                    byte[] b = param.getBytes("ISO-8859-1");
                    param = new String(b,"UTF-8");
                    return param;
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
            return param;
        }

    }

    @Override
    public void init(FilterConfig filterConfig){}

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpReq = (HttpServletRequest)request;
        httpReq.setCharacterEncoding("UTF-8");
        if(httpReq.getMethod().equals("GET")){
            chain.doFilter(new EncodingWrapper(httpReq),response);
        }else {
            chain.doFilter(request,response);
        }
    }

    @Override
    public void destroy(){}
}
