package com.sky.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sky.constant.JwtClaimsConstant;
import com.sky.constant.MessageConstant;
import com.sky.dto.UserLoginDTO;
import com.sky.entity.User;
import com.sky.exception.LoginFailedException;
import com.sky.mapper.UserMapper;
import com.sky.properties.JwtProperties;
import com.sky.properties.WeChatProperties;
import com.sky.service.UserService;
import com.sky.utils.HttpClientUtil;
import com.sky.utils.JwtUtil;
import com.sky.vo.UserLoginVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * desc
 *
 * @Author itcast
 * @Create 2024/6/15
 **/
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private WeChatProperties weChatProperties;
    @Autowired
    private JwtProperties jwtProperties;
    @Autowired
    private UserMapper userMapper;

    public static final String WX_LOGIN = "https://api.weixin.qq.com/sns/jscode2session";

    /**
     * 微信用户登录
     * @param userLoginDTO
     * @return
     */
    public UserLoginVO wxLogin(UserLoginDTO userLoginDTO) {
        //调用微信登录接口，获取当前微信用户的唯一标识openid
        String openid = getOpenid(userLoginDTO.getCode());

        if(openid == null){
            //登录失败，抛出业务异常
            throw new LoginFailedException(MessageConstant.LOGIN_FAILED);
        }

        User user = userMapper.getByOpenid(openid);
        if(user == null){
            //新用户，需要自动完成注册
            user = new User();
            user.setOpenid(openid);
            //user.setAvatar();
            //user.setPhone();
            userMapper.insert(user);
        }

        //为用户生成jwt令牌
        Map<String, Object> map = new HashMap<>();
        map.put(JwtClaimsConstant.USER_ID,user.getId());
        String token = JwtUtil.createJWT(jwtProperties.getUserSecretKey(), jwtProperties.getUserTtl(), map);

        //封装返回结果UserLoginVO
        UserLoginVO userLoginVO = UserLoginVO.builder()
                .id(user.getId())
                .openid(openid)
                .token(token)
                .build();

        return userLoginVO;
    }

    /**
     * 获取微信用户的openid
     * @param code
     * @return
     */
    public String getOpenid(String code){
        Map<String, String> map = new HashMap<>();
        map.put("appid",weChatProperties.getAppid());
        map.put("secret",weChatProperties.getSecret());
        map.put("js_code",code);
        map.put("grant_type","authorization_code");
        String response = HttpClientUtil.doGet(WX_LOGIN, map);
        //从结果中解析出openid
        JSONObject jsonObject = JSON.parseObject(response);
        String openid = (String) jsonObject.get("openid");
        return openid;
    }

}
