package com.example.banco_app.model;

import java.time.LocalDate;

public class Movimiento {
	private String tipo;
	private LocalDate fecha;
	private double valor;
	
	public Movimiento(String string, double d) {
		// TODO Auto-generated constructor stub
	}
	public String getTipo() {
		return tipo;
	}
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	public LocalDate getFecha() {
		return fecha;
	}
	public void setFecha(LocalDate fecha) {
		this.fecha = fecha;
	}
	public double getValor() {
		return valor;
	}
	public void setValor(double valor) {
		this.valor = valor;
	}

}
