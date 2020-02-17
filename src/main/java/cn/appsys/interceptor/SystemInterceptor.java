package cn.appsys.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import cn.appsys.pojo.BackendUser;
import cn.appsys.pojo.DevUser;
import cn.appsys.tools.Constants;



/**
 * 系统拦截器 拦截后台管理用户和开发者用户
 * @author Administrator
 *
 */
public class SystemInterceptor implements HandlerInterceptor {
	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		HttpSession session = request.getSession();
		DevUser devUser = (DevUser)session.getAttribute(Constants.DEV_USER_SESSION);
		BackendUser backendUser = (BackendUser)session.getAttribute(Constants.USER_SESSION);
		if(null != devUser){//开发者用户登录成功
			return true;
		}else if(null != backendUser){//后台管理用户登录成功
			return true;
		}else{
			response.sendRedirect(request.getContextPath()+"/403.jsp");
			return false;
		}
	}

}
