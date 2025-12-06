package com.servicio.reservas.agenda.infraestructure.config;

import org.springframework.amqp.core.TopicExchange;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AgendaRabbitConfig {

    @Value("${agenda.events.exchange}")
    private String agendaEventsExchange;

    @Bean
    public TopicExchange agendaEventsExchange() {
        return new TopicExchange(agendaEventsExchange, true, false);
    }

}
