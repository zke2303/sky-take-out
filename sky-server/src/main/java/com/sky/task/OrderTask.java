package com.sky.task;

import com.sky.entity.Orders;
import com.sky.mapper.OrderMapper;
import com.sky.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author zhangke
 * @version 1.0
 * @description 定时任务类，定时处理订单状态
 * @since 2025/8/2
 */

@Component
@Slf4j
public class OrderTask {

    @Autowired
    private OrderMapper orderMapper;

    /**
     * 处理超时订单
     */
    @Scheduled(cron = "0 * * * * ? ")
    public void processTimeoutOrder() {
        log.info("定时处理超时订单,LocalDateTime:{},", LocalDateTime.now());

        LocalDateTime time = LocalDateTime.now().plusMinutes(-15);
        // 1.查询是否有超时订到
        List<Orders> ordersList = orderMapper.getByStatusAndOrderTimeLT(Orders.PENDING_PAYMENT, time);
        // 2.如果有超时订单，则自动取消超时订单
        if (ordersList != null && !ordersList.isEmpty()) {
            for (Orders orders : ordersList) {
                orders.setStatus(Orders.CANCELLED);
                orders.setCancelReason("订单超时, 自动取消");
                orders.setCancelTime(LocalDateTime.now());
                orderMapper.update(orders);
            }
        }
    }

    /**
     * 处理一直处理派送中的订单
     */
    @Scheduled(cron = "0 0 1 * * ?")
    public void processDeliveryOrder() {
        log.info("处理一直处理派送中的订单, LocalDateTime:{},", LocalDateTime.now());

        LocalDateTime time = LocalDateTime.now().plusHours(-1);
        List<Orders> ordersList = orderMapper.getByStatusAndOrderTimeLT(Orders.DELIVERY_IN_PROGRESS, time);

        if (ordersList != null && !ordersList.isEmpty()) {
            for (Orders orders : ordersList) {
                orders.setStatus(Orders.CANCELLED);
                orderMapper.update(orders);
            }
        }
    }
}


