package cn.app.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.app.pojo.AppInfo;

public interface AppInfoMapper {
	
	/**
	 * 根据软件名称，app状态，所属平台，分类查询总数
	 * @param querySoftwareName
	 * @param queryStatus
	 * @param queryFlatformId
	 * @param queryCategoryLevel1
	 * @param queryCategoryLevel2
	 * @param queryCategoryLevel3
	 * @return
	 */
	public int findAppInfoCount(@Param("querySoftwareName")String querySoftwareName,
			@Param("queryStatus")Integer queryStatus,
			@Param("queryFlatformId")Integer queryFlatformId,
			@Param("queryCategoryLevel1")Integer queryCategoryLevel1,
			@Param("queryCategoryLevel2")Integer queryCategoryLevel2,
			@Param("queryCategoryLevel3")Integer queryCategoryLevel3);
	
	/**
	 * 根据软件名称，app状态，所属平台，分类查询APP列表
	 * @param querySoftwareName
	 * @param queryStatus
	 * @param queryFlatformId
	 * @param queryCategoryLevel1
	 * @param queryCategoryLevel2
	 * @param queryCategoryLevel3
	 * @param from
	 * @param pageSize
	 * @return
	 */
	public List<AppInfo> findAppInfoList(@Param("querySoftwareName")String querySoftwareName,
			@Param("queryStatus")Integer queryStatus,
			@Param("queryFlatformId")Integer queryFlatformId,
			@Param("queryCategoryLevel1")Integer queryCategoryLevel1,
			@Param("queryCategoryLevel2")Integer queryCategoryLevel2,
			@Param("queryCategoryLevel3")Integer queryCategoryLevel3,
			@Param("from")int from,@Param("pageSize")int pageSize);
	
	/**
	 * 查找有没有重复的APKName
	 * @param apkName
	 * @return
	 */
	public int findAppInfoByAPKName(@Param("APKName") String apkName);
	
	/**
	 * 添加APP信息
	 * @param appInfo
	 * @return
	 */
	public int insertAppInfo(AppInfo appInfo);
	
	/**
	 * 根据id查找app信息
	 * @param id
	 * @return
	 */
	public AppInfo findAppInfoById(@Param("id")int id);
	
	/**
	 * 根据id删除app图片
	 * @param id
	 * @return
	 */
	public int updateApplogo(@Param("id")int id);
	
	/**
	 * 修改APP
	 * @param appInfo
	 * @return
	 */
	public int updateAppInfo(AppInfo appInfo);
}
