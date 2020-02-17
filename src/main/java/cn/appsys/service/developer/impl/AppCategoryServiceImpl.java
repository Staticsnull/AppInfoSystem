package cn.appsys.service.developer.impl;

import java.util.List;

import javax.annotation.Resource;

import cn.appsys.dao.developer.AppCategoryDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import cn.appsys.pojo.AppCategory;
import cn.appsys.service.developer.AppCategoryService;

@Service
public class AppCategoryServiceImpl implements AppCategoryService {
	@Autowired
	private AppCategoryDao appCategoryDao;
	@Override
	public List<AppCategory> getCatagoryListByParentId(Integer parentId)
			throws Exception {
		return appCategoryDao.getAppCategoryListByParentId(parentId);
	}

}
