package com.example.banco_app.service;

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
	
	public Optional<Cliente> buscarPorNombre(String nombre){
		return clienteRepository.buscarPorNombre(nombre); 
	}
	
	public void agregarCliente(Cliente cliente) {
		clienteRepository.agregarCliente(cliente);
	}
	
	public boolean eliminarCliente(String nombre) {
		return clienteRepository.eliminarCliente(nombre);
	}

}
