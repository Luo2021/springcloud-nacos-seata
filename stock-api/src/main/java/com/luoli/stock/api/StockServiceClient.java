package com.luoli.stock.api;

import com.luoli.stock.api.dto.StockDto;
import com.luoli.stock.api.fallback.StockServiceClientFallBack;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @Author liluo
 * @create 2023/10/16 14:54
 */
@FeignClient(value = "stock-service", contextId = "stock", fallback = StockServiceClientFallBack.class)
public interface StockServiceClient {

    /**
     * 增加库存
     * @param stockDto
     * @return
     */
    @PostMapping("/insetStock")
    String insertStock(@RequestBody StockDto stockDto);

    /**
     * 查询库存
     * @return
     */
    @GetMapping("/selectStock")
    String selectStock() throws InterruptedException;
}
