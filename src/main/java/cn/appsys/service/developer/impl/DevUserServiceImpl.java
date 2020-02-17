package cn.appsys.service.developer.impl;

import cn.appsys.dao.developer.DevUserDao;
import cn.appsys.pojo.DevUser;
import cn.appsys.service.developer.DevUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DevUserServiceImpl implements DevUserService {
    @Autowired
    private DevUserDao devUserDao;
    @Override
    public DevUser login(String devCode, String devPassword) {
        DevUser devUser = devUserDao.getLoginUser(devCode);
        if (devUser!=null && devPassword.equals(devUser.getDevPassword())){
            return devUser;
        }
        return null;
    }
}
