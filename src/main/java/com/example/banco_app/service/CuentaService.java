package com.example.banco_app.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.banco_app.model.Cuenta;
import com.example.banco_app.repository.CuentaRepository;

@Service
public class CuentaService {
    private final CuentaRepository cuentaRepository;

    public CuentaService(CuentaRepository cuentaRepository) {
        this.cuentaRepository = cuentaRepository;
    }

    public List<Cuenta> obtenerTodas() {
        return cuentaRepository.obtenerCuentas();
    }

    public Optional<Cuenta> buscarPorNumero(String numero) {
        return cuentaRepository.buscarPorNumero(numero);
    }

    public void agregarCuenta(Cuenta cuenta) {
        cuentaRepository.agregarCuenta(cuenta);
    }

    public boolean eliminarCuenta(String numero) {
        return cuentaRepository.eliminarCuenta(numero);
    }
}

