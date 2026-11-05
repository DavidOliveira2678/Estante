package br.com.estante.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfig {

    public static final String TOPICO_ATIVIDADES = "topico-atividades";

    @Bean
    public NewTopic topicoAtividades() {
        return TopicBuilder.name(TOPICO_ATIVIDADES).partitions(3).replicas(1).build();
    }
}