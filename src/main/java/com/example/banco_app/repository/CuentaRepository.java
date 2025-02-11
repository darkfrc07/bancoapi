package com.example.banco_app.repository;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;

import com.example.banco_app.model.Cuenta;

@Repository
public class CuentaRepository {
	private final List<Cuenta> cuentas = new ArrayList<>();
	
	public List<Cuenta> obtenerCuentas(){
		return cuentas;
	}
	
	public Optional<Cuenta> buscarPorNumero(String numero) {
	    for (Cuenta cuenta : cuentas) {
	        if (cuenta.getNumero().equalsIgnoreCase(numero)) {
	            return Optional.of(cuenta);
	        }
	    }
	    return Optional.empty();
	}
	
	public void agregarCuenta (Cuenta cuenta) {
		cuentas.add(cuenta);
	}
	
	public boolean eliminarCuenta(String numero ) {
	    Iterator<Cuenta> iterator = cuentas.iterator();
	    while (iterator.hasNext()) {
	        Cuenta cliente = iterator.next();
	        if (cliente.getNumero().equalsIgnoreCase(numero)) {
	            iterator.remove();
	            return true;
	        }
	    }
	    return false;
	}
	

}
