package com.example.banco_app.model;

import java.time.LocalDate;

public class Movimiento {
    private Long id;
    private String tipo;
    private LocalDate fecha;
    private double valor;
    private String numeroCuenta;

    // Constructor vacío (necesario para Jackson)
    public Movimiento() {
    }

    // Constructor completo
    public Movimiento(Long id, String tipo, LocalDate fecha, double valor, String numeroCuenta) {
        this.id = id;
        this.tipo = tipo;
        this.fecha = fecha;
        this.valor = valor;
        this.numeroCuenta = numeroCuenta;
    }

    // Constructor sin ID (para inserciones)
    public Movimiento(String tipo, double valor, String numeroCuenta) {
        this.tipo = tipo;
        this.valor = valor;
        this.fecha = LocalDate.now(); // Fecha actual
        this.numeroCuenta = numeroCuenta;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public LocalDate getFecha() { return fecha; }
    public void setFecha(LocalDate fecha) { this.fecha = fecha; }

    public double getValor() { return valor; }
    public void setValor(double valor) { this.valor = valor; }

    public String getNumeroCuenta() { return numeroCuenta; }
    public void setNumeroCuenta(String numeroCuenta) { this.numeroCuenta = numeroCuenta; }

    @Override
    public String toString() {
        return "Movimiento{" +
                "id=" + id +
                ", tipo='" + tipo + '\'' +
                ", fecha=" + fecha +
                ", valor=" + valor +
                ", numeroCuenta='" + numeroCuenta + '\'' +
                '}';
    }
}
