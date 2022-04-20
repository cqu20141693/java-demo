package com.wujt.autoconfig.selector;

import com.wujt.autoconfig.domain.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.util.StringUtils;

import java.util.Collection;
import java.util.HashSet;

/**
 * @author wujt  2021/6/1
 */
@Slf4j
public class MyImportSelector implements ImportSelector {
    @Override
    public String[] selectImports(AnnotationMetadata importingClassMetadata) {
        log.info("MyImportSelector invoked");
        return StringUtils.toStringArray(getCompanyImportClass());
    }

    private Collection<String> getCompanyImportClass() {

        HashSet<String> set = new HashSet<>();
        set.add(HuaWei.class.getName());
        set.add(XiaoMi.class.getName());
        set.add(Alibaba.class.getName());
        return set;
    }
}
