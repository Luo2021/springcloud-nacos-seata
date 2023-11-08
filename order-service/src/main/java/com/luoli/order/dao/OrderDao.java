package com.luoli.order.dao;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.luoli.order.dao.entity.Order;
import com.luoli.order.dao.mapper.OrderMapper;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author liluo
 * @create 2023/10/16 11:27
 */
@Repository
public class OrderDao {

    @Resource
   private OrderMapper orderMapper;

    public int insertOrder(Order order) {
        return orderMapper.insert(order);
    }

    public List<Order> selectOrder(String orderNo) {
        QueryWrapper<Order> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("order_no", orderNo);
        return orderMapper.selectList(queryWrapper);
    }
}
