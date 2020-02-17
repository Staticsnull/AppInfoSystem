package cn.appsys.service.developer.impl;

import java.util.List;

import javax.annotation.Resource;

import cn.appsys.dao.developer.AppInfoDao;
import cn.appsys.dao.developer.AppVersionDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.appsys.pojo.AppVersion;
import cn.appsys.service.developer.AppVersionService;

@Service
public class AppVersionServiceImpl implements AppVersionService {
	@Autowired
	private AppVersionDao appVersionDao;
	@Autowired
	private AppInfoDao appInfoDao;
	@Override
	public List<AppVersion> getAppVersionListByAppId(Integer appId)
			throws Exception {
		return appVersionDao.getAppVersionListByAppId(appId);
	}
	@Override
	public boolean appsysAdd(AppVersion appVersion) throws Exception {
		boolean flag = false;
		Integer versionId = null;
		if(appVersionDao.add(appVersion) > 0){
			flag = true;
			versionId = appVersion.getId();
		}
//		if(appInfoDao.updateVersionId(versionId,appVersion.getAppId())>0 && flag){
//			flag = true;
//		}
//		return flag;
		//根据appInfo中id(来自appVersion中的appId)修改versionId
		return appInfoDao.updateVersionId(versionId,appVersion.getAppId())>0 && flag?true:false;
		
	}
	@Override
	public AppVersion getAppVersionById(Integer versionId) throws Exception {
		return appVersionDao.getAppVersionById(versionId);
	}
	@Override
	public boolean modify(AppVersion appVersion) throws Exception {
		return appVersionDao.modify(appVersion)>0 ? true : false;
	}
	@Override
	public boolean deleteAPKFile(Integer id) throws Exception {
		return appVersionDao.deleteAPKFile(id) > 0 ? true : false;
	}

}
