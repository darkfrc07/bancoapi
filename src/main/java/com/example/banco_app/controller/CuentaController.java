package com.example.banco_app.controller;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.banco_app.model.Cuenta;
import com.example.banco_app.model.Movimiento;
import com.example.banco_app.service.CuentaService;

@RestController
@RequestMapping("/cuentas")
public class CuentaController {
    private final CuentaService cuentaService;

    public CuentaController(CuentaService cuentaService) {
        this.cuentaService = cuentaService;
    }

    // Obtener todas las cuentas
    @GetMapping
    public List<Cuenta> obtenerTodas() {
        return cuentaService.obtenerTodas();
    }

    // Buscar cuenta por número
    @GetMapping("/{numero}")
    public ResponseEntity<Cuenta> buscarPorNumero(@PathVariable String numero) {
        return cuentaService.buscarPorNumero(numero)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> agregarCuenta(@RequestBody Cuenta cuenta) {
        try {
            cuentaService.agregarCuenta(cuenta);
            
            // Validamos si la cuenta tiene un cliente asignado
            Long clienteId = (cuenta.getClienteId() != null) ? cuenta.getClienteId() : null;

            Map<String, Object> response = new HashMap<>();
            response.put("Mensaje", "Cuenta creada exitosamente.");
            response.put("numeroCuenta", cuenta.getNumero());
            response.put("IdCliente", clienteId);

            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // Actualizar una cuenta
    @PutMapping("/{numero}")
    public ResponseEntity<Cuenta> actualizarCuenta(@PathVariable String numero, @RequestBody Cuenta cuentaActualizada) {
        return cuentaService.actualizarCuenta(numero, cuentaActualizada)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Eliminar cuenta
    @DeleteMapping("/{numero}")
    public ResponseEntity<String> eliminarCuenta(@PathVariable String numero) {
        try {
            boolean eliminada = cuentaService.eliminarCuenta(numero);
            if (eliminada) {
                return ResponseEntity.ok("Cuenta eliminada correctamente.");
            } else {
                return ResponseEntity.badRequest().body("La cuenta no existe.");
            }
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Registrar un movimiento en la cuenta
    @PostMapping("/{numero}/movimientos")
    public ResponseEntity<String> registrarMovimiento(@PathVariable String numero, @RequestBody Movimiento movimiento) {
        try {
            boolean registrado = cuentaService.registrarMovimiento(numero, movimiento);
            if (registrado) {
                return ResponseEntity.ok("Movimiento registrado correctamente.");
            } else {
                return ResponseEntity.badRequest().body("Movimiento no permitido: saldo insuficiente.");
            }
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Generar reporte de cuentas por cliente y rango de fechas
    @GetMapping("/reporte")
    public ResponseEntity<Map<String, Object>> generarReporte(
            @RequestParam Long clienteId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin) {
        try {
            Map<String, Object> reporte = cuentaService.generarReporte(clienteId, fechaInicio, fechaFin);
            return ResponseEntity.ok(reporte);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}
