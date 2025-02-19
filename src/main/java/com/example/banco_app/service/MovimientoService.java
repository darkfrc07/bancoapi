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
        Optional<Cuenta> cuentaOpt = cuentaRepository.buscarPorNumero(numeroCuenta);

        if (!cuentaOpt.isPresent()) {
            return "Error: Cuenta no encontrada.";
        }

        Cuenta cuenta = cuentaOpt.get();
        double nuevoSaldo;

        // Ajuste correcto: Débito resta y Crédito suma
        if ("débito".equalsIgnoreCase(movimiento.getTipo())) {
            nuevoSaldo = cuenta.getSaldo() - movimiento.getValor();
        } else { 
            nuevoSaldo = cuenta.getSaldo() + movimiento.getValor();
        }

        if (nuevoSaldo < 0) {
            return "Error: Saldo insuficiente.";
        }

        // Registrar el movimiento en la BDD
        Long movimientoId = movimientoRepository.agregarMovimiento(numeroCuenta, movimiento);
        if (movimientoId == null) {
            return "Error al registrar el movimiento.";
        }

        // Actualizar saldo en la cuenta
        cuenta.setSaldo(nuevoSaldo);
        cuentaRepository.actualizarCuenta(numeroCuenta, nuevoSaldo);

        return "Movimiento registrado con éxito. ID: " + movimientoId;
    }
}
