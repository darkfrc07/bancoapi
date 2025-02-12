package com.example.banco_app.model;

import java.util.ArrayList;
import java.util.List;

public class Cuenta {
	private String numero;
	private double saldo;
	private List<Movimiento> movimientos = new ArrayList<>();
	
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
	public List<Movimiento> getMovimientos() {
		return movimientos;
	}
	public void setMovimientos(List<Movimiento> movimientos) {
		this.movimientos = movimientos;
	}
	
	public boolean registrarMovimiento(Movimiento movimiento) {
	    if (movimiento.getTipo().equals("débito") && movimiento.getValor() > saldo) {
	        return false;
	    } else if (movimiento.getTipo().equals("débito")) {
	        saldo = saldo - movimiento.getValor();
	    } else {
	        saldo = saldo + movimiento.getValor();
	    }
	    movimientos.add(movimiento);
	    return true;
	}



	
}
