package com.example.banco_app.service;

import com.example.banco_app.model.Cuenta;
import com.example.banco_app.repository.CuentaRepository;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
@Service

public class CuentaService {
	private static final Logger log = LoggerFactory.getLogger(CuentaService.class);
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

    public ResponseEntity<String> crearCuenta(Cuenta cuenta) {
        Optional<Cuenta> cuentaExistente = cuentaRepository.buscarPorNumero(cuenta.getNumero());

        if (cuentaExistente.isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: El número de cuenta ya existe.");
        }

        if (cuenta.getSaldo() < 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: El saldo inicial no puede ser negativo.");
        }

        cuentaRepository.agregarCuenta(cuenta);
        return ResponseEntity.status(HttpStatus.CREATED).body("Cuenta creada exitosamente.");
    }


    public boolean actualizarSaldo(String numero, double saldo) {
        return cuentaRepository.actualizarCuenta(numero, saldo) > 0;
    }

    public boolean eliminarCuenta(String numero) {
        return cuentaRepository.eliminarCuenta(numero) > 0;
    }
}