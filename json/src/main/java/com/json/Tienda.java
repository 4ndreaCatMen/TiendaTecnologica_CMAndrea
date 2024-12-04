package com.json;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.io.InputStream;

public class Tienda {
    public static void main(String[] args)throws IOException {
        InputStream inputStream;
        // Configuración básica de la ventana
        JFrame ventana = new JFrame("Mi Tienda Online");
        ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ventana.setSize(800, 500);
        ventana.setResizable(false); // Tamaño fijo
        ventana.setLayout(null);

        // Panel principal para contener el fondo y demás elementos
        JPanel panelFondo = new JPanel();
        panelFondo.setLayout(null);
        panelFondo.setBounds(0, 0, 800, 500);

        // Imagen de fondo
        JLabel fondo = new JLabel();
        inputStream = Tienda.class.getResourceAsStream("/Fondo1.png");
        ImageIcon imagenFondo = new ImageIcon(inputStream.readAllBytes()); // Cambia a la ruta de tu imagen
        Image imagenEscalada = imagenFondo.getImage().getScaledInstance(800, 500, Image.SCALE_SMOOTH); // Ajustar al tamaño de la ventana
        fondo.setIcon(new ImageIcon(imagenEscalada));
        fondo.setBounds(0, 0, 800, 500);

        // Barra de navegación superior
        JPanel barraNavegacion = new JPanel();
        barraNavegacion.setLayout(new FlowLayout(FlowLayout.LEFT, 20, 10));
        barraNavegacion.setBackground(new Color(33, 33, 33, 200)); // Con transparencia (RGBA)
        barraNavegacion.setBounds(0, 0, 800, 50);

        // Logo en la barra de navegación (ajustado)
        inputStream = Tienda.class.getResourceAsStream("/Logo.png");
        ImageIcon iconoOriginal = new ImageIcon(inputStream.readAllBytes()); // Cambia a la ruta de tu logo
        Image iconoRedimensionado = iconoOriginal.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH); // Ajustar tamaño
        JLabel logo = new JLabel(new ImageIcon(iconoRedimensionado));
        barraNavegacion.add(logo);

        // Botones de la barra de navegación
        JButton botonInicio = crearBotonNavegacion("Inicio");
        JButton botonUsuarios = crearBotonNavegacion("Usuarios");
        JButton botonProductos = crearBotonNavegacion("Categorías");
        JButton botonContacto = crearBotonNavegacion("Contacto");

        // Acciones de los botones
        botonUsuarios.addActionListener(e -> {
            Tienda tienda = new Tienda();
            tienda.mostrarUsuarios();
        });

        botonProductos.addActionListener(e -> {
            Tienda tienda = new Tienda();
            tienda.mostrarCategorias();
        });

        barraNavegacion.add(botonInicio);
        barraNavegacion.add(botonUsuarios);
        barraNavegacion.add(botonProductos);
        barraNavegacion.add(botonContacto);

        // Texto principal (reposicionado a la izquierda)
        JLabel textoPrincipal = new JLabel("<html>Bienvenidxs a Tecnología:<br>Smart Devices</html>");
        textoPrincipal.setFont(new Font("Arial", Font.BOLD, 26));
        textoPrincipal.setForeground(Color.WHITE);
        textoPrincipal.setBounds(50, 160, 400, 60); // Posicionado a la izquierda y con un ancho específico

        // Texto descriptivo (alineado debajo del texto principal)
        JLabel textoDescripcion = new JLabel("<html>Explora los mejores productos tecnológicos y descubre las últimas tendencias en innovación.</html>");
        textoDescripcion.setFont(new Font("Arial", Font.PLAIN, 16));
        textoDescripcion.setForeground(Color.WHITE);
        textoDescripcion.setBounds(50, 240, 400, 60); // Más a la izquierda y centrado debajo del principal

        // Footer (Barra inferior)
        JPanel footer = new JPanel();
        footer.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10)); // Footer y contenido
        footer.setBackground(new Color(33, 33, 33, 200)); // Con transparencia (RGBA)
        footer.setBounds(0, 430, 800, 40);

        // Texto en el footer
        JLabel textoFooter = new JLabel("Contacto: smartdevices@techshop.com | Teléfono: +34 658 12 34 56");
        textoFooter.setFont(new Font("Arial", Font.PLAIN, 12));
        textoFooter.setForeground(Color.WHITE);
        footer.add(textoFooter);

        // Añadir componentes al panel de fondo
        panelFondo.add(barraNavegacion);
        panelFondo.add(textoPrincipal);
        panelFondo.add(textoDescripcion);
        panelFondo.add(footer);

        // Añadir la imagen de fondo detrás de todo
        panelFondo.add(fondo);
        fondo.setBounds(0, 0, 800, 500);

        // Añadir el panel de fondo a la ventana
        ventana.add(panelFondo);

        // Hacer visible la ventana
        ventana.setLocationRelativeTo(null); // Centrar en la pantalla
        ventana.setVisible(true);
    }

    // Método para crear botones estilizados para la barra de navegación
    private static JButton crearBotonNavegacion(String texto) {
        JButton boton = new JButton(texto);
        boton.setFont(new Font("Arial", Font.BOLD, 14));
        boton.setForeground(Color.WHITE);
        boton.setBackground(new Color(45, 45, 45));
        boton.setFocusPainted(false);
        boton.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        return boton;
    }

    // Métodos para las acciones de los botones
    private void mostrarUsuarios() {
        // Aquí se abre la ventana Usuarios
        new Usuarios().mostrarVentana();
    }

    private void mostrarCategorias() {
        // Aquí se abre la ventana Productos
        new Categorias().mostrarVentana();
    }
}
