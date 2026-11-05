package br.com.estante.kafka;

import br.com.estante.config.KafkaTopicConfig;
import br.com.estante.event.AtividadeEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class AtividadePublisher {

    private final KafkaTemplate<String, String> kafkaTemplate;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void aoConfirmar(AtividadeEvent evento) {
        kafkaTemplate.send(
                KafkaTopicConfig.TOPICO_ATIVIDADES,
                String.valueOf(evento.actorId()),
                evento.toMensagem());
    }
}