var InputStreamCallback = Java.type("org.apache.nifi.processor.io.InputStreamCallback");
var OutputStreamCallback = Java.type("org.apache.nifi.processor.io.OutputStreamCallback");
var IOUtils = Java.type("org.apache.commons.io.IOUtils");
var StandardCharsets = Java.type("java.nio.charset.StandardCharsets");
var flowFile = session.get();
if (flowFile != null) {
    try {
        var text = [];
        // 读取flowFile中内容
        session.read(flowFile, new InputStreamCallback(function (inputStream) {
            var str = IOUtils.toString(inputStream, StandardCharsets.UTF_8);
            // log.info("origin"+str);
            //由JSON字符串转换为JSON对象
            var obj = JSON.parse(str);
            Object.keys(obj).forEach(function (key) {
                obj[key].forEach(function (property) {
                    log.info("ts" + property.ts);
                    text.push(property.values)
                })
            })
            //将JSON对象转化为JSON字符
            var ary = obj.d8702b8eb9b1fd2b31c8d2a62b2a553b;
            var arrStr = JSON.stringify(ary);
            log.info("device:" + arrStr);
            var props = JSON.parse(arrStr);
            log.info("values" + JSON.stringify(props[0].values));
        }));
        log.info("txt:" + JSON.stringify(text));
        // 向flowFile中写入内容
        flowFile = session.write(flowFile, new OutputStreamCallback(function (outputStream) {
            outputStream.write(text)
        }));
        session.transfer(flowFile, REL_SUCCESS)
    } catch (e) {
        log.info(e)
        session.transfer(flowFile, REL_FAILURE)
    }
}
