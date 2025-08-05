package com.sky.service.impl;

import com.sky.constant.StatusConstant;
import com.sky.entity.Orders;
import com.sky.mapper.DishMapper;
import com.sky.mapper.OrderMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.mapper.UserMapper;
import com.sky.service.WorkspaceService;
import com.sky.vo.BusinessDataVO;
import com.sky.vo.DishOverViewVO;
import com.sky.vo.OrderOverViewVO;
import com.sky.vo.SetmealOverViewVO;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;

/**
 * @author zhangke
 * @version 1.0
 * @since 2025/8/5
 */

@Service
public class WorkspaceServiceImpl implements WorkspaceService {

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private DishMapper dishMapper;

    @Autowired
    private SetmealMapper setmealMapper;

    /**
     * - 营业额：已完成订单的总金额
     * - 有效订单：已完成订单的数量
     * - 订单完成率：有效订单数 / 总订单数 * 100%
     * - 平均客单价：营业额 / 有效订单数
     * - 新增用户：新增用户的数量
     * @return
     */
    @Override
    public BusinessDataVO getBusinessData() {

        LocalDateTime begin = LocalDateTime.of(LocalDate.now(), LocalTime.MIN);
        LocalDateTime end = LocalDateTime.of(LocalDate.now(), LocalTime.MAX);

        // 统计今日营业额
        Map<String, Object> map = new HashMap<>();
        map.put("beginTime", begin);
        map.put("endTime", end);
        map.put("status", Orders.COMPLETED);
        Double turnover = orderMapper.sumByMap(map) == null ? 0 : orderMapper.sumByMap(map);


        // 有效订单
        Integer validOrderCount = orderMapper.countByMap(map) == null ? 0 : orderMapper.countByMap(map);


        // 总订单
        map.put("status", null);
        Integer totalOrderCount = orderMapper.countByMap(map) == null ? 0 : orderMapper.countByMap(map);


        // 订单完成率
        Double orderCompletionRate = 0.0;
        if(totalOrderCount != 0) {
            orderCompletionRate = validOrderCount.doubleValue() / totalOrderCount;
        }
        // 平均客单价：营业额 / 有效订单数
        Double unitPrice = 0.0;
        if (turnover != 0.0) {
            unitPrice = turnover.doubleValue() / validOrderCount;
        }

        // 新增用户：新增用户的数量
        map.remove("status");
        Integer newUserCount = userMapper.countByMap(map) == null ? 0 : userMapper.countByMap(map);

        // 封装成BusinessDataVO对象
        BusinessDataVO businessDataVO = BusinessDataVO.builder()
                .turnover(turnover)
                .validOrderCount(validOrderCount)
                .orderCompletionRate(orderCompletionRate)
                .newUsers(newUserCount)
                .unitPrice(unitPrice)
                .build();

        return businessDataVO;
    }


    /**
     * 查询订单管理数据
     * @return
     */
    @Override
    public OrderOverViewVO getOrderOverView() {
        LocalDateTime begin = LocalDateTime.of(LocalDate.now(), LocalTime.MIN);
        LocalDateTime end = LocalDateTime.of(LocalDate.now(), LocalTime.MAX);

        Map<String, Object> map = new HashMap<>();
        map.put("beginTime", begin);
        map.put("endTime", end);
        map.put("status", null);

        // 统计全部订单
        Integer allOrders = orderMapper.countByMap(map) == null ? 0 : orderMapper.countByMap(map);

        // 已取消订单
        map.put("status", Orders.CANCELLED);
        Integer cancelOrders = orderMapper.countByMap(map) == null ? 0 : orderMapper.countByMap(map);

        // 已完成订单
        map.put("status", Orders.COMPLETED);
        Integer completeOrders = orderMapper.countByMap(map) == null ? 0 : orderMapper.countByMap(map);

        // 派送订单
        map.put("status", Orders.DELIVERY_IN_PROGRESS);
        Integer deliveryOrders = orderMapper.countByMap(map) == null ? 0 : orderMapper.countByMap(map);

        // 待接定档
        map.put("status", Orders.TO_BE_CONFIRMED);
        Integer waitingOrders = orderMapper.countByMap(map) == null ? 0 : orderMapper.countByMap(map);

        // 封装成OrderOverViewVO对象
        return OrderOverViewVO.builder()
                .allOrders(allOrders)
                .cancelledOrders(cancelOrders)
                .completedOrders(completeOrders)
                .deliveredOrders(deliveryOrders)
                .waitingOrders(waitingOrders)
                .build();
    }

    /**
     * 查询菜品总览
     * @return
     */
    @Override
    public DishOverViewVO getDishOverView() {

        Map<String, Object> map = new HashMap<>();
        // 已停售菜品数量
        map.put("status", StatusConstant.DISABLE);
        Integer discontinued = dishMapper.countByMap(map) == null ? 0 : dishMapper.countByMap(map);

        // 已启售菜品数量
        map.put("status", StatusConstant.ENABLE);

        Integer sold = dishMapper.countByMap(map) == null ? 0 : dishMapper.countByMap(map);

        // 封装成DishOverViewOV对象
        return DishOverViewVO.builder()
                .discontinued(discontinued)
                .sold(sold)
                .build();
    }


    /**
     * 查询套餐总览
     * @return
     */
    @Override
    public SetmealOverViewVO getSetmealOverView() {
        Map<String, Object> map = new HashMap<>();
        // 已停售菜品数量
        map.put("status", StatusConstant.DISABLE);
        Integer discontinued = setmealMapper.countByMap(map) == null ? 0 : setmealMapper.countByMap(map);

        // 已启售菜品数量
        map.put("status", StatusConstant.ENABLE);
        Integer sold = setmealMapper.countByMap(map) == null ? 0 : setmealMapper.countByMap(map);

        // 封装成DishOverViewOV对象
        return SetmealOverViewVO.builder()
                .discontinued(discontinued)
                .sold(sold)
                .build();
    }
}
