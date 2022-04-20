package com.wujt;

import com.wujt.fallback.TestClientCallback;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author wujt
 */
@FeignClient(value = "producer-service", fallback = TestClientCallback.class)
@RequestMapping("producer")
public interface TestClient {
    @GetMapping("/test")
    String getInfo(@RequestParam("name") String name);
}
