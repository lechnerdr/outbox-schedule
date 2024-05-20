package com.exemplo.richard.outboxschedule.schedule;

import com.exemplo.richard.outboxschedule.service.NotificarCriacaoTesteService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class TesteOutboxSchedule {

    private final NotificarCriacaoTesteService notificarCriacaoTesteService;

    @Scheduled(initialDelayString = "${schedule.outbox.inicio-execucao:1000}",
        fixedDelayString = "${schedule.outbox.intervalo-execucao:10000}")
    @SchedulerLock(name = "TaskScheduler_scheduleNotificarCriacaoTeste",
        lockAtLeastFor = "PT7S", lockAtMostFor = "PT9S"
    )
    void scheduleNotificarCriacaoTeste() {
        notificarCriacaoTesteService.notificarCriacaoTeste();
    }

}
