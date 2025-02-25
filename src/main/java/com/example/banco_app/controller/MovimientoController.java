package com.example.banco_app.controller;

import com.example.banco_app.model.Movimiento;
import com.example.banco_app.service.MovimientoService;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/movimientos")
public class MovimientoController {
    private final MovimientoService movimientoService;

    public MovimientoController(MovimientoService movimientoService) {
        this.movimientoService = movimientoService;
    }

    @PostMapping
    public ResponseEntity<String> agregarMovimiento(@RequestBody Movimiento movimiento) {
        String resultado = movimientoService.actualizarMovimiento(movimiento.getNumeroCuenta(), movimiento);
        return ResponseEntity.ok(resultado);
    }

    @GetMapping
    public ResponseEntity<List<Movimiento>> obtenerTodos() {
        return ResponseEntity.ok(movimientoService.obtenerTodos());
    }

    @GetMapping("/cuenta/{numeroCuenta}")
    public ResponseEntity<?> obtenerMovimientosPorNumeroCuenta(@PathVariable String numeroCuenta) {
        List<Movimiento> movimientos = movimientoService.buscarPorNumeroCuenta(numeroCuenta);

        if (!movimientos.isEmpty()) {
            return ResponseEntity.ok(movimientos);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("{\"mensaje\": \"No se encontraron movimientos para la cuenta " + numeroCuenta + ".\"}");
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> actualizarMovimiento(@PathVariable Long id, @RequestBody Movimiento movimiento) {
        String resultado = movimientoService.actualizarMovimiento(movimiento.getNumeroCuenta(), movimiento);
        
        if (resultado.startsWith("Error")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resultado);
        }
        return ResponseEntity.ok(resultado);
    }
    
    @GetMapping("/reporte")
    public ResponseEntity<List<Movimiento>> obtenerReporte(
            @RequestParam String cuentaId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin) {

        List<Movimiento> movimientos = movimientoService.generarReporteMovimientos(cuentaId, fechaInicio, fechaFin);
        return ResponseEntity.ok(movimientos);
    }


}
