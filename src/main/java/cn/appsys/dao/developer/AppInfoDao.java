package cn.appsys.dao.developer;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.appsys.pojo.AppInfo;

public interface AppInfoDao {
	
	/**
	 * 根据软件名称,app状态,一级分类,二级分类,三级分类,平台id
	 * 开发者id查询总记录数
	 * @param softwareName
	 * @param queryStatus
	 * @param queryCategoryLevel1
	 * @param queryCategoryLevel2
	 * @param queryCategoryLevel3
	 * @param queryFormId
	 * @param devId
	 * @return
	 * @throws Exception
	 */
	int getAppInfoCount(@Param("softwareName") String querySoftwareName,
                        @Param("status") Integer queryStatus,
                        @Param("categoryLevel1") Integer queryCategoryLevel1,
                        @Param("categoryLevel2") Integer queryCategoryLevel2,
                        @Param("categoryLevel3") Integer queryCategoryLevel3,
                        @Param("flatformId") Integer queryFormId,
                        @Param("devId") Integer devId) throws Exception;
	/**
	 * 根据软件名称,app状态,一级,二级,三级分类,
	 * 平台id,开发者id,起始位置,每页显示的记录数 查询Appinfo信息
	 * @param querySoftwareName
	 * @param queryStatus
	 * @param queryCategoryLevel1
	 * @param queryCategoryLevel2
	 * @param queryCategoryLevel3
	 * @param queryFormId
	 * @param devId
	 * @param startIndex
	 * @param pageSize
	 * @return
	 * @throws Exception
	 */
	List<AppInfo> getAppInfoList(@Param("softwareName") String querySoftwareName,
                                 @Param("status") Integer queryStatus,
                                 @Param("categoryLevel1") Integer queryCategoryLevel1,
                                 @Param("categoryLevel2") Integer queryCategoryLevel2,
                                 @Param("categoryLevel3") Integer queryCategoryLevel3,
                                 @Param("flatformId") Integer queryFlatformId,
                                 @Param("devId") Integer devId,
                                 @Param("startIndex") Integer startIndex,
                                 @Param("pageSize") Integer pageSize) throws Exception;
	AppInfo getAppInfo(@Param("id") Integer id, @Param("APKName") String aPKName) throws Exception;
	int add(AppInfo appInfo) throws Exception;
	int deleteAppLogo(@Param("id") Integer id) throws Exception;
	int modify(AppInfo appInfo) throws Exception;
	/**
	 * 根据appId 修改versionId
	 * @param versionId
	 * @param appId
	 * @return
	 * @throws Exception
	 */
	int updateVersionId(@Param("versionId") Integer versionId,
                        @Param("id") Integer appId) throws Exception;
	int deleteAppInfoById(@Param("id") int id);
	
	int updateStatus(@Param("status") Integer status, @Param("id") Integer id);
	

}
