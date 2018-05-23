package org.basaraba.flowers.events;

import org.springframework.context.ApplicationEvent;

public class ContextInitialized extends ApplicationEvent {
    public ContextInitialized(Object source) {
        super(source);
    }
}
