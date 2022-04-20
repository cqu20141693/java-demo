package com.cc.core.search;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class SearchAppDTO extends BaseSearchDTO{

    /**
     * 用户id
     */
    private Integer userId;
    /**
     * 应用名称（支持模糊查询）
     */
    private String name;

    public SearchAppDTO(Object object){
        JSONObject jsonObject = JSON.parseObject(JSON.toJSONString(object));

        if(jsonObject.getString("name") != null){
            this.name = jsonObject.getString("name");
        }
        this.page = Integer.valueOf(jsonObject.getString("page"));
        this.pageSize = Integer.valueOf(jsonObject.getString("pageSize"));
    }

    public SearchAppDTO() {

    }
}
