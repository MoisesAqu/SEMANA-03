/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controlador;


import Modelo.Contado;
import Vista.IContado;
import Vista.IVentaProductos;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
/**
 *
 * @author ADMIN
 */
public class ControladorContado {
    private IContado vistaContado;
    private IVentaProductos vistaPrincipal; // Declarar la ventana principal

    // Constructor
    public ControladorContado(IContado vistaContado, IVentaProductos vistaPrincipal) {
        this.vistaContado = vistaContado;
        this.vistaPrincipal = vistaPrincipal; // Inicializar la ventana principal
        inicializarEventos();
    }

    // Asignar eventos de los botones
    private void inicializarEventos() {
        vistaContado.getBtnAdquirir().addActionListener(e -> procesarVentaContado());
        vistaContado.getBtnEliminar().addActionListener(e -> eliminarProducto());
        vistaContado.getBtnSalir().addActionListener(e -> salir(vistaContado));
        vistaContado.getBtnEditar().addActionListener(e -> habilitarEdicion());
        vistaContado.getBtnOk().addActionListener(e -> guardarEdicion());
    }
    
    private void procesarVentaContado() {
        try {
            // Configurar el formato de moneda
            DecimalFormatSymbols simbolos = new DecimalFormatSymbols();
            simbolos.setDecimalSeparator('.');
            simbolos.setGroupingSeparator(',');
            DecimalFormat df = new DecimalFormat("$ #,##0.00", simbolos);

            // Obtener los datos del nuevo producto desde la vista
            String cliente = vistaContado.getNombreCliente();
            String ruc = vistaContado.getRUC();
            String producto = vistaContado.getProducto();
            int cantidad = vistaContado.getCantidad();

            // Crear una instancia del modelo Contado
            Contado venta = new Contado(cantidad, cliente, producto, ruc);

            // Calcular el subtotal del nuevo producto
            double subtotalProducto = venta.calculaSubtotal();

            // Añadir el nuevo producto a la tabla en la vista
            DefaultTableModel model = (DefaultTableModel) vistaContado.getTblProducto().getModel();
            model.addRow(new Object[]{
                model.getRowCount() + 1,  // Item
                producto,                 // Descripción del producto
                cantidad,                 // Cantidad seleccionada
                df.format(venta.getPrecioProducto()),  // Precio formateado
                df.format(subtotalProducto)            // Subtotal formateado
            });

            // Sumar todos los subtotales en la tabla
            double subtotalTotal = 0.0;
            for (int i = 0; i < model.getRowCount(); i++) {
                String subtotalStr = model.getValueAt(i, 4).toString().replace("$", "").replace(",", "");
                subtotalTotal += Double.parseDouble(subtotalStr);  // Sumar los subtotales
            }

            // Calcular el descuento total basado en el subtotal acumulado
            double descuentoTotal = venta.calculaDescuento(subtotalTotal);

            // Calcular el total neto
            double totalNeto = subtotalTotal - descuentoTotal;

            // Actualizar el valor de lblNeto con el total acumulado
            vistaContado.getLblNeto().setText(df.format(totalNeto));

            // Actualizar el resumen de la venta
            vistaContado.setResumen("** RESUMEN DE VENTA **\n"
                    + "--------------------------------\n"
                    + "CLIENTE: " + vistaContado.getNombreCliente() + "\n"
                    + "RUC: " + vistaContado.getRUC() + "\n"
                    + "FECHA: " + vistaContado.getLblFecha().getText() + "\n"
                    + "HORA: " + vistaContado.getLblHora().getText() + "\n"
                    + "--------------------------------\n"
                    + "SUBTOTAL: " + df.format(subtotalTotal) + "\n"
                    + "DESCUENTO: " + df.format(descuentoTotal) + "\n"
                    + "TOTAL NETO: " + df.format(totalNeto));

        } catch (Exception ex) {
            vistaContado.mostrarMensaje("Error al procesar la venta: " + ex.getMessage());
        }
    }

    


private void eliminarProducto() {
    int filaSeleccionada = vistaContado.getTblProducto().getSelectedRow();  // Obtener la fila seleccionada

    if (filaSeleccionada >= 0) {
        // Confirmar si el usuario realmente quiere eliminar el producto
        int confirmacion = JOptionPane.showConfirmDialog(null, "¿Estás seguro de que deseas eliminar este producto?", "Confirmar Eliminación", JOptionPane.YES_NO_OPTION);

        if (confirmacion == JOptionPane.YES_OPTION) {
            DefaultTableModel modelo = (DefaultTableModel) vistaContado.getTblProducto().getModel();
            modelo.removeRow(filaSeleccionada);  // Eliminar la fila seleccionada

            recalcularTotalContado();  // Recalcular el total después de eliminar
            vistaContado.mostrarMensaje("Producto eliminado correctamente.");
        }
    } else {
        vistaContado.mostrarMensaje("Por favor, selecciona un producto para eliminar.");
    }
}

private void habilitarEdicion() {
    int filaSeleccionada = vistaContado.getTblProducto().getSelectedRow();  // Seleccionar la fila a editar

    if (filaSeleccionada >= 0) {
        // Obtener los valores de la fila seleccionada y colocarlos en los campos de edición
        vistaContado.getCbxProducto().setSelectedItem(vistaContado.getTblProducto().getValueAt(filaSeleccionada, 1).toString()); // Producto
        vistaContado.getTxtCantidadSolicitada().setText(vistaContado.getTblProducto().getValueAt(filaSeleccionada, 2).toString()); // Cantidad

        // Habilitar los campos para la edición
        vistaContado.getCbxProducto().setEnabled(true);
        vistaContado.getTxtCantidadSolicitada().setEnabled(true);
    } else {
        vistaContado.mostrarMensaje("Por favor, selecciona una fila para editar.");
    }
}

private void guardarEdicion() {
    int filaSeleccionada = vistaContado.getTblProducto().getSelectedRow();  // Seleccionar la fila a editar

    if (filaSeleccionada >= 0) {
        // Obtener los nuevos valores desde los campos de edición
        String nuevoProducto = vistaContado.getCbxProducto().getSelectedItem().toString();
        int nuevaCantidad = Integer.parseInt(vistaContado.getTxtCantidadSolicitada().getText());

        // Actualizar la tabla con los nuevos datos ingresados
        vistaContado.getTblProducto().setValueAt(nuevoProducto, filaSeleccionada, 1);  // Producto
        vistaContado.getTblProducto().setValueAt(nuevaCantidad, filaSeleccionada, 2);  // Cantidad

        recalcularTotalContado();  // Recalcular el total después de editar
        vistaContado.mostrarMensaje("Producto editado correctamente.");
    } else {
        vistaContado.mostrarMensaje("No se seleccionó ninguna fila para editar.");
    }
}

private void recalcularTotalContado() {
    DefaultTableModel modelo = (DefaultTableModel) vistaContado.getTblProducto().getModel();
    double subtotalTotal = 0.0;

    for (int i = 0; i < modelo.getRowCount(); i++) {
        String subtotalStr = modelo.getValueAt(i, 4).toString().replace("$", "").replace(",", "");
        subtotalTotal += Double.parseDouble(subtotalStr);
    }

    // Calcular el descuento y total neto
    Contado venta = new Contado(0, vistaContado.getNombreCliente(), "", vistaContado.getRUC());
    double descuentoTotal = venta.calculaDescuento(subtotalTotal);
    double totalNeto = subtotalTotal - descuentoTotal;

    // Actualizar el lblNeto
    DecimalFormat df = new DecimalFormat("$ #,##0.00");
    vistaContado.getLblNeto().setText(df.format(totalNeto));
}


    private void salir(javax.swing.JFrame ventanaActual) {
        ventanaActual.setVisible(false);  // Ocultar la ventana actual (por ejemplo, ICredito o IContado)
        vistaPrincipal.setVisible(true);  // Mostrar la ventana principal IVentaProductos
    }

}
