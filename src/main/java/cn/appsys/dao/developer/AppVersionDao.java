package cn.appsys.dao.developer;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.appsys.pojo.AppVersion;
import cn.appsys.pojo.DevUser;

/**
 * 开发者AppVersion映射接口
 * @author Administrator
 *
 */
public interface AppVersionDao {
	/**
	 * 根据appId查询相应的app版本列表
	 * @param appId
	 * @return
	 * @throws Exception
	 */
	List<AppVersion> getAppVersionListByAppId(@Param("appId") Integer appId) throws Exception;
	
	/**
	 * 保存新增app版本信息
	 * @param appVersion
	 * @return
	 * @throws Exception
	 */
	int add(AppVersion appVersion) throws Exception;
	
	/**
	 * 根据appVersion id获取appVersion信息
	 * @param versionId
	 * @return
	 * @throws Exception
	 */
	AppVersion getAppVersionById(@Param("id") Integer versionId) throws Exception;
	/**
	 * 根据appVersion id 修改app信息
	 * @param appVersion
	 * @return
	 */
	int modify(AppVersion appVersion) throws Exception;

	int deleteAPKFile(@Param("id") Integer id) throws Exception;
	/**
	 * 根据appId 获取app版本信息记录
	 * @param id
	 * @return
	 */
	int getAppVersionCountByAppId(@Param("appId") int appId);
	
	
	int deleteAppVersionById(@Param("appId") int appId);
	

}
