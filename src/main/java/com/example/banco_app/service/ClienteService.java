package com.example.banco_app.service;

import com.example.banco_app.model.Cliente;
import com.example.banco_app.repository.ClienteRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class ClienteService {
    private final ClienteRepository clienteRepository;

    public ClienteService(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    public List<Cliente> obtenerClientes() {
        return clienteRepository.obtenerClientes();
    }

    public Optional<Cliente> buscarPorId(Long id) {
        return clienteRepository.buscarPorId(id);
    }

    public Cliente agregarCliente(Cliente cliente) {
        return clienteRepository.agregarCliente(cliente);
    }

    public Optional<Cliente> actualizarCliente(Long id, Cliente clienteActualizado) {
        return clienteRepository.actualizarCliente(id, clienteActualizado);
    }

    public boolean eliminarClientePorId(Long id) {
        return clienteRepository.eliminarClientePorId(id);
    }

    public boolean asignarCuenta(Long clienteId, Long cuentaId) {
        return clienteRepository.asignarCuenta(clienteId, cuentaId);
    }
}
