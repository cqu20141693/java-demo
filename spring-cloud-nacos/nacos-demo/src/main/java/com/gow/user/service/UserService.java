package com.gow.user.service;

import com.gow.user.model.UserInfo;

import java.util.List;

public interface UserService {

    List<UserInfo> list();

    UserInfo getUserInfo();
}
