/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo;

import CONEXION.Conexion;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 *
 * @author USER 17
 */
public class Ventas {
   int cantidad;
    String cliente, fecha, hora, producto, ruc;

    // Instancia del conector de base de datos
    Conexion dbConnector = new Conexion();

    public Ventas() {
        dbConnector.conectar();  // Conectar a la base de datos
    }

    public Ventas(int cantidad, String cliente, String producto, String ruc) {
        this.cantidad = cantidad;
        this.cliente = cliente;
        this.producto = producto;
        this.ruc = ruc;

        // Se obtiene la fecha y hora automáticamente
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter htf = DateTimeFormatter.ofPattern("HH:mm:ss");
        this.fecha = dtf.format(LocalDateTime.now());
        this.hora = htf.format(LocalDateTime.now());

        dbConnector.conectar();  // Conectar a la base de datos
        guardarVenta();          // Guardar la venta en la base de datos
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public String getCliente() {
        return cliente;
    }

    public void setCliente(String cliente) {
        this.cliente = cliente;
    }

    public String getFecha() {
        return fecha;
    }

    public String getHora() {
        return hora;
    }

    public String getProducto() {
        return producto;
    }

    public void setProducto(String producto) {
        this.producto = producto;
    }

    public String getRuc() {
        return ruc;
    }

    public void setRuc(String ruc) {
        this.ruc = ruc;
    }

    public double calculaSubtotal() throws Exception {
        return getCantidad() * getPrecioProducto();
    }

    public double getPrecioProducto() throws Exception {
        switch (getProducto().toLowerCase()) {
            case "lavadora":
                return 1500.00;
            case "refrigeradora":
                return 3500.00;
            case "licuadora":
                return 500.00;
            case "extractora":
                return 150.00;
            case "radiograbadora":
                return 750.00;
            case "dvd":
                return 100.00;
            case "blue ray":
                return 250.00;
            default:
                throw new Exception("Producto no encontrado");
        }
    }

    // Guardar la venta en la base de datos
    private void guardarVenta() {
        try {
            double subtotal = calculaSubtotal();
            dbConnector.insertarVenta(cliente, producto, cantidad, fecha, hora, ruc, subtotal);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
 