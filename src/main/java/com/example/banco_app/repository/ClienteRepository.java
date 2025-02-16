package com.example.banco_app.repository;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.stereotype.Repository;

import com.example.banco_app.model.Cliente;
import com.example.banco_app.model.Cuenta;

@Repository
public class ClienteRepository {
    private final List<Cliente> clientes = new ArrayList<>();
    private AtomicLong idGenerator = new AtomicLong(1); // Generador de IDs

    // Obtener todos los clientes
    public List<Cliente> obtenerClientes() {
        return clientes;
    }

    // Buscar un cliente por ID
    public Optional<Cliente> buscarPorId(Long id) {
        return clientes.stream().filter(cliente -> cliente.getId().equals(id)).findFirst();
    }

    // Agregar un nuevo cliente
    public Cliente agregarCliente(Cliente cliente) {
        cliente.setId(idGenerator.getAndIncrement()); // Asigna un ID único
        clientes.add(cliente);
        return cliente; // Retorna el cliente con ID asignado
    }

    // Actualizar cliente por ID
    public Optional<Cliente> actualizarCliente(Long id, Cliente clienteActualizado) {
        for (Cliente cliente : clientes) {
            if (cliente.getId().equals(id)) {
                cliente.setNombre(clienteActualizado.getNombre());
                cliente.setDireccion(clienteActualizado.getDireccion());
                cliente.setTelefono(clienteActualizado.getTelefono());
                return Optional.of(cliente);
            }
        }
        return Optional.empty();
    }

    // Eliminar cliente por ID
    public boolean eliminarClientePorId(Long id) {
        Iterator<Cliente> iterator = clientes.iterator();
        while (iterator.hasNext()) {
            Cliente cliente = iterator.next();
            if (cliente.getId().equals(id)) {
                iterator.remove();
                return true;
            }
        }
        return false;
    }

    // Asignar una cuenta a un cliente existente
    public Optional<Cliente> asignarCuenta(Long id, Cuenta cuenta) {
        Optional<Cliente> clienteOpt = buscarPorId(id);
        if (clienteOpt.isPresent()) {
            Cliente cliente = clienteOpt.get();
            cliente.getCuentas().add(cuenta); // Agregar la cuenta a la lista del cliente
            return Optional.of(cliente);
        }
        return Optional.empty(); // Cliente no encontrado
    }
}
