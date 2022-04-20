package cmo.gow.dubbo.spi.uitls;

import org.apache.dubbo.common.extension.ExtensionLoader;

public class DubboServiceLoader {

    public static <T> T getDubboSPIInstance(Class<T> type) {
        return ExtensionLoader.getExtensionLoader(type).getDefaultExtension();
    }

    public static <T> ExtensionLoader<T> getExtensionLoader(Class<T> type) {
        return ExtensionLoader.getExtensionLoader(type);
    }
}
