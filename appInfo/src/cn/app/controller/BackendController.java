package cn.app.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/backend")
public class BackendController {
	@RequestMapping("/main.html")
	public String main(){
		return "backend/BackendMain";
	}
}
