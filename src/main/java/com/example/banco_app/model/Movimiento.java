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

    @NotBlank(message = "El número de cuenta es obligatorio")
    private String numeroCuenta;

    public Movimiento() {}

    public Movimiento(Long id, String tipo, LocalDate fecha, double valor, String numeroCuenta) {
        this.id = id;
        this.tipo = tipo;
        this.fecha = (fecha != null) ? fecha : LocalDate.now(); // Usa la fecha actual si es null
        this.valor = valor;
        this.numeroCuenta = numeroCuenta;
    }

    public Movimiento(String tipo, double valor, String numeroCuenta) {
        this(null, tipo, LocalDate.now(), valor, numeroCuenta);
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public LocalDate getFecha() { return fecha; }
    public void setFecha(LocalDate fecha) { this.fecha = fecha; }

    public double getValor() { return valor; }
    public void setValor(double valor) {
        if (valor < 0) {
            throw new IllegalArgumentException("El valor del movimiento no puede ser negativo");
        }
        this.valor = valor;
    }

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
