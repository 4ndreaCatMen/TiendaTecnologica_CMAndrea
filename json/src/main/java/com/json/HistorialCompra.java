package com.json;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class HistorialCompra {

    // Método para realizar la compra
    public void realizarCompra(String nombreUsuario, String nombreProducto) {
        // Obtenemos el ID del usuario usando su nombre
        int idUsuario = obtenerIdUsuarioDesdeJson(nombreUsuario);

        // Si no encontramos al usuario, mostramos un error y salimos
        if (idUsuario == -1) {
            JOptionPane.showMessageDialog(null, "El usuario no fue encontrado.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/tienda", "root", "root")) {
            // Aseguramos que la tabla exista antes de insertar datos
            crearTablaTienda(conn);

            // Preparamos la consulta para insertar la compra
            String query = "INSERT INTO historico_compras (nombre_usuario, id_usuario, nombre_producto) VALUES (?, ?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setString(1, nombreUsuario); // Asignamos el nombre del usuario
                stmt.setInt(2, idUsuario);       // Asignamos el ID del usuario
                stmt.setString(3, nombreProducto); // Asignamos el producto comprado
                stmt.executeUpdate(); // Ejecutamos la consulta
                JOptionPane.showMessageDialog(null, "Compra realizada exitosamente.");
            }
        } catch (SQLException e) {
            // Si hay un error con la base de datos, mostramos un mensaje
            JOptionPane.showMessageDialog(null, "Error al realizar la compra: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void crearTablaTienda(Connection conn) {
        String sql = "CREATE TABLE IF NOT EXISTS historico_compras ("
                + "id INT AUTO_INCREMENT PRIMARY KEY, " // ID único para cada compra
                + "nombre_usuario VARCHAR(100), "       // Nombre del usuario
                + "id_usuario INT, "                    // ID del usuario
                + "nombre_producto VARCHAR(100))";      // Producto comprado

        try (Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(sql); // Ejecutamos el comando SQL
        } catch (SQLException e) {
            // Mostramos un mensaje si hay un error al crear la tabla
            System.out.println("Error al crear la tabla: " + e.getMessage());
        }
    }

    private int obtenerIdUsuarioDesdeJson(String nombreUsuario) {
        // Obtenemos la lista de usuarios desde el JSON simulado
        List<Usuario> usuarios = obtenerUsuariosJson();

        // Buscamos al usuario por nombre
        for (Usuario usuario : usuarios) {
            if (usuario.getNombre().equals(nombreUsuario)) {
                return usuario.getId(); // Devolvemos el ID si lo encontramos
            }
        }
        return -1; // Si no encontramos al usuario, devolvemos -1
    }

    private List<Usuario> obtenerUsuariosJson() {
        return List.of(
                new Usuario(1, "Juan Pérez"),
                new Usuario(2, "Ana García")
        );
    }

    public void mostrarHistorial() {
        JFrame ventana = crearVentanaBase("Historial de Compras");

        JPanel panelHistorial = new JPanel();
        panelHistorial.setOpaque(false);
        panelHistorial.setLayout(new BoxLayout(panelHistorial, BoxLayout.Y_AXIS));

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/tienda", "root", "root")) {
            // Consulta para obtener todas las compras
            String query = "SELECT nombre_usuario, nombre_producto FROM historico_compras";
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(query)) {
                while (rs.next()) {
                    String nombreUsuario = rs.getString("nombre_usuario");
                    String nombreProducto = rs.getString("nombre_producto");

                    // Creamos una etiqueta para mostrar cada compra
                    JLabel registro = new JLabel("Usuario: " + nombreUsuario + " - Producto: " + nombreProducto);
                    registro.setFont(new Font("Arial", Font.PLAIN, 14)); // Tamaño y estilo de fuente
                    registro.setForeground(Color.WHITE); // Texto blanco
                    panelHistorial.add(registro); // Añadimos al panel
                    panelHistorial.add(Box.createRigidArea(new Dimension(0, 10))); // Espaciado
                }
            }
        } catch (SQLException e) {
            // Mostramos un mensaje si hay error al cargar el historial
            JLabel errorLabel = new JLabel("Error al cargar el historial: " + e.getMessage());
            errorLabel.setFont(new Font("Arial", Font.PLAIN, 14));
            errorLabel.setForeground(Color.RED); // Texto rojo para el error
            panelHistorial.add(errorLabel);
        }

        JScrollPane scrollPane = new JScrollPane(panelHistorial); // Hacemos el panel desplazable
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        ventana.add(scrollPane, BorderLayout.CENTER); // Añadimos el panel a la ventana
        ventana.setVisible(true); // Mostramos la ventana
    }

    private JFrame crearVentanaBase(String titulo) {
        // Creamos la ventana principal con título
        JFrame ventana = new JFrame(titulo);
        ventana.setSize(800, 600); // Dimensiones fijas
        ventana.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Cerramos al salir
        ventana.setLocationRelativeTo(null); // Centrada en la pantalla
        ventana.setResizable(false); // No se puede redimensionar

        // Fondo de la ventana
        JLabel fondo = new JLabel(new ImageIcon(getClass().getResource("/Fondo1.png")));
        fondo.setLayout(new BorderLayout());
        ventana.setContentPane(fondo); // Establecemos el fondo

        // Barra de navegación
        JPanel barraNavegacion = new JPanel();
        barraNavegacion.setBackground(new Color(30, 30, 30)); // Fondo oscuro
        barraNavegacion.setLayout(new BorderLayout()); // Layout para los componentes
        barraNavegacion.setPreferredSize(new Dimension(800, 50)); // Tamaño fijo

// Logo de la tienda en la barra de navegación
        JLabel logo = new JLabel();
        ImageIcon originalLogo = new ImageIcon(getClass().getResource("/Logo.png"));
        Image imagenEscalada = originalLogo.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH); // Redimensionamos
        logo.setIcon(new ImageIcon(imagenEscalada));
        barraNavegacion.add(logo, BorderLayout.WEST); // Colocamos a la izquierda

