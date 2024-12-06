package com.json;

import com.google.gson.reflect.TypeToken;
import com.google.gson.Gson;
import javax.swing.*;
import java.awt.*;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

// Clase principal que maneja la lista de usuarios y su visualización
public class Usuarios {
    private List<String> listaUsuarios; // Lista con los nombres de los usuarios extraídos del JSON
    private Map<String, Object> tiendaData; // Mapa que contiene toda la información del JSON

    public Usuarios() {
        // Al instanciar la clase, cargamos los datos del JSON y preparamos la lista de usuarios
        tiendaData = cargarJsonData();
        listaUsuarios = obtenerUsuariosJson();
    }

    // Método que carga el archivo JSON y lo convierte en un mapa de datos
    private Map<String, Object> cargarJsonData() {
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("info.json");
        Gson gson = new Gson();
        Map<String, Object> data = null;

        try (InputStreamReader reader = new InputStreamReader(inputStream)) {
            // Usamos Gson para transformar el archivo JSON en un mapa genérico
            Type type = new TypeToken<Map<String, Object>>() {}.getType();
            data = gson.fromJson(reader, type);
        } catch (Exception e) {
            e.printStackTrace(); // Mostramos el error si ocurre
        }

        return data; // Retornamos el mapa con los datos
    }

    // Método que obtiene los nombres de los usuarios del JSON
    private List<String> obtenerUsuariosJson() {
        List<String> nombresUsuarios = new ArrayList<>();
        if (tiendaData != null) {
            // Extraemos la sección "usuarios" dentro de la sección "tienda"
            Map<String, Object> tienda = (Map<String, Object>) tiendaData.get("tienda");
            List<Map<String, Object>> usuarios = (List<Map<String, Object>>) tienda.get("usuarios");

            // Recorremos la lista de usuarios y obtenemos sus nombres
            for (Map<String, Object> usuario : usuarios) {
                String nombre = (String) usuario.get("nombre");
                nombresUsuarios.add(nombre); // Añadimos el nombre a nuestra lista
            }
        }
        return nombresUsuarios; // Retornamos la lista de nombres
    }

    // Método principal para mostrar la ventana de usuarios
    public void mostrarVentana() {
        JFrame ventana = new JFrame("Usuarios"); // Creamos la ventana principal
        ventana.setSize(800, 600); // Tamaño fijo
        ventana.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Acción al cerrar la ventana
        ventana.setLocationRelativeTo(null); // Centramos la ventana en la pantalla
        ventana.setResizable(false); // Evitamos que el usuario pueda redimensionar la ventana

        // Fondo de la ventana
        JLabel fondo = new JLabel(new ImageIcon(getClass().getResource("/Fondo1.png")));
        fondo.setLayout(new BorderLayout());
        ventana.setContentPane(fondo); // Añadimos el fondo a la ventana

        // Barra de navegación en la parte superior
        JPanel barraNavegacion = new JPanel();
        barraNavegacion.setBackground(new Color(30, 30, 30)); // Fondo oscuro
        barraNavegacion.setLayout(new BorderLayout());
        barraNavegacion.setPreferredSize(new Dimension(800, 50)); // Tamaño fijo

        // Logo dentro de la barra de navegación
        JLabel logo = new JLabel();
        ImageIcon originalLogo = new ImageIcon(getClass().getResource("/Logo.png"));
        Image imagenEscalada = originalLogo.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH); // Ajustamos el tamaño
        logo.setIcon(new ImageIcon(imagenEscalada));
        barraNavegacion.add(logo, BorderLayout.WEST); // Colocamos el logo a la izquierda

