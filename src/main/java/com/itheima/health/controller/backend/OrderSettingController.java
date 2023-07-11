package com.itheima.health.controller.backend;

import com.itheima.health.constant.MessageConstant;
import com.itheima.health.entity.Result;
import com.itheima.health.pojo.OrderSetting;
import com.itheima.health.service.OrderSettingService;
import com.itheima.health.utils.POIUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/ordersetting")
public class OrderSettingController {
    @Autowired
    private OrderSettingService orderSettingService;

    /**
     * Excel文件上传，并解析文件内容保存到数据库
     * @param multipartFile 前端上传的名字
     * @return 统一响应结果
     */
    @PostMapping("/upload.do")
    public Result upload(@RequestPart("excelFile") MultipartFile multipartFile) {
        try {
            // 1. 读取Excel文件数据
            List<String[]> excelDataList = POIUtils.readExcel(multipartFile);
            // 2. 创建List集合保存Excel中的每行数据
            List<OrderSetting> orderSettingList = new ArrayList<>();
            // 3. 遍历得到excel的每一行数据, 放到OrderSetting对象中
            for (String[] excelData : excelDataList) {
                OrderSetting orderSetting = new OrderSetting(new Date(excelData[0]), Integer.parseInt(excelData[1]));
                // 4. 把OrderSetting对象保存得到集合中
                orderSettingList.add(orderSetting);
            }
            // 5. 把读取的数据保存得到数据库
            orderSettingService.add(orderSettingList);
        } catch (IOException e) {
            e.printStackTrace();
            // 6. 出现异常，返回统一响应结果，设置失败信息
            return new Result(false, MessageConstant.IMPORT_ORDERSETTING_FAIL);
        }

        // 7. 没有异常，返回统一响应结果，设置成功信息
        return new Result(true, MessageConstant.IMPORT_ORDERSETTING_SUCCESS);
    }

    /**
     * 根据日期查询预约设置数据(获取指定日期所在月份的预约设置数据)
     * 按照月份查询出预约信息
     * @param month 月份，格式为：2023-6
     * @return 统一响应结果,包含预约设置数据
     */
    @RequestMapping("/getOrderSettingByMonth.do")
    public Result getOrderSettingByMonth(@DateTimeFormat(pattern = "yyyy-MM") Date month) {
        // 1. 调用业务根据日期查询预约设置数据
        List<OrderSetting> list = orderSettingService.getOrderSettingByMonth(month);

        // 2. 响应统一结果Result
        return new Result(true, MessageConstant.QUERY_ORDER_SUCCESS, list);
    }

    /**
     * 基于日历实现预约人数设置
     * 根据指定日期修改可预约人数
     * @param orderSetting 新的可预约人数, 要修改的日期
     * @return 统一响应结果
     */
    @RequestMapping("/editNumberByOrderDate.do")
    public Result editNumberByOrderDate(@RequestBody OrderSetting orderSetting) {
        // 1. 调用业务根据指定日期修改可预约人数
        orderSettingService.editNumberByOrderDate(orderSetting);

        return new Result(true, MessageConstant.ORDERSETTING_SUCCESS);
    }
}
