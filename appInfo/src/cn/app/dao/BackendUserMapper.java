package cn.app.dao;

import org.apache.ibatis.annotations.Param;

import cn.app.pojo.BackendUser;
import cn.app.pojo.DevUser;

public interface BackendUserMapper {
	/**
	 * 根据编号查询管理员
	 * @param devCode
	 * @return
	 */
	public BackendUser findBackendUserByDevCode(@Param("BackendCode") String BackendCode);
}
