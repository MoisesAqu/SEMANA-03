/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package CONEXION;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Conexion {
    private Connection conexion;

    // Connection properties
    private String usuario = "sa";     // Username
    private String contraseña = "1234";    // Password
    private String db = "VentaDB";        // Database name
    private String ip = "DESKTOP-83UNMC4";
    private String puerto = "1433";        // Port number (default SQL Server port)

    // Método para conectar a SQL Server
    public void conectar() {
        try {
            String cadena = "jdbc:sqlserver://" + ip + ":" + puerto + ";databaseName=" + db + ";trustServerCertificate=true;";
            conexion = DriverManager.getConnection(cadena, usuario, contraseña);
            System.out.println("Conexión establecida con SQL Server");
        } catch (SQLException e) {
            System.err.println("Error al conectar a la base de datos: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Simulación del método para verificar si la conexión está activa
    public boolean estaConectado() {
        // Simulación de conexión siempre activa
        System.out.println("Simulación: Conexión activa.");
        return true;
    }

    // Simulación del método para cerrar la conexión
    public void cerrar() {
        // Simulación de cierre de conexión
        System.out.println("Simulación: Conexión cerrada.");
    }


    public int insertarVenta(String cliente, String producto, int cantidad, String fecha, String hora, String ruc, double subtotal) {
        // Aquí ya no se realiza la inserción en la base de datos
        // Solo simularíamos el proceso y devolveríamos un ID ficticio de venta para continuar el flujo

        int idVenta = (int) (Math.random() * 1000); // Generar un ID de venta aleatorio para simular el flujo
        System.out.println("Venta guardada correctamente con ID: " + idVenta);

        // Simulación del proceso de venta
        System.out.println("Detalles de la venta:");
        System.out.println("Cliente: " + cliente);
        System.out.println("Producto: " + producto);
        System.out.println("Cantidad: " + cantidad);
        System.out.println("Fecha: " + fecha);
        System.out.println("Hora: " + hora);
        System.out.println("RUC: " + ruc);
        System.out.println("Subtotal: " + subtotal);

        return idVenta;  // Retornar el ID de la venta (simulado)
    }
}
    
