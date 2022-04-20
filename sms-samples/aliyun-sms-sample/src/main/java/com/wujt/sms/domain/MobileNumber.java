package com.wujt.sms.domain;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author wujt
 */
@Data
public class MobileNumber {
    private String country;
    private String mobile;

    public MobileNumber(String mobile) throws Exception {
        this("86", mobile);
    }

    public MobileNumber(String country, String mobile) throws Exception {
        this.country = country;
        this.setMobile(mobile);
    }

    public void setMobile(String mobile) throws Exception {
        if (!isCorrectMobile(mobile)) {
            throw new Exception(String.format("not correct mobile num: %s", mobile));
        }
        this.mobile = mobile;
    }


    public String toNumberString() {
        return this.country + this.mobile;
    }

    @Override
    public String toString() {
        return String.format("%s-%s", this.country, this.mobile);
    }

    /**
     * create new MobileNumber instance by format string "country-mobile"
     */
    public static MobileNumber fromString(String mobileStr) throws Exception {
        if (mobileStr == null || mobileStr.isEmpty()) {
            throw new Exception("mobileStr is empty or null");
        }

        String[] segments = mobileStr.split("-");
        if (segments.length != 2) {
            throw new Exception("mobileStr must format as 'coutry-mobilenumber'");
        }

        return new MobileNumber(segments[0], segments[1]);
    }

    public static List<MobileNumber> fromListString(List<String> mobiles) throws Exception {
        if (mobiles == null) {
            return null;
        }

        List<MobileNumber> res = new ArrayList<>(mobiles.size());
        if (mobiles.isEmpty()) {
            return res;
        }


        for (String m : mobiles) {
            res.add(MobileNumber.fromString(m));
        }

        return res;
    }

    public boolean isCorrectMobile(String mobile) {
        String[] split = mobile.split("-");
        if (split.length == 2) {
            mobile = split[1];
        }

        if (mobile == null || mobile.isEmpty()) {
            return false;
        }

        for (int i = 0; i < mobile.length(); i++) {
            if (!Character.isDigit(mobile.charAt(i))) {
                return false;
            }
        }

        return true;
    }
}
