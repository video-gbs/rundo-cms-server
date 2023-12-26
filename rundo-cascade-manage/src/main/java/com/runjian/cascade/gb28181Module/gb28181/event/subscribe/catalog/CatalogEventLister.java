package com.runjian.cascade.gb28181Module.gb28181.event.subscribe.catalog;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;


/**
 * catalog事件
 */
@Component
public class CatalogEventLister implements ApplicationListener<CatalogEvent> {

    private final static Logger logger = LoggerFactory.getLogger(CatalogEventLister.class);



    @Override
    public void onApplicationEvent(CatalogEvent event) {

    }
}
 