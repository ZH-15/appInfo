package cn.app.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/dev")
public class DevController {
	@RequestMapping("/main.html")
	public String main(){
		return "dev/DevMain";
	}
	
	@RequestMapping("/appList.html")
	public String appList(){
		return "dev/appList";
	}
}
