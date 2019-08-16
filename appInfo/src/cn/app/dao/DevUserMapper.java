package cn.app.dao;

import org.apache.ibatis.annotations.Param;

import cn.app.pojo.DevUser;

public interface DevUserMapper {
	/**
	 * 根据编号查询开发者
	 * @param devCode
	 * @return
	 */
	public DevUser findDevUserByDevCode(@Param("DevCode") String devCode);
}
