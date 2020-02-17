package cn.appsys.controller.backend;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import cn.appsys.pojo.BackendUser;
import cn.appsys.service.backend.BackendUserService;
import cn.appsys.tools.Constants;
/**
 * 后台管理系统用户登录控制器
 * @author Administrator
 *
 */
@Controller
@RequestMapping("/manager")
public class BackendLoginController {
	private Logger logger = LogManager.getLogger(BackendLoginController.class);
	@Autowired
	private BackendUserService backendUserService;
	@RequestMapping("/login")
	public String login(){
		logger.info("BackendLoginController == login==>");
		return "backendlogin";
	}
	
	@RequestMapping(value="/dologin",method=RequestMethod.POST)
	public String doLogin(@RequestParam("userCode") String userCode,
				@RequestParam("userPassword") String userPassword,
				HttpServletRequest request,HttpSession session){
		BackendUser user = null;
		try {
			user = backendUserService.login(userCode, userPassword);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(null != user){
			session.setAttribute(Constants.USER_SESSION, user);
			return "redirect:/manager/backend/main";
		}else{
			request.setAttribute("error", "用户名或者密码有误!");
			return "backendlogin";
		}
	}
	@RequestMapping("/backend/main")
	public String main(HttpSession session){
		if(null == session.getAttribute(Constants.USER_SESSION)){
			return "redirect:/manager/login";
		}
		return "backend/main";
	}
	@RequestMapping("/logout")
	public String logout(HttpSession session){
		session.removeAttribute(Constants.USER_SESSION);
		return "backendlogin";
	}
}
