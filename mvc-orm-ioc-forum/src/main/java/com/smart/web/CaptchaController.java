package com.smart.web;


import com.smart.captcha.GraphicHelper;
import com.smart.core.ioc.annotations.Controller;
import com.smart.core.mvc.annotationhandlers.RequestMethod;
import com.smart.core.mvc.annotations.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.OutputStream;

@Controller
public class CaptchaController {

    @RequestMapping(value = "/captcha",method = RequestMethod.GET)
    public void service(HttpServletRequest httpServletRequest, HttpServletResponse httpResponse) throws IOException {
        HttpSession session = httpServletRequest.getSession();

        // 从请求中获得 URI ( 统一资源标识符 )
        //String uri = httpServletRequest.getRequestURI();
        //System.out.println(uri);测试

        final int width = 180; // 图片宽度
        final int height = 40; // 图片高度
        final String imgType = "jpeg"; // 指定图片格式 (不是指MIME类型)
        final OutputStream output = httpResponse.getOutputStream(); // 获得可以向客户端返回图片的输出流
        // (字节流)
        // 创建验证码图片并返回图片上的字符串
        String code = GraphicHelper.create(width, height, imgType, output);

        // 建立 uri 和 相应的 验证码 的关联 ( 存储到当前会话对象的属性中 )
        //session.setAttribute(uri, code);
        session.setAttribute("Captcha", code);
    }


}
