package com.gow.validate.rest;

import com.alibaba.fastjson.JSONObject;
import com.gow.validate.pojo.common.Result;
import com.gow.validate.pojo.common.Save;
import com.gow.validate.pojo.common.Update;
import com.gow.validate.pojo.common.ValidationList;
import com.gow.validate.pojo.common.annotation.CollectionSize;
import com.gow.validate.pojo.model.UserDTO;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.constraints.Length;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author wujt  2021/5/18
 */
@RestController
@RequestMapping("validator")
@Slf4j
@Validated  //requestParam/PathVariable参数校验
public class ValidateController {
    /**
     * 如果想使用DTO中的验证注解， 使用@Valid和@Validated都可以
     *
     * @param userDTO
     * @return com.gow.validate.pojo.common.Result
     * @date 2021/5/18 17:04
     */
    @PostMapping("/save")
    public Result saveUser(@RequestBody @Validated(Save.class) UserDTO userDTO) {
        log.info("userDTO={}", JSONObject.toJSONString(userDTO));
        // 校验通过，才会执行业务逻辑处理
        return Result.ok();
    }

    /**
     * 如果想使用参数验证注解，必须在Controller类上标注@Validated注解
     *
     * @date 2021/5/18 17:07
     */
    @GetMapping("{userId}")
    public Result detail(@PathVariable("userId") @Min(10000000000000000L) Long userId) {
        // 校验通过，才会执行业务逻辑处理
        UserDTO userDTO = new UserDTO();
        userDTO.setUserId(userId);
        userDTO.setAccount("11111111111111111");
        userDTO.setUserName("xixi");
        userDTO.setAccount("11111111111111111");
        return Result.ok(userDTO);
    }

    /**
     * 查询参数
     *
     * @date 2021/5/18 17:09
     */
    @GetMapping("getByAccount")
    public Result getByAccount(@Length(min = 6, max = 20) @NotNull String account) {
        // 校验通过，才会执行业务逻辑处理
        UserDTO userDTO = new UserDTO();
        userDTO.setUserId(10000000000000003L);
        userDTO.setAccount(account);
        userDTO.setUserName("xixi");
        userDTO.setAccount("11111111111111111");
        return Result.ok(userDTO);
    }

    /**
     * 当同一个DTO不同场景验证的方式不同时，可以提供分组验证
     *
     * @param userDTO
     * @return com.gow.validate.pojo.common.Result
     * @date 2021/5/18 17:36
     */
    @PostMapping("/update")
    public Result updateUser(@RequestBody @Validated(Update.class) UserDTO userDTO) {
        log.info("userDTO={}", JSONObject.toJSONString(userDTO));
        // 校验通过，才会执行业务逻辑处理
        return Result.ok();
    }

    /**
     * 当同一个DTO不同场景验证的方式不同时，可以提供分组验证
     *
     * @param userDTO
     * @return com.gow.validate.pojo.common.Result
     * @date 2021/5/18 17:36
     */
    @PostMapping("/save/nested")
    public Result saveNested(@RequestBody @Validated(Save.class) UserDTO userDTO) {
        log.info("userDTO={}", JSONObject.toJSONString(userDTO));
        // 校验通过，才会执行业务逻辑处理
        return Result.ok();
    }

    /**
     * 集合对象验证
     *
     * @param userList
     * @return com.gow.validate.pojo.common.Result
     * @date 2021/5/18 18:04
     */
    @PostMapping("/saveList")
    public Result saveList(@RequestBody @Validated(Save.class) ValidationList<UserDTO> userList) {
        // 校验通过，才会执行业务逻辑处理
        return Result.ok();
    }

    /**
     * 自定义验证器，验证JSONString
     *
     * @param jsonString
     * @return com.gow.validate.pojo.common.Result
     * @date 2021/5/18 22:25
     */
    @GetMapping("/jsonValid")
    public Result jsonValid(@RequestParam("jsonString") String jsonString) {
        log.info("value={}", jsonString);
        return Result.ok();
    }

    /**
     * 自定义验证集合长度
     *
     * @param values
     * @return com.gow.validate.pojo.common.Result
     * @date 2021/5/18 23:33
     */
    @GetMapping("/collectionSizeValid")
    public Result collectionSizeValid(@RequestParam("values") @CollectionSize(maxLength = 2) List<String> values) {
        log.info("value={}", values);
        return Result.ok();
    }
}