        // Panel con botones de navegación
        JPanel panelBotones = new JPanel();
        panelBotones.setBackground(new Color(30, 30, 30)); // Fondo oscuro
        panelBotones.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10)); // Botones alineados a la izquierda
        panelBotones.add(crearBotonNav("Inicio"));
        panelBotones.add(crearBotonNav("Usuarios"));
        panelBotones.add(crearBotonNav("Categorías"));
        panelBotones.add(crearBotonNav("Contacto"));
        barraNavegacion.add(panelBotones, BorderLayout.CENTER);

        ventana.add(barraNavegacion, BorderLayout.NORTH); // Añadimos la barra a la ventana

        // Footer (barra inferior)
        JPanel footer = new JPanel();
        footer.setBackground(new Color(30, 30, 30)); // Fondo oscuro
        footer.setPreferredSize(new Dimension(800, 30));
        footer.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 5)); // Contenido centrado
        JLabel footerLabel = new JLabel("Contacto: smartdevices@techshop.com | Teléfono: +34 658 12 34 56");
        footerLabel.setForeground(Color.WHITE); // Texto blanco
        footerLabel.setFont(new Font("Arial", Font.PLAIN, 12)); // Fuente pequeña
        footer.add(footerLabel);
        ventana.add(footer, BorderLayout.SOUTH); // Añadimos el footer

        // Panel principal con la lista de usuarios
        JPanel panelUsuarios = crearPanelUsuarios();
        panelUsuarios.setOpaque(false); // Transparente para que el fondo se vea
        JScrollPane scrollPane = new JScrollPane(panelUsuarios); // Hacemos la lista desplazable
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        ventana.add(scrollPane, BorderLayout.CENTER); // Añadimos la lista al centro de la ventana

        ventana.setVisible(true); // Mostramos la ventana
    }

    // Método que crea los paneles de usuarios
    private JPanel crearPanelUsuarios() {
        JPanel panelUsuarios = new JPanel();
        panelUsuarios.setLayout(new GridBagLayout()); // Centramos los elementos
        panelUsuarios.setOpaque(false);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = GridBagConstraints.RELATIVE; // Cada elemento en una nueva fila
        gbc.insets = new Insets(10, 0, 10, 0); // Espaciado vertical

        // Por cada usuario, creamos un panel con sus botones
        for (String usuario : listaUsuarios) {
            JPanel panelUsuario = new JPanel();
            panelUsuario.setLayout(new BorderLayout());
            panelUsuario.setOpaque(false);
            panelUsuario.setPreferredSize(new Dimension(400, 50));

            JButton botonUsuario = new JButton(usuario); // Botón principal con el nombre del usuario
            botonUsuario.setBackground(Color.DARK_GRAY);
            botonUsuario.setForeground(Color.WHITE);
            botonUsuario.setFocusPainted(false);
            botonUsuario.setFont(new Font("Arial", Font.BOLD, 16));
            botonUsuario.setPreferredSize(new Dimension(250, 40));
            String detalles = buscarDetallesUsuarioJson(usuario); // Obtenemos los detalles del usuario
            botonUsuario.addActionListener(e -> mostrarDetallesUsuario(usuario, detalles));

            JButton botonHistorial = new JButton("Historial"); // Botón para ver el historial
            botonHistorial.setBackground(new Color(60, 63, 65));
            botonHistorial.setForeground(Color.WHITE);
            botonHistorial.setFocusPainted(false);
            botonHistorial.setFont(new Font("Arial", Font.BOLD, 14));
            botonHistorial.setPreferredSize(new Dimension(120, 40));
            botonHistorial.addActionListener(e -> mostrarHistorialUsuario(usuario));

            panelUsuario.add(botonUsuario, BorderLayout.WEST); // Botón a la izquierda
            panelUsuario.add(botonHistorial, BorderLayout.EAST); // Botón a la derecha

            panelUsuarios.add(panelUsuario, gbc); // Añadimos el panel al contenedor
        }

        return panelUsuarios;
    }

    private void mostrarHistorialUsuario(String nombreUsuario) {
        HistorialCompra historialCompra = new HistorialCompra();
        List<String> historial = historialCompra.obtenerHistorial(nombreUsuario);
        // Creamos una instancia de `HistorialCompra` para obtener el historial del usuario.
        // Usamos el método `obtenerHistorial` para obtener la lista de productos comprados.

        JFrame ventanaHistorial = new JFrame("Historial de Compras - " + nombreUsuario);
        ventanaHistorial.setSize(800, 600);
        ventanaHistorial.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        ventanaHistorial.setLocationRelativeTo(null); // Centra la ventana en la pantalla.
        ventanaHistorial.setResizable(false); // Evita que se pueda redimensionar.
        // Configuramos una nueva ventana para mostrar el historial del usuario.

        JLabel fondo = new JLabel(new ImageIcon(getClass().getResource("/Fondo1.png")));
        fondo.setLayout(new BorderLayout());
        ventanaHistorial.setContentPane(fondo);
        // Añadimos un fondo visual a la ventana usando una imagen.

        JLabel encabezado = new JLabel("Historial de Compras de " + nombreUsuario, SwingConstants.CENTER);
        encabezado.setFont(new Font("Arial", Font.BOLD, 16)); // Fuente grande y en negrita.
        encabezado.setForeground(Color.WHITE); // Texto blanco para que sea visible sobre el fondo.
        ventanaHistorial.add(encabezado, BorderLayout.NORTH);
        // Creamos un encabezado con el nombre del usuario para mostrarlo en la parte superior de la ventana.

        JPanel panelHistorial = new JPanel();
        panelHistorial.setLayout(new BoxLayout(panelHistorial, BoxLayout.Y_AXIS));
        panelHistorial.setOpaque(false);
        // Creamos un panel para mostrar el historial de productos.
        // Usamos un layout vertical para apilar los productos uno debajo del otro.

        if (historial.isEmpty()) {
            JLabel sinCompras = new JLabel("No se encontraron compras para este usuario.", SwingConstants.CENTER);
            sinCompras.setFont(new Font("Arial", Font.ITALIC, 14)); // Fuente cursiva para el mensaje.
            sinCompras.setForeground(Color.WHITE); // Texto blanco.
            sinCompras.setAlignmentX(Component.CENTER_ALIGNMENT); // Centrado horizontal.
            panelHistorial.add(sinCompras);
            // Si el historial está vacío, mostramos un mensaje indicando que no hay compras registradas.
        } else {
            for (String producto : historial) {
                JLabel labelProducto = new JLabel("Producto: " + producto, SwingConstants.CENTER);
                labelProducto.setFont(new Font("Arial", Font.PLAIN, 14)); // Fuente estándar para cada producto.
                labelProducto.setForeground(Color.WHITE); // Texto blanco.
                labelProducto.setAlignmentX(Component.CENTER_ALIGNMENT); // Centrado horizontal.
                panelHistorial.add(labelProducto);
                panelHistorial.add(Box.createRigidArea(new Dimension(0, 10))); // Añadimos un espacio entre productos.
                // Recorremos el historial y creamos una etiqueta para cada producto comprado.
            }
        }

        JScrollPane scrollPane = new JScrollPane(panelHistorial);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        ventanaHistorial.add(scrollPane, BorderLayout.CENTER);
        // Hacemos el panel desplazable para manejar casos donde haya muchos productos.

        ventanaHistorial.setVisible(true);
        // Mostramos la ventana de historial.
    }



    private JButton crearBotonNav(String texto) {
        JButton boton = new JButton(texto);
        boton.setBackground(new Color(60, 63, 65)); // Fondo oscuro
        boton.setForeground(Color.WHITE); // Texto blanco
        boton.setFocusPainted(false); // Quitamos el borde de selección al hacer clic
        boton.setFont(new Font("Arial", Font.BOLD, 14)); // Fuente negrita para mayor visibilidad
        boton.setPreferredSize(new Dimension(120, 30)); // Tamaño fijo del botón
        return boton; // Retornamos el botón ya configurado
    }

    private String buscarDetallesUsuarioJson(String usuario) {
        // Iniciamos con un mensaje predeterminado
        String detalles = "Detalles no encontrados";
        if (tiendaData != null) { // Verificamos que los datos del JSON estén cargados
            Map<String, Object> tienda = (Map<String, Object>) tiendaData.get("tienda");
            List<Map<String, Object>> usuarios = (List<Map<String, Object>>) tienda.get("usuarios");

            // Recorremos la lista de usuarios para buscar al usuario indicado
            for (Map<String, Object> usuarioMap : usuarios) {
                String nombreUsuario = (String) usuarioMap.get("nombre");
                if (nombreUsuario.equals(usuario)) { // Comparamos los nombres
                    String email = (String) usuarioMap.get("email");
                    Map<String, Object> direccion = (Map<String, Object>) usuarioMap.get("direccion");

                    // Construimos un mensaje con los detalles del usuario
                    detalles = "Email: " + email + "\n";
                    detalles += "Dirección: " + direccion.get("calle") + " " + direccion.get("numero") + ", "
                            + direccion.get("ciudad") + ", " + direccion.get("pais");
                    break; // Salimos del bucle al encontrar al usuario
                }
            }
        }
        return detalles; // Retornamos los detalles encontrados

    }

    private void mostrarDetallesUsuario(String usuario, String detalles) {
// Usamos JOptionPane para mostrar un mensaje con los detalles del usuario
        JOptionPane.showMessageDialog(null, "Detalles de " + usuario + "\n" + detalles);
    }
}
