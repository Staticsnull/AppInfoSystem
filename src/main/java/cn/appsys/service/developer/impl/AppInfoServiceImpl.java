package cn.appsys.service.developer.impl;

import java.io.File;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import cn.appsys.dao.developer.AppInfoDao;
import cn.appsys.dao.developer.AppVersionDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import cn.appsys.pojo.AppInfo;
import cn.appsys.pojo.AppVersion;
import cn.appsys.service.developer.AppInfoService;

@Service
public class AppInfoServiceImpl implements AppInfoService {
	@Autowired
	private AppInfoDao appInfoDao;
	@Autowired
	private AppVersionDao appVersionDao;
	
	@Override
	public int getAppInfoCount(String querySoftwareName, Integer queryStatus,
			Integer queryCategoryLevel1, Integer queryCategoryLevel2,
			Integer queryCategoryLevel3, Integer queryFlatformId, Integer devId)
			throws Exception {
		
		return appInfoDao.getAppInfoCount(querySoftwareName, queryStatus, 
					queryCategoryLevel1, queryCategoryLevel2, queryCategoryLevel3,
					queryFlatformId, devId);
	}

	@Override
	public List<AppInfo> getAppInfoList(String querySoftwareName,
			Integer queryStatus, Integer queryCategoryLevel1,
			Integer queryCategoryLevel2, Integer queryCategoryLevel3,
			Integer queryFlatformId, Integer devId, Integer currentPageNo,
			Integer pageSize) throws Exception {
		System.out.println("appInfoService===>"+(currentPageNo-1)*pageSize);
		return appInfoDao.getAppInfoList(querySoftwareName,queryStatus, 
				queryCategoryLevel1, queryCategoryLevel2, queryCategoryLevel3, 
				queryFlatformId, devId, (currentPageNo-1)*pageSize, pageSize);
	}

	@Override
	public AppInfo getAppInfo(Integer id, String APKName) throws Exception {
		return appInfoDao.getAppInfo(id,APKName);
	}

	@Override
	public boolean add(AppInfo appInfo) throws Exception {
		return appInfoDao.add(appInfo) > 0 ? true : false;
	}

	@Override
	public boolean deleteAppLogo(Integer id) throws Exception {
		return appInfoDao.deleteAppLogo(id) > 0 ? true : false;
	}

	@Override
	public boolean modify(AppInfo appInfo) throws Exception {
		return appInfoDao.modify(appInfo) > 0 ? true : false;
	}

	/**
	 * 根据app的id删除app信息 
	 * 1.通过appId 查询是否有appVersion信息,查询app_version表中是否有关联的数据
	 * 2.若有关联的app_version 信息 ,则先删除版本信息,然后删除app信息
	 * 3.若没有关联的app_version 信息,则直接删除app信息
	 */
	@Override
	public boolean appsysDeleteAppInfoById(int id) throws Exception {
		//根据appinfo id 查询表app_version中是否还有记录
		int versionCount = appVersionDao.getAppVersionCountByAppId(id);
		List<AppVersion> appVersionList = null;
		//若有记录 先删除app版本信息
		if(versionCount > 0){
			appVersionList = appVersionDao.getAppVersionListByAppId(id);
			//删除app版本信息 需先删除apk文件
			for(AppVersion appVersion:appVersionList){
				if(null != appVersion.getApkLocPath() && !"".equals(appVersion.getApkLocPath())){
					File file = new File(appVersion.getApkLocPath());
					if(file.exists()){
						if(!file.delete()){
							throw new RuntimeException("apk文件删除失败");
						}
					}
				}
			}
			appVersionDao.deleteAppVersionById(id);
		}
		//删除appInfo信息
		AppInfo appInfo = appInfoDao.getAppInfo(id, null);
		if(null != appInfo.getLogoLocPath() && !"".equals(appInfo.getLogoLocPath())){
			File file = new File(appInfo.getLogoLocPath());
			if(file.exists()){
				if(!file.delete()){
					throw new RuntimeException("appInfo图片删除失败!");
				}
			}
		}
		return appInfoDao.deleteAppInfoById(id)>0?true:false;
	}
	/**
	 * 上架:
	 * 1.修改app_info中的status的值,由2或者5 改为4 已上架
	 * 2.根据app_info中的versionId修改app_version中的
	 * publishStatus的值为2(审核通过).
	 * 下架:
	 * 修改app_info中的status字段,将值由4修改为5(已下架)
	 */
	@Override
	public boolean appsysUpdateSaleStatusByAppId(AppInfo appInfo)
			throws Exception {
		Integer operator = appInfo.getModifyBy();
		if(operator < 0 || appInfo.getId() < 0){
			throw new RuntimeException("用户id有误,无法进行修改");
		}
		AppInfo app = appInfoDao.getAppInfo(appInfo.getId(), null);
		if(null == app){
			return false;
		}else{
			switch(app.getStatus()){
			case 2://当状态为审核通过时，可以进行上架操作
				onSale(app,operator,4,2);
				break;
			case 5://当状态为下架时，可以进行上架操作
				onSale(app,operator,4,2);
				break;
			case 4://当状态为上架时，可以进行下架操作
				offSale(app,operator,5);
				break;
			default:
				return false;
			}
		}
		return true;
	}


	private void onSale(AppInfo app, Integer operator, 
				int appInfoStatus, int appVersionStatus) throws Exception{
		//修改app_info的状态,修改时间 上架以及下架时间
		offSale(app,operator,appInfoStatus);
		//修改app_version的版本状态 以及发布状态id,修改者以及修改时间
		setSaleSwitchToAppVersion(app,operator,appVersionStatus);
		
	}
	
	private boolean setSaleSwitchToAppVersion(AppInfo app, Integer operator,
			int appVersionStatus) throws Exception{
		AppVersion appVersion = new AppVersion();
		appVersion.setId(app.getVersionId());
		appVersion.setPublishStatus(appVersionStatus);
		appVersion.setModifyBy(operator);
		appVersion.setModifyDate(new Date(System.currentTimeMillis()));
		appVersionDao.modify(appVersion);
		return true;
	}

	private boolean offSale(AppInfo app, Integer operator, 
				int appInfoStatus) throws Exception{
		AppInfo a = new AppInfo();
		a.setId(app.getId());
		a.setModifyBy(operator);
		a.setStatus(appInfoStatus);
		a.setOffSaleDate(new Date(System.currentTimeMillis()));
		
		return appInfoDao.modify(a)>0?true:false;
	}

}
