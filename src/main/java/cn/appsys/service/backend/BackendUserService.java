package cn.appsys.service.backend;

import cn.appsys.pojo.BackendUser;

public interface BackendUserService {
	
	BackendUser login(String userCode, String userPassword) throws Exception;

}
