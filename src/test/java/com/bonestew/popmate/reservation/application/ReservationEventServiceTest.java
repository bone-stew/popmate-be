package com.bonestew.popmate.reservation.application;

import static org.junit.jupiter.api.Assertions.*;

import com.bonestew.popmate.reservation.application.dto.ReservationRequest;
import com.bonestew.popmate.reservation.application.dto.WifiInfoRequest;
import com.bonestew.popmate.reservation.application.dto.WifiRequest;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class ReservationEventServiceTest {

    @Autowired
    private ReservationEventService reservationEventService;

    @Test
    void 동시에_예약을_요청하면_정해진_인원수만_예약된다() throws InterruptedException {
        final WifiInfoRequest wifiInfoRequest = new WifiInfoRequest("ssid", "bssid");
        final ReservationRequest reservationRequest = new ReservationRequest(3, List.of(wifiInfoRequest));
        final int peopleCount = 10;
        final ExecutorService executorService = Executors.newFixedThreadPool(peopleCount);
        final CountDownLatch countDownLatch = new CountDownLatch(peopleCount);

        for (int i = 0; i < peopleCount; i++) {
            executorService.execute(() -> {
                try {
                    reservationEventService.reserve(1L, 1L, reservationRequest);
                } finally {
                    countDownLatch.countDown();
                }
            });
        }

        countDownLatch.await(); // 모든 스레드가 완료될 때까지 대기

    }
}
