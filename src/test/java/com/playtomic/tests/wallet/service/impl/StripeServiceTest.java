package com.playtomic.tests.wallet.service.impl;


import static com.github.tomakehurst.wiremock.client.WireMock.*;

import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import com.playtomic.tests.wallet.service.StripeAmountTooSmallException;
import com.playtomic.tests.wallet.service.StripeService;
import com.playtomic.tests.wallet.service.StripeServiceException;
import java.math.BigDecimal;
import java.net.URI;
import org.junit.jupiter.api.*;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpStatus;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.wiremock.integrations.testcontainers.WireMockContainer;

@Testcontainers
@WireMockTest
@Disabled // TODO check how-to make wiremock library use testcontaienrs image
public class StripeServiceTest {

    @Container
    private static final WireMockContainer wiremockServer = new WireMockContainer("wiremock/wiremock:2.35.0");


    private StripeService subject;

    @BeforeAll
    static void beforeAll() {
        wiremockServer.start();
        WireMock.configureFor(wiremockServer.getPort());
    }

    @AfterAll
    static void afterAll() {
        wiremockServer.stop();
    }

    @BeforeEach
    void setup() {
        URI chargesTestUri = URI.create(wiremockServer.getBaseUrl()+"/charges");
        URI refundTestUri = URI.create(wiremockServer.getBaseUrl()+"/refunds");
        subject = new StripeService(chargesTestUri, refundTestUri, new RestTemplateBuilder());
    }

    @Test
    public void test_exception() {
        stubFor(post("/charges")
                .willReturn(serverError().withStatus(HttpStatus.UNPROCESSABLE_ENTITY.value())));

        Assertions.assertThrows(StripeAmountTooSmallException.class, () -> {
            subject.charge("4242 4242 4242 4242", new BigDecimal(5));
        });
    }

    @Test
    public void test_ok() throws StripeServiceException {
        stubFor(post("/charges")
                .willReturn(ok()
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"id\": \"foo\"}")));

        subject.charge("4242 4242 4242 4242", new BigDecimal(15));
    }
}
