
package com.smart.web;

import com.smart.core.ioc.annotations.Autowired;
import com.smart.core.ioc.annotations.Controller;
import com.smart.core.mvc.annotationhandlers.RequestMethod;
import com.smart.core.mvc.ModelAndView;
import com.smart.core.mvc.annotations.RequestMapping;
import com.smart.domain.User;
import com.smart.exception.UserExistException;
import com.smart.service.UserService;

import javax.servlet.http.HttpServletRequest;

@Controller
public class RegisterController extends BaseController {

	private UserService userService;

	@Autowired
	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	/**
	 * 用户登录
	 * @param request
	 * @param user
	 * @return
	 */
	@RequestMapping(value = "/register", method = RequestMethod.POST)
	//@Transactional(readOnly = false)
	public ModelAndView register(HttpServletRequest request, User user) throws Exception {
		System.out.println(user.getUserName());
		ModelAndView view = new ModelAndView();
		view.setViewName("/success");
		try {
			userService.register(user);
		} catch (UserExistException e) {
			view.addObject("errorMsg", "用户名已经存在，请选择其它的名字。");
			view.setViewName("forward:/register.jsp");
		}
		setSessionUser(request,user);
		return view;
	}
	
}
