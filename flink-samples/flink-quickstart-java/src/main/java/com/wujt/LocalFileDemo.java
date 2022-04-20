package com.wujt;

import org.apache.flink.api.common.functions.FilterFunction;
import org.apache.flink.api.java.DataSet;
import org.apache.flink.api.java.ExecutionEnvironment;

import java.util.Optional;

/**
 * @Author jiangtaoW
 * @Date 2019/5/9
 * @Version 1.0
 * @Description : TODO
 */
public class LocalFileDemo {
    public static void main(String[] args) throws Exception {
        ExecutionEnvironment env = ExecutionEnvironment.createLocalEnvironment();
        ClassLoader classLoader = LocalFileDemo.class.getClassLoader();
        java.net.URL appURL = classLoader.getResource("app.txt");
        Optional.of(appURL).map(url -> {
            String path=appURL.getPath();
            DataSet<String> data = env.readTextFile(path);

            data.filter(new FilterFunction<String>() {
                @Override
                public boolean filter(String value) {
                    return value.startsWith("http://");
                }
            });
            //   .writeAsText("D:\\working\\cqu20141693\\flink");
            try {
                data.print();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return true;
            //JobExecutionResult res = env.execute();
        }).orElseThrow(()->new Exception("app.txt file is not exit"));



    }
}
