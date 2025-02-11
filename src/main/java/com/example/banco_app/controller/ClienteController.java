package com.example.banco_app.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.banco_app.model.Cliente;
import com.example.banco_app.service.ClienteService;

@RestController
@RequestMapping("/clientes")
public class ClienteController {
    
    private final ClienteService clienteService;

    public ClienteController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    // Endpoint para obtener la lista de clientes
    @GetMapping
    public ResponseEntity<List<Cliente>> obtenerClientes() {
        List<Cliente> clientes = clienteService.obtenerCliente();
        return ResponseEntity.ok(clientes);
    }

    // Endpoint para buscar un cliente por nombre
    @GetMapping("/{nombre}")
    public ResponseEntity<Optional<Cliente>> buscarPorNombre(@PathVariable String nombre) {
        Optional<Cliente> cliente = clienteService.buscarPorNombre(nombre);
        if (cliente.isPresent()) {
            return ResponseEntity.ok(cliente);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Endpoint para crear un nuevo cliente
    @PostMapping
    public ResponseEntity<String> crearCliente(@RequestBody Cliente cliente) {
        clienteService.agregarCliente(cliente);
        return ResponseEntity.ok("Cliente creado correctamente");
    }

    // Endpoint para eliminar un cliente por nombre
    @DeleteMapping("/{nombre}")
    public ResponseEntity<Boolean> eliminarCliente(@PathVariable String nombre) {
        boolean eliminado = clienteService.eliminarCliente(nombre);
        if (eliminado) {
            return ResponseEntity.ok(true);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
