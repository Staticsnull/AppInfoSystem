package cn.appsys.dao.developer;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.appsys.pojo.AppCategory;

/**
 * 开发者分类信息映射接口
 * @author Administrator
 *
 */
public interface AppCategoryDao {
	
	List<AppCategory> getAppCategoryListByParentId(
            @Param("parentId") Integer parentId) throws Exception;
	

}
