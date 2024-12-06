package com.json;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import javax.swing.*;
import java.awt.*;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Categorias {

    // Lista que almacena todas las categorías, cargadas desde un archivo JSON
    private final List<Categoria> listaCategorias = obtenerCategoriasJson();

    // Método para cargar las categorías desde el JSON
    private List<Categoria> obtenerCategoriasJson() {
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("info.json"); // Abre el archivo JSON
        List<Categoria> categorias = new ArrayList<>();

        try {
            // Convertimos el archivo JSON a un mapa utilizando Gson
            InputStreamReader reader = new InputStreamReader(inputStream);
            Map<String, Object> data = new Gson().fromJson(reader, Map.class); // Convierte el JSON en un mapa de datos
            Map<String, Object> tienda = (Map<String, Object>) data.get("tienda"); // Accede a la clave "tienda"
            List<Map<String, Object>> categoriasJson = (List<Map<String, Object>>) tienda.get("categorias"); // Obtiene la lista de categorías

            // Recorremos la lista de categorías para convertirla en objetos de la clase `Categoria`
            for (Map<String, Object> categoriaJson : categoriasJson) {
                String nombre = (String) categoriaJson.get("nombre"); // Obtenemos el nombre de la categoría
                List<Map<String, Object>> productos = (List<Map<String, Object>>) categoriaJson.get("productos"); // Obtenemos los productos
                categorias.add(new Categoria(nombre, productos)); // Añadimos una nueva categoría a la lista
            }
        } catch (Exception e) {
            e.printStackTrace(); // Mostramos el error si algo falla al leer el JSON
        }

        return categorias; // Devolvemos la lista de categorías
    }

    // Método para mostrar la ventana principal de categorías
    public void mostrarVentana() {
        JFrame ventana = crearVentanaBase("Categorías"); // Crea una ventana base con el título "Categorías"

        // Panel donde se mostrarán los botones de las categorías
        JPanel panelCategorias = new JPanel();
        panelCategorias.setOpaque(false); // Permite ver el fondo de la ventana
        panelCategorias.setLayout(new GridBagLayout()); // Centra los botones
        GridBagConstraints gbc = new GridBagConstraints(); // Configuración para posicionar los botones
        gbc.insets = new Insets(10, 0, 10, 0); // Espaciado entre botones
        gbc.gridx = 0; // Colocamos los botones en una única columna
        gbc.gridy = GridBagConstraints.RELATIVE; // Cada botón en una fila nueva

        // Recorremos la lista de categorías y creamos un botón para cada una
        for (Categoria categoria : listaCategorias) {
            JButton botonCategoria = new JButton(categoria.getNombre()); // El texto del botón será el nombre de la categoría
            botonCategoria.setFont(new Font("Arial", Font.PLAIN, 16)); // Estilo y tamaño de fuente
            botonCategoria.setBackground(Color.DARK_GRAY); // Fondo oscuro
            botonCategoria.setForeground(Color.WHITE); // Texto blanco
            botonCategoria.setFocusPainted(false); // Quita el borde de enfoque
            botonCategoria.setPreferredSize(new Dimension(300, 50)); // Tamaño del botón

            // Acción al hacer clic: mostrar los productos de la categoría
            botonCategoria.addActionListener(e -> {
                try {
                    mostrarProductos(categoria); // Muestra la ventana de productos de esa categoría
                } catch (Exception ex) {
                    throw new RuntimeException(ex); // Manejamos cualquier error
                }
            });

            // Añadimos el botón al panel de categorías
            panelCategorias.add(botonCategoria, gbc);
        }

        // Hacemos el panel desplazable en caso de muchas categorías
        JScrollPane scrollPane = new JScrollPane(panelCategorias);
        scrollPane.setOpaque(false); // Transparente
        scrollPane.getViewport().setOpaque(false);
        ventana.add(scrollPane, BorderLayout.CENTER); // Añadimos el scroll al centro de la ventana

        ventana.setVisible(true); // Mostramos la ventana
    }

    // Método para crear una ventana base (con fondo, barra de navegación y footer)
    private JFrame crearVentanaBase(String titulo) {
        JFrame ventana = new JFrame(titulo); // Creamos la ventana con el título recibido como parámetro
        ventana.setSize(800, 600); // Tamaño fijo
        ventana.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Cierra la ventana al salir
        ventana.setLocationRelativeTo(null); // Centra la ventana en la pantalla
        ventana.setResizable(false); // Evita que el usuario redimensione la ventana

        // Fondo de la ventana
        JLabel fondo = new JLabel(new ImageIcon(getClass().getResource("/Fondo1.png"))); // Cargamos el fondo desde los recursos
        fondo.setLayout(new BorderLayout()); // Permite agregar elementos sobre el fondo
        ventana.setContentPane(fondo); // Establecemos el fondo como el contenido principal de la ventana

        // Barra de navegación superior
        JPanel barraNavegacion = new JPanel();
        barraNavegacion.setBackground(new Color(30, 30, 30)); // Color oscuro
        barraNavegacion.setLayout(new BorderLayout()); // Organizamos los elementos en la barra
        barraNavegacion.setPreferredSize(new Dimension(800, 50)); // Tamaño fijo

        // Logo en la barra de navegación
        JLabel logo = new JLabel();
        ImageIcon originalLogo = new ImageIcon(getClass().getResource("/Logo.png")); // Cargamos el logo desde los recursos
        Image imagenEscalada = originalLogo.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH); // Ajustamos el tamaño
        logo.setIcon(new ImageIcon(imagenEscalada)); // Establecemos el logo como ícono
        barraNavegacion.add(logo, BorderLayout.WEST); // Añadimos el logo a la izquierda

        // Panel para los botones de navegación
        JPanel panelBotones = new JPanel();
        panelBotones.setBackground(new Color(30, 30, 30)); // Fondo oscuro
        panelBotones.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10)); // Posicionamos los botones horizontalmente
        panelBotones.add(crearBotonNav("Inicio")); // Botón de inicio
        panelBotones.add(crearBotonNav("Usuarios")); // Botón de usuarios
        panelBotones.add(crearBotonNav("Categorías")); // Botón de categorías
        panelBotones.add(crearBotonNav("Contacto")); // Botón de contacto
        barraNavegacion.add(panelBotones, BorderLayout.CENTER); // Añadimos los botones al centro de la barra

        ventana.add(barraNavegacion, BorderLayout.NORTH); // Añadimos la barra en la parte superior de la ventana

        // Footer (barra inferior)
        JPanel footer = new JPanel();
        footer.setBackground(new Color(30, 30, 30)); // Fondo oscuro
        footer.setPreferredSize(new Dimension(800, 30)); // Tamaño fijo
        footer.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 5)); // Contenido centrado
        JLabel footerLabel = new JLabel("Contacto: smartdevices@techshop.com | Teléfono: +34 658 12 34 56"); // Texto del footer
        footerLabel.setForeground(Color.WHITE); // Texto blanco
        footerLabel.setFont(new Font("Arial", Font.PLAIN, 12)); // Fuente pequeña
        footer.add(footerLabel); // Añadimos el texto al footer
        ventana.add(footer, BorderLayout.SOUTH); // Añadimos el footer a la parte inferior de la ventana

        return ventana; // Devolvemos la ventana base
    }

    private JButton crearBotonNav(String texto) {
        // Este método crea botones estilizados para la barra de navegación
        JButton boton = new JButton(texto); // Creamos el botón con el texto recibido como parámetro
        boton.setBackground(new Color(60, 63, 65)); // Color de fondo gris oscuro
        boton.setForeground(Color.WHITE); // Texto en blanco
        boton.setFocusPainted(false); // Eliminamos el borde de enfoque que aparece al seleccionarlo
        boton.setFont(new Font("Arial", Font.BOLD, 14)); // Estilizamos la fuente con negrita y tamaño 14
        boton.setPreferredSize(new Dimension(120, 30)); // Definimos un tamaño fijo para el botón
        return boton; // Devolvemos el botón ya configurado
    }

    private void mostrarProductos(Categoria categoria) throws Exception {
        // Este método muestra una ventana con los productos de una categoría específica
        JFrame ventanaProductos = crearVentanaBase("Productos - " + categoria.getNombre()); // Creamos una ventana base

        // Creamos un panel para listar los productos
        JPanel panelProductos = new JPanel();
        panelProductos.setLayout(new BoxLayout(panelProductos, BoxLayout.Y_AXIS)); // Organizamos los productos en una lista vertical
        panelProductos.setOpaque(false); // Hacemos el fondo del panel transparente

        // Recorremos la lista de productos de la categoría
        for (Map<String, Object> producto : categoria.getProductos()) {
            String nombreProducto = (String) producto.get("nombre"); // Obtenemos el nombre del producto
            List<String> imagenes = (List<String>) producto.get("imagenes"); // Obtenemos la lista de imágenes
            String imagenPath = imagenes.get(0); // Tomamos la primera imagen (siempre hay al menos una)

            // Creamos un panel individual para cada producto
            JPanel panelProducto = new JPanel();
            panelProducto.setLayout(new BorderLayout()); // Usamos un layout para posicionar los elementos
            panelProducto.setOpaque(false); // Fondo transparente
            panelProducto.setPreferredSize(new Dimension(150, 200)); // Definimos el tamaño del panel

            // Creamos un botón para mostrar el producto
            JButton botonProducto = new JButton(nombreProducto); // El texto del botón será el nombre del producto
            InputStream inputStream = Categorias.class.getResourceAsStream("/" + imagenPath); // Cargamos la imagen
            if (inputStream != null) {
                ImageIcon iconoProducto = new ImageIcon(inputStream.readAllBytes()); // Leemos y convertimos la imagen
                Image imagenRedimensionada = iconoProducto.getImage().getScaledInstance(60, 60, Image.SCALE_SMOOTH); // Redimensionamos la imagen
                botonProducto.setIcon(new ImageIcon(imagenRedimensionada)); // Añadimos la imagen al botón
            }

            botonProducto.setFont(new Font("Arial", Font.PLAIN, 12)); // Fuente más pequeña y simple
            botonProducto.setBackground(Color.DARK_GRAY); // Fondo oscuro
            botonProducto.setForeground(Color.WHITE); // Texto blanco
            botonProducto.setFocusPainted(false); // Sin borde de enfoque
            botonProducto.setHorizontalAlignment(SwingConstants.CENTER); // Centramos el texto y el ícono
            botonProducto.setPreferredSize(new Dimension(140, 100)); // Definimos un tamaño fijo
            botonProducto.addActionListener(e -> mostrarDetallesProducto(producto)); // Acción al hacer clic: mostrar detalles

            // Añadimos el botón al panel individual
            panelProducto.add(botonProducto, BorderLayout.CENTER);

            // Añadimos el panel individual al panel general
            panelProductos.add(Box.createRigidArea(new Dimension(0, 10))); // Espaciado entre productos
            panelProductos.add(panelProducto);
        }

        // Añadimos un scroll al panel para manejar muchas categorías
        JScrollPane scrollPane = new JScrollPane(panelProductos);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        ventanaProductos.add(scrollPane, BorderLayout.CENTER);

        ventanaProductos.setVisible(true); // Mostramos la ventana
    }
    private void mostrarDetallesProducto(Map<String, Object> producto) {
        // Este método muestra una ventana emergente con los detalles del producto
        String nombreProducto = (String) producto.get("nombre"); // Obtenemos el nombre
        double precio = (Double) producto.get("precio"); // Obtenemos el precio
        String descripcion = (String) producto.get("descripcion"); // Obtenemos la descripción

        // Construimos un string HTML para mostrar los detalles de forma ordenada
        String detalles = "<html><strong>Nombre:</strong> " + nombreProducto + "<br>" +
                "<strong>Precio:</strong> " + precio + " €<br>" +
                "<strong>Descripción:</strong> " + descripcion + "</html>";

        // Mostramos los detalles en un cuadro de diálogo
        JOptionPane.showMessageDialog(null, detalles, "Detalles del Producto", JOptionPane.INFORMATION_MESSAGE);

        // Preguntamos al usuario si desea comprar el producto
        int opcion = JOptionPane.showOptionDialog(
                null,
                "¿Deseas comprar este producto?",
                "Comprar Producto",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                new String[]{"Comprar", "Cancelar"}, // Opciones de botones
                "Comprar");

        if (opcion == JOptionPane.YES_OPTION) {
            seleccionarUsuarioParaCompra(producto); // Si elige comprar, abrimos la ventana para seleccionar un usuario
        }
    }

    private void seleccionarUsuarioParaCompra(Map<String, Object> producto) {
        // Este método permite seleccionar un usuario para realizar la compra
        List<String> listaUsuarios = obtenerUsuariosJson(); // Obtenemos la lista de usuarios desde el JSON

        // Si no hay usuarios disponibles, mostramos un error
        if (listaUsuarios.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No hay usuarios disponibles para realizar la compra.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Mostramos un cuadro de diálogo para seleccionar al usuario
        procesarCompra(producto.get("nombre").toString()); // Procesamos la compra con el producto seleccionado
    }

    private List<String> obtenerUsuariosJson() {
        // Este método lee el archivo JSON y devuelve una lista de nombres de usuarios
        List<String> nombresUsuarios = new ArrayList<>();
        InputStream inputStream = Categorias.class.getClassLoader().getResourceAsStream("info.json");
        try (InputStreamReader reader = new InputStreamReader(inputStream)) {
            Gson gson = new Gson();
            Type type = new TypeToken<Map<String, Object>>() {}.getType();
            Map<String, Object> data = gson.fromJson(reader, type); // Convertimos el JSON a un mapa

            // Accedemos a la sección de usuarios dentro del JSON
            Map<String, Object> tienda = (Map<String, Object>) data.get("tienda");
            List<Map<String, Object>> usuarios = (List<Map<String, Object>>) tienda.get("usuarios");
            for (Map<String, Object> usuario : usuarios) {
                String nombre = (String) usuario.get("nombre"); // Obtenemos el nombre de cada usuario
                nombresUsuarios.add(nombre); // Añadimos el nombre a la lista
            }
        } catch (Exception e) {
            e.printStackTrace(); // Mostramos el error si algo falla
        }

        return nombresUsuarios; // Devolvemos la lista de nombres
    }


    private void procesarCompra(String nombreProducto) {
        // Este método guarda la compra en el historial
        List<String> listaUsuarios = obtenerUsuariosJson(); // Obtenemos la lista de usuarios

        // Si no hay usuarios, mostramos un error
        if (listaUsuarios.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No hay usuarios disponibles para realizar la compra.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Mostramos un cuadro de selección para elegir al usuario
        String usuarioSeleccionado = (String) JOptionPane.showInputDialog(
                null,
                "Selecciona el usuario que realizará la compra",
                "Selección de usuario",
                JOptionPane.QUESTION_MESSAGE,
                null,
                listaUsuarios.toArray(),
                listaUsuarios.get(0) // Usuario seleccionado por defecto
        );

        // Si se selecciona un usuario, procesamos la compra
        if (usuarioSeleccionado != null) {
            HistorialCompra historialCompra = new HistorialCompra(); // Usamos la clase `HistorialCompra`
            historialCompra.realizarCompra(usuarioSeleccionado, nombreProducto); // Registramos la compra
        }
    }

    private static class Categoria {
        private final String nombre; // Nombre de la categoría
        private final List<Map<String, Object>> productos; // Lista de productos en la categoría

        public Categoria(String nombre, List<Map<String, Object>> productos) {
            this.nombre = nombre; // Inicializamos el nombre
            this.productos = productos; // Inicializamos la lista de productos
        }

        public String getNombre() {
            return nombre; // Devuelve el nombre de la categoría
        }

        public List<Map<String, Object>> getProductos() {
            return productos; // Devuelve la lista de productos
        }
    }

}
