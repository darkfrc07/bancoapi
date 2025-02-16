package com.example.banco_app.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.banco_app.model.Cliente;
import com.example.banco_app.model.Cuenta;
import com.example.banco_app.service.ClienteService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/clientes")
public class ClienteController {
    
    private final ClienteService clienteService;

    public ClienteController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    // Obtener la lista de clientes
    @GetMapping
    public ResponseEntity<List<Cliente>> obtenerClientes() {
        List<Cliente> clientes = clienteService.obtenerClientes();
        return ResponseEntity.ok(clientes);
    }

    // Buscar un cliente por id
    @GetMapping("/{id}")
    public ResponseEntity<Cliente> obtenerClientePorId(@PathVariable Long id) {
        Optional<Cliente> clienteOpt = clienteService.buscarPorId(id);
        return clienteOpt.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Crear un nuevo cliente
    @PostMapping
    public ResponseEntity<Cliente> crearCliente(@Valid @RequestBody Cliente cliente) {
        Cliente nuevoCliente = clienteService.agregarCliente(cliente);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoCliente);
    }

    // Actualizar un cliente por id
    @PutMapping("/{id}")
    public ResponseEntity<Cliente> actualizarCliente(@PathVariable Long id, @Valid @RequestBody Cliente clienteActualizado) {
        Optional<Cliente> clienteActualizadoOpt = clienteService.actualizarCliente(id, clienteActualizado);
        return clienteActualizadoOpt.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Eliminar un cliente por id
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarCliente(@PathVariable Long id) {
        boolean eliminado = clienteService.eliminarClientePorId(id);
        return eliminado ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    // Asignar una cuenta a un cliente
    @PostMapping("/{id}/cuentas")
    public ResponseEntity<Cliente> asignarCuentaACliente(@PathVariable Long id, @RequestBody Cuenta cuenta) {
        Optional<Cliente> clienteOpt = clienteService.asignarCuenta(id, cuenta);
        return clienteOpt.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }
}
