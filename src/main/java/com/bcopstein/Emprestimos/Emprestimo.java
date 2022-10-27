package com.bcopstein.Emprestimos;

public class Emprestimo {
    public static final double IOF = 0.06;
    private boolean segurado;
    private boolean jurosCompostos;
    private double valor;
    private double taxa;
    private int nroParcelas;
    private CalculoDeJuros calculoDeJuros;

    private Emprestimo(Builder builder) {
        this.segurado = builder.segurado;
        this.jurosCompostos = builder.jurosCompostos;
        this.valor = builder.valor;
        this.taxa = builder.taxa;
        this.nroParcelas = builder.nroParcelas;
        this.calculoDeJuros = builder.calculoDeJuros;
    }

    public static class Builder{
        private boolean segurado = true;
        private boolean jurosCompostos = true;
        private double valor = 1000.0;
        private double taxa = 0.035;
        private int nroParcelas = 5;
        private CalculoDeJuros calculoDeJuros = null;

        public Builder(CalculoDeJuros calc){
            calculoDeJuros = calc;
        }

        public Builder comSeguro(){
            segurado = true;
            return this;
        }

        public Builder semSeguro(){
            segurado = false;
            return this;
        }

        public Builder jurosCompostos(){
            jurosCompostos = true;
            return this;
        }
    
        public Builder jurosSimples(){
            jurosCompostos = false;
            return this;
        }

        public Builder valor(double valorEmp){
            valor = valorEmp;
            return this;
        }

        public Builder taxa(double txEmp){
            taxa = txEmp;
            return this;
        }

        public Builder parcelas(int nroPar){
            nroParcelas = nroPar;
            return this;
        }

        public Emprestimo build(){
            return new Emprestimo(this);
        }
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

    public double custoTotal() {
        double valorIof = valor * IOF;
        if (this.isJurosCompostos()) {
            return valor + valorIof + calculoDeJuros.jurosEmprestimoJurosCompostos(getValor(), getTaxa(), getNroParcelas());
        }else{
            return valor + valorIof + calculoDeJuros.jurosEmprestimoJurosSimples(getValor(), getTaxa(), getNroParcelas());
        }
    }

    public double valorParcela(){
        return custoTotal()/getNroParcelas();
    }
}
