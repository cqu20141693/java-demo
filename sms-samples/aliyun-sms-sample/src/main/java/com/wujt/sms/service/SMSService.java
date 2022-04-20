package com.wujt.sms.service;

import com.wujt.sms.domain.SMSReqDTO;

/**
 * @author wujt
 */
public interface SMSService {
    Object sendSMS(SMSReqDTO smsReqDTO) throws Exception;
}
