package cn.appsys.controller.developer;

import cn.appsys.pojo.DevUser;
import cn.appsys.service.developer.DevUserService;
import cn.appsys.tools.Constants;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/dev")
public class DevLoginController {
    @Autowired
    private DevUserService devUserService;

    /**
     * 登录页面
     * @return
     */
    @RequestMapping("/login")
    public String login(){
        return "devlogin";
    }

    private Logger logger = LogManager.getLogger();
    /**
     * 实现用户登录
     * @param devCode
     * @param devPassword
     * @param request
     * @return
     */
    @RequestMapping("/dologin")
    public String doLogin(@RequestParam String devCode,
                          @RequestParam String devPassword,
                          HttpServletRequest request, HttpSession session){
        DevUser user =devUserService.login(devCode, devPassword);
        System.out.println("doLogin======>");
        if(null != user){
            session.setAttribute(Constants.DEV_USER_SESSION,user);
            return "redirect:/dev/flatform/main";
        }else{
            request.setAttribute("error", "用户名或者密码有误!");
            return "devlogin";
        }
    }



    /**
     * 开发者用户主页面
     * @return
     */
    @RequestMapping(value="/flatform/main")
    public String main(HttpSession session){
        logger.info("main===>");
        if(null == session.getAttribute(Constants.DEV_USER_SESSION)){
            return "devlogin";
        }
        return "developer/main";
    }

    /**
     * 注销用户信息
     * @param session
     * @return
     */
    @RequestMapping("/logout")
    public String logout(HttpSession session){
        logger.info("logout===>");
        session.removeAttribute(Constants.DEV_USER_SESSION);
        return "devlogin";
    }


}
