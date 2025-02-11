package com.example.banco_app.model;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Cliente {
    private String nombre;
    private String direccion;
    private String telefono;
    private List<Cuenta> cuentas = new ArrayList<>();

    public Cliente(String nombre, String direccion, String telefono) {
        this.setNombre(nombre);
        this.setDireccion(direccion);
        this.setTelefono(telefono);
    }

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getDireccion() {
		return direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	public String getTelefono() {
		return telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	public List<Cuenta> getCuentas() {
		return cuentas;
	}

	public void setCuentas(List<Cuenta> cuentas) {
		this.cuentas = cuentas;
	}
}
