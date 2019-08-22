package cn.app.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.app.pojo.AppVersion;

public interface AppVersionMapper {
	/**
	 * 根据appid查找版本信息
	 * @param id
	 * @return
	 */
	public List<AppVersion> findAppVersionsByAPPId(@Param("id")int id);
	
	/**
	 * 添加版本
	 * @param appVersion
	 * @return
	 */
	public int insertAppVersion(AppVersion appVersion);
	
	/**
	 * 根据id查找版本信息
	 * @param id
	 * @return
	 */
	public AppVersion findAppVersionById(@Param("id")int id);
	
	/**
	 * 根据id删除apk信息
	 * @param id
	 * @return
	 */
	public int updateApk(@Param("id")int id);
	
	/**
	 * 更改版本信息
	 * @param appVersion
	 * @return
	 */
	public int updateVersion(AppVersion appVersion);
	
	/**
	 * 删除版本信息
	 * @param id
	 * @return
	 */
	public int deleteVersion(@Param("id")int id);
}
