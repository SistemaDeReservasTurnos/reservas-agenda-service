package com.servicio.reservas.agenda.infraestructure.output.messaging;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.servicio.reservas.agenda.infraestructure.exception.TechnicalException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class EventPublisher {

    private final ObjectMapper objectMapper;
    private final RabbitTemplate rabbitTemplate;

    @Value("${agenda.events.exchange}")
    private String exchange;

    public EventPublisher(RabbitTemplate rabbitTemplate, ObjectMapper objectMapper) {
        this.rabbitTemplate = rabbitTemplate;
        this.objectMapper = objectMapper;
    }

    public void publishEvent(String routingKey, Object event) {
        try {
            String json = objectMapper.writeValueAsString(event);
            rabbitTemplate.convertAndSend(exchange, routingKey, json);
        } catch (Exception e) {
            throw new TechnicalException("Error serializing event", e);
        }
    }
}
