/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.ejemplo04;

import Controlador.Controlador;
import Vista.IVentaProductos;

/**
 *
 * @author USER 17
 */
public class Ejemplo04 {

    public static void main(String[] args) {
        IVentaProductos vista = new IVentaProductos();  // Instanciamos la vista principal
        Controlador controlador = new Controlador(vista);  // Controlador para manejar la navegación
        vista.setVisible(true);  // Mostrar la vista principal
    }
}
