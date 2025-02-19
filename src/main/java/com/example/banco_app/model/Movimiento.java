package com.example.banco_app.model;

import java.time.LocalDate;

public class Movimiento {
    private Long id;
    private String tipo;
    private LocalDate fecha;
    private double valor;

    // Constructor completo
    public Movimiento(Long id, String tipo, LocalDate fecha, double valor) {
        this.id = id;
        this.tipo = tipo;
        this.fecha = fecha;
        this.valor = valor;
    }

    // Constructor sin ID (para inserciones)
    public Movimiento(String tipo, double valor) {
        this.tipo = tipo;
        this.valor = valor;
        this.fecha = LocalDate.now(); // Fecha actual
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public LocalDate getFecha() { return fecha; }
    public void setFecha(LocalDate fecha) { this.fecha = fecha; }

    public double getValor() { return valor; }
    public void setValor(double valor) { this.valor = valor; }

    @Override
    public String toString() {
        return "Movimiento{" +
                "id=" + id +
                ", tipo='" + tipo + '\'' +
                ", fecha=" + fecha +
                ", valor=" + valor +
                '}';
    }
}
