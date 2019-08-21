package cn.app.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.app.dao.AppInfoMapper;
import cn.app.pojo.AppInfo;
import cn.app.service.AppInfoService;

@Service("AppInfoService")
public class AppInfoServiceImpl implements AppInfoService {

	@Resource
	private AppInfoMapper appInfoMapper;
	
	@Override
	public int findAppInfoCount(String querySoftwareName, Integer queryStatus,
			Integer queryFlatformId, Integer queryCategoryLevel1,
			Integer queryCategoryLevel2, Integer queryCategoryLevel3) {
		return appInfoMapper.findAppInfoCount(querySoftwareName, queryStatus, queryFlatformId, queryCategoryLevel1, queryCategoryLevel2, queryCategoryLevel3);
	}

	@Override
	public List<AppInfo> findAppInfoList(String querySoftwareName,
			Integer queryStatus, Integer queryFlatformId,
			Integer queryCategoryLevel1, Integer queryCategoryLevel2,
			Integer queryCategoryLevel3, int from, int pageSize) {
		from = (from-1)*pageSize;
		return appInfoMapper.findAppInfoList(querySoftwareName, queryStatus, queryFlatformId, queryCategoryLevel1, queryCategoryLevel2, queryCategoryLevel3, from, pageSize);
	}

	@Override
	public boolean findAppInfoByAPKName(String apkName) {
		if(appInfoMapper.findAppInfoByAPKName(apkName) >= 1)
		{
			return true;
		}else{
			return false;
		}
	}

	@Override
	public boolean insertAppInfo(AppInfo appInfo) {
		if(appInfoMapper.insertAppInfo(appInfo) >= 1){
			return true;
		}else{
			return false;
		}
	}

	@Override
	public AppInfo findAppInfoById(int id) {
		// TODO Auto-generated method stub
		return appInfoMapper.findAppInfoById(id);
	}

	@Override
	public boolean updateApplogo(int id) {
		if(appInfoMapper.updateApplogo(id) >= 1){
			return true;
		}else{
			return false;
		}
	}

	@Override
	public boolean updateAppInfo(AppInfo appInfo) {
		if(appInfoMapper.updateAppInfo(appInfo) >=1){
			return true;
		}else{
			return false;
		}
	}

}
