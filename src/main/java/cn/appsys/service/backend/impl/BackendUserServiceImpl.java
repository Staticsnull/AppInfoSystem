package cn.appsys.service.backend.impl;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.appsys.dao.backend.BackendUserDao;
import cn.appsys.pojo.BackendUser;
import cn.appsys.service.backend.BackendUserService;

@Service
public class BackendUserServiceImpl implements BackendUserService {
	@Autowired
	private BackendUserDao backendUserDao;
	@Override
	public BackendUser login(String userCode, String userPassword)
			throws Exception {
		BackendUser user = null;
		user = backendUserDao.getLoginUser(userCode);
		if(null != user){
			if(!userPassword.equals(user.getUserPassword())){
				user = null;
			}
		}
		return user;
	}

}
