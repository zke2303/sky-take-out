package com.sky.service;

import com.sky.dto.UserLoginDTO;
import com.sky.vo.UserLoginVO;

/**
 * 微信用户操作接口
 *
 * @Author itcast
 * @Create 2024/6/15
 **/
public interface UserService {
    /**
     * 微信用户登录
     * @param userLoginDTO
     * @return
     */
    UserLoginVO wxLogin(UserLoginDTO userLoginDTO);
}
