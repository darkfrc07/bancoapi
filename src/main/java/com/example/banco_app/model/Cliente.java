package com.example.banco_app.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jakarta.validation.Valid;

public class Cliente {
    private Long id;

    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 100, message = "El nombre no puede tener más de 100 caracteres")
    private String nombre;

    @NotBlank(message = "La dirección es obligatoria")
    @Size(max = 200, message = "La dirección no puede tener más de 200 caracteres")
    private String direccion;

    @NotBlank(message = "El teléfono es obligatorio")
    @Size(max = 15, message = "El teléfono no puede tener más de 15 caracteres")
    private String telefono;

    // Constructor vacío
    public Cliente() {}

    // Constructor con parámetros
    public Cliente(Long id, String nombre, String direccion, String telefono) {
        this.id = id;
        this.nombre = nombre;
        this.direccion = direccion;
        this.telefono = telefono;
    }

    // Getters
    public Long getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getDireccion() {
        return direccion;
    }

    public String getTelefono() {
        return telefono;
    }

    // Setters con @Valid para validación
    public void setId(Long id) {
        this.id = id;
    }

    public void setNombre(@Valid String nombre) {
        this.nombre = nombre;
    }

    public void setDireccion(@Valid String direccion) {
        this.direccion = direccion;
    }

    public void setTelefono(@Valid String telefono) {
        this.telefono = telefono;
    }
}
