package com.example.banco_app.repository;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.example.banco_app.model.Cliente;

@Repository
public class ClienteRepository {
	private final List<Cliente> clientes = new ArrayList<>();
	public List<Cliente> obtenerClientes(){
		return clientes;
	}
	
	public Optional<Cliente> buscarPorNombre(String nombre) {
	    for (Cliente cliente : clientes) {
	        if (cliente.getNombre().equalsIgnoreCase(nombre)) {
	            return Optional.of(cliente);
	        }
	    }
	    return Optional.empty();
	}
	
	public void agregarCliente (Cliente cliente) {
		clientes.add(cliente);
	}
	
	public boolean eliminarCliente(String nombre) {
	    Iterator<Cliente> iterator = clientes.iterator();
	    while (iterator.hasNext()) {
	        Cliente cliente = iterator.next();
	        if (cliente.getNombre().equalsIgnoreCase(nombre)) {
	            iterator.remove();
	            return true;
	        }
	    }
	    return false;
	}

}
