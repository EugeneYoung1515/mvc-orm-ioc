package com.smart.web;

import com.smart.cons.CommonConstant;
import com.smart.core.ioc.annotations.Autowired;
import com.smart.core.ioc.annotations.Controller;
import com.smart.core.mvc.ModelAndView;
import com.smart.core.mvc.annotationhandlers.RequestMethod;
import com.smart.core.mvc.annotations.RequestMapping;
import com.smart.domain.User;
import com.smart.service.UserService;
import org.apache.commons.lang.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Date;


//@Transactional(readOnly = true)
@Controller
public class LoginController extends BaseController {

	private UserService userService;

	@Autowired
	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	/**
     * 用户登陆
     * @param request
     * @param user
     * @return
     */
	@RequestMapping(value = "/login/doLogin",method = RequestMethod.POST)
	public ModelAndView login(HttpServletRequest request, User user) throws Exception{
		try {
			String captcha = (String) request.getSession().getAttribute("Captcha");
			System.out.println(captcha);
			System.out.println(request.getParameter("captcha"));
			User dbUser = userService.getUserByUserName(user.getUserName());
			ModelAndView mav = new ModelAndView();
			mav.setViewName("forward:/login.jsp");//"forward:/login.jsp"
			if (dbUser == null) {
				mav.addObject("errorMsg", "用户名不存在");
			} else if (!captcha.equals(request.getParameter("captcha"))) {
				mav.addObject("errorMsg", "验证码错误");
			} else if (!dbUser.getPassword().equals(user.getPassword())) {
				mav.addObject("errorMsg", "用户密码不正确");
			} else if (dbUser.getLocked() == User.USER_LOCK) {
				mav.addObject("errorMsg", "用户已经被锁定，不能登录。");
			} else {
				dbUser.setLastIp(request.getRemoteAddr());
				dbUser.setLastVisit(new Date());
				userService.loginSuccess(dbUser);
				setSessionUser(request, dbUser);
				String toUrl = (String) request.getSession().getAttribute(CommonConstant.LOGIN_TO_URL);
				request.getSession().removeAttribute(CommonConstant.LOGIN_TO_URL);
				//如果当前会话中没有保存登录之前的请求URL，则直接跳转到主页
				if (StringUtils.isEmpty(toUrl)) {
					toUrl = "/index.html";
				}
				mav.setViewName("redirect:" + toUrl);
			}
			return mav;
		}catch (Exception e){
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 登录注销
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/login/doLogout",method = RequestMethod.GET)
	public String logout(HttpSession session) {
		session.removeAttribute(CommonConstant.USER_CONTEXT);
		return "forward:/index.jsp";
	}

}
