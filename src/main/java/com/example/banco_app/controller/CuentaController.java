package com.example.banco_app.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.banco_app.model.Cuenta;
import com.example.banco_app.service.CuentaService;

@RestController
@RequestMapping("/cuentas")
public class CuentaController {
    private final CuentaService cuentaService;

    public CuentaController(CuentaService cuentaService) {
        this.cuentaService = cuentaService;
    }

    @GetMapping
    public List<Cuenta> obtenerTodas() {
        return cuentaService.obtenerTodas();
    }

    @GetMapping("/{numero}")
    public Optional<Cuenta> buscarPorNumero(@PathVariable String numero) {
        return cuentaService.buscarPorNumero(numero);
    }

    @PostMapping
    public void agregarCuenta(@RequestBody Cuenta cuenta) {
        cuentaService.agregarCuenta(cuenta);
    }

    @DeleteMapping("/{numero}")
    public boolean eliminarCuenta(@PathVariable String numero) {
        return cuentaService.eliminarCuenta(numero);
    }
}
