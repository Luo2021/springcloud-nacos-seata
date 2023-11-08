package com.luoli.stock.controller;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.luoli.stock.api.StockServiceClient;
import com.luoli.stock.api.dto.StockDto;
import com.luoli.stock.service.StockService;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @Author liluo
 * @create 2022/7/8 10:58
 */
@RestController
public class StockController implements StockServiceClient {

    @Resource
    private StockService stockService;

    @Override
    public String insertStock(StockDto stockDto) {
        stockService.addStock(stockDto);
        return "OK";
    }

    @SentinelResource(value = "selectStock", blockHandlerClass = StockController.class, blockHandler = "testBlock")
    @Override
    public String selectStock() {
        try {
            Thread.sleep(500);
        }catch (Exception e){

        }
        return "查询库存成功！";
    }


    public static String testBlock(BlockException e){
        return "Sentinel流量控制！";
    }
}
