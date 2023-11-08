package com.luoli.stock.dao.entity;

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
@TableName("tb_stock")
public class Stock {
    /**
     * 主键
     */
    @TableId(type = IdType.AUTO, value = "id")
    private Long id;

    /**
     * 库存编号
     */
    private String stockNo;

    /**
     * 库存数量
     */
    private Long num;

    /**
     * 是否有效，0:有效 1:无效
     */
    @TableLogic
    private Integer disabled;
}