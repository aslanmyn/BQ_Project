package kz.oneoiq.identity.domain.port.out;

public interface DomainEventPublisher {

    void publish(String topic, Object event);
}
