package com.json;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.io.InputStream;

public class Tienda {
    public static void main(String[] args) throws IOException {
        InputStream inputStream;

        // Configuración básica de la ventana principal
        JFrame ventana = new JFrame("Mi Tienda Online");
        ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Cierra el programa al cerrar la ventana
        ventana.setSize(800, 500); // Tamaño de la ventana
        ventana.setResizable(false); // Evita que el usuario redimensione la ventana
        ventana.setLayout(null); // Usamos layout nulo para controlar las posiciones manualmente

        // Panel principal que contendrá todos los elementos de la ventana
        JPanel panelFondo = new JPanel();
        panelFondo.setLayout(null);
        panelFondo.setBounds(0, 0, 800, 500);

        // Imagen de fondo de la ventana
        JLabel fondo = new JLabel();
        inputStream = Tienda.class.getResourceAsStream("/Fondo1.png"); // Carga la imagen del fondo desde los recursos
        ImageIcon imagenFondo = new ImageIcon(inputStream.readAllBytes()); // Convierte la imagen a un ícono
        Image imagenEscalada = imagenFondo.getImage().getScaledInstance(800, 500, Image.SCALE_SMOOTH); // Ajusta la imagen al tamaño de la ventana
        fondo.setIcon(new ImageIcon(imagenEscalada));
        fondo.setBounds(0, 0, 800, 500); // Posiciona y redimensiona el fondo

        // Barra de navegación superior
        JPanel barraNavegacion = new JPanel();
        barraNavegacion.setLayout(new FlowLayout(FlowLayout.LEFT, 20, 10)); // Layout para alinear los botones
        barraNavegacion.setBackground(new Color(33, 33, 33, 200)); // Color oscuro con transparencia
        barraNavegacion.setBounds(0, 0, 800, 50); // Ocupa la parte superior de la ventana

        // Logo de la barra de navegación
        inputStream = Tienda.class.getResourceAsStream("/Logo.png"); // Carga el logo desde los recursos
        ImageIcon iconoOriginal = new ImageIcon(inputStream.readAllBytes());
        Image iconoRedimensionado = iconoOriginal.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH); // Ajusta el tamaño del logo
        JLabel logo = new JLabel(new ImageIcon(iconoRedimensionado));
        barraNavegacion.add(logo); // Añade el logo a la barra

        // Botones de navegación
        JButton botonInicio = crearBotonNavegacion("Inicio"); // Botón de "Inicio"
        JButton botonUsuarios = crearBotonNavegacion("Usuarios"); // Botón para abrir la ventana de Usuarios
        JButton botonProductos = crearBotonNavegacion("Categorías"); // Botón para abrir la ventana de Categorías
        JButton botonContacto = crearBotonNavegacion("Contacto"); // Botón de "Contacto"

        // Acciones para los botones de navegación
        botonUsuarios.addActionListener(e -> {
            Tienda tienda = new Tienda();
            tienda.mostrarUsuarios(); // Llama al método que abre la ventana Usuarios
        });

        botonProductos.addActionListener(e -> {
            Tienda tienda = new Tienda();
            tienda.mostrarCategorias(); // Llama al método que abre la ventana Categorías
        });

        // Añadir botones a la barra de navegación
        barraNavegacion.add(botonInicio);
        barraNavegacion.add(botonUsuarios);
        barraNavegacion.add(botonProductos);
        barraNavegacion.add(botonContacto);

        // Texto principal, un mensaje de bienvenida
        JLabel textoPrincipal = new JLabel("<html>Bienvenides a Tecnología:<br>Smart Devices</html>");
        textoPrincipal.setFont(new Font("Arial", Font.BOLD, 26)); // Fuente grande y en negrita
        textoPrincipal.setForeground(Color.WHITE); // Texto en blanco
        textoPrincipal.setBounds(50, 160, 400, 60); // Posicionado en la esquina superior izquierda

        // Texto descriptivo debajo del mensaje principal
        JLabel textoDescripcion = new JLabel("<html>Explora los mejores productos tecnológicos y descubre las últimas tendencias en innovación.</html>");
        textoDescripcion.setFont(new Font("Arial", Font.PLAIN, 16)); // Fuente más pequeña y normal
        textoDescripcion.setForeground(Color.WHITE);
        textoDescripcion.setBounds(50, 240, 400, 60); // Posicionado debajo del mensaje principal

        // Footer en la parte inferior de la ventana
        JPanel footer = new JPanel();
        footer.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10)); // Centrado
        footer.setBackground(new Color(33, 33, 33, 200)); // Color oscuro con transparencia
        footer.setBounds(0, 430, 800, 40); // Ocupa toda la parte inferior

        // Texto del footer
        JLabel textoFooter = new JLabel("Contacto: smartdevices@techshop.com | Teléfono: +34 658 12 34 56");
        textoFooter.setFont(new Font("Arial", Font.PLAIN, 12)); // Fuente pequeña
        textoFooter.setForeground(Color.WHITE); // Texto blanco
        footer.add(textoFooter); // Añade el texto al footer

        // Añadir todos los elementos al panel de fondo
        panelFondo.add(barraNavegacion); // Añade la barra de navegación
        panelFondo.add(textoPrincipal); // Añade el mensaje principal
        panelFondo.add(textoDescripcion); // Añade el texto descriptivo
        panelFondo.add(footer); // Añade el footer

        // Añade el fondo detrás de todo
        panelFondo.add(fondo);

        // Configuración final de la ventana
        ventana.add(panelFondo); // Añade el panel principal a la ventana
        ventana.setLocationRelativeTo(null); // Centra la ventana en la pantalla
        ventana.setVisible(true); // Hace visible la ventana
    }

    // Método para crear botones estilizados para la barra de navegación
    private static JButton crearBotonNavegacion(String texto) {
        JButton boton = new JButton(texto);
        boton.setFont(new Font("Arial", Font.BOLD, 14)); // Fuente de los botones
        boton.setForeground(Color.WHITE); // Texto blanco
        boton.setBackground(new Color(45, 45, 45)); // Fondo gris oscuro
        boton.setFocusPainted(false); // Elimina el efecto de enfoque
        boton.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15)); // Espaciado interno
        return boton;
    }

    // Métodos para mostrar las ventanas de Usuarios y Categorías
    private void mostrarUsuarios() {
        new Usuarios().mostrarVentana(); // Abre la ventana Usuarios
    }

    private void mostrarCategorias() {
        new Categorias().mostrarVentana(); // Abre la ventana Categorías
    }
}

