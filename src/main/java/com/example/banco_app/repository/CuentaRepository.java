package com.example.banco_app.repository;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import org.springframework.stereotype.Repository;

import com.example.banco_app.model.Cuenta;
import com.example.banco_app.model.Movimiento;

@Repository
public class CuentaRepository {
    private final List<Cuenta> cuentas = new ArrayList<>();

    public List<Cuenta> obtenerCuentas() {
        return cuentas;
    }

    public Optional<Cuenta> buscarPorNumero(String numero) {
        return cuentas.stream()
                .filter(cuenta -> cuenta.getNumero().equalsIgnoreCase(numero))
                .findFirst();
    }
    
    public boolean registrarMovimiento(String numero, Movimiento movimiento) {
        Optional<Cuenta> cuentaOpt = buscarPorNumero(numero);
        if (cuentaOpt.isPresent()) {
            Cuenta cuenta = cuentaOpt.get();
            return cuenta.registrarMovimiento(movimiento); // Se usa el método de Cuenta
        }
        return false;
    }


    public void agregarCuenta(Cuenta cuenta) {
        cuentas.add(cuenta);
    }

    public Optional<Cuenta> actualizarCuenta(String numero, Cuenta cuentaActualizada) {
        return buscarPorNumero(numero).map(cuenta -> {
            cuenta.setSaldo(cuentaActualizada.getSaldo());
            return cuenta;
        });
    }

    public boolean eliminarCuenta(String numero) {
        return cuentas.removeIf(cuenta -> cuenta.getNumero().equalsIgnoreCase(numero));
    }

   
    // Obtener cuentas por cliente
    public List<Cuenta> obtenerCuentasPorCliente(Long clienteId) {
        return cuentas.stream()
                .filter(cuenta -> cuenta.getClienteId().equals(clienteId))
                .collect(Collectors.toList());
    }

    // Generar reporte de cuentas con movimientos en un rango de fechas
    public Map<String, Object> generarReporte(Long clienteId, LocalDate fechaInicio, LocalDate fechaFin) {
        List<Cuenta> cuentasCliente = obtenerCuentasPorCliente(clienteId);

        List<Map<String, Object>> cuentasReporte = new ArrayList<>();
        double totalDebitos = 0;
        double totalCreditos = 0;

        for (Cuenta cuenta : cuentasCliente) {
            List<Movimiento> movimientosFiltrados = cuenta.getMovimientos().stream()
                    .filter(mov -> !mov.getFecha().isBefore(fechaInicio) && !mov.getFecha().isAfter(fechaFin))
                    .collect(Collectors.toList());

            double sumDebitos = movimientosFiltrados.stream()
                    .filter(m -> m.getTipo().equalsIgnoreCase("débito"))
                    .mapToDouble(Movimiento::getValor).sum();

            double restCreditos = movimientosFiltrados.stream()
                    .filter(m -> m.getTipo().equalsIgnoreCase("crédito"))
                    .mapToDouble(Movimiento::getValor).sum();

            totalDebitos += sumDebitos;
            totalCreditos -= restCreditos;

            Map<String, Object> cuentaData = new HashMap<>();
            cuentaData.put("numeroCuenta", cuenta.getNumero());
            cuentaData.put("saldo", cuenta.getSaldo());
            cuentaData.put("debitos", sumDebitos);
            cuentaData.put("creditos", restCreditos);
            cuentasReporte.add(cuentaData);
        }

        Map<String, Object> reporte = new HashMap<>();
        reporte.put("clienteId", clienteId);
        reporte.put("cuentas", cuentasReporte);
        reporte.put("totalDebitos", totalDebitos);
        reporte.put("totalCreditos", totalCreditos);
        return reporte;
    }
}
