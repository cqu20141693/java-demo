package com.gow.spi.core;

/**
 * @author gow
 * @date 2021/6/25
 * SpiExtensionFactory.
 */
@Join
public class SpiExtensionFactory implements ExtensionFactory {

    @Override
    public <T> T getExtension(final String key, final Class<T> clazz) {
        if (clazz.isInterface() && clazz.isAnnotationPresent(SPI.class)) {
            ExtensionLoader<T> extensionLoader = ExtensionLoader.getExtensionLoader(clazz);
            return extensionLoader.getDefaultJoin();
        }
        return null;
    }
}
