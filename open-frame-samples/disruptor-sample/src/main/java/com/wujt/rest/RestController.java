package com.wujt.rest;

import com.wujt.model.Element;
import com.gow.util.DisruptorUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author wujt
 */
@org.springframework.web.bind.annotation.RestController
@RequestMapping("/disruptor")
public class RestController {
    @Autowired
    private DisruptorUtil disruptorUtil;

    @GetMapping("/onElement")
    public Boolean onElement(@RequestParam("value") Integer value) {
        Element element = new Element();
        element.setValue(value);
        disruptorUtil.onElement(element);
        return true;
    }
}
