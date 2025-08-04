package com.sky.controller.admin;

import com.sky.result.Result;
import com.sky.service.ReportService;
import com.sky.vo.OrderReportVO;
import com.sky.vo.SalesTop10ReportVO;
import com.sky.vo.TurnoverReportVO;
import com.sky.vo.UserReportVO;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

/**
 * @author zhangke
 * @version 1.0
 * @since 2025/8/4
 */

@RestController
@RequestMapping(value = "/admin/report")
@Slf4j
@Api(tags = "统计")
public class ReportController {

    @Autowired
    private ReportService reportService;

    /**
     * 统计范围内每天的营业额
     * @param begin 开始日期
     * @param end 结束日期
     * @return
     */
    @GetMapping(value = "/turnoverStatistics")
    @ApiOperation(value = "统计营业额")
    public Result<TurnoverReportVO> turnoverStatistics(@DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate begin,
                                                       @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate end){

        TurnoverReportVO data = reportService.getTurnover(begin, end);

        return Result.success(data);
    }


    /**
     * 用户统计
     * @param begin 开始日期
     * @param end 结束日期
     * @return
     */
    @GetMapping(value = "/userStatistics")
    @ApiOperation(value = "用户统计")
    public Result<UserReportVO> userStatistics(@DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate begin,
                                               @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate end){
        return Result.success(reportService.getUserStatistics(begin, end));
    }

    /**
     * 用户统计
     * @param begin 开始日期
     * @param end 结束日期
     * @return
     */
    @GetMapping(value = "/ordersStatistics")
    @ApiOperation(value = "订单统计")
    public Result<OrderReportVO> ordersStatistics(@DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate begin,
                                               @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate end){

        return Result.success(reportService.getOrderStatistics(begin, end));
    }


    /**
     * 销量排名Top10
     * @param begin 开始日期
     * @param end 结束日期
     * @return
     */
    @GetMapping(value = "/top10")
    @ApiOperation(value = "销量排名Top10")
    public Result<SalesTop10ReportVO> top10(@DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate begin,
                                                  @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate end){

        return Result.success(reportService.getSalesTop10(begin, end));
    }
}
