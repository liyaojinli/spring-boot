package org.liyaojin.springboot.listeners;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationListener;

/**
 * 监听SpringBoot的started事件
 * {@link ApplicationStartedEvent} extends {@link org.springframework.boot.context.event.SpringApplicationEvent}
 *
 * @author liyaojin
 */
public class SpringApplicationStartedListener implements ApplicationListener<ApplicationStartedEvent> {

    private final static Logger LOGGER = LoggerFactory.getLogger(SpringApplicationStartedListener.class);

    @Override
    public void onApplicationEvent(ApplicationStartedEvent event) {
        LOGGER.info("the ApplicationStartedEvent is fired,the event is {}", event);
    }
}