// Botones de navegación
        JPanel panelBotones = new JPanel();
        panelBotones.setBackground(new Color(30, 30, 30));
        panelBotones.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10)); // Layout horizontal
        panelBotones.add(crearBotonNav("Inicio"));
        panelBotones.add(crearBotonNav("Usuarios"));
        panelBotones.add(crearBotonNav("Categorías"));
        panelBotones.add(crearBotonNav("Contacto"));
        barraNavegacion.add(panelBotones, BorderLayout.CENTER); // Centramos los botones

        ventana.add(barraNavegacion, BorderLayout.NORTH); // Añadimos la barra de navegación

// Footer (barra inferior)
        JPanel footer = new JPanel();
        footer.setBackground(new Color(30, 30, 30));
        footer.setPreferredSize(new Dimension(800, 30));
        footer.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 5)); // Layout centrado
        JLabel footerLabel = new JLabel("Contacto: smartdevices@techshop.com | Teléfono: +34 658 12 34 56");
        footerLabel.setForeground(Color.WHITE); // Texto blanco
        footerLabel.setFont(new Font("Arial", Font.PLAIN, 12)); // Fuente estándar
        footer.add(footerLabel);
        ventana.add(footer, BorderLayout.SOUTH); // Añadimos el footer

        return ventana; // Devolvemos la ventana creada
    }

    private JButton crearBotonNav(String texto) {
        // Botón básico con estilo para la navegación
        JButton boton = new JButton(texto);
        boton.setBackground(new Color(60, 63, 65)); // Fondo oscuro
        boton.setForeground(Color.WHITE); // Texto blanco
        boton.setFocusPainted(false); // Sin borde al seleccionar
        boton.setFont(new Font("Arial", Font.BOLD, 14)); // Fuente en negrita
        boton.setPreferredSize(new Dimension(120, 30)); // Tamaño fijo
        return boton; // Devolvemos el botón
    }

    public List<String> obtenerHistorial(String nombreUsuario) {
        List<String> historial = new ArrayList<>(); // Creamos una lista para almacenar los nombres de los productos comprados.

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/tienda", "root", "root")) {
            // Establecemos una conexión con la base de datos MySQL, usando el nombre de usuario y contraseña proporcionados.

            String query = "SELECT nombre_producto FROM historico_compras WHERE nombre_usuario = ?";
            // Definimos una consulta SQL para obtener los nombres de los productos comprados por un usuario específico.

            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setString(1, nombreUsuario);
                // Establecemos el valor del parámetro en la consulta SQL (el nombre del usuario).

                try (ResultSet rs = stmt.executeQuery()) {
                    // Ejecutamos la consulta y obtenemos los resultados en un objeto ResultSet.

                    while (rs.next()) {
                        historial.add(rs.getString("nombre_producto"));
                        // Por cada fila de resultados, obtenemos el nombre del producto y lo añadimos a la lista `historial`.
                    }
                }
            }
        } catch (SQLException e) {
            // Si ocurre algún error al interactuar con la base de datos, lanzamos una excepción con el mensaje correspondiente.
            throw new RuntimeException("Error al obtener el historial: " + e.getMessage());
        }

        return historial;
        // Devolvemos la lista con los nombres de los productos comprados por el usuario.
    }

    // Clase simple para representar un usuario
    private static class Usuario {
        private final int id; // ID único del usuario
        private final String nombre; // Nombre del usuario

        public Usuario(int id, String nombre) {
            this.id = id; // Inicializamos el ID
            this.nombre = nombre; // Inicializamos el nombre
        }

        public int getId() {
            return id; // Obtenemos el ID
        }

        public String getNombre() {
            return nombre; // Obtenemos el nombre
        }
    }

}
