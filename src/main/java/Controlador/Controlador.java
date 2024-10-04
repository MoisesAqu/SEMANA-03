/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controlador;

import Modelo.Contado;
import Modelo.Credito;
import Vista.IVentaProductos;
import Vista.ICredito;
import Vista.IContado; 
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author USER 17
 */
public class Controlador {
    private IVentaProductos vistaPrincipal;

    public Controlador(IVentaProductos vista) {
        this.vistaPrincipal = vista;

        // Asignar eventos a los botones de la vista principal
        this.vistaPrincipal.getBtnCredito().addActionListener(e -> abrirCredito());
        this.vistaPrincipal.getBtnContado().addActionListener(e -> abrirContado());
    }

    // Método para abrir la ventana de ventas a crédito
    private void abrirCredito() {
        ICredito creditoVista = new ICredito();  // Instanciamos la ventana de crédito
        creditoVista.setVisible(true);           // Mostramos la ventana de crédito
        vistaPrincipal.setVisible(false);        // Ocultamos la ventana actual

        // Asignar eventos a los botones de la vista de crédito
        creditoVista.getBtnAdquirir().addActionListener(e -> procesarVentaCredito(creditoVista));
        creditoVista.getBtnMostrarResumen().addActionListener(e -> mostrarResumenCredito(creditoVista));
        creditoVista.getBtnSalir().addActionListener(e -> salir(creditoVista));  // Llamamos al método salir y pasamos la ventana actual
    }

    // Método para abrir la ventana de ventas al contado
    private void abrirContado() {
        IContado contadoVista = new IContado();  // Instanciamos la ventana de contado
        contadoVista.setVisible(true);           // Mostramos la ventana de contado
        vistaPrincipal.setVisible(false);        // Ocultamos la ventana actual

        // Asignar eventos a los botones de la vista de contado
        contadoVista.getBtnAdquirir().addActionListener(e -> procesarVenta(contadoVista));  
        contadoVista.getBtnSalir().addActionListener(e -> salir(contadoVista));  // Llamamos al método salir y pasamos la ventana actual
    }

    // Método actualizado para manejar el evento de salir en ICredito y IContado
    private void salir(javax.swing.JFrame ventanaActual) {
        ventanaActual.setVisible(false); // Ocultar la ventana actual
        vistaPrincipal.setVisible(true); // Mostrar la ventana principal (IVentaProductos)
    }

    // Método que maneja la lógica del botón "Adquirir" para ventas al contado
    private void procesarVenta(IContado vistaContado) {
        try {
            // Configurar el formato para mostrar el símbolo de moneda y dos decimales
            DecimalFormatSymbols simbolos = new DecimalFormatSymbols();
            simbolos.setDecimalSeparator('.');
            simbolos.setGroupingSeparator(',');
            DecimalFormat df = new DecimalFormat("$ #,##0.00", simbolos);

            // 1. Obtener los datos desde la vista
            String cliente = vistaContado.getNombreCliente();  // Obtiene el nombre del cliente
            String ruc = vistaContado.getRUC();  // Obtiene el RUC del cliente
            String producto = vistaContado.getProducto();  // Obtiene el producto seleccionado
            int cantidad = vistaContado.getCantidad();  // Obtiene la cantidad seleccionada

            // 2. Crear una instancia de la clase Contado (Modelo)
            Contado venta = new Contado(cantidad, cliente, producto, ruc);

            // 3. Calcular el subtotal y el total con descuento
            double subtotal = venta.calculaSubtotal();
            double descuento = venta.calculaDescuento(subtotal);
            double total = venta.calculaTotal();

            // 4. Añadir el producto a la tabla en la vista
            DefaultTableModel model = (DefaultTableModel) vistaContado.getTblProducto().getModel();
            model.addRow(new Object[]{
                model.getRowCount() + 1,  // Item (número de la fila)
                producto,  // Descripción del producto
                cantidad,  // Cantidad seleccionada
                df.format(venta.getPrecioProducto()),  // Precio formateado
                df.format(subtotal)  // Subtotal formateado
            });

            // 5. Actualizar el resumen de la venta en la vista
            vistaContado.setResumen("** RESUMEN DE VENTA **\n"
                + "--------------------------------\n"
                + "CLIENTE: " + cliente + "\n"
                + "RUC: " + ruc + "\n"
                + "FECHA: " + vistaContado.getLblFecha().getText() + "\n"
                + "HORA: " + vistaContado.getLblHora().getText() + "\n"
                + "--------------------------------\n"
                + "SUBTOTAL: " + df.format(subtotal) + "\n"
                + "DESCUENTO: " + df.format(descuento) + "\n"
                + "NETO: " + df.format(total));

            // 6. Actualizar el valor del neto a pagar en el label "lblNetoPago"
            vistaContado.getLblNetoPago().setText(df.format(total));

        } catch (Exception ex) {
            // Mostrar mensaje de error en caso de excepción
            vistaContado.mostrarMensaje("Error al procesar la venta: " + ex.getMessage());
        }
    }

