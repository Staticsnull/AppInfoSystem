package cn.appsys.service.developer;

import java.util.List;

import cn.appsys.pojo.AppVersion;

/**
 * appVersion 业务接口
 * @author Administrator
 *
 */
public interface AppVersionService {
	
	/**
	 * 
	 * @param appId
	 * @return
	 */
	List<AppVersion> getAppVersionListByAppId(Integer appId) throws Exception;
	/**
	 * 保存新增app版本信息
	 * @param appVersion
	 * @return
	 */
	boolean appsysAdd(AppVersion appVersion) throws Exception;
	/**
	 * 根据appVersion id 查找appVersion信息
	 * @param versionId
	 * @return
	 * @throws Exception
	 */
	AppVersion getAppVersionById(Integer versionId) throws Exception;
	/**
	 * 根据AppVersion id修改appVersion信息
	 * @param appVersion
	 * @return
	 * @throws Exception
	 */
	boolean modify(AppVersion appVersion) throws Exception;
	/**
	 * 根据id 将appVersion中的字段APKLocPath 设置为null
	 * @param id
	 * @return
	 * @throws Exception
	 */
	boolean deleteAPKFile(Integer id) throws Exception;
	

}
