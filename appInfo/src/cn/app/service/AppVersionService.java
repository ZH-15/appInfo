package cn.app.service;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.app.pojo.AppVersion;

public interface AppVersionService {
	/**
	 * 根据appid查找版本信息
	 * @param id
	 * @return
	 */
	public List<AppVersion> findAppVersionsByAPPId(int id);
	
	/**
	 * 添加版本
	 * @param appVersion
	 * @return
	 */
	public boolean insertAppVersion(AppVersion appVersion);
	
	/**
	 * 根据id查找版本信息
	 * @param id
	 * @return
	 */
	public AppVersion findAppVersionById(int id);

	/**
	 * 根据id删除apk信息
	 * @param id
	 * @return
	 */
	public boolean updateApk(int id);
	
	/**
	 * 更改版本信息
	 * @param appVersion
	 * @return
	 */
	public boolean updateVersion(AppVersion appVersion);
}