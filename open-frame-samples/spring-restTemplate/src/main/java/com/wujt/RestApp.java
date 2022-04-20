package com.wujt;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

/**
 * @author wujt
 */
@SpringBootApplication
public class RestApp implements CommandLineRunner {

    @Autowired
    private RestTemplate restTemplate;

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    public static void main(String[] args) {

        SpringApplication.run(RestApp.class, args);
    }

    @Override
    public void run(String... args) {
        String url="http://wibu.com/storage/ga-ea/common/202204/c1c9229781a94f538a307ca2d22535c6.whl";
        //testSendMsg();
      //  Integer ssrc = getSsrc("32323232323");
      //  System.out.println(ssrc);
    }

    private Integer getSsrc(String user) {

        String url =
                "http://172.30.203.21:1985/api/v1/gb28181?action=create_channel&stream=[stream]&port_mode=fixed&app"
                        + "=live&id="
                        + user;
        ResponseEntity<SrsResponse> entity = restTemplate.getForEntity(url, SrsResponse.class);
        Integer ssrc = entity.getBody().getSsrc();
        return ssrc;
    }

    private void testSendMsg() {
        Map<String, Object> params = new HashMap<>();
        String deviceKey = "53387c0762ad468685a4fec9a168bdbd";
        String type = "command";
        String businessId = "123456789";
        String topic = "ctl/13343242";
        params.put("deviceKey", deviceKey);
        params.put("type", type);
        params.put("businessId", businessId);
        params.put("topic", topic);
        String url =
                "http://localhost:9093/devices/sendQos1Msg?deviceKey={deviceKey}&type={type}&businessId={businessId"
                        + "}&topic={topic}";


        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        byte[] bytes = new byte[]{116, 97, 111, 103, 101};
        HttpEntity<byte[]> entity = new HttpEntity<>(bytes, headers);

        ResponseEntity<Boolean> booleanResponseEntity = restTemplate.postForEntity(url, entity, Boolean.class, params);
        System.out.println(booleanResponseEntity.getBody());
    }
}
