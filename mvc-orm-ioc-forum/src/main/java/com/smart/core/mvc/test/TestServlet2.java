package com.smart.core.mvc.test;

import com.smart.core.mvc.annotationhandlers.RequestMethod;
import com.smart.core.mvc.annotations.RequestMapping;
import com.smart.core.mvc.annotations.ResponseBody;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Writer;

/*
@WebServlet(urlPatterns = "/kkw")
public class TestServlet extends HttpServlet {

    @Override
    public void doGet(HttpServletRequest httpServletRequest,HttpServletResponse httpServletResponse) throws ServletException,IOException{
        Writer writer = httpServletResponse.getWriter();
        writer.write("test");
        writer.close();
    }
}
*/
public class TestServlet2 extends HttpServlet {
    private static int num;
    public TestServlet2(){
        num++;
        System.out.println(num);
    }

    @RequestMapping(value="/kke",method = RequestMethod.GET)
    public void doGet(HttpServletRequest httpServletRequest,HttpServletResponse httpServletResponse) throws ServletException,IOException{
        Writer writer = httpServletResponse.getWriter();
        writer.write("kke");
        writer.close();
    }

    @RequestMapping(value="/kkr",method = RequestMethod.GET)
    @ResponseBody
    public TestObject test2(HttpServletRequest httpServletRequest,HttpServletResponse httpServletResponse) throws ServletException,IOException{
        TestObject testObject = new TestObject();
        testObject.setNum(2);
        testObject.setName("young");
        return testObject;
    }

    @RequestMapping(value="/kk3",method = RequestMethod.DELETE)
    public void test3(HttpServletRequest httpServletRequest,HttpServletResponse httpServletResponse) throws ServletException,IOException{
        Writer writer = httpServletResponse.getWriter();
        writer.write("kk3");
        writer.close();
    }
}
