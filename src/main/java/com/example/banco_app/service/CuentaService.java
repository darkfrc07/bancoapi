package com.example.banco_app.service;

import com.example.banco_app.model.Cuenta;
import com.example.banco_app.repository.CuentaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CuentaService {
    private final CuentaRepository cuentaRepository;

    public CuentaService(CuentaRepository cuentaRepository) {
        this.cuentaRepository = cuentaRepository;
    }

    public List<Cuenta> obtenerTodasLasCuentas() {
        return cuentaRepository.obtenerCuentas();
    }

    public Optional<Cuenta> obtenerCuentaPorNumero(String numero) {
        return cuentaRepository.buscarPorNumero(numero);
    }

    public void crearCuenta(Cuenta cuenta) {
        cuentaRepository.agregarCuenta(cuenta);
    }

    public boolean actualizarSaldo(String numero, double saldo) {
        return cuentaRepository.actualizarCuenta(numero, saldo) > 0;
    }

    public boolean eliminarCuenta(String numero) {
        return cuentaRepository.eliminarCuenta(numero) > 0;
    }
}