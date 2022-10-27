package com.bcopstein.Emprestimos;

public class EmprestimoDTO {
    private boolean segurado;
    private boolean jurosCompostos;
    private double valor;
    private double taxa;
    private int nroParcelas;
    private double valorTotal;
    private double valorParcela;
    
    public EmprestimoDTO(boolean segurado, boolean jurosCompostos, double valor, double taxa, int nroParcelas,
            double valorTotal, double valorParcela) {
        this.segurado = segurado;
        this.jurosCompostos = jurosCompostos;
        this.valor = valor;
        this.taxa = taxa;
        this.nroParcelas = nroParcelas;
        this.valorTotal = valorTotal;
        this.valorParcela = valorParcela;
    }

    public boolean isSegurado() {
        return segurado;
    }

    public boolean isJurosCompostos() {
        return jurosCompostos;
    }

    public double getValor() {
        return valor;
    }

    public double getTaxa() {
        return taxa;
    }

    public int getNroParcelas() {
        return nroParcelas;
    }

    public double getValorTotal() {
        return valorTotal;
    }

    public double getValorParcela() {
        return valorParcela;
    }
}
