package com.bcopstein.Emprestimos;

public class CalculoDeJuros {
    public static final double TXSEGUROPADRAO = 0.01;
    private double txSeguro;
    private boolean comSeguro;

    public CalculoDeJuros() {
        this.txSeguro = TXSEGUROPADRAO;
        this.comSeguro = true;
    }

    public void setSeguro(boolean seguro) {
        this.comSeguro = seguro;
    }

    public boolean comSeguro() {
        return this.comSeguro;
    }

    public void setTaxaSeguro(double taxa){
        txSeguro = taxa;
    }

    public double getTaxaSeguro(){
        return txSeguro;
    }

    public double jurosEmprestimoJurosSimples(double valor, double taxa, int nroParcelas) {
        if (comSeguro) {
            return (valor * (taxa + txSeguro) * nroParcelas);
        } else {
            return (valor * taxa * nroParcelas);
        }
    }

    public double jurosEmprestimoJurosCompostos(double valor, double taxa, int nroParcelas) {
        double tx = taxa;
        if (comSeguro){
            tx += txSeguro;
        }
        double valorAcum = valor;
        while (nroParcelas > 0) {
            valorAcum += valorAcum * tx;
            nroParcelas--;
        }
        return valorAcum - valor;
    }
}
