package com.wujt.influx;

import com.wujt.influx.service.WriteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author wujt
 */
@RestController
@RequestMapping("influx")
public class WriteController {

    @Autowired
    private WriteService writeService;

    @GetMapping("startInsert")
    public void startInsert() {
        writeService.startInsert();
    }
}
