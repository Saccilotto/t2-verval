package com.bcopstein.Emprestimos;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;

import java.util.LinkedHashMap;

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

    @Test
    public void testSimpleFees() throws Exception {
        double valor = 20000;
        double taxa = 2.5;
        int nroParcelas = 10;

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

    public void testCompositeFees() throws Exception {
        double valor = 20000;
        double taxa = 2.5;
        int nroParcelas = 10;

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