    // Método que maneja la lógica del botón "Adquirir" en ICredito
private void procesarVentaCredito(ICredito vistaCredito) {
    try {
        // Configurar el formato para mostrar el símbolo de moneda y dos decimales
        DecimalFormatSymbols simbolos = new DecimalFormatSymbols();
        simbolos.setDecimalSeparator('.');
        simbolos.setGroupingSeparator(',');
        DecimalFormat df = new DecimalFormat("$ #,##0.00", simbolos);

        // 1. Obtener los datos desde la vista
        String cliente = vistaCredito.getTxtRazonSocial();  // Obtener nombre del cliente
        String ruc = vistaCredito.getTxtRuc();              // Obtener RUC del cliente
        String producto = vistaCredito.getCbxProducto();    // Obtener el producto seleccionado
        int cantidad = vistaCredito.getCantidad();          // Obtener la cantidad seleccionada
        int letras = vistaCredito.getCbxLetras();           // Obtener el número de letras de pago

        // 2. Crear una instancia de la clase Credito (Modelo)
        Credito venta = new Credito(cantidad, cliente, producto, ruc, letras);

        // 3. Calcular el subtotal y el monto mensual
        double subtotal = venta.calculaSubtotal();
        double totalMensual = venta.calculaMontoMensual();

        // 4. Añadir el producto a la tabla en la vista
        DefaultTableModel model = (DefaultTableModel) vistaCredito.getTblProducto().getModel();
        model.addRow(new Object[]{
            model.getRowCount() + 1,  // Item (número de la fila)
            producto,                 // Descripción del producto
            cantidad,                 // Cantidad seleccionada
            df.format(venta.getPrecioProducto()),  // Precio formateado
            df.format(subtotal)       // Subtotal formateado
        });

        // 5. Actualizar el valor del neto a pagar en el label "lblNetoPago"
        vistaCredito.getLblNetoPago().setText(df.format(subtotal));  // Mostrar el subtotal como neto a pagar al inicio

    } catch (Exception ex) {
        vistaCredito.mostrarMensaje("Error al procesar la venta: " + ex.getMessage());
    }
}

// Método para mostrar el resumen de las letras de pago
private void mostrarResumenCredito(ICredito vistaCredito) {
    try {
        // Configurar el formato de moneda
        DecimalFormatSymbols simbolos = new DecimalFormatSymbols();
        simbolos.setDecimalSeparator('.');
        simbolos.setGroupingSeparator(',');
        DecimalFormat df = new DecimalFormat("$ #,##0.00", simbolos);

        // Obtener la cantidad de letras y el subtotal
        int letras = vistaCredito.getCbxLetras();
        double total = Double.parseDouble(vistaCredito.getLblNetoPago().getText().replace("$", "").replace(",", ""));  // Obtener el total del neto

        // Calcular el monto mensual de acuerdo con las letras
        double montoMensual = total / letras;

        // Construir el resumen de letras en el JTextPane
        StringBuilder resumen = new StringBuilder();
        resumen.append("** RESUMEN DE PAGOS **\n")
                .append("----------------------------\n");

        // Mostrar la distribución de letras
        for (int i = 1; i <= letras; i++) {
            resumen.append("Letra ").append(i).append(": ").append(df.format(montoMensual)).append("\n");
        }

        // Mostrar el resumen en la vista
        vistaCredito.getTxpResumenCredito().setText(resumen.toString());

    } catch (Exception ex) {
        vistaCredito.mostrarMensaje("Error al mostrar el resumen: " + ex.getMessage());
    }
}


    // Método para manejar el evento de salir
    private void salir() {
        System.exit(0);  // Cierra la aplicación
    }
}
