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

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void contextLoads() throws Exception {
        assertThat(controller).isNotNull();
    }

    @Test
    public void testSimpleFees() throws Exception {
        double valor = 20000;
        double taxa = 2.5;
        int nroParcelas = 10;

        String buildedUrl = "http://localhost:" + port + "/emprestimo/jurosSimples?valor=" + valor + "&parcelas=" + nroParcelas + "&taxa=" + taxa;

        LinkedHashMap response = this.restTemplate.getForObject(buildedUrl,
                LinkedHashMap.class);

        // TODO CALCULAR O VALOR BASEADO NA ENTRADA
        Double totalValueExpected = 523199.99999999994;
        Double totalShareExpected = 52319.99999999999;

        Double receivedTotalValue = (Double)response.get("valorTotal");
        Double receivedTotalShare = (Double)response.get("valorParcela");

        assertThat(totalValueExpected).isEqualTo((receivedTotalValue));
        assertThat(receivedTotalShare).isEqualTo((totalShareExpected));
    }

    public void testCompositeFees() throws Exception {
        var x = this.restTemplate.getForObject("http://localhost:" + port + "/emprestimo/jurosCompostos?valor=2000&parcelas=10&taxa=1.5",
                Object.class);


        assertThat(this.restTemplate.getForObject("http://localhost:" + port + "/emprestimo/jurosCompostos?valor=2000&parcelas=10&taxa=1.5",
                String.class)).contains("Hello, World");
    }

}
