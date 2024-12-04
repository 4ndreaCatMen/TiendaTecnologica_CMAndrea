package com.json;

import javax.swing.*;
import java.sql.*;
import java.util.List;

public class HistorialCompra {

    // Método para realizar la compra
    public void realizarCompra(String nombreUsuario, String nombreProducto) {
        // Obtener el ID del usuario desde el JSON
        int idUsuario = obtenerIdUsuarioDesdeJson(nombreUsuario);

        if (idUsuario == -1) {
            JOptionPane.showMessageDialog(null, "El usuario no fue encontrado.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Realizar el insert en la base de datos
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/tienda", "root", "root")) {
            /** CREAR LA TABLA **/
            crearTablaTienda(conn);
            String query = "INSERT INTO historico_compras (nombre_usuario, id_usuario, nombre_producto) VALUES (?, ?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setString(1, nombreUsuario);
                stmt.setInt(2, idUsuario);
                stmt.setString(3, nombreProducto);
                stmt.executeUpdate();
                JOptionPane.showMessageDialog(null, "Compra realizada exitosamente.");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al realizar la compra: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Crear la tabla en la base de datos si no existe
    private void crearTablaTienda(Connection conn) {
        String sql = "CREATE TABLE IF NOT EXISTS historico_compras ("
                + "id INT AUTO_INCREMENT PRIMARY KEY, "
                + "nombre_usuario VARCHAR(100), "
                + "id_usuario INT, "
                + "nombre_producto VARCHAR(100))";

        try (Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(sql);
            System.out.println("Tabla 'historico_compras' creada exitosamente.");
        } catch (SQLException e) {
            System.out.println("Error al crear la tabla: " + e.getMessage());
        }
    }

    // Método para obtener el ID del usuario desde el JSON
    private int obtenerIdUsuarioDesdeJson(String nombreUsuario) {
        // Obtener la lista de usuarios desde el archivo JSON o simulada
        List<Usuario> usuarios = obtenerUsuariosJson();  // Método que te devuelve la lista de usuarios

        for (Usuario usuario : usuarios) {
            if (usuario.getNombre().equals(nombreUsuario)) {
                return usuario.getId();  // Retorna el ID del usuario si se encuentra
            }
        }
        // Si no se encuentra el usuario, devolvemos -1
        return -1;
    }

    // Método para obtener la lista de usuarios desde el JSON
    private List<Usuario> obtenerUsuariosJson() {
        // Aquí deberías implementar la lectura del archivo JSON y devolver la lista de usuarios
        // Simulamos una lista de usuarios de ejemplo
        return List.of(
                new Usuario(1, "Juan Pérez"),
                new Usuario(2, "Ana García")
        );
    }

    // Clase interna para representar a un Usuario
    public static class Usuario {
        private final int id;
        private final String nombre;

        public Usuario(int id, String nombre) {
            this.id = id;
            this.nombre = nombre;
        }

        public int getId() {
            return id;
        }

        public String getNombre() {
            return nombre;
        }
    }
}
