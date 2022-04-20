package com.gow.aop;

import com.gow.aop.aspectj.proxyfactory.AspectArgusConfig;
import com.gow.aop.aspectj.service.LogService;
import com.gow.aop.aspectj.service.LogServiceImpl;
import com.gow.model.ReqInfo;
import java.util.HashSet;
import org.springframework.aop.aspectj.annotation.AspectJProxyFactory;

/**
 * @author gow
 * @date 2022/1/18
 */
public class ArgusParseTest {

    public static void main(String[] args) {
        try {
            //对应目标对象
            LogService target = new LogServiceImpl();
            //创建AspectJProxyFactory对象
            AspectJProxyFactory proxyFactory = new AspectJProxyFactory();
            //设置被代理的目标对象
            proxyFactory.setTarget(target);
            //设置标注了@Aspect注解的类
            proxyFactory.addAspect(AspectArgusConfig.class);
            //生成代理对象
            LogService proxy = proxyFactory.getProxy();
            //使用代理对象
            ReqInfo reqInfo = new ReqInfo();
            HashSet<String> roles = new HashSet<>();
            roles.add("console");
            roles.add("admin");
            reqInfo.setRoles(roles);
            reqInfo.setTime(System.currentTimeMillis());
            proxy.printLog("0001", "0000001", reqInfo);
        } catch (Exception e) {
        }
    }
}
