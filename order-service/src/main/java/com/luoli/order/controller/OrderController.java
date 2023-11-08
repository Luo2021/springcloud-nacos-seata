package com.luoli.order.controller;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.fastjson.JSON;
import com.luoli.order.dao.entity.Order;
import com.luoli.order.service.OrderService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Author liluo
 * @create 2022/7/8 10:58
 */
@RestController
@RequestMapping("/order")
public class OrderController {

    @Resource
    private OrderService orderService;

    private AtomicInteger count = new AtomicInteger(0);

    @GetMapping("/testQPS")
    public String testQPS() {
        count.incrementAndGet();
        System.out.println(Thread.currentThread().getName()+"----------"+count);
        return "OK!";
    }

    @GetMapping("/selectOrder")
    @SentinelResource(value = "selectOrder", blockHandlerClass = OrderController.class, blockHandler = "testBlock")
    public String selectOrder(@RequestParam String orderNo) {
        return JSON.toJSONString(orderService.selectOrder(orderNo));
    }

    public static String testBlock(@RequestParam String orderNo, BlockException e){
        return "Sentinel流量控制！";
    }

    @PostMapping("/insetOrder")
    public String inserOrder(@RequestBody Order order) {
        orderService.addOrder(order);
        return "新增订单成功！";
    }

    @GetMapping("/selectStock")
    public String selectStock() throws InterruptedException {
        return JSON.toJSONString(orderService.selectStack());
    }

}
