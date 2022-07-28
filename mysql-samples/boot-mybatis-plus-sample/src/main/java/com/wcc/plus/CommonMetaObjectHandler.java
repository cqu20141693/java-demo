package com.wcc.plus;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * wcc 2022/7/27
 */
@Component
public class CommonMetaObjectHandler implements MetaObjectHandler {


    @Override
    public void insertFill(MetaObject metaObject) {

        // 或者
        this.strictInsertFill(metaObject, "gmtCreate", LocalDateTime::now, LocalDateTime.class); // 起始版本 3.3.3(推荐)
        this.strictInsertFill(metaObject, "gmtModified", LocalDateTime::now, LocalDateTime.class); // 起始版本 3.3.3(推荐)
    }

    @Override
    public void updateFill(MetaObject metaObject) {

        // 或者
        this.strictUpdateFill(metaObject, "gmtModified", LocalDateTime::now, LocalDateTime.class); // 起始版本 3.3.3(推荐)
    }


}
