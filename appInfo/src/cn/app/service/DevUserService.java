package cn.app.service;

import cn.app.pojo.DevUser;

public interface DevUserService {
	/**
	 * 登录
	 * @param devCode
	 * @param devPwd
	 * @return
	 */
	public DevUser login(String devCode,String devPwd);
}
