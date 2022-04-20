package com.wujt.autoconfig;

import com.wujt.autoconfig.selector.MyImportSelector;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @author wujt  2021/6/1
 */
@Configuration
@Import({MyImportSelector.class})
public class MySelectorConfiguration {

}
