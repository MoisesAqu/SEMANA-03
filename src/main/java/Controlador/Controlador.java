/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controlador;

import Vista.IVentaProductos;
import Vista.ICredito;
import Vista.IContado;

/**
 *
 * @author USER 17
 */
public class Controlador {
     private IVentaProductos vistaPrincipal;

    public Controlador(IVentaProductos vistaPrincipal) {
        this.vistaPrincipal = vistaPrincipal;

        // Asignar eventos a los botones de la vista principal
        this.vistaPrincipal.getBtnCredito().addActionListener(e -> abrirCredito());
        this.vistaPrincipal.getBtnContado().addActionListener(e -> abrirContado());
    }

    private void abrirCredito() {
        ICredito creditoVista = new ICredito();
        new ControladorCredito(creditoVista, vistaPrincipal);  // Pasar la referencia de vistaPrincipal
        creditoVista.setVisible(true);
        vistaPrincipal.setVisible(false);
    }

    private void abrirContado() {
        IContado contadoVista = new IContado();
        new ControladorContado(contadoVista, vistaPrincipal);  // Pasar la referencia de vistaPrincipal
        contadoVista.setVisible(true);
        vistaPrincipal.setVisible(false);
    }

    private void salir(javax.swing.JFrame ventanaActual) {
        ventanaActual.setVisible(false);  // Oculta la ventana actual
        vistaPrincipal.setVisible(true);  // Muestra la ventana principal
    }
}

