package com.example.banco_app.controller;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.banco_app.model.Cliente;
import com.example.banco_app.service.ClienteService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/clientes")
public class ClienteController {
    
    private final ClienteService clienteService;

    public ClienteController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    @GetMapping
    public ResponseEntity<List<Cliente>> obtenerClientes() {
        return ResponseEntity.ok(clienteService.obtenerClientes());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Cliente> obtenerClientePorId(@PathVariable Long id) {
        return clienteService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Cliente> crearCliente(@Valid @RequestBody Cliente cliente) {
        return ResponseEntity.status(HttpStatus.CREATED).body(clienteService.agregarCliente(cliente));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Cliente> actualizarCliente(@PathVariable Long id, @Valid @RequestBody Cliente clienteActualizado) {
        return clienteService.actualizarCliente(id, clienteActualizado)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarCliente(@PathVariable Long id) {
        return clienteService.eliminarClientePorId(id) ? 
                ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    @PostMapping("/{clienteId}/cuentas/{cuentaId}")
    public ResponseEntity<String> asignarCuenta(@PathVariable Long clienteId, @PathVariable Long cuentaId) {
        return clienteService.asignarCuenta(clienteId, cuentaId) ? 
                ResponseEntity.ok("Cuenta asignada correctamente al cliente.") : 
                ResponseEntity.badRequest().body("No se pudo asignar la cuenta.");
    }
}
