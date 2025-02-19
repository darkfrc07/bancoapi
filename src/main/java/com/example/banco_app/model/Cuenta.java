package com.example.banco_app.model;

public class Cuenta {
    private String numero;
    private double saldo;
    private Long clienteId;
    
    public Cuenta() {}

    public Cuenta(String numero, double saldo, Long clienteId) {
        this.numero = numero;
        this.saldo = saldo;
        this.clienteId = clienteId;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public double getSaldo() {
        return saldo;
    }

    public void setSaldo(double saldo) {
        this.saldo = saldo;
    }

    public Long getClienteId() {
        return clienteId;
    }

    public void setClienteId(Long clienteId) {
        this.clienteId = clienteId;
    }
}