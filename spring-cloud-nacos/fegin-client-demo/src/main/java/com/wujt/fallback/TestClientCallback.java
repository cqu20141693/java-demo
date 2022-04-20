package com.wujt.fallback;

import com.wujt.TestClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author wujt
 */
@Component
@RequestMapping({"producer/fallback"})
public class TestClientCallback implements TestClient {


    @Override
    public String getInfo(String name) {
        return null;
    }
}
