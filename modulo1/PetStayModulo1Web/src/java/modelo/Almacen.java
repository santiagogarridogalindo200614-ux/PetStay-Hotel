package modelo;

import java.util.ArrayList;
import java.util.List;

// Guarda en memoria los usuarios y las mascotas registrados (no usa base de datos).
// Los datos se pierden cuando Tomcat se reinicia o el proyecto se vuelve a ejecutar.
public class Almacen {

    private static final List<Usuario> usuarios = new ArrayList<>();
    private static final List<Mascota> mascotas = new ArrayList<>();
    private static int siguienteIdUsuario = 1;
    private static int siguienteIdMascota = 1;

    // synchronized: Tomcat atiende varias peticiones a la vez y las listas se comparten.
    // Devuelve false si ya existe un usuario con esa cédula.
    public static synchronized boolean agregarUsuario(Usuario u) {
        if (buscarUsuarioPorCedula(u.getCedula()) != null) {
            return false;
        }
        u.setIdUsuario(siguienteIdUsuario++);
        usuarios.add(u);
        return true;
    }

    // Devuelve el usuario con esa cédula, o null si no existe
    public static synchronized Usuario buscarUsuarioPorCedula(String cedula) {
        for (Usuario u : usuarios) {
            if (u.getCedula().equals(cedula)) {
                return u;
            }
        }
        return null;
    }

    public static synchronized void agregarMascota(Mascota m) {
        m.setIdMascota(siguienteIdMascota++);
        mascotas.add(m);
    }

    // Devuelven una copia para que la lista original no se modifique desde afuera
    public static synchronized List<Usuario> listarUsuarios() {
        return new ArrayList<>(usuarios);
    }

    public static synchronized List<Mascota> listarMascotas() {
        return new ArrayList<>(mascotas);
    }
}