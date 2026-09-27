package modulo3;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Modulo3 {

    // Datos de conexión a la base de datos PetStayHotel
    static final String URL = "jdbc:mysql://localhost:3306/PetStayHotel";
    static final String USUARIO_DB = "root";
    static final String PASSWORD_DB = "Wasquear1123@";

    static Connection conexion;
    static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {

        // Conexión con MySQL
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conexion = DriverManager.getConnection(URL, USUARIO_DB, PASSWORD_DB);

            System.out.println("======================================");
            System.out.println("   MÓDULO 3 - RESERVAS");
            System.out.println("   Conexión exitosa con MySQL");
            System.out.println("======================================");

            mostrarMenu();

        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(Modulo3.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("No fue posible conectar con la base de datos.");
        }
    }

    // Menú principal del módulo
    static void mostrarMenu() {

        int opcion;

        do {
            System.out.println();
            System.out.println("------ MÓDULO DE RESERVAS ------");
            System.out.println("1. Insertar reserva");
            System.out.println("2. Consultar reservas");
            System.out.println("3. Actualizar reserva");
            System.out.println("4. Eliminar reserva");
            System.out.println("0. Salir");
            System.out.print("Elige una opción: ");

            opcion = Integer.parseInt(sc.nextLine());

            switch (opcion) {
                case 1:
                    insertarReserva();
                    break;

                case 2:
                    consultarReservas();
                    break;

                case 3:
                    actualizarReserva();
                    break;

                case 4:
                    eliminarReserva();
                    break;

                case 0:
                    System.out.println("Saliendo del módulo de reservas...");
                    break;

                default:
                    System.out.println("Opción no válida.");
            }

        } while (opcion != 0);
    }

    // INSERTAR RESERVA
    static void insertarReserva() {

        try {

            System.out.println();
            System.out.println("--- INSERTAR RESERVA ---");

            System.out.print("ID de la mascota: ");
            int idMascota = Integer.parseInt(sc.nextLine());

            System.out.print("ID de la habitación: ");
            int idHabitacion = Integer.parseInt(sc.nextLine());

            System.out.print("Fecha de ingreso (AAAA-MM-DD): ");
            String fechaIngreso = sc.nextLine();

            System.out.print("Fecha de salida (AAAA-MM-DD): ");
            String fechaSalida = sc.nextLine();

            System.out.print("Estado: ");
            String estado = sc.nextLine();

            System.out.print("Total: ");
            double total = Double.parseDouble(sc.nextLine());

            String sql = "INSERT INTO reserva "
                    + "(Id_Mascota, Id_Habitacion, Fecha_Ingreso, Fecha_Salida, Estado, Total) "
                    + "VALUES (?, ?, ?, ?, ?, ?)";

            PreparedStatement ps = conexion.prepareStatement(sql);

            ps.setInt(1, idMascota);
            ps.setInt(2, idHabitacion);
            ps.setDate(3, java.sql.Date.valueOf(fechaIngreso));
            ps.setDate(4, java.sql.Date.valueOf(fechaSalida));
            ps.setString(5, estado);
            ps.setDouble(6, total);

            int filas = ps.executeUpdate();

            if (filas > 0) {
                System.out.println("Reserva registrada correctamente.");
            }

        } catch (SQLException | IllegalArgumentException ex) {
            Logger.getLogger(Modulo3.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("No fue posible registrar la reserva.");
        }
    }

    // CONSULTAR RESERVAS
    static void consultarReservas() {

        try {

            System.out.println();
            System.out.println("--- LISTADO DE RESERVAS ---");

            String sql = "SELECT Id_Reserva, Id_Mascota, Id_Habitacion, "
                    + "Fecha_Ingreso, Fecha_Salida, Estado, Total "
                    + "FROM reserva";

            PreparedStatement ps = conexion.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            boolean hayReservas = false;

            while (rs.next()) {

                hayReservas = true;

                System.out.println(
                        "ID Reserva: " + rs.getInt("Id_Reserva")
                        + " | Mascota: " + rs.getInt("Id_Mascota")
                        + " | Habitación: " + rs.getInt("Id_Habitacion")
                        + " | Ingreso: " + rs.getDate("Fecha_Ingreso")
                        + " | Salida: " + rs.getDate("Fecha_Salida")
                        + " | Estado: " + rs.getString("Estado")
                        + " | Total: " + rs.getDouble("Total")
                );
            }

            if (!hayReservas) {
                System.out.println("No hay reservas registradas.");
            }

        } catch (SQLException ex) {
            Logger.getLogger(Modulo3.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("No fue posible consultar las reservas.");
        }
    }

    // ACTUALIZAR RESERVA
    static void actualizarReserva() {

        try {

            System.out.println();
            System.out.println("--- ACTUALIZAR RESERVA ---");

            System.out.print("ID de la reserva a actualizar: ");
            int idReserva = Integer.parseInt(sc.nextLine());

            System.out.print("Nuevo ID de mascota: ");
            int idMascota = Integer.parseInt(sc.nextLine());

            System.out.print("Nuevo ID de habitación: ");
            int idHabitacion = Integer.parseInt(sc.nextLine());

            System.out.print("Nueva fecha de ingreso (AAAA-MM-DD): ");
            String fechaIngreso = sc.nextLine();

            System.out.print("Nueva fecha de salida (AAAA-MM-DD): ");
            String fechaSalida = sc.nextLine();

            System.out.print("Nuevo estado: ");
            String estado = sc.nextLine();

            System.out.print("Nuevo total: ");
            double total = Double.parseDouble(sc.nextLine());

            String sql = "UPDATE reserva SET "
                    + "Id_Mascota=?, "
                    + "Id_Habitacion=?, "
                    + "Fecha_Ingreso=?, "
                    + "Fecha_Salida=?, "
                    + "Estado=?, "
                    + "Total=? "
                    + "WHERE Id_Reserva=?";

            PreparedStatement ps = conexion.prepareStatement(sql);

            ps.setInt(1, idMascota);
            ps.setInt(2, idHabitacion);
            ps.setDate(3, java.sql.Date.valueOf(fechaIngreso));
            ps.setDate(4, java.sql.Date.valueOf(fechaSalida));
            ps.setString(5, estado);
            ps.setDouble(6, total);
            ps.setInt(7, idReserva);

            int filas = ps.executeUpdate();

            if (filas > 0) {
                System.out.println("Reserva actualizada correctamente.");
            } else {
                System.out.println("No se encontró una reserva con ese ID.");
            }

        } catch (SQLException | IllegalArgumentException ex) {
            Logger.getLogger(Modulo3.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("No fue posible actualizar la reserva.");
        }
    }

    // ELIMINAR RESERVA
    static void eliminarReserva() {

        try {

            System.out.println();
            System.out.println("--- ELIMINAR RESERVA ---");

            System.out.print("ID de la reserva a eliminar: ");
            int idReserva = Integer.parseInt(sc.nextLine());

            String sql = "DELETE FROM reserva WHERE Id_Reserva=?";

            PreparedStatement ps = conexion.prepareStatement(sql);
            ps.setInt(1, idReserva);

            int filas = ps.executeUpdate();

            if (filas > 0) {
                System.out.println("Reserva eliminada correctamente.");
            } else {
                System.out.println("No se encontró una reserva con ese ID.");
            }

        } catch (SQLException | IllegalArgumentException ex) {
            Logger.getLogger(Modulo3.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("No fue posible eliminar la reserva.");
        }
    }
}