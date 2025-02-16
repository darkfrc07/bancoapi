package com.example.banco_app.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.banco_app.model.Cliente;
import com.example.banco_app.model.Cuenta;
import com.example.banco_app.repository.ClienteRepository;

@Service
public class ClienteService {
    private final ClienteRepository clienteRepository;

    public ClienteService(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    // Obtener todos los clientes
    public List<Cliente> obtenerClientes() {
        return clienteRepository.obtenerClientes();
    }

    // Buscar cliente por ID
    public Optional<Cliente> buscarPorId(Long id) {
        return clienteRepository.buscarPorId(id);
    }

    // Agregar nuevo cliente
    public Cliente agregarCliente(Cliente cliente) {
        return clienteRepository.agregarCliente(cliente);
    }

    // Actualizar cliente
    public Optional<Cliente> actualizarCliente(Long id, Cliente clienteActualizado) {
        return clienteRepository.actualizarCliente(id, clienteActualizado);
    }

    // Eliminar cliente
    public boolean eliminarClientePorId(Long id) {
        return clienteRepository.eliminarClientePorId(id);
    }

    // Asignar una cuenta a un cliente existente
    public Optional<Cliente> asignarCuenta(Long id, Cuenta cuenta) {
        Optional<Cliente> clienteOpt = clienteRepository.buscarPorId(id);
        if (clienteOpt.isPresent()) {
            Cliente cliente = clienteOpt.get();
            cliente.getCuentas().add(cuenta); // Agregar la cuenta a la lista del cliente
            return Optional.of(cliente); // Retornar el cliente actualizado
        }
        return Optional.empty(); // Cliente no encontrado
    }
}
