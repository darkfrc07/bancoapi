package com.example.banco_app.model;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class Cuenta {
    @NotBlank(message = "El número de cuenta no puede estar vacío")
    private String numero;

    @Min(value = 0, message = "El saldo no puede ser negativo")
    private double saldo;

    @NotNull(message = "El ID del cliente es obligatorio")
    private Long idCliente;

    // Constructor vacío requerido por frameworks como Spring y JDBC
    public Cuenta() {}

    // Constructor con todos los atributos
    public Cuenta(String numero, double saldo, Long idCliente) {
        this.numero = numero;
        this.saldo = saldo;
        this.idCliente = idCliente;
    }

    // Getters y Setters
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
        if (saldo < 0) {
            throw new IllegalArgumentException("El saldo no puede ser negativo");
        }
        this.saldo = saldo; 
    }

    public Long getIdCliente() { 
        return idCliente; 
    }

    public void setIdCliente(Long idCliente) { 
        this.idCliente = idCliente; 
    }

    @Override
    public String toString() {
        return "Cuenta{" +
                "numero='" + numero + '\'' +
                ", saldo=" + saldo +
                ", idCliente=" + idCliente +
                '}';
    }
}
