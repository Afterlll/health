package com.itheima.health.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * 预约设置
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderSetting implements Serializable {
    private Integer id;

    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date orderDate; // 预约设置日期
    private int number; // 可预约人数
    private int reservations; // 已预约人数
    private int date;

    public OrderSetting(Date orderDate, int number) {
        this.orderDate = orderDate;
        this.number = number;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
        date = orderDate.getDate();
    }
}
