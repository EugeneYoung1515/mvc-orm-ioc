
package com.smart.web;

import com.smart.core.ioc.annotations.Autowired;
import com.smart.core.ioc.annotations.Controller;
import com.smart.core.mvc.annotationhandlers.RequestMethod;
import com.smart.core.mvc.ModelAndView;
import com.smart.core.mvc.annotations.RequestMapping;
import com.smart.core.mvc.annotations.RequestParam;
import com.smart.core.mvc.annotations.ResponseBody;
import com.smart.domain.Board;
import com.smart.domain.User;
import com.smart.service.ForumService;
import com.smart.service.UserService;


import java.util.*;

@Controller
public class ForumManageController extends BaseController {
	private ForumService forumService;

	private UserService userService;

	@Autowired
	public void setForumService(ForumService forumService) {
		this.forumService = forumService;
	}

	@Autowired
	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	/**
	 * 列出所有的论坛模块
	 * @return
	 */
	@RequestMapping(value = "/index", method = RequestMethod.GET)
	public ModelAndView listAllBoards()  throws Exception {
		ModelAndView view =new ModelAndView();
		List<Board> boards=null;
		//System.out.println(forumService);
		boards= (List<Board>) forumService.getAllBoards();
		view.addObject("boards", boards);
		view.setViewName("/listAllBoards");
		return view;
	}

	/**
	 *  添加一个主题帖
	 * @return
	 */
	@RequestMapping(value = "/addBoardPage", method = RequestMethod.GET)
	//和这里没有匹配
	public String addBoardPage()  throws Exception {
		return "/addBoard";
	}

	/**
	 * 添加一个主题帖
	 * @param board
	 * @return
	 */
	@RequestMapping(value = "/boards", method = RequestMethod.PUT)//原来jsp addboard中 加了一行 使得请求方法变成put
	//这里要从post改成put

	//Request method 'PUT' not supported
	//访问一个url http方法不对 会说这个方法不支持

	//几个问题 jsp和java类
	//http 方法不一致
	//大小写不一致
	//http请求参数名不一致
	public String addBoard(Board board)  throws Exception {
		try {
			System.out.println("addboard");
			forumService.addBoard(board);
			return "/addBoardSuccess";
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 指定论坛管理员的页面
	 * @return
	 */

	/*
	@RequestMapping(value = "/forum/setBoardManagerPage", method = RequestMethod.GET)
	public ModelAndView setBoardManagerPage()  throws Exception {
		ModelAndView view =new ModelAndView();
		List<Board> boards = forumService.getAllBoards();
		List<User> users = userService.getAllUsers();
		view.addObject("boards", boards);
		view.addObject("users", users);
		view.setViewName("/setBoardManager");
		return view;
	}
	*/
	@RequestMapping(value = "/setBoardManagerPage", method = RequestMethod.GET)
	@ResponseBody
	public Map<String,Object> setBoardManagerPage()  throws Exception {
			List<Board> boards = forumService.getAllBoards();
			//List<Integer> boardIds=new ArrayList<>();
			//for(Board board:boards){
			//boardIds.add(board.getBoardId());
			//}
			List<User> users = userService.getAllUsers();
			List<String> userIds = new ArrayList<>();
			for (User user : users) {
				userIds.add(user.getUserName());
			}
			Map<String, Object> map = new HashMap<String, Object>();
			//map.put("boards",boardIds);//
			map.put("boards", boards);
			map.put("users", userIds);//map.put("users",users);
			return map;
	}

	/**
     * 设置版块管理
     * @return
     */
	@RequestMapping(value = "/users/managers", method = RequestMethod.PATCH)
	public ModelAndView setBoardManager(@RequestParam("userName") String userName
			,@RequestParam("boardId") String boardId)  throws Exception {
		try {
			ModelAndView view = new ModelAndView();
			User user = userService.getUserByUserName(userName);
			if (user == null) {
				view.addObject("errorMsg", "用户名(" + userName
						+ ")不存在");
				view.setViewName("/fail");
			} else {
				Board board = forumService.getBoardById(Integer.parseInt(boardId));
				user.getManBoards().add(board);//这里是多对多关系
				userService.update(user);
				view.setViewName("/success");
			}
			return view;
		}catch (Exception e){
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 用户锁定及解锁管理页面
	 * @return
	 */
	/*
	@RequestMapping(value = "/forum/userLockManagePage", method = RequestMethod.GET)
	public ModelAndView userLockManagePage()  throws Exception {
		ModelAndView view =new ModelAndView();
		List<User> users = userService.getAllUsers();
		view.setViewName("/userLockManage");
		view.addObject("users", users);
		return view;
	}
	 */
	@RequestMapping(value = "/userLockManagePage", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> userLockManagePage()  throws Exception {
		List<User> users = userService.getAllUsers();
		List<String> userIds=new ArrayList<>();
		for(User user:users){
			userIds.add(user.getUserName());
		}
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("users",userIds);//map.put("users",users);
		return map;
	}

	/**
	 * 用户锁定及解锁设定
	 * @return
	 */
	@RequestMapping(value = "/users/locks", method = RequestMethod.PATCH)
	public ModelAndView userLockManage(@RequestParam("userName") String userName
			,@RequestParam("locked") String locked)  throws Exception {
		ModelAndView view =new ModelAndView();
        User user = userService.getUserByUserName(userName);
		if (user == null) {
			view.addObject("errorMsg", "用户名(" + userName
					+ ")不存在");
			view.setViewName("/fail");
		} else {
			user.setLocked(Integer.parseInt(locked));
			userService.update(user);
			view.setViewName("/success");
		}
		return view;
	}
}
