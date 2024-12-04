package com.json;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import javax.swing.*;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.sql.*;
import java.util.Collections;
import java.util.List;
import java.util.Map;

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

    private int obtenerIdUsuarioDesdeJson(String nombreUsuario) {
        // Cargar el archivo JSON
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("info.json");
        if (inputStream == null) {
            throw new IllegalStateException("No se pudo cargar el archivo JSON.");
        }

        Gson gson = new Gson();
        try (InputStreamReader reader = new InputStreamReader(inputStream)) {
            Type type = new TypeToken<Map<String, Object>>() {}.getType();
            Map<String, Object> data = gson.fromJson(reader, type);
            Map<String, Object> tienda = (Map<String, Object>) data.get("tienda");
            List<Map<String, Object>> usuarios = (List<Map<String, Object>>) tienda.get("usuarios");
            // Buscar el ID del usuario por su nombre
            for (Map<String, Object> usuario : usuarios) {
                String nombre = (String) usuario.get("nombre");
                if (nombreUsuario.equals(nombre)) {
                    // Devolver el ID del usuario
                    return ((Double) usuario.get("id")).intValue();
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Error al procesar el JSON.", e);
        }
        throw new IllegalArgumentException("Usuario no encontrado: " + nombreUsuario);
    }


    // Método para obtener la lista de usuarios desde el JSON
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
