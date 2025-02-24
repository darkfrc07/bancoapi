package com.example.banco_app.service;

import com.example.banco_app.model.Cliente;
import com.example.banco_app.repository.ClienteRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class ClienteService {
    private final ClienteRepository clienteRepository;
    private static final Logger log = LoggerFactory.getLogger(ClienteService.class);

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
        Objects.requireNonNull(cliente, "El cliente no puede ser nulo");

        if (cliente.getNombre() == null || cliente.getNombre().trim().isEmpty() ||
            cliente.getDireccion() == null || cliente.getDireccion().trim().isEmpty() ||
            cliente.getTelefono() == null || cliente.getTelefono().trim().isEmpty()) {

            log.warn("Intento de crear cliente con datos incompletos: {}", cliente);
            throw new IllegalArgumentException("Los campos nombre, dirección y teléfono son obligatorios");
        }

        Cliente nuevoCliente = clienteRepository.agregarCliente(cliente);
        log.info("Cliente guardado correctamente en BD: {}", nuevoCliente);
        return nuevoCliente;
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
