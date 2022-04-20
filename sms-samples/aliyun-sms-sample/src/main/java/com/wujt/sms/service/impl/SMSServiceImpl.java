package com.wujt.sms.service.impl;

import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.http.MethodType;
import com.wujt.sms.config.SMSConfig;
import com.wujt.sms.domain.MobileNumber;
import com.wujt.sms.domain.SMSReqDTO;
import com.wujt.sms.service.SMSService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author wujt
 */
@Slf4j
@Service
public class SMSServiceImpl implements SMSService {
    @Autowired
    private SMSConfig smsConfig;

    @Autowired
    private IAcsClient acsClient;

    @Override
    public Object sendSMS(SMSReqDTO smsReqDTO) throws Exception {
        log.info("smsReqDTO={}", smsReqDTO);
        SendSmsRequest request = new SendSmsRequest();
        request.setMethod(MethodType.POST);
        MobileNumber mobileNumber = MobileNumber.fromString(smsReqDTO.getMobile());
        request.setPhoneNumbers(mobileNumber.toNumberString());
        request.setSignName(smsConfig.getSignatureName());
        request.setTemplateCode(smsConfig.getTplMap().get(smsReqDTO.getTplCode()));
        request.setTemplateParam(smsReqDTO.getParams());
        log.debug("mobile: {},code: {},param: {}", request.getPhoneNumbers(), request.getTemplateCode(), request.getTemplateParam());

        SendSmsResponse sendSmsResponse = acsClient.getAcsResponse(request);
        if (sendSmsResponse.getCode() != null && sendSmsResponse.getCode().equals("OK")) {
            log.info("发送{}短信成功", request.getPhoneNumbers());
            return sendSmsResponse.getBizId();
        } else if (sendSmsResponse.getCode() != null && "isv.BUSINESS_LIMIT_CONTROL".equals(sendSmsResponse.getCode())) {
            String message = sendSmsResponse.getMessage();
            log.info("发送短信错误信息:{}", message);
            if (message.contains("分钟级")) {
                throw new Exception("minute Permits:1");
            } else if (message.contains("小时级")) {
                throw new Exception("hour Permits:5");
            } else {
                throw new Exception("day Permits:10");
            }
        } else {
            String msg = "alidayu response error";
            log.error("发送短信错误(code:{},message:{})", sendSmsResponse.getCode(), sendSmsResponse.getMessage());
            throw new Exception(msg);
        }
    }
}
