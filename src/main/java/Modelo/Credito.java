/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo;

/**
 *
 * @author USER 17
 */
public class Credito extends Ventas {
     private int letras;  // Cantidad de letras para el crédito
    private int x;       // Cantidad de productos (variable solicitada)

    // Constructor que inicializa la cantidad de productos, cliente, producto, RUC y las letras
    public Credito(int cantidad, String cliente, String producto, String ruc, int letras) {
        super(cantidad, cliente, producto, ruc);
        this.letras = letras;
        this.x = cantidad;  // Asignamos la cantidad de productos a la variable x
    }

    // Método para obtener la cantidad de productos (X)
    public int getX() {
        return x;
    }

    // Método para obtener la cantidad de letras
    public int getLetras() {
        return letras;
    }

    // Método para establecer la cantidad de letras
    public void setLetras(int letras) {
        this.letras = letras;
    }

    // Método para calcular el interés basado en la tabla proporcionada
    public double calculaInteres() throws Exception {
        double subtotal = calculaSubtotal();
        if (subtotal < 1000) {
            return subtotal * 0.03;  // Interés del 3% si es menor a 1000
        } else if (subtotal >= 1000 && subtotal <= 3000) {
            return subtotal * 0.05;  // Interés del 5% entre 1000 y 3000
        } else {
            return subtotal * 0.08;  // Interés del 8% si es mayor a 3000
        }
    }

    // Método para calcular el monto mensual del crédito basado en las letras
    public double calculaMontoMensual() throws Exception {
        double subtotal = calculaSubtotal();  // Calculamos el subtotal
        double totalConInteres = subtotal + calculaInteres();  // Total con interés
        return totalConInteres / getLetras();  // Monto mensual
    }
}
