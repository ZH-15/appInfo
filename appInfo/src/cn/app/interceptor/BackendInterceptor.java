package cn.app.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import cn.app.pojo.BackendUser;
import cn.app.util.Constants;

public class BackendInterceptor extends HandlerInterceptorAdapter {
	
	private Logger logger = Logger.getLogger(BackendInterceptor.class);
	
	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		logger.info("进入BackendInterceptor");
		BackendUser backendUser = (BackendUser) request.getSession().getAttribute(Constants.BACKEND_USER);
		if(backendUser == null){
			response.sendRedirect(request.getContextPath()+"/403.jsp");
			return false;
		}
		return true;
	}
}
