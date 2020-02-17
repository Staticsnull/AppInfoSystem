package cn.appsys.dao.developer;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.appsys.pojo.DataDictionary;


/**
 * 开发者数据字典映射接口
 * @author Administrator
 *
 */
public interface DataDictionaryDao {
	
	List<DataDictionary> getDataDictionaryList(@Param("typeCode") String typeCode);

}
