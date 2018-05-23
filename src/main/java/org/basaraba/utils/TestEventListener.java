package org.basaraba.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Function;

/**
 * Test utility class for attaching to spring application context to listen for events.
 */
public class TestEventListener implements ApplicationListener {
    private static final Logger LOGGER = LoggerFactory.getLogger(TestEventListener.class);

    private final List<Object> events = new ArrayList<>();
    private ApplicationEventPublisher publisher;

    public TestEventListener attach(final ApplicationContext context) {
        if (context instanceof ConfigurableApplicationContext) {
            ConfigurableApplicationContext configurableContext = (ConfigurableApplicationContext) context;
            configurableContext.addApplicationListener(this);
            configurableContext.getBeanFactory().registerSingleton(this.getClass().getSimpleName(), this);
            configurableContext.getBeanFactory().initializeBean(this, this.getClass().getSimpleName());
            publisher = configurableContext;
        } else {
            throw new RuntimeException("Expected an application context of type: " + ConfigurableApplicationContext.class.getCanonicalName());
        }
        return this;
    }

    public void publishEvent(Object o) {
        this.publisher.publishEvent(o);
    }

    @Override
    public void onApplicationEvent(ApplicationEvent applicationEvent) {
        LOGGER.debug("Event received {}", applicationEvent);
        events.add(applicationEvent);
    }

    public synchronized <T> boolean validateEventOccurs(final Class<T> clazz) {
        for (Object event : new ArrayList<>(events)) {
            if (event.getClass().equals(clazz)) {
                return true;
            }
        }
        return false;
    }
}
