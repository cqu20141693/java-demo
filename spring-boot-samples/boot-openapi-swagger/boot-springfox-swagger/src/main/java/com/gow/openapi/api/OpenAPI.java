package com.gow.openapi.api;

import com.gow.openapi.model.Goods;
import com.gow.openapi.model.RestResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.Parameter;
import java.math.BigDecimal;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

/**
 * @author gow
 * @date 2021/7/12 0012
 */
@Api(tags = "商品信息管理接口")
@RestController
@RequestMapping("api/")
@Slf4j
public class OpenAPI {

    @ApiOperation(value = "获取商品信息", notes = "商品详情,针对得到单个商品的信息")
    @GetMapping("/getGoods")
    public RestResult<Goods> getGoods(
            @Parameter(description = "商品id,正整数") @RequestParam(value = "goodsid", required = false, defaultValue = "0")
                    Integer goodsid) {
        Goods goodsone = new Goods();
        goodsone.setGoodsId(3L);
        goodsone.setGoodsName("电子书");
        goodsone.setSubject("学python,学ai");
        goodsone.setPrice(new BigDecimal(60));
        goodsone.setStock(10);
        RestResult res = new RestResult();
        return res.success(goodsone);
    }

    @ApiOperation(value = "提交订单", notes = "提交订单")
    @PostMapping("/order")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userid", value = "用户id", dataTypeClass = Long.class, paramType = "query",
                    example = "12345"),
            @ApiImplicitParam(name = "goodsid", value = "商品id", dataTypeClass = Integer.class, paramType = "query",
                    example = "12345"),
            @ApiImplicitParam(name = "mobile", value = "手机号", dataTypeClass = String.class, paramType = "query",
                    example = "13866668888"),
            @ApiImplicitParam(name = "comment", value = "发货备注", dataTypeClass = String.class, paramType = "query",
                    example = "请在情人节当天送到")
    })
    public RestResult<String> order(@ApiIgnore @RequestParam Map<String, String> params) {
        log.info("params={}", params);
        RestResult res = new RestResult();
        return res.success("success");
    }
}
