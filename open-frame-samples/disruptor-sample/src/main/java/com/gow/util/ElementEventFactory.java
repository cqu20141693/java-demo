package com.gow.util;

import com.lmax.disruptor.EventFactory;
import com.wujt.model.Element;

/**
 * @author wujt
 */
public class ElementEventFactory implements EventFactory<Element>
{
    @Override
    public Element newInstance() {
        return new Element();
    }
}
