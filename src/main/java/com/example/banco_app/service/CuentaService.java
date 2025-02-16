package com.example.banco_app.service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.banco_app.model.Cliente;
import com.example.banco_app.model.Cuenta;
import com.example.banco_app.model.Movimiento;
import com.example.banco_app.repository.ClienteRepository;
import com.example.banco_app.repository.CuentaRepository;

@Service
public class CuentaService {
    private final CuentaRepository cuentaRepository;
    private final ClienteRepository clienteRepository;

    public CuentaService(CuentaRepository cuentaRepository, ClienteRepository clienteRepository) {
        this.cuentaRepository = cuentaRepository;
        this.clienteRepository = clienteRepository;
    }

    // Obtener todas las cuentas
    public List<Cuenta> obtenerTodas() {
        return cuentaRepository.obtenerCuentas();
    }

    // Buscar cuenta por número
    public Optional<Cuenta> buscarPorNumero(String numero) {
        return cuentaRepository.buscarPorNumero(numero);
    }

    // Agregar nueva cuenta
    public void agregarCuenta(Cuenta cuenta) {
        if (buscarPorNumero(cuenta.getNumero()).isPresent()) {
            throw new RuntimeException("Ya existe una cuenta con este número.");
        }
        if (cuenta.getSaldo() < 0) {
            throw new RuntimeException("No se puede crear una cuenta con saldo negativo.");
        }
        cuentaRepository.agregarCuenta(cuenta);
    }

    // Actualizar cuenta
    public Optional<Cuenta> actualizarCuenta(String numero, Cuenta cuentaActualizada) {
        return cuentaRepository.actualizarCuenta(numero, cuentaActualizada);
    }

    // Eliminar cuenta si no tiene movimientos
    public boolean eliminarCuenta(String numero) {
        Optional<Cuenta> cuentaOpt = buscarPorNumero(numero);
        if (cuentaOpt.isPresent()) {
            if (!cuentaOpt.get().getMovimientos().isEmpty()) {
                throw new RuntimeException("No se puede eliminar una cuenta con movimientos asociados.");
            }
            return cuentaRepository.eliminarCuenta(numero);
        }
        return false;
    }

    // Registrar movimiento en una cuenta
    public boolean registrarMovimiento(String numero, Movimiento movimiento) {
        Optional<Cuenta> cuentaOpt = cuentaRepository.buscarPorNumero(numero);
        if (!cuentaOpt.isPresent()) {
            throw new RuntimeException("La cuenta no existe.");
        }

        Cuenta cuenta = cuentaOpt.get();
        String tipoMovimiento = movimiento.getTipo().toLowerCase();

        if (tipoMovimiento.equals("débito")) {
            if (movimiento.getValor() > cuenta.getSaldo()) {
                throw new RuntimeException("Saldo insuficiente para realizar el débito.");
            }
            cuenta.setSaldo(cuenta.getSaldo() - movimiento.getValor());
        } else if (tipoMovimiento.equals("crédito")) {
            cuenta.setSaldo(cuenta.getSaldo() + movimiento.getValor());
        } else {
            throw new IllegalArgumentException("El tipo de movimiento debe ser 'débito' o 'crédito'.");
        }

        cuenta.getMovimientos().add(movimiento);
        return true;
    }

    // Generar reporte de cuentas por cliente y rango de fechas
    public Map<String, Object> generarReporte(Long clienteId, LocalDate fechaInicio, LocalDate fechaFin) {
        Optional<Cliente> clienteOpt = clienteRepository.buscarPorId(clienteId);
        if (!clienteOpt.isPresent()) {
            throw new IllegalArgumentException("Cliente no encontrado.");
        }

        Cliente cliente = clienteOpt.get();
        List<Cuenta> cuentas = cliente.getCuentas();

        double totalDebitos = 0;
        double totalCreditos = 0;
        
        Map<String, Object> reporte = new HashMap<>();

        for (Cuenta cuenta : cuentas) {
            List<Movimiento> movimientosFiltrados = cuenta.getMovimientos().stream()
                .filter(mov -> !mov.getFecha().isBefore(fechaInicio) && !mov.getFecha().isAfter(fechaFin))
                .collect(Collectors.toList());

            for (Movimiento mov : movimientosFiltrados) {
                if (mov.getTipo().equalsIgnoreCase("débito")) {
                    totalDebitos += mov.getValor();
                } else if (mov.getTipo().equalsIgnoreCase("crédito")) {
                    totalCreditos += mov.getValor();
                }
            }

            reporte.put(cuenta.getNumero(), Map.of(
                "saldo", cuenta.getSaldo(),
                "movimientos", movimientosFiltrados
            ));
        }

        reporte.put("cliente", cliente.getNombre());
        reporte.put("totalDebitos", totalDebitos);
        reporte.put("totalCreditos", totalCreditos);
        
        return reporte;
    }
}
