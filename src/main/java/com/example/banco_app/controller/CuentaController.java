package com.example.banco_app.controller;

import com.example.banco_app.model.Cuenta;
import com.example.banco_app.service.CuentaService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.ResponseEntity.BodyBuilder;
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
    public ResponseEntity<List<Cuenta>> obtenerCuentas() {
        List<Cuenta> cuentas = cuentaService.obtenerTodasLasCuentas();
        return ResponseEntity.ok(cuentas);
    }

    @GetMapping("/{numero}")
    public ResponseEntity<Cuenta> obtenerCuenta(@PathVariable String numero) {
        Optional<Cuenta> cuenta = cuentaService.obtenerCuentaPorNumero(numero);
        return cuenta.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<String> crearCuenta(@Valid @RequestBody Cuenta cuenta) {
        return cuentaService.crearCuenta(cuenta);
    }

    @PutMapping("/{numero}")
    public ResponseEntity<String> actualizarSaldo(@PathVariable String numero, @RequestBody Cuenta cuenta) {
        ResponseEntity<String> respuesta = cuentaService.actualizarSaldo(numero, cuenta.getSaldo());

        return respuesta.getStatusCode() == HttpStatus.OK 
            ? ResponseEntity.ok("Saldo actualizado con éxito.") 
            : ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error: Cuenta no encontrada.");
    }


    @DeleteMapping("/{numero}")
    public ResponseEntity<String> eliminarCuenta(@PathVariable String numero) {
        boolean eliminado = cuentaService.eliminarCuenta(numero);
        return eliminado 
            ? ResponseEntity.ok("Cuenta eliminada correctamente.") 
            : ((BodyBuilder) ResponseEntity.notFound()).body("Error: Cuenta no encontrada.");
    }
}
