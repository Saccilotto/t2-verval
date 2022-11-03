package com.bcopstein.Emprestimos;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;

import java.util.LinkedHashMap;
import java.util.stream.Stream;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class EmprestimosApplicationTests {
    @Autowired
    private EmprestimosApplication controller;

    @LocalServerPort
    private int port;

    public static final double TXSEGUROPADRAO = 0.01;
    public static final double IOF = 0.06;

    @Autowired
    private TestRestTemplate restTemplate;

    private static Stream<Arguments> provideValuesToTest() {
        return Stream.of(
                Arguments.of(20000, 2.5, 10),
                Arguments.of("", 2, 5),
                Arguments.of(15000, 1, 12),
                Arguments.of(180000, 0, "")
        );
    }

    @Test
    public void contextLoads() throws Exception {
        assertThat(controller).isNotNull();
    }

    private double calculateSimpleFee(double valor, double taxa, int nroParcelas) {
        double valorIof = valor * IOF;
        return valor + valorIof + (valor * (taxa + TXSEGUROPADRAO) * nroParcelas);
    }

    private double calculateCompositeFees(double valor, double taxa, int nroParcelas) {
        double tx = taxa;
        double valorIof = valor * IOF;
        tx += TXSEGUROPADRAO;
        double valorAcum = valor;
        while (nroParcelas > 0) {
            valorAcum += valorAcum * tx;
            nroParcelas--;
        }
        return valor + valorIof + (valorAcum - valor);
    }

    @ParameterizedTest
    @MethodSource("provideValuesToTest")
    public void testSimpleFees(double valor, double taxa, int nroParcelas) throws Exception {

        String buildedUrl = "http://localhost:" + port + "/emprestimo/jurosSimples?valor=" + valor + "&parcelas=" + nroParcelas + "&taxa=" + taxa;

        LinkedHashMap response = this.restTemplate.getForObject(buildedUrl,
                LinkedHashMap.class);

        Double totalValueExpected = this.calculateSimpleFee(valor, taxa, nroParcelas);
        Double totalShareExpected = totalValueExpected / nroParcelas;

        Double receivedTotalValue = (Double) response.get("valorTotal");
        Double receivedTotalShare = (Double) response.get("valorParcela");

        assertThat(totalValueExpected).isEqualTo((receivedTotalValue));
        assertThat(receivedTotalShare).isEqualTo((totalShareExpected));
    }

    @ParameterizedTest
    @MethodSource("provideValuesToTest")
    public void testCompositeFees(double valor, double taxa, int nroParcelas) throws Exception {

        String buildedUrl = "http://localhost:" + port + "/emprestimo/jurosCompostos?valor=" + valor + "&parcelas=" + nroParcelas + "&taxa=" + taxa;

        LinkedHashMap response = this.restTemplate.getForObject(buildedUrl,
                LinkedHashMap.class);

        Double totalValueExpected = this.calculateCompositeFees(valor, taxa, nroParcelas);
        Double totalShareExpected = totalValueExpected / nroParcelas;

        Double receivedTotalValue = (Double) response.get("valorTotal");
        Double receivedTotalShare = (Double) response.get("valorParcela");

        assertThat(totalValueExpected).isEqualTo((receivedTotalValue));
        assertThat(receivedTotalShare).isEqualTo((totalShareExpected));
    }
}
