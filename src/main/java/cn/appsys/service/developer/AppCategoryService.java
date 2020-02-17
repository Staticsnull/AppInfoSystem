package cn.appsys.service.developer;

import java.util.List;

import cn.appsys.pojo.AppCategory;

public interface AppCategoryService {

	List<AppCategory> getCatagoryListByParentId(Integer parentId) throws Exception;

}
