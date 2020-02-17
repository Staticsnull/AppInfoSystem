package cn.appsys.dao.developer;

import cn.appsys.pojo.DevUser;
import org.apache.ibatis.annotations.Param;

/**
 * 开发者用户映射接口
 */
public interface DevUserDao {
    DevUser getLoginUser(@Param("devCode") String devCode);
}
