-- Base de datos del proyecto PetStay Hotel
-- Estructura basada en el modelo entidad-relación del proyecto.

CREATE DATABASE IF NOT EXISTS PetStayHotel;
USE PetStayHotel;

CREATE TABLE Usuario (
    Id_Usuario INT PRIMARY KEY AUTO_INCREMENT,
    Nombre VARCHAR(50),
    Apellido VARCHAR(50),
    Cedula VARCHAR(20),
    Telefono VARCHAR(20),
    Correo VARCHAR(100),
    Direccion VARCHAR(150)
);

CREATE TABLE Mascota (
    Id_Mascota INT PRIMARY KEY AUTO_INCREMENT,
    Id_Usuario INT NOT NULL,
    Nombre VARCHAR(50),
    Especie VARCHAR(50),
    Raza VARCHAR(50),
    Edad INT,
    Peso DECIMAL(5,2),
    Observaciones VARCHAR(255),
    CONSTRAINT FK_Mascota_Usuario
        FOREIGN KEY (Id_Usuario) REFERENCES Usuario(Id_Usuario)
);

CREATE TABLE Habitacion (
    Id_Habitacion INT PRIMARY KEY AUTO_INCREMENT,
    Numero VARCHAR(20),
    Tipo VARCHAR(50),
    Capacidad INT,
    Precio_Noche DECIMAL(10,2),
    Estado VARCHAR(30)
);

CREATE TABLE Reserva (
    Id_Reserva INT PRIMARY KEY AUTO_INCREMENT,
    Id_Mascota INT NOT NULL,
    Id_Habitacion INT NOT NULL,
    Fecha_Ingreso DATE,
    Fecha_Salida DATE,
    Estado VARCHAR(30),
    Total DECIMAL(10,2),
    CONSTRAINT FK_Reserva_Mascota
        FOREIGN KEY (Id_Mascota) REFERENCES Mascota(Id_Mascota),
    CONSTRAINT FK_Reserva_Habitacion
        FOREIGN KEY (Id_Habitacion) REFERENCES Habitacion(Id_Habitacion)
);

CREATE TABLE Pago (
    Id_Pago INT PRIMARY KEY AUTO_INCREMENT,
    Id_Reserva INT NOT NULL,
    Fecha_Pago DATE,
    Valor DECIMAL(10,2),
    Modelo_Pago VARCHAR(50),
    Estado VARCHAR(30),
    CONSTRAINT FK_Pago_Reserva
        FOREIGN KEY (Id_Reserva) REFERENCES Reserva(Id_Reserva)
);

CREATE TABLE Servicio (
    Id_Servicio INT PRIMARY KEY AUTO_INCREMENT,
    Nombre VARCHAR(100),
    Descripcion VARCHAR(255),
    Precio DECIMAL(10,2),
    Estado VARCHAR(30)
);

CREATE TABLE Reserva_Servicio (
    Id_Reserva_Servicio INT PRIMARY KEY AUTO_INCREMENT,
    Id_Reserva INT NOT NULL,
    Id_Servicio INT NOT NULL,
    Cantidad INT,
    Precio_Unitario DECIMAL(10,2),
    Subtotal DECIMAL(10,2),
    CONSTRAINT FK_ReservaServicio_Reserva
        FOREIGN KEY (Id_Reserva) REFERENCES Reserva(Id_Reserva),
    CONSTRAINT FK_ReservaServicio_Servicio
        FOREIGN KEY (Id_Servicio) REFERENCES Servicio(Id_Servicio)
);
