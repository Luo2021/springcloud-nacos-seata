package com.luoli.stock.service;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.luoli.stock.api.dto.StockDto;
import com.luoli.stock.dao.StockDao;
import io.seata.core.context.RootContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * @Author liluo
 * @create 2022/7/8 10:57
 */
@Slf4j
@Service
public class StockService {

    @Resource
    private StockDao stockDao;

    @Transactional(rollbackFor = {Exception.class})
    @SentinelResource(value = "addStock")
    public void addStock(StockDto stockDto) {
        log.info("addStock当前XID{}", RootContext.getXID());
        stockDao.insertStock(stockDto);
        if("STO123".equals(stockDto.getStockNo())) {
            int i = 1 / 0;
        }
    }
}
