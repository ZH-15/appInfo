package cn.app.service;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.app.pojo.AppInfo;

public interface AppInfoService {
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
	public int findAppInfoCount(String querySoftwareName,Integer queryStatus,
			Integer queryFlatformId,Integer queryCategoryLevel1,Integer queryCategoryLevel2,
			Integer queryCategoryLevel3);
	
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
	public List<AppInfo> findAppInfoList(String querySoftwareName,
			Integer queryStatus,
			Integer queryFlatformId,
			Integer queryCategoryLevel1,
			Integer queryCategoryLevel2,
			Integer queryCategoryLevel3,
			int from,int pageSize);
}
