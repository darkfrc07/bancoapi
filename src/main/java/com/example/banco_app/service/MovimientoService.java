package com.example.banco_app.service;

import com.example.banco_app.model.Cuenta;
import com.example.banco_app.model.Movimiento;
import com.example.banco_app.model.Movimiento.TipoMovimiento;
import com.example.banco_app.repository.MovimientoRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class MovimientoService {
    private static final Logger log = LoggerFactory.getLogger(MovimientoService.class);
    private final MovimientoRepository movimientoRepository;
    private final CuentaService cuentaService;

    public MovimientoService(MovimientoRepository movimientoRepository, CuentaService cuentaService) {
        this.movimientoRepository = movimientoRepository;
        this.cuentaService = cuentaService;
    }

    public List<Movimiento> obtenerTodos() {
        return movimientoRepository.obtenerMovimientos();
    }

    public List<Movimiento> buscarPorNumeroCuenta(String numeroCuenta) {
        return movimientoRepository.buscarPorNumeroCuenta(numeroCuenta);
    }

    @Transactional
    public String actualizarMovimiento(String numeroCuenta, Movimiento movimiento) {
        Optional<Cuenta> cuentaOpt = cuentaService.obtenerCuentaPorNumero(numeroCuenta);
        if (!cuentaOpt.isPresent()) {
            return "Error: Cuenta no encontrada.";
        }

        Cuenta cuenta = cuentaOpt.get();
        if (movimiento.getFecha() == null) {
            movimiento.setFecha(LocalDate.now());
        }

        double nuevoSaldo = cuenta.getSaldo();
        TipoMovimiento tipo = TipoMovimiento.fromString(movimiento.getTipo());

        if (tipo == TipoMovimiento.DEBITO) {
            nuevoSaldo += movimiento.getValor();
        } else if (tipo == TipoMovimiento.CREDITO) {
            nuevoSaldo -= movimiento.getValor();
            if (nuevoSaldo < 0) {
                return "Error: Saldo insuficiente.";
            }
        } else {
            return "Error: Tipo de movimiento inválido.";
        }
        
        ResponseEntity<String> respuestaSaldo = cuentaService.actualizarSaldo(numeroCuenta, nuevoSaldo);
        if (respuestaSaldo.getStatusCode() != HttpStatus.OK && respuestaSaldo.getStatusCode() != HttpStatus.CREATED) {
            return "Error: No se pudo actualizar el saldo.";
        }

        Long movimientoId = movimientoRepository.agregarMovimiento(numeroCuenta, movimiento);
        if (movimientoId == null) {
            return "Error: No se pudo registrar el movimiento.";
        }

        log.info("Movimiento registrado: {} - Cuenta: {} - Nuevo saldo: {}", tipo, numeroCuenta, nuevoSaldo);
        return "Movimiento registrado con éxito. ID: " + movimientoId;
    }
    
    public List<Movimiento> generarReporteMovimientos(String cuentaId, LocalDate fechaInicio, LocalDate fechaFin) {
        return movimientoRepository.obtenerMovimientosEntreFechas(cuentaId, fechaInicio, fechaFin);
    }


}
