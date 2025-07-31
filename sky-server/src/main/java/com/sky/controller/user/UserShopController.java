package com.sky.controller.user;

import com.sky.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用户端店铺操作
 *
 * @Author itcast
 * @Create 2024/6/14
 **/
@RestController
@RequestMapping("/user/shop")
@Api(tags = "用户端店铺状态相关接口")
public class UserShopController {

    public static final String SHOP_STATUS = "SHOP_STATUS";

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 获取营业状态
     * @return
     */
    @GetMapping("/status")
    @ApiOperation("获取营业状态")
    public Result<Integer> getStatus(){
        Integer status = (Integer) redisTemplate.opsForValue().get(SHOP_STATUS);
        return Result.success(status);
    }

}
