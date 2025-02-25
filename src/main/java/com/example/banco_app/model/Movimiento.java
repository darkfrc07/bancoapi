package com.example.banco_app.model;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;

public class Movimiento {
    private Long id;

    @NotBlank(message = "El tipo de movimiento es obligatorio")
    private String tipo;

    @NotNull(message = "La fecha no puede ser nula")
    @JsonSerialize(using = LocalDateSerializer.class)
    private LocalDate fecha;

    @Min(value = 0, message = "El valor del movimiento no puede ser negativo")
    private double valor;

    @NotNull(message = "El número de cuenta es obligatorio")
    private String numeroCuenta;

    public Movimiento() {}

    public Movimiento(Long id, String tipo, LocalDate fecha, double valor, String numeroCuenta) {
        this.id = id;
        this.setTipo(tipo);  // Usa el setter para validar
        this.fecha = (fecha != null) ? fecha : LocalDate.now();
        this.setValor(valor);  // Usa el setter para validar
        this.numeroCuenta = numeroCuenta;
    }

    public Movimiento(String numeroCuenta, String tipo, double valor, LocalDate fecha) {
        this(null, tipo, fecha, valor, numeroCuenta);
    }

    public Movimiento(String tipo, double valor, String numeroCuenta) {
        this(null, tipo, LocalDate.now(), valor, numeroCuenta);
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) {
        if (tipo == null || tipo.isBlank()) {
            throw new IllegalArgumentException("El tipo de movimiento no puede ser nulo o vacío");
        }
        this.tipo = TipoMovimiento.fromString(tipo).name(); // Guarda siempre en mayúsculas
    }

    public LocalDate getFecha() { return fecha; }
    public void setFecha(LocalDate fecha) { 
        this.fecha = (fecha != null) ? fecha : LocalDate.now();
    }

    public double getValor() { return valor; }
    public void setValor(double valor) {
        if (valor < 0) {
            throw new IllegalArgumentException("El valor del movimiento no puede ser negativo");
        }
        this.valor = valor;
    }

    public String getNumeroCuenta() { return numeroCuenta; }
    public void setNumeroCuenta(String numeroCuenta) {
        if (numeroCuenta == null || numeroCuenta.isBlank()) {
            throw new IllegalArgumentException("El número de cuenta no puede ser nulo o vacío");
        }
        this.numeroCuenta = numeroCuenta;
    }

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

    public enum TipoMovimiento {
        DEBITO, CREDITO;

        public static TipoMovimiento fromString(String tipo) {
            if (tipo == null) throw new IllegalArgumentException("El tipo de movimiento no puede ser nulo");
            switch (tipo.toLowerCase().replace("é", "e")) {
                case "debito": return DEBITO;
                case "credito": return CREDITO;
                default: throw new IllegalArgumentException("Tipo de movimiento inválido: " + tipo);
            }
        }
    }
}
