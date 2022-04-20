package com.gow.controller;

import com.gow.mysql.dao.AddressMapper;
import com.gow.mysql.dao.OrdercMapper;
import com.gow.mysql.model.Address;
import com.gow.mysql.model.Orderc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 使用master-slave 配置
 * @author wujt
 */
@RestController
@RequestMapping("read-write-split")
public class ReadWriteSplitController {

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
    public Object getAddress() {
        return addressMapper.selectByPrimaryKey(1);
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
