package com.example.banco_app.controller;

import com.example.banco_app.model.Cuenta;
import com.example.banco_app.service.CuentaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/cuentas")
public class CuentaController {
    private final CuentaService cuentaService;

    public CuentaController(CuentaService cuentaService) {
        this.cuentaService = cuentaService;
    }

    @GetMapping
    public List<Cuenta> obtenerCuentas() {
        return cuentaService.obtenerTodasLasCuentas();
    }

    @GetMapping("/{numero}")
    public ResponseEntity<Cuenta> obtenerCuenta(@PathVariable String numero) {
        Optional<Cuenta> cuenta = cuentaService.obtenerCuentaPorNumero(numero);
        return cuenta.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public void crearCuenta(@RequestBody Cuenta cuenta) {
        cuentaService.crearCuenta(cuenta);
    }

    @PutMapping("/{numero}")
    public ResponseEntity<Void> actualizarSaldo(@PathVariable String numero, @RequestParam double saldo) {
        boolean actualizado = cuentaService.actualizarSaldo(numero, saldo);
        return actualizado ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{numero}")
    public ResponseEntity<Void> eliminarCuenta(@PathVariable String numero) {
        boolean eliminado = cuentaService.eliminarCuenta(numero);
        return eliminado ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }
}