package cn.appsys.service.developer;

import java.util.List;

import cn.appsys.pojo.AppInfo;

public interface AppInfoService {
	
	int getAppInfoCount(String querySoftwareName, Integer queryStatus,
                        Integer queryCategoryLevel1, Integer queryCategoryLevel2,
                        Integer queryCategoryLevel3, Integer queryFlatformId,
                        Integer devId)throws Exception;

	List<AppInfo> getAppInfoList(String querySoftwareName, Integer queryStatus,
                                 Integer queryCategoryLevel1, Integer queryCategoryLevel2,
                                 Integer queryCategoryLevel3, Integer queryFlatformId,
                                 Integer devId, Integer currentPageNo, Integer pageSize) throws Exception;

	AppInfo getAppInfo(Integer id, String APKName) throws Exception;

	boolean add(AppInfo appInfo) throws Exception;

	boolean deleteAppLogo(Integer id) throws Exception;

	boolean modify(AppInfo appInfo) throws Exception;
	/**
	 * 根据app的id删除app信息 
	 * 1.通过appId 查询是否有appVersion信息,查询app_version表中是否有关联的数据
	 * 2.若有关联的app_version 信息 ,则先删除版本信息,然后删除app信息
	 * 3.若没有关联的app_version 信息,则直接删除app信息
	 * @param id
	 * @return
	 * @throws Exception
	 */
	boolean appsysDeleteAppInfoById(int id) throws Exception;

	boolean appsysUpdateSaleStatusByAppId(AppInfo appInfo) throws Exception;

}
