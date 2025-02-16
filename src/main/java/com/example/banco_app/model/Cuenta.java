package com.example.banco_app.model;

import java.util.ArrayList;
import java.util.List;

public class Cuenta {
    private String numero;
    private double saldo;
    private Long clienteId; // Agregado para asociar la cuenta a un cliente
    private List<Movimiento> movimientos = new ArrayList<>();

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

    public List<Movimiento> getMovimientos() {
        return movimientos;
    }

    public void setMovimientos(List<Movimiento> movimientos) {
        this.movimientos = movimientos;
    }

    public boolean registrarMovimiento(Movimiento movimiento) {
        if (movimiento.getTipo().equalsIgnoreCase("débito")) {
            if (movimiento.getValor() > saldo) {
                return false; // No se permite dejar saldo negativo
            }
            saldo -= movimiento.getValor();
        } else if (movimiento.getTipo().equalsIgnoreCase("crédito")) {
            saldo += movimiento.getValor();
        }
        movimientos.add(movimiento);
        return true;
    }
}
