package com.luoli.order.service;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.luoli.order.dao.OrderDao;
import com.luoli.order.dao.entity.Order;
import com.luoli.stock.api.StockServiceClient;
import com.luoli.stock.api.dto.StockDto;
import io.seata.core.context.RootContext;
import io.seata.spring.annotation.GlobalTransactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author liluo
 * @create 2022/7/8 10:57
 */
@Slf4j
@Service
public class OrderService {

    @Resource
    private OrderDao orderDao;

    @Resource
    private StockServiceClient stockServiceClient;

    @GlobalTransactional(name = "addOrder")
    @Transactional(rollbackFor = {Exception.class})
    @SentinelResource(value = "addOrder")
    public void addOrder(Order order) {
        log.info("addOrder当前XID{}", RootContext.getXID());
        orderDao.insertOrder(order);
        StockDto stock = new StockDto();
        stock.setStockNo(order.getOrderNo().replaceAll("ORD","STO"));
        stock.setNum(order.getNum());
       String result = stockServiceClient.insertStock(stock);
       log.info("---------------result{}", result);
       if(!"OK".equals(result)) {
           throw new RuntimeException();
       }
       int i = 1/0;
    }

    /**
     * @SentinelResource 注解用来标识资源是否被限流、降级
     * @param orderNo
     * @return
     */
    public List<Order> selectOrder(String orderNo) {
        return orderDao.selectOrder(orderNo);
    }

    public String selectStack() throws InterruptedException {
        return stockServiceClient.selectStock();
    }

}
