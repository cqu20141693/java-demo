package com.wcc.plus.generator;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;
import com.baomidou.mybatisplus.generator.fill.Column;
import org.junit.jupiter.api.Test;

import java.util.Collections;

/**
 * wcc 2022/7/27
 */
public class MysqlDemo {

    @Test
    public void quickCreation() {
        String url = "jdbc:mysql://192.168.96.163:3306/scada?useUnicode=true&characterEncoding=utf8&rewriteBatchedStatements=true&zeroDateTimeBehavior=convertToNull&serverTimezone=Asia/Shanghai";
        String user = "root";
        String password = "cc@123";

        // 1 配置数据源
        FastAutoGenerator.create(url, user, password)
                // 2. 全局配置
                .globalConfig(builder -> {
                    builder.author("wcc") // 设置作者
                            .enableSwagger() // 开启 swagger 模式
                            .dateType(DateType.TIME_PACK)   //定义生成的实体类中日期的类型 TIME_PACK=LocalDateTime;ONLY_DATE=Date;
                            .fileOverride() // 覆盖已生成文件
                            .disableOpenDir()
                            // 指定输出目录
                            .outputDir("D:\\java-project\\java-demo\\mysql-samples\\boot-mybatis-plus-sample\\src\\main\\java");
                })
                // 3 包配置
                .packageConfig(builder -> {
                    builder.parent("com.wcc") // 设置父包名
                            .moduleName("scada") // 设置父包模块名
                            .entity("core.entity")   //pojo 实体类包名
                            .service("core.service") //Service 包名
                            .serviceImpl("core.service.impl") // ***ServiceImpl 包名
                            .mapper("core.mapper")   //Mapper 包名
                            .xml("core.mapper")  //Mapper XML 包名
                            .controller("api") //Controller 包名
                            // 设置mapperXml生成路径
                            .pathInfo(Collections.singletonMap(OutputFile.xml, "D:\\java-project\\java-demo\\mysql-samples\\boot-mybatis-plus-sample\\src\\main\\resources\\mybatis\\mapper"));
                })
                // 4 配置策略
                .strategyConfig(builder -> {
                    builder.addInclude("command") // 设置需要生成的表名
                            .addTablePrefix("t_", "c_") // 设置过滤表前缀
                            //4.1、Mapper策略配置
                            .mapperBuilder()
                            .superClass(BaseMapper.class)   //设置父类
                            .formatMapperFileName("%sMapper")   //格式化 mapper 文件名称
                            .enableMapperAnnotation()       //开启 @Mapper 注解
                            .formatXmlFileName("%sMapper") //格式化 Xml 文件名称
                            // 4.2 service
                            .serviceBuilder()
                            .formatServiceFileName("%sService") //格式化 service 接口文件名称，%s进行匹配表名，如 UserService
                            .formatServiceImplFileName("%sServiceImpl") //格式化 service 实现类文件名称，%s进行匹配表名，如 UserServiceImpl
                            // 4.3 entity
                            .entityBuilder()
                            .enableLombok() //开启 Lombok
                            .disableSerialVersionUID()  //不实现 Serializable 接口，不生产 SerialVersionUID
                            .logicDeleteColumnName("deleted")   //逻辑删除字段名
                            .naming(NamingStrategy.underline_to_camel)  //数据库表映射到实体的命名策略：下划线转驼峰命
                            // 自动填充，需要自实现MetaObjectHandler
                            .addTableFills(
                                    new Column("gmt_create", FieldFill.INSERT),
                                    new Column("gmt_modified", FieldFill.INSERT_UPDATE)
                            )   //"create_time"字段自动填充为插入时间，"modify_time"字段自动填充为插入修改时间
                            .enableTableFieldAnnotation()       // 开启生成实体时生成字段注解
                            .fileOverride()  //覆盖文件
                            //4.4、Controller策略配置
                            .controllerBuilder()
                            .formatFileName("%sController") //格式化 Controller 类文件名称，%s进行匹配表名，如 UserController
                            .enableRestStyle()  //开启生成 @RestController 控制器
                    ;
                })
                // 配置模板
                .templateEngine(new FreemarkerTemplateEngine()) // 使用Freemarker引擎模板，默认的是Velocity引擎模板
                // 执行
                .execute();

    }
}
