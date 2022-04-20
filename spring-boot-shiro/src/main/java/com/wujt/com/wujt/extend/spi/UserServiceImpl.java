package com.wujt.com.wujt.extend.spi;

import com.wujt.com.wujt.extend.model.StatusEnum;
import com.wujt.com.wujt.extend.model.UserInfo;
import com.wujt.com.wujt.shiro.domain.SysUserInfo;
import com.wujt.com.wujt.shiro.spi.UserService;
import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.utility.RandomString;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.springframework.stereotype.Component;

/**
 * @author wujt
 */
@Component
@Slf4j
public class UserServiceImpl implements UserService {

    @Override
    public SysUserInfo getByName(String username) {
        log.info("getUserByName");
        UserInfo userInfo = new UserInfo();
        userInfo.setUsername(username);
        String password = "admin123";
        String salt = RandomString.make(10);
        String toHex = new SimpleHash("MD5", password, salt.getBytes(), 2).toHex();


        userInfo.setPassword(toHex);
        userInfo.setSalt(salt);
        userInfo.setStatus(StatusEnum.ENABLE.getValue());
        userInfo.setUserKey("userKey");
        return userInfo;
    }
}
