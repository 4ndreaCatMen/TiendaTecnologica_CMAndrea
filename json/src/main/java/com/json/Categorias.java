package com.json;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import javax.swing.*;
import java.awt.*;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Categorias {

    private final List<Categoria> listaCategorias = obtenerCategoriasJson();

    private List<Categoria> obtenerCategoriasJson() {
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("info.json");
        Gson gson = new Gson();
        List<Categoria> categorias = new ArrayList<>();

        try (InputStreamReader reader = new InputStreamReader(inputStream)) {
            Type type = new TypeToken<Map<String, Object>>() {}.getType();
            Map<String, Object> data = gson.fromJson(reader, type);

            // Acceder a las categorías
            Map<String, Object> tienda = (Map<String, Object>) data.get("tienda");
            List<Map<String, Object>> categoriasJson = (List<Map<String, Object>>) tienda.get("categorias");

            for (Map<String, Object> categoriaJson : categoriasJson) {
                String nombre = (String) categoriaJson.get("nombre");
                List<Map<String, Object>> productos = (List<Map<String, Object>>) categoriaJson.get("productos");
                categorias.add(new Categoria(nombre, productos));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return categorias;
    }

    public void mostrarVentana() {
        JFrame ventana = new JFrame("Categorías");
        ventana.setSize(800, 600);
        ventana.getContentPane().setBackground(Color.LIGHT_GRAY);
        ventana.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        ventana.setLayout(new BorderLayout());
        ventana.setLocationRelativeTo(null);

        JLabel encabezado = new JLabel("Categorías Disponibles", SwingConstants.CENTER);
        encabezado.setFont(new Font("Arial", Font.BOLD, 24));
        encabezado.setForeground(Color.DARK_GRAY);
        ventana.add(encabezado, BorderLayout.NORTH);

        JPanel panelCategorias = new JPanel();
        panelCategorias.setLayout(new BoxLayout(panelCategorias, BoxLayout.Y_AXIS));
        panelCategorias.setBackground(Color.LIGHT_GRAY);

        for (Categoria categoria : listaCategorias) {
            JButton botonCategoria = new JButton(categoria.getNombre());
            botonCategoria.setFont(new Font("Arial", Font.PLAIN, 16));
            botonCategoria.setAlignmentX(Component.CENTER_ALIGNMENT);

            botonCategoria.addActionListener(e -> {
                try {
                    mostrarProductos(categoria);
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            });
            panelCategorias.add(Box.createRigidArea(new Dimension(0, 10))); // Espaciado
            panelCategorias.add(botonCategoria);
        }

        JScrollPane scrollPane = new JScrollPane(panelCategorias);
        ventana.add(scrollPane, BorderLayout.CENTER);

        ventana.setVisible(true);
    }

    private void mostrarProductos(Categoria categoria) throws Exception {
        InputStream inputStream;
        JFrame ventanaProductos = new JFrame("Productos - " + categoria.getNombre());
        ventanaProductos.setSize(800, 600);
        ventanaProductos.getContentPane().setBackground(Color.LIGHT_GRAY);
        ventanaProductos.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        ventanaProductos.setLayout(new BorderLayout());
        ventanaProductos.setLocationRelativeTo(null);

        JLabel encabezado = new JLabel("Productos en " + categoria.getNombre(), SwingConstants.CENTER);
        encabezado.setFont(new Font("Arial", Font.BOLD, 24));
        encabezado.setForeground(Color.DARK_GRAY);
        ventanaProductos.add(encabezado, BorderLayout.NORTH);

        JPanel panelProductos = new JPanel();
        panelProductos.setLayout(new GridLayout(0, 1, 10, 10));
        panelProductos.setBackground(Color.LIGHT_GRAY);

        for (Map<String, Object> producto : categoria.getProductos()) {
            String nombreProducto = (String) producto.get("nombre");

            // Obtener la lista de imágenes y seleccionar la primera
            List<String> imagenes = (List<String>) producto.get("imagenes");
            String imagenPath = imagenes.get(0); // Siempre hay imagen

            // Crear el botón con el nombre del producto
            JButton botonProducto = new JButton(nombreProducto);

            // Cargar la imagen para el botón
            inputStream = Categorias.class.getResourceAsStream("/" + imagenPath);
            if (inputStream != null) {
                ImageIcon iconoProducto = new ImageIcon(inputStream.readAllBytes());
                Image imagenRedimensionada = iconoProducto.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH); // Ajusta el tamaño (100x100 en este caso)
                botonProducto.setIcon(new ImageIcon(imagenRedimensionada)); // Asigna la imagen redimensionada al botón
            }

            botonProducto.setFont(new Font("Arial", Font.PLAIN, 16));

            // Acción al hacer clic en el botón
            botonProducto.addActionListener(e -> mostrarDetallesProducto(producto));

            // Añadir el botón al panel
            panelProductos.add(botonProducto);
        }

        JScrollPane scrollPane = new JScrollPane(panelProductos);
        ventanaProductos.add(scrollPane, BorderLayout.CENTER);

        ventanaProductos.setVisible(true);
    }

    // Método para establecer la imagen de fondo en una ventana
    private void establecerFondo(JFrame ventana) {
        String rutaImagen = "Fondo1.jpg";  // Ajusta esta ruta si es necesario (si está en un subdirectorio, agrega la carpeta)
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(rutaImagen);

        try {
            // Cargar la imagen
            ImageIcon imageIcon = new ImageIcon(inputStream.readAllBytes());
            Image image = imageIcon.getImage();
            ImageIcon fondoIcono = new ImageIcon(image.getScaledInstance(ventana.getWidth(), ventana.getHeight(), Image.SCALE_SMOOTH));

            // Crear un JPanel con la imagen de fondo
            JPanel panelFondo = new JPanel() {
                @Override
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    g.drawImage(fondoIcono.getImage(), 0, 0, this); // Dibuja la imagen de fondo
                }
            };
            panelFondo.setLayout(new BorderLayout());  // Usar BorderLayout o el layout adecuado para tu ventana

            // Colocar el panel de fondo en la ventana
            ventana.setContentPane(panelFondo);
            ventana.revalidate();  // Asegurarse de que la ventana se redibuje con el nuevo fondo
        } catch (Exception e) {
            e.printStackTrace();  // En caso de error, se muestra el stacktrace
        }
    }

    private void mostrarDetallesProducto(Map<String, Object> producto) {
        String nombre = (String) producto.get("nombre");
        double precio = (Double) producto.get("precio");
        String descripcion = (String) producto.get("descripcion");
        Map<String, Object> caracteristicas = (Map<String, Object>) producto.get("caracteristicas");

        StringBuilder detalles = new StringBuilder();
        detalles.append("<html><strong>Nombre:</strong> ").append(nombre).append("<br>");
        detalles.append("<strong>Precio:</strong> ").append(precio).append(" €<br>");
        detalles.append("<strong>Descripción:</strong> ").append(descripcion).append("<br>");
        detalles.append("<strong>Características:</strong><br>");

        for (Map.Entry<String, Object> entrada : caracteristicas.entrySet()) {
            detalles.append("  - ").append(entrada.getKey()).append(": ").append(entrada.getValue()).append("<br>");
        }

        // Mostrar detalles en un mensaje
        JOptionPane.showMessageDialog(null, detalles.toString(), "Detalles del Producto", JOptionPane.INFORMATION_MESSAGE);

        // Preguntar al usuario si desea comprar
        int opcion = JOptionPane.showOptionDialog(
                null,
                "¿Deseas comprar este producto?",
                "Comprar Producto",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                new String[]{"Comprar", "Cancelar"},
                "Comprar");

        if (opcion == JOptionPane.YES_OPTION) {
            seleccionarUsuarioParaCompra(producto);
        }
    }

    private void seleccionarUsuarioParaCompra(Map<String, Object> producto) {
        // Obtener la lista de usuarios desde el JSON o alguna fuente
        List<String> listaUsuarios = obtenerUsuariosJson();  // Método que ya tienes

        // Verificar si la lista de usuarios está vacía
        if (listaUsuarios.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No hay usuarios disponibles para realizar la compra.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        // Mostrar un cuadro de diálogo para seleccionar el usuario
        procesarCompra(producto.get("nombre").toString());

    }

    private List<String> obtenerUsuariosJson() {
        List<String> nombresUsuarios = new ArrayList<>();
        // Obtener el archivo JSON desde el classpath
        InputStream inputStream = Categorias.class.getClassLoader().getResourceAsStream("info.json");
            try (InputStreamReader reader = new InputStreamReader(inputStream)) {
                Gson gson = new Gson();
                Type type = new TypeToken<Map<String, Object>>() {}.getType();
                //El gson transforma el fichero en un objeto json accesible
                Map<String, Object> data = gson.fromJson(reader, type);

                // Acceder a la lista de usuarios
                Map<String, Object> tienda = (Map<String, Object>) data.get("tienda");
                List<Map<String, Object>> usuarios = (List<Map<String, Object>>) tienda.get("usuarios");
                for (Map<String, Object> usuario : usuarios) {
                    String nombre = (String) usuario.get("nombre");
                    nombresUsuarios.add(nombre);
                }

            } catch (Exception e){
            e.printStackTrace();
        }

        return nombresUsuarios;
    }


    private void procesarCompra(String nombreProducto) {
        List<String> listaUsuarios = obtenerUsuariosJson(); // Método que ya tienes

        if (listaUsuarios.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No hay usuarios disponibles para realizar la compra.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String usuarioSeleccionado = (String) JOptionPane.showInputDialog(
                null,
                "Selecciona el usuario que realizará la compra",
                "Selección de usuario",
                JOptionPane.QUESTION_MESSAGE,
                null,
                listaUsuarios.toArray(),
                listaUsuarios.get(0)
        );

        if (usuarioSeleccionado != null) {
            HistorialCompra historialCompra = new HistorialCompra();
            historialCompra.realizarCompra(usuarioSeleccionado, nombreProducto);
        }
    }

    // Clase interna para manejar la estructura de una categoría
    private static class Categoria {
        private final String nombre;
        private final List<Map<String, Object>> productos;

        public Categoria(String nombre, List<Map<String, Object>> productos) {
            this.nombre = nombre;
            this.productos = productos;
        }

        public String getNombre() {
            return nombre;
        }

        public List<Map<String, Object>> getProductos() {
            return productos;
        }
    }
}