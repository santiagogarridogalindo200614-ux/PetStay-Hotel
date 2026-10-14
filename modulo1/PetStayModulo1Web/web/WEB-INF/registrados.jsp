<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.util.List, modelo.Almacen, modelo.Usuario, modelo.Mascota"%>
<%!
    // Declaración JSP: método auxiliar para escapar el texto antes de mostrarlo en HTML
    private String esc(String texto) {
        if (texto == null) {
            return "";
        }
        return texto.replace("&", "&amp;").replace("<", "&lt;")
                    .replace(">", "&gt;").replace("\"", "&quot;");
    }
%>
<%
    // Scriptlet: toma los datos registrados para mostrarlos en las tablas
    List<Usuario> usuarios = Almacen.listarUsuarios();
    List<Mascota> mascotas = Almacen.listarMascotas();
%>
<!DOCTYPE html>
<html lang="es">
<head>
    <title>Registrados - PetStay Hotel</title>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
</head>
<body>
    <h1>Usuarios y mascotas registrados</h1>
    <p><a href="index.html">Volver a los formularios</a></p>
    <p>Los datos se guardan en memoria: se borran cuando Tomcat se reinicia.</p>

    <%-- Expresión JSP: se imprime el valor de una variable Java dentro del HTML --%>
    <h2>Usuarios (<%= usuarios.size() %>)</h2>
    <% if (usuarios.isEmpty()) { %>
        <p>Todavía no hay usuarios registrados.</p>
    <% } else { %>
        <table border="1" cellpadding="4">
            <tr>
                <th>ID</th><th>Nombre</th><th>Apellido</th><th>Cédula</th>
                <th>Teléfono</th><th>Correo</th><th>Dirección</th>
            </tr>
            <% for (Usuario u : usuarios) { %>
                <tr>
                    <td><%= u.getIdUsuario() %></td>
                    <td><%= esc(u.getNombre()) %></td>
                    <td><%= esc(u.getApellido()) %></td>
                    <td><%= esc(u.getCedula()) %></td>
                    <td><%= esc(u.getTelefono()) %></td>
                    <td><%= esc(u.getCorreo()) %></td>
                    <td><%= esc(u.getDireccion()) %></td>
                </tr>
            <% } %>
        </table>
    <% } %>

    <h2>Mascotas (<%= mascotas.size() %>)</h2>
    <% if (mascotas.isEmpty()) { %>
        <p>Todavía no hay mascotas registradas.</p>
    <% } else { %>
        <table border="1" cellpadding="4">
            <tr>
                <th>ID</th><th>Nombre</th><th>Especie</th><th>Raza</th><th>Edad</th>
                <th>Peso (kg)</th><th>Dueño</th><th>Observaciones</th>
            </tr>
            <% for (Mascota m : mascotas) { %>
                <tr>
                    <td><%= m.getIdMascota() %></td>
                    <td><%= esc(m.getNombre()) %></td>
                    <td><%= esc(m.getEspecie()) %></td>
                    <td><%= esc(m.getRaza()) %></td>
                    <td><%= m.getEdad() %></td>
                    <td><%= m.getPeso() %></td>
                    <td><%= esc(m.getNombreDueno()) %></td>
                    <td><%= esc(m.getObservaciones()) %></td>
                </tr>
            <% } %>
        </table>
    <% } %>
</body>
</html>