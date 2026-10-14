package controladores;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import modelo.Almacen;
import modelo.Usuario;

// Servlet que recibe el formulario "Registrar usuario" de index.html (action="RegistroUsuario").
// Misma estructura del ejercicio RecibeDatos: doGet() lee y valida, processRequest() arma la respuesta
// con el mensaje, y doPost() delega en doGet() para atender también method="post".
public class RegistroUsuario extends HttpServlet {

    // Arma la página HTML de respuesta con el mensaje recibido
    protected void processRequest(HttpServletRequest request, HttpServletResponse response, String msg)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            out.println("<!DOCTYPE html>");
            out.println("<html lang=\"es\">");
            out.println("<head>");
            out.println("<meta charset=\"UTF-8\">");
            out.println("<title>Registro de usuario</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>" + msg + "</h1>");
            out.println("<p><a href=\"index.html\">Volver al formulario</a> | ");
            out.println("<a href=\"registrados.jsp\">Ver registrados</a></p>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    // Atiende formularios con method="get": lee los datos, valida, registra y responde
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // getParameter("nombre") lee el input name="nombre" del formulario (igual, letra por letra)
        Usuario u = new Usuario();
        u.setNombre(limpiar(request.getParameter("nombre")));
        u.setApellido(limpiar(request.getParameter("apellido")));
        u.setCedula(limpiar(request.getParameter("cedula")));
        u.setTelefono(limpiar(request.getParameter("telefono")));
        u.setCorreo(limpiar(request.getParameter("correo")));
        u.setDireccion(limpiar(request.getParameter("direccion")));

        String msg;
        if (u.getNombre().isEmpty() || u.getApellido().isEmpty() || u.getCedula().isEmpty()) {
            msg = "Registro inválido: nombre, apellido y cédula son obligatorios";
        } else if (Almacen.agregarUsuario(u)) {
            msg = "Registro exitoso: el usuario quedó registrado";
        } else {
            msg = "Registro inválido: ya existe un usuario con esa cédula";
        }
        processRequest(request, response, msg);
    }

    // Atiende formularios con method="post": delega en doGet()
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8"); // para que lleguen bien las tildes y la ñ
        doGet(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Registra un usuario en memoria";
    }

    // Quita espacios sobrantes; si el campo no llegó devuelve texto vacío
    private String limpiar(String texto) {
        return texto == null ? "" : texto.trim();
    }
}