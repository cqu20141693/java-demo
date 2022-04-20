package com.gow.user.service.impl;

import com.gow.user.model.UserInfo;
import com.gow.user.service.UserService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    @Override
    public List<UserInfo> list() {
        UserInfo gow = new UserInfo().setUserKey("001").setUsername("gow").setPassword("CC@123");
        ArrayList<UserInfo> objects = new ArrayList<>();
        objects.add(gow);
        return objects;
    }

    @Override
    public UserInfo getUserInfo() {
        return new UserInfo().setUserKey("001").setUsername("gow").setPassword("CC@123");
    }
}
