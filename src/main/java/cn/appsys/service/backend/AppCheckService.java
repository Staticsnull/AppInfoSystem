package cn.appsys.service.backend;

import java.util.List;

import cn.appsys.pojo.AppInfo;

public interface AppCheckService {
	
	/**
	 * 根据软件名称 分类id 平台id获取总记录数
	 * @param querySoftwareName
	 * @param queryCategoryLevel1
	 * @param queryCategoryLevel2
	 * @param queryCategoryLevel3
	 * @param queryFlatformId
	 * @return
	 */
	int getAppInfoCount(String querySoftwareName, Integer queryCategoryLevel1,
                        Integer queryCategoryLevel2, Integer queryCategoryLevel3,
                        Integer queryFlatformId) throws Exception;
	
	/**
	 * 根据软件名称 分类id 当前页面 页面记录数 查询待审核的appinfo
	 * @param querySoftwareName
	 * @param queryCategoryLevel1
	 * @param queryCategoryLevel2
	 * @param queryCategoryLevel3
	 * @param currentPageNo
	 * @param pageSize
	 * @return
	 * @throws Exception
	 */
	List<AppInfo> getAppInfoList(String querySoftwareName,
                                 Integer queryCategoryLevel1, Integer queryCategoryLevel2,
                                 Integer queryCategoryLevel3, Integer queryFlatformId,
                                 int currentPageNo, int pageSize) throws Exception;
	
	AppInfo getAppInfoById(Integer appId) throws Exception;
	/**
	 * 根据id修改appInfo的状态信息
	 * @param status
	 * @param id
	 * @return
	 */
	boolean updateStatus(Integer status, Integer id) throws Exception;

}
