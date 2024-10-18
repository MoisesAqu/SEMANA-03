/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo;

/**
 *
 * @author USER 17
 */
public class Contado extends Ventas{
    
    private int n;  // Variable para la cantidad de productos

    // Constructor que recibe la cantidad de productos y otros datos
    public Contado(int cantidad, String cliente, String producto, String ruc) {
        super(cantidad, cliente, producto, ruc);
        this.n = cantidad;  // Asignamos la cantidad de productos a la variable n
    }

    // Método getN para devolver la cantidad de productos
    public int getN() {
        return n;
    }

    public double calculaDescuento(double subtotal) {
        if (subtotal < 1000) {
            return subtotal * 0.05;  // 5% de descuento
        } else if (subtotal >= 1000 && subtotal <= 3000) {
            return subtotal * 0.08;  // 8% de descuento
        } else {
            return subtotal * 0.12;  // 12% de descuento
        }
    }


    // Método para calcular el total con descuento
    public double calculaTotal() throws Exception {
        double subtotal = calculaSubtotal();  // Calculamos el subtotal usando el método de Venta
        double descuento = calculaDescuento(subtotal);  // Calculamos el descuento
        return subtotal - descuento;  // Retornamos el total a pagar con el descuento aplicado
    }
}
