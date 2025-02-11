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
	    //(if1) Si el movimiento es un débito, verificamos que el saldo sea suficiente.
		//(if2) Si el valor a retirar es mayor que el saldo, no se permite la operación.
		//(else1) Si hay saldo suficiente, se resta el valor del saldo.
		// (else2) Si el movimiento no es un débito, asumimos que es un crédito.
	    if (movimiento.getTipo().equals("débito")) {
	        if (movimiento.getValor() > saldo) {
	            return false;
	        } else {
	            saldo = saldo - movimiento.getValor();
	        }
	    } else {
	        saldo = saldo + movimiento.getValor();
	    }
	    
	    movimientos.add(movimiento);
	    return true;
	}

}
