package com.bcopstein.Emprestimos;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.util.stream.Stream;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class EmprestimosApplicationMockTests {
    @Autowired
    private MockMvc mockMvc;

    public static final double TXSEGUROPADRAO = 0.01;
    public static final double IOF = 0.06;

    private static Stream<Arguments> provideValuesToTest() {
        return Stream.of(
                Arguments.of(20000, 2.5, 10),
                Arguments.of("", 2, 5),
                Arguments.of(15000, 1, 12),
                Arguments.of(180000, 0, "")
        );
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
    public void testMockSimpleFees(double valor, double taxa, int nroParcelas) throws Exception {
        //when(service.jurosEmprestimoJurosSimples(valor, taxa, nroParcelas));

        Double totalValueExpected = this.calculateSimpleFee(valor, taxa, nroParcelas);
        Double totalShareExpected = totalValueExpected / nroParcelas;

        this.mockMvc.perform(get("/emprestimo/jurosSimples?valor=" + valor + "&parcelas=" + nroParcelas + "&taxa=" + taxa)).andDo(print()).andExpect(status().isOk());
        //.andExpect(content().string(containsString("Hello, World")))
    }

    @ParameterizedTest
    @MethodSource("provideValuesToTest")
    public void testMockCompositeFees(double valor, double taxa, int nroParcelas) throws Exception {
        //when(service.jurosEmprestimoJurosCompostos(valor, taxa, nroParcelas));

        Double totalValueExpected = this.calculateCompositeFees(valor, taxa, nroParcelas);
        Double totalShareExpected = totalValueExpected / nroParcelas;

        this.mockMvc.perform(get("/emprestimo/jurosCompostos?valor=" + valor + "&parcelas=" + nroParcelas + "&taxa=" + taxa)).andDo(print()).andExpect(status().isOk())
                .andExpect(content().string(containsString("Hello, World")));
    }
}
