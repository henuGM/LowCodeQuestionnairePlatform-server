package com.wenjuan.server.service.impl;

import com.wenjuan.server.mapper.UserMapper;
import com.wenjuan.server.pojo.User;
import com.wenjuan.server.service.UserService;
import com.wenjuan.server.utils.Md5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public User findByUserName(String username) {
        User u=userMapper.findByUserName(username);
        return u;
    }

    @Override
    public void register(String username, String password) {
        String md5String = Md5Util.getMD5String(password);
        userMapper.add(username,md5String);
    }

}
