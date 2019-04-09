package com.smart.web;

import javax.servlet.http.HttpServletRequest;
import com.smart.cons.CommonConstant;
import com.smart.domain.User;

/**
 * 所有Controller的基类
 */

public class BaseController {
	protected static final String ERROR_MSG_KEY = "errorMsg";

	/**
	 * 获取保存在Session中的用户对象
	 * 
	 * @param request
	 * @return
	 */
	protected User getSessionUser(HttpServletRequest request) {
		return (User) request.getSession().getAttribute(
				CommonConstant.USER_CONTEXT);
	}
   
	/**
	 * 保存用户对象到Session中
	 * @param request
	 * @param user
	 */
	protected void setSessionUser(HttpServletRequest request,User user) {
		request.getSession().setAttribute(CommonConstant.USER_CONTEXT,
				user);
	}
}
