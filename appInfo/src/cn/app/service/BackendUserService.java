package cn.app.service;

import cn.app.pojo.BackendUser;
import cn.app.pojo.DevUser;

public interface BackendUserService {
	/**
	 * 登录
	 * @param devCode
	 * @param devPwd
	 * @return
	 */
	public BackendUser login(String backendCode,String backendPwd);
}
