package com.example.banco_app.controller;


import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.banco_app.model.Cliente;
import com.example.banco_app.service.ClienteService;

import jakarta.validation.Valid;

@CrossOrigin(origins = "http://localhost:4200") 
@RestController
@RequestMapping("/cliente")
public class ClienteController {
    
    private final ClienteService clienteService;
    private static final Logger log = LoggerFactory.getLogger(ClienteController.class); 


    public ClienteController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    @GetMapping
    public ResponseEntity<List<Cliente>> obtenerClientes() {
        return ResponseEntity.ok(clienteService.obtenerClientes());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Cliente> obtenerClientePorId(@PathVariable Long id) {
        log.info("Buscando cliente con ID: {}", id);
        
        return clienteService.buscarPorId(id)
                .map(cliente -> {
                    log.info("Cliente encontrado: {}", cliente);
                    return ResponseEntity.ok(cliente);
                })
                .orElseGet(() -> {
                    log.warn("Cliente no encontrado con ID: {}", id);
                    return ResponseEntity.status(HttpStatus.NOT_FOUND)
                            .header("Mensaje-Error", "Cliente con ID " + id + " no encontrado.")
                            .build();
                });
    }

    @PostMapping
    public ResponseEntity<Cliente> crearCliente(@Valid @RequestBody Cliente cliente) {
        log.info("Intentando crear cliente: {}", cliente); 
        Cliente nuevoCliente = clienteService.agregarCliente(cliente);
        log.info("Cliente creado con éxito: {}", nuevoCliente);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoCliente);
    }


    @CrossOrigin(origins = "*")
    @PutMapping("/{id}")
    public ResponseEntity<Cliente> actualizarCliente(@PathVariable Long id, @Valid @RequestBody Cliente clienteActualizado) {
        return clienteService.actualizarCliente(id, clienteActualizado)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarCliente(@PathVariable Long id) {
        boolean eliminado = clienteService.eliminarClientePorId(id);
        if (eliminado) {
            return ResponseEntity.noContent().build(); 
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PostMapping("/{clienteId}/cuentas/{cuentaId}")
    public ResponseEntity<String> asignarCuenta(@PathVariable Long clienteId, @PathVariable Long cuentaId) {
        return clienteService.asignarCuenta(clienteId, cuentaId) ? 
                ResponseEntity.ok("Cuenta asignada correctamente al cliente.") : 
                ResponseEntity.badRequest().body("No se pudo asignar la cuenta.");
    }
}