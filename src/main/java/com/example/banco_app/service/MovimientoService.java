package com.example.banco_app.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.banco_app.model.Cuenta;
import com.example.banco_app.model.Movimiento;
import com.example.banco_app.repository.CuentaRepository;
import com.example.banco_app.repository.MovimientoRepository;

@Service
public class MovimientoService {
    private final MovimientoRepository movimientoRepository;
    private final CuentaRepository cuentaRepository;

    public MovimientoService(MovimientoRepository movimientoRepository, CuentaRepository cuentaRepository) {
        this.movimientoRepository = movimientoRepository;
        this.cuentaRepository = cuentaRepository;
    }

    public List<Movimiento> obtenerTodos() {
        return movimientoRepository.obtenerMovimientos();
    }

    public String agregarMovimiento(String numeroCuenta, Movimiento movimiento) {
        // Buscar la cuenta por número
        Optional<Cuenta> cuentaOpt = cuentaRepository.buscarPorNumero(numeroCuenta);

        if (!cuentaOpt.isPresent()) {
            return "Error: Cuenta no encontrada.";
        }

        // Obtener la cuenta y verificar el tipo de movimiento
        Cuenta cuenta = cuentaOpt.get();
        double nuevoSaldo;

        // Validar tipo de movimiento
        if (movimiento.getTipo() == null || (!movimiento.getTipo().equalsIgnoreCase("débito") && !movimiento.getTipo().equalsIgnoreCase("crédito"))) {
            return "Error: Tipo de movimiento inválido.";
        }

        // Calcular nuevo saldo según el tipo de movimiento (invertido)
        if ("débito".equalsIgnoreCase(movimiento.getTipo())) {
            nuevoSaldo = cuenta.getSaldo() + movimiento.getValor(); // Sumar para débito
        } else { // Si es crédito
            nuevoSaldo = cuenta.getSaldo() - movimiento.getValor(); // Restar para crédito
        }

        // Verificar que el nuevo saldo no sea negativo
        if (nuevoSaldo < 0) {
            return "Error: Saldo insuficiente.";
        }

        // Actualizar saldo y registrar el movimiento
        cuenta.setSaldo(nuevoSaldo);
        movimientoRepository.agregarMovimiento(movimiento);

        return "Movimiento registrado con éxito.";
    }

}

