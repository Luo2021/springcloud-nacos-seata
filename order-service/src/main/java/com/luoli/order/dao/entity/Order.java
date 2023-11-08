package com.luoli.order.dao.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @Author liluo
 * @create 2023/10/16 11:38
 */
@Data
@TableName("tb_order")
public class Order {
    /**
     * 主键
     */
    @TableId(type = IdType.AUTO, value = "id")
    private Long id;

    /**
     * 订单编号
     */
    private String orderNo;

    /**
     * 订单数量
     */
    private Long num;

    /**
     * 是否有效，0:有效 1:无效
     */
    @TableLogic
    private Integer isDelete;
}
