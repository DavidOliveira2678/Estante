package br.com.estante.event;

import br.com.estante.entity.enums.ActivityType;

public record AtividadeEvent(Long actorId, ActivityType type, Long targetId) {

    public String toMensagem() {
        return actorId + "|" + type.name() + "|" + targetId;
    }

    public static AtividadeEvent fromMensagem(String mensagem) {
        String[] p = mensagem.split("\\|");
        return new AtividadeEvent(Long.valueOf(p[0]), ActivityType.valueOf(p[1]), Long.valueOf(p[2]));
    }
}