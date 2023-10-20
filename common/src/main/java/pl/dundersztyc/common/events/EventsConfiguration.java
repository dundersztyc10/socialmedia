package pl.dundersztyc.common.events;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class EventsConfiguration {

    @Bean
    EventPublisher eventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        return applicationEventPublisher::publishEvent;
    }

}
