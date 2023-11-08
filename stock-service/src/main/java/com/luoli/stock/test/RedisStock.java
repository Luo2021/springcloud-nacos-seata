package com.luoli.stock.test;

import lombok.AllArgsConstructor;

import java.io.Serializable;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Author liluo
 * @create 2023/11/1 9:50
 */
@AllArgsConstructor
public class RedisStock implements Serializable {
    /**
     * 库存字段
     */
   private String stockName;

    /**
     * 库存数据, 原子类来保证原子性 num的原子性
     */
   private AtomicInteger num;

    public RedisStock(String stockName, int num) {
        this.stockName = stockName;
        this.num = new AtomicInteger(num);
    }

    public void setNum(int num) {
        this.num.set(num);
    }

    public String getStockName() {
        return stockName;
    }

    public void setStockName(String stockName) {
        this.stockName = stockName;
    }

    public int getNum() {
        return this.num.get();
    }

    @Override
    public String toString() {
        return "RedisStock{" +
                "stockName='" + stockName + '\'' +
                ", num=" + num.get() +
                '}';
    }
}