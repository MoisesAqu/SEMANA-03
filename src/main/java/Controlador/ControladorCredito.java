/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controlador;

import CONEXION.Conexion;
import Modelo.Credito;
import Vista.ICredito;
import Vista.IVentaProductos;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
/**
 *
 * @author ADMIN
 */
public class ControladorCredito {
     private ICredito vistaCredito;
    private IVentaProductos vistaPrincipal;

    // Constructor
    public ControladorCredito(ICredito vistaCredito, IVentaProductos vistaPrincipal) {
        this.vistaCredito = vistaCredito;
        this.vistaPrincipal = vistaPrincipal;
        inicializarEventos();
    }

    // Asignar eventos de los botones
    private void inicializarEventos() {
        vistaCredito.getBtnAdquirir().addActionListener(e -> procesarVentaCredito());
        vistaCredito.getBtnEliminar().addActionListener(e -> eliminarProducto());
        vistaCredito.getBtnSalir().addActionListener(e -> salir(vistaCredito));
        vistaCredito.getBtnEditar().addActionListener(e -> habilitarEdicion());
        vistaCredito.getBtnOk().addActionListener(e -> guardarEdicion());
        vistaCredito.getBtnMostrarResumen().addActionListener(e -> mostrarResumenCredito(vistaCredito));
    }

    private void procesarVentaCredito() {
        try {
            // Configurar el formato de moneda
            DecimalFormatSymbols simbolos = new DecimalFormatSymbols();
            simbolos.setDecimalSeparator('.');
            simbolos.setGroupingSeparator(',');
            DecimalFormat df = new DecimalFormat("$ #,##0.00", simbolos);

            String cliente = vistaCredito.getTxtRazonSocial().getText();
            String ruc = vistaCredito.getTxtRuc().getText();
            String producto = vistaCredito.getCbxProducto().getSelectedItem().toString();
            int cantidad = Integer.parseInt(vistaCredito.getTxtCantidadSolicitada().getText());
            int letras = Integer.parseInt(vistaCredito.getCbxLetras().getSelectedItem().toString());

            // Crear una instancia del modelo Credito
            Credito venta = new Credito(cantidad, cliente, producto, ruc, letras);
            double subtotal = venta.calculaSubtotal();

            // Añadir el nuevo producto a la tabla en la vista
            DefaultTableModel model = (DefaultTableModel) vistaCredito.getTblProducto().getModel();
            model.addRow(new Object[] {
                model.getRowCount() + 1,
                producto,
                cantidad,
                df.format(venta.getPrecioProducto()),
                df.format(subtotal)
            });
            
            recalcularTotalCredito(vistaCredito);
            vistaCredito.mostrarMensaje("Venta a crédito procesada correctamente.");

        } catch (Exception ex) {
            vistaCredito.mostrarMensaje("Error al procesar la venta: " + ex.getMessage());
        }
    }



private void eliminarProducto() {
    int filaSeleccionada = vistaCredito.getTblProducto().getSelectedRow();

    if (filaSeleccionada >= 0) {
        int confirmacion = JOptionPane.showConfirmDialog(null, "¿Estás seguro de que deseas eliminar este producto?", "Confirmar Eliminación", JOptionPane.YES_NO_OPTION);

        if (confirmacion == JOptionPane.YES_OPTION) {
            DefaultTableModel modelo = (DefaultTableModel) vistaCredito.getTblProducto().getModel();
            modelo.removeRow(filaSeleccionada);
            recalcularTotalCredito(vistaCredito);

            vistaCredito.mostrarMensaje("Producto eliminado correctamente.");
        }
    } else {
        vistaCredito.mostrarMensaje("Por favor, selecciona un producto para eliminar.");
    }
}

private void habilitarEdicion() {
    int filaSeleccionada = vistaCredito.getTblProducto().getSelectedRow();

    if (filaSeleccionada >= 0) {
        // Cargar producto y cantidad desde la tabla
        vistaCredito.setCbxProducto(vistaCredito.getTblProducto().getValueAt(filaSeleccionada, 1).toString());
        vistaCredito.setCantidad(Integer.parseInt(vistaCredito.getTblProducto().getValueAt(filaSeleccionada, 2).toString()));

        // Habilitar los campos para la edición
        vistaCredito.getCbxProducto().setEnabled(true);
        vistaCredito.getTxtCantidadSolicitada().setEnabled(true);
    } else {
        vistaCredito.mostrarMensaje("Por favor, selecciona una fila para editar.");
    }
}


private void guardarEdicion() {
    int filaSeleccionada = vistaCredito.getTblProducto().getSelectedRow();

    if (filaSeleccionada >= 0) {
        try {
            String nuevoProducto = vistaCredito.getCbxProducto().getSelectedItem().toString();
            int nuevaCantidad = Integer.parseInt(vistaCredito.getTxtCantidadSolicitada().getText());

            // Obtener el precio del producto actualizado
            Credito ventaEditada = new Credito(nuevaCantidad, "", nuevoProducto, "", 0);
            double nuevoSubtotal = ventaEditada.calculaSubtotal();

            // Formato para mostrar en la tabla
            DecimalFormatSymbols simbolos = new DecimalFormatSymbols();
            simbolos.setDecimalSeparator('.');
            simbolos.setGroupingSeparator(',');
            DecimalFormat df = new DecimalFormat("$ #,##0.00", simbolos);

            // Actualizar la tabla con los nuevos valores
            vistaCredito.getTblProducto().setValueAt(nuevoProducto, filaSeleccionada, 1);
            vistaCredito.getTblProducto().setValueAt(nuevaCantidad, filaSeleccionada, 2);
            vistaCredito.getTblProducto().setValueAt(df.format(ventaEditada.getPrecioProducto()), filaSeleccionada, 3);
            vistaCredito.getTblProducto().setValueAt(df.format(nuevoSubtotal), filaSeleccionada, 4);

            // Recalcular el total del neto a pagar
            recalcularTotalCredito(vistaCredito);

            vistaCredito.mostrarMensaje("Producto editado correctamente.");

        } catch (Exception ex) {
            vistaCredito.mostrarMensaje("Error al guardar los cambios: " + ex.getMessage());
        }
    } else {
        vistaCredito.mostrarMensaje("No se seleccionó ninguna fila para editar.");
    }
}

