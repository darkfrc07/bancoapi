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

        if (cuentaOpt.isPresent()) {
            Cuenta cuenta = cuentaOpt.get();
            double nuevoSaldo = movimiento.getTipo().equalsIgnoreCase("débito")
                ? cuenta.getSaldo() - movimiento.getValor()
                : cuenta.getSaldo() + movimiento.getValor();

            if (nuevoSaldo < 0) {
                return "Error: Saldo insuficiente.";
            }

            cuenta.setSaldo(nuevoSaldo);
            movimientoRepository.agregarMovimiento(movimiento);
            return "Movimiento registrado con éxito.";
        }
        return "Error: Cuenta no encontrada.";
    }
}

