package com.exemplo.richard.outboxschedule.service;

import com.exemplo.richard.outboxschedule.domain.TesteMessage;
import com.exemplo.richard.outboxschedule.entity.TesteOutbox;
import com.exemplo.richard.outboxschedule.repository.TesteOutboxRepository;
import io.opentelemetry.api.GlobalOpenTelemetry;
import io.opentelemetry.api.trace.*;
import io.opentelemetry.context.Context;
import io.opentelemetry.context.Scope;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
@Slf4j
public class NotificarCriacaoTesteService {

    private final String topico;
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final TesteOutboxRepository testeOutboxRepository;
    private static final Random RANDOM = new Random();

    NotificarCriacaoTesteService(@Value("${topic.name}") String topico,
                                 KafkaTemplate<String, Object> kafkaTemplate,
                                 TesteOutboxRepository testeOutboxRepository) {

        this.topico = topico;
        this.kafkaTemplate = kafkaTemplate;
        this.testeOutboxRepository = testeOutboxRepository;
    }

    public void notificarCriacaoTeste() {

        var testesOutbox = testeOutboxRepository.findAll();

        testesOutbox
            .stream()
            .map(this::criarTesteMessage)
            .forEach(this::enviarMensagem);

        testeOutboxRepository.deleteAllById(testesOutbox.stream().map(TesteOutbox::getId).toList());

    }

    private TesteMessage criarTesteMessage(TesteOutbox testeOutbox) {
        return new TesteMessage(testeOutbox.getName(), testeOutbox.getEmail(), testeOutbox.getTraceId());
    }

    private void enviarMensagem(TesteMessage testeMessage) {

        var span = createNewSpan(testeMessage.traceId());

        try (Scope ignored = span.makeCurrent()) {
            kafkaTemplate.send(topico, testeMessage);
            log.info("Teste notificado com sucesso: {}", testeMessage);
        } finally {
            span.end();
        }
    }

    private Span createNewSpan(String traceId) {
        var remoteContext = SpanContext.createFromRemoteParent(traceId, SpanId.fromLong(RANDOM.nextLong()),
            TraceFlags.getSampled(),
            TraceState.getDefault());
        return GlobalOpenTelemetry.getTracer("").spanBuilder("schedule-teste-outbox")
            .setParent(Context.current().with(Span.wrap(remoteContext)))
            .startSpan();
    }

}
