package cn.appsys.service.backend.impl;

import java.util.List;

import javax.annotation.Resource;

import cn.appsys.dao.developer.AppInfoDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.appsys.pojo.AppInfo;
import cn.appsys.service.backend.AppCheckService;

@Service
public class AppCheckServiceImpl implements AppCheckService {
	@Autowired
	private AppInfoDao appInfoDao;
	@Override
	public int getAppInfoCount(String querySoftwareName,
			Integer queryCategoryLevel1, Integer queryCategoryLevel2,
			Integer queryCategoryLevel3, Integer queryFlatformId)
			throws Exception {
		return appInfoDao.getAppInfoCount(querySoftwareName, 1, 
				queryCategoryLevel1, queryCategoryLevel2, 
				queryCategoryLevel3, queryFlatformId, null);
	}

	@Override
	public List<AppInfo> getAppInfoList(String querySoftwareName,
			Integer queryCategoryLevel1, Integer queryCategoryLevel2,
			Integer queryCategoryLevel3, Integer queryFlatformId,
			int currentPageNo, int pageSize)throws Exception {
		return appInfoDao.getAppInfoList(querySoftwareName, 1, 
				queryCategoryLevel1, queryCategoryLevel2, 
				queryCategoryLevel3, queryFlatformId, null, 
				(currentPageNo-1)*pageSize, pageSize);
	}

	@Override
	public AppInfo getAppInfoById(Integer appId) throws Exception {
		return appInfoDao.getAppInfo(appId, null);
	}

	@Override
	public boolean updateStatus(Integer status, Integer id) throws Exception {
		return appInfoDao.updateStatus(status,id)>0?true:false;
	}

}
