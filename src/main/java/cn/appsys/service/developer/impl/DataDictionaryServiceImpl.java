package cn.appsys.service.developer.impl;

import java.util.List;

import javax.annotation.Resource;

import cn.appsys.dao.developer.DataDictionaryDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.appsys.pojo.DataDictionary;
import cn.appsys.service.developer.DataDictionaryService;
@Service
public class DataDictionaryServiceImpl implements DataDictionaryService{
	@Autowired
	private DataDictionaryDao dataDictionaryDao;
	@Override
	public List<DataDictionary> getDataDictionaryList(String typeCode)
			throws Exception {
		return dataDictionaryDao.getDataDictionaryList(typeCode);
	}

}
