package com.luoli.stock.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.luoli.stock.dao.entity.Stock;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Author liluo
 * @create 2023/10/16 11:37
 */
@Mapper
public interface StockMapper extends BaseMapper<Stock> {
}
