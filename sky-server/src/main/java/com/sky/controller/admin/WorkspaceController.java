package com.sky.controller.admin;

import com.sky.result.Result;
import com.sky.service.OrderService;
import com.sky.service.WorkspaceService;
import com.sky.vo.BusinessDataVO;
import com.sky.vo.DishOverViewVO;
import com.sky.vo.OrderOverViewVO;
import com.sky.vo.SetmealOverViewVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * @author zhangke
 * @version 1.0
 * @since 2025/8/5
 */

@RestController
@RequestMapping(value = "/admin/workspace")
@Slf4j
@Api(tags = "工作台相关接口")
public class WorkspaceController {

    @Autowired
    private WorkspaceService workspaceService;

    /**
     * 今日数据管理
     * @return
     */
    @GetMapping(value = "/businessData")
    @ApiOperation(value = "今日数据")
    public Result<BusinessDataVO> businessData(){
        log.info("今日数据统计");

        BusinessDataVO businessDataVO = workspaceService.getBusinessData();

        return Result.success(businessDataVO);
    }


    /**
     * 查询订单管理数据
     * @return
     */
    @GetMapping("/overviewOrders")
    @ApiOperation("查询订单管理数据")
    public Result<OrderOverViewVO> orderOverView(){
        log.info("查询订单管理数据");
        OrderOverViewVO orderOverViewVO = workspaceService.getOrderOverView();

        return Result.success(orderOverViewVO);
    }


    /**
     * 查询菜品总览
     * @return
     */
    @GetMapping(value = "/overviewDishes")
    @ApiOperation(value = "查询菜品总览")
    public Result<DishOverViewVO> dishOverView(){
        return Result.success(workspaceService.getDishOverView());
    }


    /**
     * 查询套餐总览
     * @return
     */
    @GetMapping(value = "overviewSetmeals")
    @ApiOperation(value = "查询套餐总览")
    public Result<SetmealOverViewVO> setmealOverView(){
        return Result.success(workspaceService.getSetmealOverView());
    }
}
