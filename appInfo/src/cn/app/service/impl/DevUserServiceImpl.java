package cn.app.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.app.dao.DevUserMapper;
import cn.app.pojo.DevUser;
import cn.app.service.DevUserService;

@Service("DevUserService")
public class DevUserServiceImpl implements DevUserService {

	@Resource
	private DevUserMapper devUserMapper;
	
	@Override
	public DevUser login(String devCode, String devPwd) {
		DevUser devUser = devUserMapper.findDevUserByDevCode(devCode);
		if(devUser != null){
			if(devUser.getDevPassword().equals(devPwd)){
				return devUser;
			}
		}
		return null;
	}

}