    private void recalcularTotalCredito(ICredito vistaCredito) {
        DefaultTableModel modelo = (DefaultTableModel) vistaCredito.getTblProducto().getModel();
        double total = 0.0;

        // Recorrer todas las filas de la tabla y sumar los subtotales
        for (int i = 0; i < modelo.getRowCount(); i++) {
            try {
                String subtotalStr = modelo.getValueAt(i, 4).toString().replace("$", "").replace(",", "").trim();
                total += Double.parseDouble(subtotalStr);
            } catch (NumberFormatException ex) {
                vistaCredito.mostrarMensaje("Error al convertir el subtotal: " + ex.getMessage());
            }
        }

        // Configurar el formato con el punto para los miles y la coma para los decimales
        DecimalFormatSymbols simbolos = new DecimalFormatSymbols();
        simbolos.setDecimalSeparator('.');  // Punto para separar los decimales
        simbolos.setGroupingSeparator(',');  // Coma para los miles
        DecimalFormat df = new DecimalFormat("$ #,##0.00", simbolos);

        // Formatear el total y mostrarlo en el lblNetoPago
        vistaCredito.getLblNetoPago().setText(df.format(total));
    }


    private void mostrarResumenCredito(ICredito vistaCredito) {
        try {
            // Configurar el formato con el punto para los miles y la coma para los decimales
            DecimalFormatSymbols simbolos = new DecimalFormatSymbols();
            simbolos.setDecimalSeparator('.');  // Punto para separar los decimales
            simbolos.setGroupingSeparator(',');  // Coma para los miles
            DecimalFormat df = new DecimalFormat("$ #,##0.00", simbolos);

            // Obtener la cantidad de letras y el monto total desde lblNetoPago
            int letras = Integer.parseInt(vistaCredito.getCbxLetras().getSelectedItem().toString());
            double montoTotal = Double.parseDouble(vistaCredito.getLblNetoPago().getText().replace("$", "").replace(",", ""));

            // Calcular el monto por letra
            vistaCredito.setResumen(letras, montoTotal);

        } catch (Exception ex) {
            vistaCredito.mostrarMensaje("Error al mostrar el resumen: " + ex.getMessage());
        }
    }



    
    private void salir(javax.swing.JFrame ventanaActual) {
        ventanaActual.setVisible(false);  // Ocultar la ventana actual (por ejemplo, ICredito o IContado)
        vistaPrincipal.setVisible(true);  // Mostrar la ventana principal IVentaProductos
    }
}
