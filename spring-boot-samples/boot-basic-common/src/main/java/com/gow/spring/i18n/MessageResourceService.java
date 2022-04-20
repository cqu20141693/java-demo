package com.gow.spring.i18n;

public interface MessageResourceService {

    String getMessage(String code);

    /**
     * @param code ：对应messages配置的key.
     * @param args : 数组参数.
     * @return
     */
    String getMessage(String code, Object[] args);

    /**
     * @param code           ：对应messages配置的key.
     * @param args           : 数组参数.
     * @param defaultMessage : 没有设置key的时候的默认值.
     * @return
     */
    String getMessage(String code, Object[] args, String defaultMessage);
}
