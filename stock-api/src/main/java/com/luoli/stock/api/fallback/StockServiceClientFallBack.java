package com.luoli.stock.api.fallback;

import com.luoli.stock.api.StockServiceClient;
import com.luoli.stock.api.dto.StockDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @Author liluo
 * @create 2023/10/16 15:32
 */
@Component
@Slf4j
public class StockServiceClientFallBack implements StockServiceClient {

    @Override
    public String insertStock(StockDto stockDto) {
        log.error("库存服务-获取库存的功能不可用");
        return "增加库存失败";
    }

    @Override
    public String selectStock() {
        return "查询库存失败";
    }
}
