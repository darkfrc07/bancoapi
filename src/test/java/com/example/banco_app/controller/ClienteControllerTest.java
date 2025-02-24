package com.example.banco_app.controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Arrays;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.example.banco_app.model.Cliente;
import com.example.banco_app.service.ClienteService;
import com.fasterxml.jackson.databind.ObjectMapper;

class ClienteControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ClienteService clienteService;

    @InjectMocks
    private ClienteController clienteController;

    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(clienteController).build();
    }

    @Test
    void testObtenerClientes() throws Exception {
        Cliente cliente1 = new Cliente(1L, "Juan Perez", "1234567890", "juan@example.com");
        Cliente cliente2 = new Cliente(2L, "Maria Gomez", "0987654321", "maria@example.com");

        when(clienteService.obtenerClientes()).thenReturn(Arrays.asList(cliente1, cliente2));

        mockMvc.perform(MockMvcRequestBuilders.get("/cliente"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.size()").value(2))
               .andExpect(jsonPath("$[0].nombre").value("Juan Perez"));
    }

    @Test
    void testObtenerClientePorId_Existe() throws Exception {
        Cliente cliente = new Cliente(1L, "Juan Perez", "1234567890", "juan@example.com");

        when(clienteService.buscarPorId(1L)).thenReturn(Optional.of(cliente));

        mockMvc.perform(MockMvcRequestBuilders.get("/cliente/1"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.nombre").value("Juan Perez"));
    }

    @Test
    void testObtenerClientePorId_NoExiste() throws Exception {
        when(clienteService.buscarPorId(1L)).thenReturn(Optional.empty());

        mockMvc.perform(MockMvcRequestBuilders.get("/cliente/1"))
               .andExpect(status().isNotFound());
    }

    @Test
    void testCrearCliente() throws Exception {
    	Cliente cliente = new Cliente(1L, "Juan Perez", "Calle 123", "1234567890"); // Dirección en el lugar correcto

    	when(clienteService.agregarCliente(any(Cliente.class))).thenReturn(cliente);

    	mockMvc.perform(MockMvcRequestBuilders.post("/cliente")
    	        .contentType(MediaType.APPLICATION_JSON)
    	        .content(objectMapper.writeValueAsString(cliente))) // Se enviará un JSON válido
    	       .andExpect(status().isCreated())
    	       .andExpect(jsonPath("$.nombre").value("Juan Perez"));
    }

    @Test
    void testActualizarCliente_Existe() throws Exception {
        Cliente clienteActualizado = new Cliente(1L, "Juan Actualizado", "1234567890", "juan@nuevo.com");

        when(clienteService.actualizarCliente(eq(1L), any(Cliente.class))).thenReturn(Optional.of(clienteActualizado));

        mockMvc.perform(MockMvcRequestBuilders.put("/cliente/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(clienteActualizado)))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.nombre").value("Juan Actualizado"));
    }

    @Test
    void testActualizarCliente_NoExiste() throws Exception {
        Cliente clienteActualizado = new Cliente(1L, "Juan Actualizado", "1234567890", "juan@nuevo.com");

        when(clienteService.actualizarCliente(eq(1L), any(Cliente.class))).thenReturn(Optional.empty());

        mockMvc.perform(MockMvcRequestBuilders.put("/cliente/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(clienteActualizado)))
               .andExpect(status().isNotFound());
    }

    @Test
    void testEliminarCliente_Existe() throws Exception {
        when(clienteService.eliminarClientePorId(1L)).thenReturn(true);

        mockMvc.perform(MockMvcRequestBuilders.delete("/cliente/1"))
               .andExpect(status().isNoContent());
    }

    @Test
    void testEliminarCliente_NoExiste() throws Exception {
        when(clienteService.eliminarClientePorId(1L)).thenReturn(false);

        mockMvc.perform(MockMvcRequestBuilders.delete("/cliente/1"))
               .andExpect(status().isNotFound());
    }

    @Test
    void testAsignarCuenta_Exito() throws Exception {
        when(clienteService.asignarCuenta(1L, 2L)).thenReturn(true);

        mockMvc.perform(MockMvcRequestBuilders.post("/cliente/1/cuentas/2"))
               .andExpect(status().isOk())
               .andExpect(content().string("Cuenta asignada correctamente al cliente."));
    }

    @Test
    void testAsignarCuenta_Fallo() throws Exception {
        when(clienteService.asignarCuenta(1L, 2L)).thenReturn(false);

        mockMvc.perform(MockMvcRequestBuilders.post("/cliente/1/cuentas/2"))
               .andExpect(status().isBadRequest())
               .andExpect(content().string("No se pudo asignar la cuenta."));
    }
}
