package com.luoli.stock.api.dto;

import lombok.Data;

/**
 * @Author liluo
 * @create 2023/10/16 14:58
 */
@Data
public class StockDto {

    /**
     * 主键
     */
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
    private Integer isDelete;
}
