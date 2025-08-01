package com.sky.service.impl;

import com.sky.context.BaseContext;
import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.Dish;
import com.sky.entity.Setmeal;
import com.sky.entity.ShoppingCart;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.mapper.ShoppingCartMapper;
import com.sky.service.ShoppingCartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author zhangke
 * @version 1.0
 * @description
 * @since 2025/7/31
 */

@Service
@Slf4j
public class ShoppingCartServiceImpl implements ShoppingCartService {

    @Autowired
    private ShoppingCartMapper shoppingCartMapper;
    @Autowired
    private DishMapper dishMapper;
    @Autowired
    private SetmealMapper setmealMapper;

    /**
     * 添加购物车
     * @param shoppingCartDTO
     * @return
     */
    @Override
    public void addShoppingCart(ShoppingCartDTO shoppingCartDTO) {
        ShoppingCart shoppingCart = new ShoppingCart();
        BeanUtils.copyProperties(shoppingCartDTO, shoppingCart);
        shoppingCart.setUserId(BaseContext.getCurrentId());
        // 1.查询购物车表中的数据
        List<ShoppingCart> shoppingCartList = shoppingCartMapper.list(shoppingCart);
        // 2.判断当前商品是否存在购物车中
        if (shoppingCartList != null && shoppingCartList.size() > 0) {
            shoppingCart = shoppingCartList.get(0);
            // 当前商品存在购物车中, 此时再进行添加商品就是把number进行加1
            shoppingCart.setNumber(shoppingCart.getNumber() + 1);
            // 更新商品数量
            shoppingCartMapper.updateNumberById(shoppingCart);
        }else {
            // 当前商品不存在购物车中，则把当前的商品添加到购物车中
            // 先判断当前商品是dish还是setmeal
            Long dishId = shoppingCartDTO.getDishId();
            if (dishId != null) {
                // 当前商品是dish
                // 1.查询当前dish的信息
                Dish dish = dishMapper.getById(dishId);
                // 2.把dish中的相关信息赋值给Shopping
                shoppingCart.setImage(dish.getImage());
                shoppingCart.setName(dish.getName());
                shoppingCart.setAmount(dish.getPrice());
            }else {
                // 当前商品是setmeal
                // 1.查询当前setmeal的信息
                Long setmealId = shoppingCartDTO.getSetmealId();
                Setmeal setmeal = setmealMapper.getById(setmealId);
                // 2.设置shoppingCart的信息
                shoppingCart.setImage(setmeal.getImage());
                shoppingCart.setName(setmeal.getName());
                shoppingCart.setAmount(setmeal.getPrice());
            }
            shoppingCart.setCreateTime(LocalDateTime.now());
            shoppingCart.setNumber(1);

            // 插入到数据库中
            shoppingCartMapper.insert(shoppingCart);
        }
    }

    /**
     * 查看购物车
     * @return
     */
    @Override
    public List<ShoppingCart> showShoppingCart() {
        //1.获取当前用户的id
        Long userId = BaseContext.getCurrentId();
        // 2.构建ShoppingCart对象
        ShoppingCart  shoppingCart = ShoppingCart.builder()
                                                .userId(userId)
                                                .build();
        // 3.查看当前用户的购物车数据
        List<ShoppingCart> list = shoppingCartMapper.list(shoppingCart);

        // 4.返回当前用户的购物车数据
        return list;
    }
}
