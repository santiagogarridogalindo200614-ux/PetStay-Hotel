package controladores;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import modelo.Almacen;
import modelo.Mascota;
import modelo.Usuario;

// Servlet que recibe el formulario "Registrar mascota" de index.html (action="RegistroMascota").
// La mascota se asocia a su dueño buscándolo por la cédula que se escribe en el formulario.
public class RegistroMascota extends HttpServlet {

    // Arma la página HTML de respuesta con el mensaje recibido
    protected void processRequest(HttpServletRequest request, HttpServletResponse response, String msg)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            out.println("<!DOCTYPE html>");
            out.println("<html lang=\"es\">");
            out.println("<head>");
            out.println("<meta charset=\"UTF-8\">");
            out.println("<title>Registro de mascota</title>");
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

        String cedulaDueno = limpiar(request.getParameter("cedulaDueno"));
        String nombre = limpiar(request.getParameter("nombre"));
        String especie = limpiar(request.getParameter("especie"));

        String msg;
        if (cedulaDueno.isEmpty() || nombre.isEmpty() || especie.isEmpty()) {
            msg = "Registro inválido: cédula del dueño, nombre y especie son obligatorios";
        } else {
            try {
                Mascota m = new Mascota();
                m.setNombre(nombre);
                m.setEspecie(especie);
                m.setRaza(limpiar(request.getParameter("raza")));
                m.setEdad(Integer.parseInt(limpiar(request.getParameter("edad"))));
                m.setPeso(Double.parseDouble(limpiar(request.getParameter("peso"))));
                m.setObservaciones(limpiar(request.getParameter("observaciones")));

                // El dueño debe existir: se busca por su cédula
                Usuario dueno = Almacen.buscarUsuarioPorCedula(cedulaDueno);
                if (dueno == null) {
                    msg = "Registro inválido: no existe un usuario con esa cédula. Registra primero al dueño";
                } else {
                    m.setIdUsuario(dueno.getIdUsuario());
                    m.setNombreDueno(dueno.getNombre() + " " + dueno.getApellido());
                    Almacen.agregarMascota(m);
                    msg = "Registro exitoso: la mascota quedó registrada";
                }
            } catch (NumberFormatException ex) {
                msg = "Registro inválido: edad y peso deben ser números";
            }
        }
        processRequest(request, response, msg);
    }

    // Atiende formularios con method="post": delega en doGet()
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        doGet(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Registra una mascota asociada a un usuario en memoria";
    }

    private String limpiar(String texto) {
        return texto == null ? "" : texto.trim();
    }
}