# Banco API

Este es el backend de una aplicación bancaria desarrollado con **Spring Boot** y **MySQL**. Permite gestionar clientes, cuentas y movimientos bancarios.

## Tecnologías utilizadas
- Java 17
- Spring Boot
- MySQL
- JDBC
- JUnit (para pruebas)

## 📌 Requisitos previos
Asegúrate de tener instalados en tu máquina:
- **JDK 17** o superior
- **Maven**
- **MySQL Server**
- **Postman** (opcional, para probar la API)

## ⚙️ Configuración

### 1️⃣ Clonar el repositorio
```sh
git clone https://github.com/darkfrc07/bancoapi.git
cd bancoapi

### Configurar la base de datos

Crea una base de datos en MySQL llamada banco_db y actualiza el archivo application.properties con tus credenciales:

spring.datasource.url=jdbc:mysql://localhost:3306/banco_db
spring.datasource.username=tu_usuario
spring.datasource.password=tu_contraseña

3️⃣ Construir y ejecutar el proyecto

mvn clean install
mvn spring-boot:run

El backend correrá en http://localhost:8080.

 Endpoints disponibles
 POST	/clientes	Crear un cliente
GET	/clientes/{id}	Obtener un cliente por ID
GET	/cuentas/{id}	Obtener cuenta por ID
POST	/movimientos	Registrar un movimiento
GET	/movimientos/reporte	Generar reporte por fechas


Método	URL	:
CLIENTES

GET:http://localhost:8080/cliente 
POST: http://localhost:8080/cliente 
POST CON BODY

PUT: http://localhost:8080/cliente/id_cliente
PUT CON BODY
DELETE http://localhost:8080/cliente/id_cliente

{
  "nombre": "Juan Pérez",
  "direccion": "Calle 123",
  "telefono": "123456789"
}

CUENTAS

GET http://localhost:8080/cuentas/numeroCuenta
POST http://localhost:8080/cuentas
PUT http://localhost:8080/cuentas/numeroCuenta

{
  "numero": "123456789",
  "saldo": 1000.0,
  "clienteId": 1
}

MOVIMIENTOS

http://localhost:8080/movimientos

{
  "tipo": "crédito",
  "valor": 500.0,
  "fecha": "2024-02-16"
}


🧪 Pruebas

Para ejecutar las pruebas unitarias:

mvn test
