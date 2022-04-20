package com.gow.controller;

import com.gow.mysql.dao.AddressMapper;
import com.gow.mysql.dao.OrdercMapper;
import com.gow.mysql.model.Address;
import com.gow.mysql.model.Orderc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 具有数据库分库分表作用和主备读写分离
 * 使用的配置：sharding-master-salve
 * @author wujt
 */
@RestController
@RequestMapping("sharding")
public class ShardingTableController {

    @Autowired
    private OrdercMapper orderMapper;

    @Autowired
    private AddressMapper addressMapper;

    @GetMapping("getOrder")
    public Object getOrder(@RequestParam("id") Long id) {
        return orderMapper.selectByPrimaryKey(id);
    }

    @PostMapping("addOrder")
    public Object getOrder(@RequestParam("name") String name) {
        Orderc orderc = new Orderc();
        orderc.setType("test");
        orderc.setName(name);
        return orderMapper.insertSelective(orderc);
    }

    @GetMapping("getAddress")
    public Object getAddress(@RequestParam("id") Integer id) {
        return addressMapper.selectByPrimaryKey(id);
    }

    @GetMapping("addAddress")
    public Object addAddress(@RequestParam("name") String name, @RequestParam("country") String country, @RequestParam("city") String city) {
        Address address = new Address();
        address.setName(name);
        address.setCountry(country);
        address.setName(city);
        return addressMapper.insertSelective(address);
    }
}
