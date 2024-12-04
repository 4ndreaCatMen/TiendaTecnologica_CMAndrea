package com.json;

import com.google.gson.reflect.TypeToken;
import com.google.gson.Gson;
import javax.swing.*;
import java.awt.*;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Usuarios {
    private List<String> listaUsuarios;
    private Map<String, Object> tiendaData;

    public Usuarios() {
        // Cargar datos desde el JSON al iniciar el objeto
        tiendaData = cargarJsonData();
        listaUsuarios = obtenerUsuariosJson();
    }

    private Map<String, Object> cargarJsonData() {
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("info.json");
        Gson gson = new Gson();
        Map<String, Object> data = null;

        try (InputStreamReader reader = new InputStreamReader(inputStream)) {
            Type type = new TypeToken<Map<String, Object>>() {}.getType();
            data = gson.fromJson(reader, type);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return data;
    }

    private List<String> obtenerUsuariosJson() {
        List<String> nombresUsuarios = new ArrayList<>();
        if (tiendaData != null) {
            // Acceder a la lista de usuarios
            Map<String, Object> tienda = (Map<String, Object>) tiendaData.get("tienda");
            List<Map<String, Object>> usuarios = (List<Map<String, Object>>) tienda.get("usuarios");

            // Recorrer la lista de usuarios y extraer los nombres
            for (Map<String, Object> usuario : usuarios) {
                String nombre = (String) usuario.get("nombre");
                nombresUsuarios.add(nombre);
            }
        }
        return nombresUsuarios;
    }

    public int obtenerIdUsuarioDesdeJson(String nombreUsuario) {
        int idUsuario = -1;
        if (tiendaData != null) {
            Map<String, Object> tienda = (Map<String, Object>) tiendaData.get("tienda");
            List<Map<String, Object>> usuarios = (List<Map<String, Object>>) tienda.get("usuarios");

            // Buscar al usuario por nombre y obtener su ID
            for (Map<String, Object> usuarioMap : usuarios) {
                String nombre = (String) usuarioMap.get("nombre");
                if (nombre.equals(nombreUsuario)) {
                    idUsuario = ((Double) usuarioMap.get("id")).intValue();
                    break;
                }
            }
        }
        return idUsuario;
    }

    public void mostrarVentana() {
        JFrame ventana = new JFrame("Usuarios");
        ventana.setSize(400, 300);
        ventana.setLayout(new BorderLayout());
        ventana.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        ventana.setLocationRelativeTo(null);

        JLabel encabezado = new JLabel("Lista de Usuarios", SwingConstants.CENTER);
        encabezado.setFont(new Font("Arial", Font.BOLD, 18));
        ventana.add(encabezado, BorderLayout.NORTH);

        JPanel panelUsuarios = crearPanelUsuarios();
        JScrollPane scrollPane = new JScrollPane(panelUsuarios);
        ventana.add(scrollPane, BorderLayout.CENTER);

        ventana.setVisible(true);
    }

    private JPanel crearPanelUsuarios() {
        JPanel panelUsuarios = new JPanel();
        panelUsuarios.setLayout(new BoxLayout(panelUsuarios, BoxLayout.Y_AXIS)); // Usamos un layout vertical

        for (String usuario : listaUsuarios) {
            JButton botonUsuario = new JButton(usuario);
            String detalles = buscarDetallesUsuarioJson(usuario);
            botonUsuario.addActionListener(e -> mostrarDetallesUsuario(usuario, detalles));
            panelUsuarios.add(botonUsuario);
        }

        return panelUsuarios;
    }

    private String buscarDetallesUsuarioJson(String usuario) {
        String detalles = "Detalles no encontrados";
        if (tiendaData != null) {
            Map<String, Object> tienda = (Map<String, Object>) tiendaData.get("tienda");
            List<Map<String, Object>> usuarios = (List<Map<String, Object>>) tienda.get("usuarios");

            for (Map<String, Object> usuarioMap : usuarios) {
                String nombreUsuario = (String) usuarioMap.get("nombre");
                if (nombreUsuario.equals(usuario)) {
                    String email = (String) usuarioMap.get("email");
                    Map<String, Object> direccion = (Map<String, Object>) usuarioMap.get("direccion");

                    detalles = "Email: " + email + "\n";
                    detalles += "Dirección: " + direccion.get("calle") + " " + direccion.get("numero") + ", "
                            + direccion.get("ciudad") + ", " + direccion.get("pais");
                    break;
                }
            }
        }
        return detalles;
    }

    private void mostrarDetallesUsuario(String usuario, String detalles) {
        JOptionPane.showMessageDialog(null, "Detalles de " + usuario + " " + detalles);
    }
}
