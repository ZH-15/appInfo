package cn.app.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.app.dao.BackendUserMapper;
import cn.app.pojo.BackendUser;
import cn.app.pojo.DevUser;
import cn.app.service.BackendUserService;

@Service
public class BackendUserServiceImpl implements BackendUserService {

	@Resource
	private BackendUserMapper backendUserMapper;
	
	@Override
	public BackendUser login(String backendCode, String backendPwd) {
		BackendUser backendUser = backendUserMapper.findBackendUserByDevCode(backendCode);
		if(backendUser != null){
			if(backendUser.getUserPassword().equals(backendPwd)){
				return backendUser;
			}
		}
		return null;
	}

}
