package com.wujt.influx;

import com.wujt.influx.service.QueryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author wujt
 */
@RestController
@RequestMapping("influx")
public class QueryController {

    @Autowired
    private QueryService queryService;

    @GetMapping("query")
    public Object query() {
        return queryService.query();
    }

    @GetMapping("pageQuery")
    public Object pageQuery(@RequestParam("page")Integer page,@RequestParam("pageSize")Integer pageSize) {
        return queryService.pageQuery(page,pageSize);
    }
}
