package com.wenjuan.server.service;

import com.wenjuan.server.pojo.User;

public interface UserService {
    User findByUserName(String username);

    void register(String username, String password);

}
