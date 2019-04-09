package com.smart.core.mvc.test;

import com.smart.core.mvc.ModelAndView;
import com.smart.core.mvc.annotationhandlers.RequestMethod;
import com.smart.core.mvc.annotations.PathVariable;
import com.smart.core.mvc.annotations.RequestMapping;
import com.smart.core.mvc.annotations.RequestParam;
import com.smart.domain.User;

import javax.servlet.ServletException;
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
public class TestServlet{
    private static int num;
    public TestServlet(){
        num++;
        System.out.println(num);
    }

    @RequestMapping(value="/kkw",method = RequestMethod.GET)
    public ModelAndView doGet(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServletException,IOException{
        ModelAndView modelAndView = new ModelAndView("test");
        modelAndView.addObject("test","test");
        return modelAndView;
    }

    @RequestMapping(value="/kkw",method = RequestMethod.POST)
    public ModelAndView doPost(HttpServletRequest httpServletRequest,HttpServletResponse httpServletResponse) throws ServletException,IOException{
        ModelAndView modelAndView = new ModelAndView("test");
        modelAndView.addObject("test","testPost");
        return modelAndView;
    }

    @RequestMapping(value="/kkq",method = RequestMethod.GET)
    public String test2(HttpServletRequest httpServletRequest,HttpServletResponse httpServletResponse) throws ServletException,IOException{
        return "redirect:/kkw";
    }

    @RequestMapping(value="/kk/{qwerty}",method = RequestMethod.GET)
    public String test3(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, @PathVariable String qwerty) throws ServletException,IOException{
        return "redirect:/kkw";
    }

    @RequestMapping(value="/kk/{test1}/ff/{test2}",method = RequestMethod.GET)
    public void test4(HttpServletResponse httpServletResponse, @PathVariable String test1, HttpServletRequest httpServletRequest, @PathVariable String test2, User user, @RequestParam("userName") String u) throws ServletException,IOException{
        Writer writer = httpServletResponse.getWriter();
        writer.write(test1);
        writer.write(user.getPassword());
        writer.write(u);
        writer.close();

    }
    @RequestMapping(value="/kk/{test1}/ff/{test2}",method = RequestMethod.DELETE)
    public void test5(HttpServletResponse httpServletResponse, @PathVariable String test1, HttpServletRequest httpServletRequest, @PathVariable String test2, User user, @RequestParam("userName") String u) throws ServletException,IOException{
        Writer writer = httpServletResponse.getWriter();
        writer.write("delete");
        writer.close();

    }
}
