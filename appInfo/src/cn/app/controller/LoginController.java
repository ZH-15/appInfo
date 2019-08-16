package cn.app.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import cn.app.pojo.BackendUser;
import cn.app.pojo.DevUser;
import cn.app.service.BackendUserService;
import cn.app.service.DevUserService;
import cn.app.util.Constants;

@Controller
@RequestMapping("/login")
public class LoginController {
	
	@Resource
	private DevUserService devUserService;
	@Resource
	private BackendUserService backendUserService;
	
	@RequestMapping(value="/devUserlogin.html",method=RequestMethod.GET)
	public String toDevUserLogin(){
		return "DevUserLogin";
	}
	
	@RequestMapping(value="/devUserlogin.html",method=RequestMethod.POST)
	public String devUserLogin(@RequestParam("devCode") String devCode,
			@RequestParam("devPassword")String devPassword,
			HttpServletRequest request,
			HttpSession session){
		DevUser devUser = devUserService.login(devCode, devPassword);
		if(devUser != null){
			session.setAttribute(Constants.DEV_USER, devUser);
			return "redirect:/dev/main.html";
		}else{
			request.setAttribute("error", "账号或密码输入错误");
			return "DevUserLogin";
		}
		
	}
	
	@RequestMapping("/devlogout.html")
	public String devLogout(HttpSession session){
		session.removeAttribute(Constants.DEV_USER);
		return "DevUserLogin";
	}
	
	@RequestMapping(value="/backendLogin.html",method=RequestMethod.GET)
	public String toBackendLogin(){
		return "BackendLogin";
	}
	
	@RequestMapping(value="/backendLogin.html",method=RequestMethod.POST)
	public String backendUserLogin(@RequestParam("backendCode") String backendCode,
			@RequestParam("backendPassword")String backendPassword,
			HttpServletRequest request,
			HttpSession session){
		BackendUser backendUser = backendUserService.login(backendCode, backendPassword);
		if(backendUser != null){
			session.setAttribute(Constants.BACKEND_USER, backendUser);
			return "redirect:/backend/main.html";
		}else{
			request.setAttribute("error", "账号或密码输入错误");
			return "BackendLogin";
		}
	}
	
	@RequestMapping("/backendlogout.html")
	public String backendLogout(HttpSession session){
		session.removeAttribute(Constants.BACKEND_USER);
		return "BackendLogin";
	}
}
