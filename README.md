# Banco API

Este es el backend de una aplicación bancaria desarrollado con **Spring Boot** y **MySQL**. Permite gestionar clientes, cuentas y movimientos bancarios.

## Tecnologías utilizadas
- Java 17
- Spring Boot
- MySQL
- JDBC
- JUnit (para pruebas)

## Requisitos previos
Asegúrate de tener instalados en tu máquina:
- **JDK 17** o superior
- **Maven**
- **MySQL Server**
- **Postman** (opcional, para probar la API)

## Configuración

### Clonar el repositorio
```sh
git clone https://github.com/darkfrc07/bancoapi.git
cd bancoapi

### Configurar la base de datos

Crea una base de datos en MySQL llamada banco_db y actualiza el archivo application.properties con las credenciales:

spring.datasource.url=jdbc:mysql://localhost:3306/banco_db
spring.datasource.username=root
spring.datasource.password=juliana09*

Construir y ejecutar el proyecto

mvn clean install
mvn spring-boot:run

SCRIPTS CREATE TABLE
CREATE TABLE Cliente (
    id SERIAL PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    direccion VARCHAR(255),
    telefono VARCHAR(15) NOT NULL
); 

CREATE TABLE Cuenta (
    numero VARCHAR(20) PRIMARY KEY,
    saldo DECIMAL(15,2) NOT NULL CHECK (saldo >= 0),
    id_cliente BIGINT UNSIGNED NOT NULL,
    FOREIGN KEY (id_cliente) REFERENCES Cliente(id) ON DELETE CASCADE
);

CREATE TABLE Movimiento (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    numero_cuenta VARCHAR(20) NOT NULL,
    tipo ENUM('crédito', 'débito') NOT NULL,
    fecha DATE NOT NULL DEFAULT (CURRENT_DATE),
    valor DECIMAL(15,2) NOT NULL CHECK (valor > 0),
    FOREIGN KEY (numero_cuenta) REFERENCES Cuenta(numero) ON DELETE CASCADE
);


Documentacion en POSTMAN: https://documenter.getpostman.com/view/33439515/2sAYdcssZp
Para POSTMAN
El backend correrá en http://localhost:8080.

-------------- Endpoints disponibles---------------

cliente--------------------------------------------
POST	/cliente Crear un cliente
GET	/cliente/{id}	Obtener un cliente por ID
GET /cliente Obtiene lista de todos los clientes
PUT /cliente/{id} Actusliza cliente del id 
DELETE /cliente/{id} Elimina cliente del id 
---------------------------------------------------

cuentas---------------------------------------------
POST	/cuentas	Registrar una cuenta
GET	/cuentas/{numeroCuenta}	Obtener cuenta por numero
GET	/cuentas	Obtener todas las cuentas
PUT /cuentas/{numeroCuenta} Actualiza cuenta coh el numero de cuenta 
DELETE /cuentas/{numeroCuenta} Elimina cuenta coh el numero de cuenta 
---------------------------------------------------------------------

movimientos --------------------------------------------------------
POST	/movimientos	Registrar un movimiento
GET	/movimientos Obtiene todos los movimientos
GET	/movimientos/cuenta/{numeroCuenta} Obtiene movimientos de la cuenta con el numero de cuenta
-------------------------------------------------------------------------------------------------

Métodos	URL:
cliente:
GET:http://localhost:8080/cliente 
POST: http://localhost:8080/cliente
PUT: http://localhost:8080/cliente/24   ---> id cliente a actualizar/cambiar con body
DELETE http://localhost:8080/cliente/24 ---> id cliente a eliminar

BODY
{
  "nombre": "Juan Pérez",
  "direccion": "Calle 123",
  "telefono": "123456789"
}
-------------------------------------------------------------------------------------------------

cuentas

GET http://localhost:8080/cuentas
POST http://localhost:8080/cuentas
PUT http://localhost:8080/cuentas/numeroCuenta
DELETE http://localhost:8080/cuentas/numeroCuenta

BODY
{
  "numero": "123456789",
  "saldo": 1000.0,
  "clienteId": 1
}
--------------------------------------------------------

movimientos

POST http://localhost:8080/movimientos
GET http://localhost:8080/movimientos
GET http://localhost:8080/movimientos/cuenta/numeroCuenta
{
    "numeroCuenta": "12345",
    "tipo": "DEBITO",
    "valor": 100.00,
    //"fecha": "2024-02-16" Sin fecha se pone la actual
}

Pruebas

Para ejecutar las pruebas unitarias:

mvn test
