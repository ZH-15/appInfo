package cn.app.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.app.dao.AppInfoMapper;
import cn.app.dao.AppVersionMapper;
import cn.app.pojo.AppVersion;
import cn.app.service.AppVersionService;
@Service("AppVersionService")
public class AppVersionServiceImpl implements AppVersionService {

	@Resource
	private AppVersionMapper appVersionMapper;
	@Resource
	private AppInfoMapper appInfoMapper;
	
	@Override
	public List<AppVersion> findAppVersionsByAPPId(int id) {
		return appVersionMapper.findAppVersionsByAPPId(id);
	}

	@Override
	public boolean insertAppVersion(AppVersion appVersion) {
		int i = appVersionMapper.insertAppVersion(appVersion);
		if(i!=0){
			if(appInfoMapper.updateAppInfoVersionId(appVersion.getAppId(), appVersion.getId()) != 0){
				return true;
			}
		}
		
		return false;
	}

	@Override
	public AppVersion findAppVersionById(int id) {
		// TODO Auto-generated method stub
		return appVersionMapper.findAppVersionById(id);
	}

	@Override
	public boolean updateApk(int id) {
		if(appVersionMapper.updateApk(id) != 0){
			return true;
		}
		return false;
	}

	@Override
	public boolean updateVersion(AppVersion appVersion) {
		int i = appVersionMapper.updateVersion(appVersion);
		if(i!=0){
			if(appInfoMapper.updateAppInfoVersionId(appVersion.getAppId(), appVersion.getId()) != 0){
				return true;
			}
		}
		
		return false;
	}

}
