package cn.appsys.dao.backend;

import org.apache.ibatis.annotations.Param;

import cn.appsys.pojo.BackendUser;

/**
 * 后台管理系统dao层
 * @author Administrator
 *
 */
public interface BackendUserDao {
	/**
	 * 根据用户编码获取用户信息
	 * @param userCode
	 * @return
	 */
	BackendUser getLoginUser(@Param("userCode") String userCode) throws Exception;
	

}
