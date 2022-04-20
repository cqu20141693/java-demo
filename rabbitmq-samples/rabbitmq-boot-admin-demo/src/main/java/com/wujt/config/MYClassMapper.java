package com.wujt.config;

import com.wujt.model.User;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.support.converter.ClassMapper;

import java.util.HashMap;
import java.util.Map;

/**
 * @author wujt
 * @version 1.0
 * @date 2020/3/23
 */
public class MYClassMapper implements ClassMapper {
    public MYClassMapper() {
        super();
    }

    private static final String DEFAULT_TYPE_CLASS = "__TypeId__";

    public static Map<String, Class<?>> mqObjectMap = new HashMap<String, Class<?>>();

    static {
        mqObjectMap.put(User.class.getSimpleName(), User.class);
    }

    @Override
    public Class<?> toClass(MessageProperties properties) {
        Object obj = properties.getHeaders().get(DEFAULT_TYPE_CLASS);
        if (null != obj) {
            return mqObjectMap.get(obj.toString());
        }
        return null;

    }

    @Override
    public void fromClass(Class<?> clazz, MessageProperties properties) {
        properties.setHeader(DEFAULT_TYPE_CLASS, clazz.getSimpleName());
    }
}
