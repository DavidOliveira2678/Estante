package br.com.estante.kafka;

import br.com.estante.config.KafkaTopicConfig;
import br.com.estante.event.AtividadeEvent;
import br.com.estante.service.FeedService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class AtividadeConsumer {

    private final FeedService feedService;

    @KafkaListener(topics = KafkaTopicConfig.TOPICO_ATIVIDADES, groupId = "estante-social")
    public void consumir(String mensagem) {
        log.info("Evento recebido do Kafka: {}", mensagem);
        AtividadeEvent evento = AtividadeEvent.fromMensagem(mensagem);
        feedService.registrarAtividade(evento.actorId(), evento.type(), evento.targetId());
    }
}