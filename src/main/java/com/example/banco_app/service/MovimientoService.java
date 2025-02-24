package com.example.banco_app.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.banco_app.model.Cuenta;
import com.example.banco_app.model.Movimiento;
import com.example.banco_app.repository.CuentaRepository;
import com.example.banco_app.repository.MovimientoRepository;

@Service
public class MovimientoService {
    private static final Logger log = LoggerFactory.getLogger(MovimientoService.class);

    private final MovimientoRepository movimientoRepository;
    private final CuentaRepository cuentaRepository;

    public MovimientoService(MovimientoRepository movimientoRepository, CuentaRepository cuentaRepository) {
        this.movimientoRepository = movimientoRepository;
        this.cuentaRepository = cuentaRepository;
    }

    public List<Movimiento> obtenerTodos() {
        return movimientoRepository.obtenerMovimientos();
    }
    
    public List<Movimiento> buscarPorNumeroCuenta(String numeroCuenta) {
        return movimientoRepository.buscarPorNumeroCuenta(numeroCuenta);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public String agregarMovimiento(String numeroCuenta, Movimiento movimiento) {
        Optional<Cuenta> cuentaOpt = cuentaRepository.buscarPorNumero(numeroCuenta);
        if (!cuentaOpt.isPresent()) {
            return "Error: Cuenta no encontrada.";
        }

        Cuenta cuenta = cuentaOpt.get();
        double nuevoSaldo = cuenta.getSaldo();
        String tipoNormalizado = movimiento.getTipo().toLowerCase().replace("é", "e");

        if (movimiento.getFecha() == null) {
            movimiento.setFecha(LocalDate.now());
        }

        if ("credito".equals(tipoNormalizado)) { 
            if (cuenta.getSaldo() < movimiento.getValor()) {
                return "Error: Saldo insuficiente.";
            }
            nuevoSaldo -= movimiento.getValor();
        } else if ("debito".equals(tipoNormalizado)) { 
            nuevoSaldo += movimiento.getValor();
        } else {
            return "Error: Tipo de movimiento inválido.";
        }

        int filasActualizadas = cuentaRepository.actualizarCuenta(numeroCuenta, nuevoSaldo);
        if (filasActualizadas == 0) {
            return "Error: No se pudo actualizar el saldo.";
        }

        Long movimientoId = movimientoRepository.agregarMovimiento(numeroCuenta, movimiento);
        if (movimientoId == null) {
            return "Error: No se pudo registrar el movimiento.";
        }

        return "Movimiento registrado con éxito. ID: " + movimientoId;
    }

    @Transactional
    public String actualizarMovimiento(Long id, Movimiento movimientoActualizado) {
        Optional<Movimiento> movimientoOpt = movimientoRepository.buscarPorId(id);
        if (!movimientoOpt.isPresent()) {
            return "Error: Movimiento no encontrado.";
        }

        Movimiento movimientoExistente = movimientoOpt.get();
        Optional<Cuenta> cuentaOpt = cuentaRepository.buscarPorNumero(movimientoExistente.getNumeroCuenta());
        if (!cuentaOpt.isPresent()) {
            return "Error: Cuenta asociada no encontrada.";
        }

        Cuenta cuenta = cuentaOpt.get();
        double saldoActual = cuenta.getSaldo();
        double saldoRevertido = saldoActual;

        if (movimientoExistente.getTipo().equalsIgnoreCase("credito")) {
            saldoRevertido += movimientoExistente.getValor();
        } else {
            saldoRevertido -= movimientoExistente.getValor();
        }

        double nuevoSaldo = saldoRevertido;
        if (movimientoActualizado.getTipo().equalsIgnoreCase("credito")) {
            if (nuevoSaldo < movimientoActualizado.getValor()) {
                return "Error: Saldo insuficiente para actualizar.";
            }
            nuevoSaldo -= movimientoActualizado.getValor();
        } else {
            nuevoSaldo += movimientoActualizado.getValor();
        }

        int filasActualizadasCuenta = cuentaRepository.actualizarCuenta(cuenta.getNumero(), nuevoSaldo);
        if (filasActualizadasCuenta == 0) {
            return "Error: No se pudo actualizar el saldo de la cuenta.";
        }

        int filasActualizadasMovimiento = movimientoRepository.actualizarMovimiento(id, movimientoActualizado);
        if (filasActualizadasMovimiento == 0) {
            return "Error: No se pudo actualizar el movimiento.";
        }

        return "Movimiento actualizado con éxito.";
    }
}
