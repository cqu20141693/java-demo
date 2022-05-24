package com.cc.ocpp.api;

import com.cc.ocpp.OCPPClient;
import com.cc.ocpp.network.cp.CPMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import static com.cc.ocpp.client.Utils.*;

/**
 * 协议api
 * wcc 2022/5/6
 */
@RestController
@RequestMapping("ocpp")
public class ProtocolAPI {

    @Autowired
    private OCPPClient app;

    @PostMapping("ping")
    public String ping() {
        CPMessage message = getDefaultPing(0);
        app.getChannel().ifPresent(channel -> channel.writeAndFlush(message));
        return "success";
    }

    @PostMapping("enableCharge")
    public String enableCharge() {
        CPMessage enable = getDefaultEnableCharging();
        app.getChannel().ifPresent(channel -> channel.writeAndFlush(enable));
        return "success";
    }

    @PostMapping("chargingReply")
    public String chargingReply(@RequestParam("num") Short num) {
        CPMessage enable = getDefaultChargingReply();
        enable.getHeader().setSequence(num);
        app.getChannel().ifPresent(channel -> channel.writeAndFlush(enable));
        return "success";
    }

    @PostMapping("login")
    public String login(@RequestBody String message) {
        CPMessage loginMessage;
        if (message == null) {
            loginMessage = getDefaultLoginMessage();
        } else {
            loginMessage = (CPMessage) getCPMessage(message).getObj();
        }
        app.getChannel().ifPresent(chan -> {
            chan.writeAndFlush(loginMessage);
        });
        return "success";

    }

}
