package com.gow.pulsar.api;

import com.gow.common.Result;
import org.apache.pulsar.client.admin.PulsarAdmin;
import org.apache.pulsar.client.admin.PulsarAdminException;
import org.apache.pulsar.client.admin.Topics;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author gow
 * @date 2021/7/15
 */
@RestController
@RequestMapping("admin")
public class AdminTopicAPI {

    @Autowired
    private PulsarAdmin pulsarAdmin;

    @DeleteMapping("deleteTopic")
    public Result<Boolean> deleteTopic(@RequestParam("topic") String topic) {
        try {

            Topics topics = pulsarAdmin.topics();
            topics.delete(topic);
            return Result.ok(true);
        } catch (PulsarAdminException pulsarAdminException) {
            pulsarAdminException.printStackTrace();
            return Result.ok(false);
        }
    }
}
