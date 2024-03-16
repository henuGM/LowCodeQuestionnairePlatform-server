package com.wenjuan.server.controller;

import com.wenjuan.server.pojo.Result;
import com.wenjuan.server.pojo.User;
import com.wenjuan.server.service.UserService;
import com.wenjuan.server.utils.JwtUtil;
import com.wenjuan.server.utils.Md5Util;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/user")
@Validated
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public Result register(String username, String password){
        User u=userService.findByUserName(username);
        if(u==null){
            userService.register(username,password);
            return Result.success(username);
        }else{
            return Result.error("用户名已被占用");
        }
    }

    @PostMapping("/login")
    public Result<String> login( String username,  String password) {
        //根据用户名查询用户
        User loginUser = userService.findByUserName(username);
        //判断该用户是否存在
        if (loginUser == null) {
            return Result.error("!23");
        }

        //判断密码是否正确  loginUser对象中的password是密文
        if (Md5Util.getMD5String(password).equals(loginUser.getPassword())) {
            //登录成功
            Map<String, Object> claims = new HashMap<>();
            claims.put("id", loginUser.getId());
            claims.put("username", loginUser.getUsername());
            String token = JwtUtil.genToken(claims);
            Map<String, Object> res = new HashMap<>();
            res.put("token",token);
            //把token存储到redis中
//            ValueOperations<String, String> operations = stringRedisTemplate.opsForValue();
//            operations.set(token,token,1, TimeUnit.HOURS);
            return Result.success(token);
        }
        return Result.error("密码错误");
    }

    @GetMapping("/info")
    public Result<Map<String,Object>> userInfo(@RequestHeader(name = "Authorization") String token){
        Map<String,Object> map =JwtUtil.parseToken(token);
        String username=(String) map.get("username");
        Map<String,Object> res =new HashMap<>();;
        res.put("username",username);
        res.put("nickname",username);
        User user=userService.findByUserName(username);
        return Result.success(res);
    }

}
