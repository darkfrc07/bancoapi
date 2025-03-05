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
        if (cuenta.getNumero() == null || cuenta.getNumero().trim().isEmpty()) {
            log.warn("Intento de crear cuenta con número vacío.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: El número de cuenta es obligatorio.");
        }

        Optional<Cuenta> cuentaExistente = cuentaRepository.buscarPorNumero(cuenta.getNumero());
        if (cuentaExistente.isPresent()) {
            log.warn("Intento de crear cuenta con número ya existente: {}", cuenta.getNumero());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: El número de cuenta ya existe.");
        }

        if (cuenta.getSaldo() < 0) {
            log.warn("Intento de crear cuenta con saldo negativo: {}", cuenta.getSaldo());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: El saldo inicial no puede ser negativo.");
        }

        cuentaRepository.agregarCuenta(cuenta);
        log.info("Cuenta creada con éxito: Número={}, Saldo={}", cuenta.getNumero(), cuenta.getSaldo());
        return ResponseEntity.status(HttpStatus.CREATED).body("Cuenta creada exitosamente.");
    }


    public ResponseEntity<String> actualizarSaldo(String numero, double nuevoSaldo) {
        if (nuevoSaldo < 0) {
            log.warn("Intento de actualizar saldo a negativo en cuenta: {} - Nuevo saldo: {}", numero, nuevoSaldo);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: No se puede tener saldo negativo.");
        }

        int filasActualizadas = cuentaRepository.actualizarCuenta(numero, nuevoSaldo);
        if (filasActualizadas > 0) {
            log.info("Saldo actualizado con éxito: Cuenta={} - Nuevo saldo={}", numero, nuevoSaldo);
            return ResponseEntity.ok("Saldo actualizado con éxito.");
        } else {
            log.error("Error al actualizar saldo: Cuenta={} - Nuevo saldo={}", numero, nuevoSaldo);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: No se pudo actualizar el saldo.");
        }
    }

    public boolean eliminarCuenta(String numero) {
        boolean eliminada = cuentaRepository.eliminarCuenta(numero) > 0;
        if (eliminada) {
            log.info("Cuenta eliminada con éxito: {}", numero);
        } else {
            log.warn("Intento de eliminar cuenta inexistente: {}", numero);
        }
        return eliminada;
    }
}
