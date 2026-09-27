package modulo1;

// Módulo 1: Usuarios y Mascotas - Proyecto PetStay Hotel
// Conexión a la base de datos mediante JDBC y operaciones CRUD
// (insertar, consultar, actualizar, eliminar) para las tablas Usuario y Mascota.

import java.sql.*;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Modulo1 {

    // Datos de conexión a la base de datos PetStayHotel
    static final String URL = "jdbc:mysql://localhost:3306/PetStayHotel";
    static final String USUARIO_DB = "root";
    static final String PASSWORD_DB = "";

    // Conexión que se abre una sola vez y se reutiliza en todo el programa
    static Connection conexion;

    // Scanner para leer lo que el usuario escribe por consola
    static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {

        // Cargar el driver de MySQL y abrir la conexión a la base de datos
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conexion = DriverManager.getConnection(URL, USUARIO_DB, PASSWORD_DB);
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(Modulo1.class.getName()).log(Level.SEVERE, null, ex);
            return; // si no hay conexión, no tiene sentido seguir ejecutando el programa
        }

        // Menú principal: se repite hasta que el usuario elija la opción 0 (salir)
        int opcion;
        do {
            mostrarMenu();
            opcion = Integer.parseInt(sc.nextLine());
            switch (opcion) {
                case 1: insertarUsuario(); break;
                case 2: consultarUsuarios(); break;
                case 3: actualizarUsuario(); break;
                case 4: eliminarUsuario(); break;
                case 5: insertarMascota(); break;
                case 6: consultarMascotas(); break;
                case 7: actualizarMascota(); break;
                case 8: eliminarMascota(); break;
                case 0: System.out.println("Saliendo..."); break;
                default: System.out.println("Opción no válida.");
            }
        } while (opcion != 0);
    }

    // Imprime las opciones disponibles del menú
    static void mostrarMenu() {
        System.out.println("\n--- MÓDULO USUARIOS Y MASCOTAS ---");
        System.out.println("1. Insertar usuario");
        System.out.println("2. Consultar usuarios");
        System.out.println("3. Actualizar usuario");
        System.out.println("4. Eliminar usuario");
        System.out.println("5. Insertar mascota");
        System.out.println("6. Consultar mascotas");
        System.out.println("7. Actualizar mascota");
        System.out.println("8. Eliminar mascota");
        System.out.println("0. Salir");
        System.out.print("Elige una opción: ");
    }

    // ================== CRUD TABLA USUARIO ==================

    // Pide los datos del usuario por consola y los inserta en la tabla Usuario
    static void insertarUsuario() {
        try {
            System.out.print("Nombre: "); String nombre = sc.nextLine();
            System.out.print("Apellido: "); String apellido = sc.nextLine();
            System.out.print("Cédula: "); String cedula = sc.nextLine();
            System.out.print("Teléfono: "); String telefono = sc.nextLine();
            System.out.print("Correo: "); String correo = sc.nextLine();
            System.out.print("Dirección: "); String direccion = sc.nextLine();

            // Los "?" son marcadores de posición que se llenan más abajo con setString()
            // (esto es un PreparedStatement, más seguro que pegar los valores directo en el texto SQL)
            String sql = "INSERT INTO Usuario (Nombre, Apellido, Cedula, Telefono, Correo, Direccion) VALUES (?,?,?,?,?,?)";
            PreparedStatement ps = conexion.prepareStatement(sql);
            ps.setString(1, nombre);
            ps.setString(2, apellido);
            ps.setString(3, cedula);
            ps.setString(4, telefono);
            ps.setString(5, correo);
            ps.setString(6, direccion);
            ps.executeUpdate(); // ejecuta el INSERT contra la base de datos
            System.out.println("Usuario insertado correctamente.");
        } catch (SQLException ex) {
            Logger.getLogger(Modulo1.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    // Lee todos los usuarios registrados y los muestra en consola
    static void consultarUsuarios() {
        try {
            String sql = "SELECT * FROM Usuario";
            Statement st = conexion.createStatement();
            ResultSet rs = st.executeQuery(sql); // ejecuta el SELECT y devuelve las filas obtenidas

            // rs.next() avanza fila por fila; devuelve false cuando ya no hay más
            while (rs.next()) {
                System.out.println(rs.getInt("Id_Usuario") + " | " +
                        rs.getString("Nombre") + " " + rs.getString("Apellido") +
                        " | Cédula: " + rs.getString("Cedula") +
                        " | Tel: " + rs.getString("Telefono"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(Modulo1.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    // Actualiza el teléfono y correo de un usuario, según el ID que se ingrese
    static void actualizarUsuario() {
        try {
            System.out.print("ID del usuario a actualizar: "); int id = Integer.parseInt(sc.nextLine());
            System.out.print("Nuevo teléfono: "); String telefono = sc.nextLine();
            System.out.print("Nuevo correo: "); String correo = sc.nextLine();

            // El WHERE es obligatorio: sin él se actualizarían TODOS los usuarios
            String sql = "UPDATE Usuario SET Telefono=?, Correo=? WHERE Id_Usuario=?";
            PreparedStatement ps = conexion.prepareStatement(sql);
            ps.setString(1, telefono);
            ps.setString(2, correo);
            ps.setInt(3, id);
            int filas = ps.executeUpdate(); // devuelve cuántas filas fueron modificadas
            System.out.println(filas > 0 ? "Usuario actualizado." : "No se encontró ese ID.");
        } catch (SQLException ex) {
            Logger.getLogger(Modulo1.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    // Elimina un usuario según el ID ingresado
    static void eliminarUsuario() {
        try {
            System.out.print("ID del usuario a eliminar: "); int id = Integer.parseInt(sc.nextLine());

            // El WHERE es obligatorio: sin él se borrarían TODOS los usuarios
            String sql = "DELETE FROM Usuario WHERE Id_Usuario=?";
            PreparedStatement ps = conexion.prepareStatement(sql);
            ps.setInt(1, id);
            int filas = ps.executeUpdate();
            System.out.println(filas > 0 ? "Usuario eliminado." : "No se encontró ese ID.");
        } catch (SQLException ex) {
            Logger.getLogger(Modulo1.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    // ================== CRUD TABLA MASCOTA ==================

    // Pide los datos de la mascota y la asocia al usuario (dueño) indicado por su ID
    static void insertarMascota() {
        try {
            System.out.print("ID del usuario dueño: "); int idUsuario = Integer.parseInt(sc.nextLine());
            System.out.print("Nombre mascota: "); String nombre = sc.nextLine();
            System.out.print("Especie: "); String especie = sc.nextLine();
            System.out.print("Raza: "); String raza = sc.nextLine();
            System.out.print("Edad: "); int edad = Integer.parseInt(sc.nextLine());
            System.out.print("Peso: "); double peso = Double.parseDouble(sc.nextLine());

            // Id_Usuario es la llave foránea que conecta la mascota con su dueño en la tabla Usuario
            String sql = "INSERT INTO Mascota (Id_Usuario, Nombre, Especie, Raza, Edad, Peso) VALUES (?,?,?,?,?,?)";
            PreparedStatement ps = conexion.prepareStatement(sql);
            ps.setInt(1, idUsuario);
            ps.setString(2, nombre);
            ps.setString(3, especie);
            ps.setString(4, raza);
            ps.setInt(5, edad);
            ps.setDouble(6, peso);
            ps.executeUpdate();
            System.out.println("Mascota insertada correctamente.");
        } catch (SQLException ex) {
            Logger.getLogger(Modulo1.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    // Lista todas las mascotas junto con el nombre de su dueño (usa JOIN entre Mascota y Usuario)
    static void consultarMascotas() {
        try {
            // JOIN une las dos tablas por la llave foránea Id_Usuario para traer el nombre del dueño
            String sql = "SELECT m.Id_Mascota, m.Nombre, m.Especie, u.Nombre AS Dueño " +
                         "FROM Mascota m JOIN Usuario u ON m.Id_Usuario = u.Id_Usuario";
            Statement st = conexion.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                System.out.println(rs.getInt("Id_Mascota") + " | " +
                        rs.getString("Nombre") + " (" + rs.getString("Especie") +
                        ") | Dueño: " + rs.getString("Dueño"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(Modulo1.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    // Actualiza el peso de una mascota, según el ID que se ingrese
    static void actualizarMascota() {
        try {
            System.out.print("ID de la mascota a actualizar: "); int id = Integer.parseInt(sc.nextLine());
            System.out.print("Nuevo peso: "); double peso = Double.parseDouble(sc.nextLine());

            String sql = "UPDATE Mascota SET Peso=? WHERE Id_Mascota=?";
            PreparedStatement ps = conexion.prepareStatement(sql);
            ps.setDouble(1, peso);
            ps.setInt(2, id);
            int filas = ps.executeUpdate();
            System.out.println(filas > 0 ? "Mascota actualizada." : "No se encontró ese ID.");
        } catch (SQLException ex) {
            Logger.getLogger(Modulo1.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    // Elimina una mascota según el ID ingresado
    static void eliminarMascota() {
        try {
            System.out.print("ID de la mascota a eliminar: "); int id = Integer.parseInt(sc.nextLine());

            String sql = "DELETE FROM Mascota WHERE Id_Mascota=?";
            PreparedStatement ps = conexion.prepareStatement(sql);
            ps.setInt(1, id);
            int filas = ps.executeUpdate();
            System.out.println(filas > 0 ? "Mascota eliminada." : "No se encontró ese ID.");
        } catch (SQLException ex) {
            Logger.getLogger(Modulo1.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}