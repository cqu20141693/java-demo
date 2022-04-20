package com.wujt.jsengine;

import com.alibaba.fastjson.JSONObject;
import delight.nashornsandbox.NashornSandbox;
import delight.nashornsandbox.NashornSandboxes;
import lombok.extern.slf4j.Slf4j;

import javax.script.Bindings;
import javax.script.ScriptException;
import java.util.concurrent.Executors;

/**
 * @author wujt
 * @version 1.0
 * @date 2020/2/27
 */
@Slf4j
public class JavaJSSandBoxDemo {
    private NashornSandbox sandbox = NashornSandboxes.create();

    public static void main(String[] args) {
        JavaJSSandBoxDemo demo = new JavaJSSandBoxDemo();

        demo.bindingDemo();
        demo.invokeFunctionDemo();

    }

    /*
     * 初始化sandbox，限制它使用的资源
     */
    JavaJSSandBoxDemo() {
        sandbox.setMaxCPUTime(100);
        //sandbox.setMaxMemory(50 * 1024);
        sandbox.allowNoBraces(false);
        sandbox.setMaxPreparedStatements(30); // because preparing scripts for execution is expensive
        sandbox.setExecutor(Executors.newSingleThreadExecutor());
    }

    public void bindingDemo() {
        try {
            final Bindings bindings = sandbox.createBindings();
            bindings.put("$ARG","hello world!");
            Object result = sandbox.eval("$ARG", bindings);
            log.info("result=" + result);


            bindings.put("k",20);
            result = sandbox.eval("k + 1", bindings);
            log.info("result=" + result);


            result = sandbox.eval("n = 1738");
            log.info("result=" + result);
            result = sandbox.get("n");
            log.info("result=" + result);

            Bindings scope = sandbox.createBindings();
            scope.put("key", "西安");
            result = sandbox.eval("key + '市'", scope);
            log.info("result=" + result);
        }
        catch (ScriptException se) {
            log.warn("binding demo exception.", se);
        }
    }


    public boolean invokeFunctionDemo() {
        log.info("---          invokeFunction         ---" );
        boolean result = true;
        try {

            final Bindings bindings = sandbox.createBindings();
            bindings.put("msg","hello world!");
            String str = "var user = {name:'张三',age:18,city:['陕西','台湾']};";
            sandbox.eval(str, bindings);

            log.info("Get msg={}", sandbox.eval("msg", bindings));
            //获取变量
            sandbox.eval("var sum = eval('1 + 2 + 3*4');");
            //调用js的eval的方法完成运算
            log.info("get sum={}", sandbox.get("sum"));

            JSONObject msg = new JSONObject();
            msg.put("temperature", 32);
            msg.put("humidity", 20);
            msg.put("voltage", 220);
            msg.put("electricity", 13);

            JSONObject metadata = new JSONObject();
            metadata.put("deviceName", "空气质量检测器01");
            metadata.put("contacts", "张三");

            JSONObject msgType = new JSONObject();
            msgType.put("type", "deviceTelemetryData1");

            //定义函数
            String func = "var result = true; \r\n" +
                    "if (msgType.type = 'deviceTelemetryData') { \r\n" +
                    "   if (msg.temperature >0 && msg.temperature < 33) { \n       result = true ;}  \n" +
                    "   else { \n       result = false;}  \n" +
                    "} else { \n     result = false;  \n" +
                    "     var errorMsg = msgType.type + ' is not deviceTelemetryData';  \n" +
                    "     print(msgType.type) \n } \n\n" +
                    "return result";
            log.info("func = {}", func);
            sandbox.eval("function filter(msg, metadata, msgType){ " + func + "}");
            // 执行js函数
            Object obj  = sandbox.getSandboxedInvocable().invokeFunction("filter", msg, metadata, msgType);

            //方法的名字，参数
            log.info("function result={}", obj);
            result = (Boolean)obj;
        }
        catch(Exception ex) {
            log.warn("exception", ex);
            result = false;
        }

        return result;
    }

}
