package com.wujt.sms.controller;

import com.wujt.sms.domain.SMSReqDTO;
import com.wujt.sms.service.SMSService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * @author wujt
 */
@RestController
public class SMSController {

    @Autowired
    private SMSService smsService;

    @PostMapping("/sms")
    public Object sendSms(@RequestBody @Valid SMSReqDTO smsReqDTO) throws Exception {
        return smsService.sendSMS(smsReqDTO);
    }
}
