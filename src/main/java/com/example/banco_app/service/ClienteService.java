package com.example.banco_app.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.banco_app.model.Cliente;
import com.example.banco_app.repository.ClienteRepository;

@Service
public class ClienteService {
	private final ClienteRepository clienteRepository;
	
	public ClienteService(ClienteRepository clienteRepository) {
		this.clienteRepository = clienteRepository;
	}
	
	public List<Cliente> obtenerCliente() {
		return clienteRepository.obtenerClientes();
	}
	
	public Optional<Cliente> buscarPorNombre(String nombre){
		return clienteRepository.buscarPorNombre(nombre); 
	}
	
	public Cliente agregarCliente(Cliente cliente) {
		return clienteRepository.agregarCliente(cliente);
	}

	public boolean eliminarCliente(String nombre) {
		return clienteRepository.eliminarCliente(nombre);
	}

	

}
