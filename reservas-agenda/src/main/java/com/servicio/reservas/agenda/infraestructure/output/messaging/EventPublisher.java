package com.servicio.reservas.agenda.infraestructure.output.messaging;

import com.fasterxml.jackson.databind.ObjectMapper;
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

    public void publicarEvento(String routingKey, Object evento) {
        try {
            String json = objectMapper.writeValueAsString(evento);
            rabbitTemplate.convertAndSend(exchange, routingKey, json);
        } catch (Exception e) {
            throw new RuntimeException("Error serializando evento", e);
        }
    }

}
