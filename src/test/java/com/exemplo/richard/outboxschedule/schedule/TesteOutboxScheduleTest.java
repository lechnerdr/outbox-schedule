package com.exemplo.richard.outboxschedule.schedule;

import com.exemplo.richard.outboxschedule.config.EnableScheduleConfig;
import com.exemplo.richard.outboxschedule.service.NotificarCriacaoTesteService;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import static org.awaitility.Awaitility.await;
import static org.awaitility.Durations.TWO_SECONDS;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;


//@SpringJUnitConfig(TesteOutboxSchedule.class)
@SpringJUnitConfig(EnableScheduleConfig.class)
@ActiveProfiles("schedule")
class TesteOutboxScheduleTest {

    @MockBean
    private NotificarCriacaoTesteService notificarCriacaoTesteService;

    @SpyBean
    private TesteOutboxSchedule testeOutboxSchedule;

    @Test
    @SneakyThrows
    void deveExecutarScheduleNotificarCriacaoTeste() {

        doNothing().when(notificarCriacaoTesteService).notificarCriacaoTeste();

        await()
            .atMost(TWO_SECONDS)
            .untilAsserted(() -> verify(testeOutboxSchedule).scheduleNotificarCriacaoTeste());

        verify(notificarCriacaoTesteService).notificarCriacaoTeste();

    }

    //    @Test

//    void deveExecutarScheduleNotificarCriacaoTeste() {
//
//        doNothing().when(notificarCriacaoTesteService).notificarCriacaoTeste();
//
//        testeOutboxSchedule.scheduleNotificarCriacaoTeste();
//
//        verify(notificarCriacaoTesteService).notificarCriacaoTeste();
//
//    }

}
