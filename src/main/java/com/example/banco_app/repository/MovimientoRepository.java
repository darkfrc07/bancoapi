package com.example.banco_app.repository;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.example.banco_app.model.Movimiento;

@Repository
public class MovimientoRepository {
	private final List<Movimiento> movimientos = new ArrayList<>();
	
	public List<Movimiento> obtenerMovimientos(){
		return movimientos;
	}
	
	public void agregarMovimiento(Movimiento movimiento) {
		movimientos.add(movimiento);
	}
	

}
