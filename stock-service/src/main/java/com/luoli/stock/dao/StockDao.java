package com.luoli.stock.dao;

import com.luoli.stock.api.dto.StockDto;
import com.luoli.stock.dao.entity.Stock;
import com.luoli.stock.dao.mapper.StockMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;

/**
 * @Author liluo
 * @create 2023/10/16 11:27
 */
@Repository
public class StockDao {

    @Resource
    private StockMapper stockMapper;

    public int insertStock(StockDto stockDto) {
        Stock stock = new Stock();
        BeanUtils.copyProperties(stockDto, stock);
        return stockMapper.insert(stock);
    }
}
