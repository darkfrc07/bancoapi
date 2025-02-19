package com.example.banco_app.controller;

import java.util.List;
import org.springframework.web.bind.annotation.*;

import com.example.banco_app.model.Movimiento;
import com.example.banco_app.service.MovimientoService;

@RestController
@RequestMapping("/movimientos")
public class MovimientoController {
    private final MovimientoService movimientoService;

    public MovimientoController(MovimientoService movimientoService) {
        this.movimientoService = movimientoService;
    }

    @GetMapping
    public List<Movimiento> obtenerTodos() {
        return movimientoService.obtenerTodos();
    }

    @PostMapping
    public String agregarMovimiento(@RequestBody Movimiento movimiento) {
        return movimientoService.agregarMovimiento(movimiento.getNumeroCuenta(), movimiento);
    }
}
