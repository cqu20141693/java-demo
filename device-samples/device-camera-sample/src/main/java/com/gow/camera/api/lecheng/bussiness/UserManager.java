package com.gow.camera.api.lecheng.bussiness;

import com.gow.camera.api.lecheng.model.resp.AccessToken;
import com.gow.camera.api.lecheng.model.resp.ResponseResult;
import java.util.HashMap;

public interface UserManager {

    /**
     * 获取管理员token
     * 根据管理员账号appId和appSecret获取accessToken，appId和appSecret可以在控制台-我的应用-应用信息中找到。
     *
     * @param paramMap 参数
     */
    ResponseResult<AccessToken> accessToken(HashMap<String, Object> paramMap);

}
