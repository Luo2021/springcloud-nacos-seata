package com.luoli.stock.test;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

/**
 * @Author liluo
 * @create 2023/11/1 9:47
 */
@Data
@AllArgsConstructor
public class StockRequest implements Serializable {
    /**
     * 会员id
     */
    private String memberId;

    /**
     * 购买数量
     */
    private int buyNum;
}