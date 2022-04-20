### swagger 3

1. 添加依赖
2. 配置swagger
3. 接口规范注解使用

``` 
@Api(tags = "商品信息管理接口") 使用到restController类 上
@ApiOperation:用来说明一个方法

@ApiImplicitParams:用来包含多个包含多个 @ApiImplicitParam，

 @ApiImplicitParam:用来说明一个请求参数 

 如果使用@Parameter来做说明，可以直接加到@RequestParam参数之前         

 @ApiIgnore:用来忽略不必要显示的参数
 
```
4. 访问页面
``` 
http://localhost:8080/swagger-ui/index.html
```