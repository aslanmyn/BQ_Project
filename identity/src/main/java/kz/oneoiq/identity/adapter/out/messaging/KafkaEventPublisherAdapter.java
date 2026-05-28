package kz.oneoiq.identity.adapter.out.messaging;

import com.fasterxml.jackson.databind.ObjectMapper;
import kz.oneoiq.identity.domain.port.out.DomainEventPublisher;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class KafkaEventPublisherAdapter implements DomainEventPublisher {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    @Override
    @SneakyThrows
    public void publish(String topic, Object event) {
        String json = objectMapper.writeValueAsString(event);
        kafkaTemplate.send(topic, json);
    }
}
