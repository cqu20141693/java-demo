package cmo.gow.dubbo.spi.core;

import org.apache.dubbo.common.extension.LoadingStrategy;
import org.apache.dubbo.common.lang.Prioritized;

public class DubboExternalLoadingStrategy implements LoadingStrategy {
    @Override
    public String directory() {
        return "META-INF/dubbo/external/";
    }

    @Override
    public boolean preferExtensionClassLoader() {
        return LoadingStrategy.super.preferExtensionClassLoader();
    }

    @Override
    public String[] excludedPackages() {
        return LoadingStrategy.super.excludedPackages();
    }

    @Override
    public boolean overridden() {
        return true;
    }

    @Override
    public int getPriority() {
       return MAX_PRIORITY+1;
    }

    @Override
    public int compareTo(Prioritized that) {
        return LoadingStrategy.super.compareTo(that);
    }
}
