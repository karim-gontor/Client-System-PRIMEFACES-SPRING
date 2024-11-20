package gontor.client_system.gui;

import gontor.client_system.modelo.Cliente;
import gontor.client_system.servicio.ClienteServicio;
import gontor.client_system.servicio.IClienteServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

@Component
public class ClientSystemGUI extends JFrame {
    private JPanel panelPrincipal;
    private JTable clientesTabla;
    private JTextField nombreTexto;
    private JTextField apellidoTexto;
    private JTextField membresiaTexto;
    private JButton guardarButton;
    private JButton eliminarButton;
    private JButton limpiarButton;
    private Integer idCliente;

    //No utilizamos @Autowired en el servicio porque debemos de iniciarlo
    //en el constructor para tener los datos antes de cuando los da @Autowired
    IClienteServicio clienteServicio;

    //Manejar los datos de la tabla
    private DefaultTableModel tablaModeloClientes;

    @Autowired
    public ClientSystemGUI(ClienteServicio clienteServicio){
        //Inicializacion del servicio
        this.clienteServicio = clienteServicio;
        //Clase principal de la app
        iniciarSystem();

        //Listeners
        guardarButton.addActionListener(e -> {
            guardarCliente();
        });
        clientesTabla.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                cargarClienteSeleccionado();
            }
        });
        eliminarButton.addActionListener(e -> {
            eliminarCliente();
        });
        limpiarButton.addActionListener(e -> {
            limpiarFormulario();
        });
    }
    private void iniciarSystem(){
        //Establecemos el panel principal
        setContentPane(panelPrincipal);
        //Comportamiento de cuando cerramos el panel
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //Tamaño de la ventana
        setSize(900,700);
        //Location de la ventana (centrado)
        setLocationRelativeTo(null);
    }

    //Creamos una tabla personalizada
    private void createUIComponents() {
        // Establecer columnas y filas de la tabla
        // Forma simple
        //this.tablaModeloClientes = new DefaultTableModel(0, 4);

        //Para evitar que las celdas de la tabla sean editables de forma individual
        this.tablaModeloClientes = new DefaultTableModel(0, 4){
            @Override
            public boolean isCellEditable(int row, int column){
                return false;
            };
        };

        // Establecer los cabeceros (Headers)
        String[] cabeceros = {"Id", "Nombre", "Apellido", "Membresia"};
        this.tablaModeloClientes.setColumnIdentifiers(cabeceros);

        //Creamos la tabla
        this.clientesTabla = new JTable(tablaModeloClientes);

        //Restringir seleccion de tabla a una sola row
        this.clientesTabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        //Cargar el listado de clientes
        listarClientes();
    }

    private void listarClientes(){
        this.tablaModeloClientes.setRowCount(0);

        //Obtenemos los clientes a traves del servicio
        List<Cliente> clientes = this.clienteServicio.listarClientes();

        //Iteracion sobre la List de clientes, por cada cliente creamos una row
        //que es un array de Objects con los datos del cliente
        clientes.forEach(cliente -> {
            Object[] renglonCliente = {
                    cliente.getId(),
                    cliente.getNombre(),
                    cliente.getApellido(),
                    cliente.getMembresia()
            };
            //Añadimos el array a la tabla con addRow
            this.tablaModeloClientes.addRow(renglonCliente);
        });
    }

    private void guardarCliente(){
        if(nombreTexto.getText().equals("")){
            mostrarMensaje("Proporciona un nombre");
            //Hacemos focus en el campo de nombre
            nombreTexto.requestFocusInWindow();
            return;
        }
        if(apellidoTexto.getText().equals("")){
            mostrarMensaje("Proporciona un apellido");
            apellidoTexto.requestFocusInWindow();
            return;
        }
        if(membresiaTexto.getText().equals("")){
            mostrarMensaje("Proporciona una membresia");
            membresiaTexto.requestFocusInWindow();
            return;
        }

        //Obtenemos valores del formulario
        String nombre = nombreTexto.getText();
        String apellido = apellidoTexto.getText();
        int membresia = Integer.parseInt(membresiaTexto.getText());

        //Creamos o Modificamos cliente con los valores obtenidos
        Cliente cliente = new Cliente(this.idCliente, nombre, apellido, membresia);

        //Insercion en BD
        this.clienteServicio.guardarCliente(cliente);

        if(this.idCliente == null)
            mostrarMensaje("Se agrego el nuevo Cliente");
        else
            mostrarMensaje("Se actualizo el cliente");

        limpiarFormulario();
        //Refresco de la lista
        listarClientes();
    }

    private void eliminarCliente(){
        var renglon = clientesTabla.getSelectedRow();
        if(renglon != -1){ //Si es -1 no ha seleccionado nada
            String idClienteStr = clientesTabla.getModel().getValueAt(renglon,0).toString();
            this.idCliente = Integer.parseInt(idClienteStr);

            Cliente cliente = new Cliente();
            cliente.setId(this.idCliente);
            clienteServicio.eliminarCliente(cliente);
            mostrarMensaje("Cliente con ID: "+idCliente+ " eliminado");

            limpiarFormulario();
            listarClientes();
        } else
            mostrarMensaje("Debe seleccionar un Cliente a eliminar");
    }
    private void cargarClienteSeleccionado(){
        //Obtenemos el registro del cliente seleccionado al hacer click en una row de la tabla
        var renglon = clientesTabla.getSelectedRow();

        if(renglon != -1){ //-1 significa que no selecciono ningun registro

            //Obtenemos valores del cliente desde la tabla => .getModel().getValueAt(renglon, indiceColumna)
            String id = clientesTabla.getModel().getValueAt(renglon, 0).toString();
            this.idCliente = Integer.parseInt(id);

            String nombre = clientesTabla.getModel().getValueAt(renglon, 1).toString();
            this.nombreTexto.setText(nombre);

            String apellido = clientesTabla.getModel().getValueAt(renglon, 2).toString();
            this.apellidoTexto.setText(apellido);

            String membresia = clientesTabla.getModel().getValueAt(renglon, 3).toString();
            this.membresiaTexto.setText(membresia);
        }
    }
    private void mostrarMensaje(String mensaje){
        //Abrimos una "alerta" con el mensaje que se proporcione
        JOptionPane.showMessageDialog(this, mensaje);
    }
    private void limpiarFormulario(){
        nombreTexto.setText("");
        apellidoTexto.setText("");
        membresiaTexto.setText("");
        //Limpiar id de cliente seleccionado
        this.idCliente = null;
        //Deseleccionamos el registro seleccionado de la tabla
        this.clientesTabla.getSelectionModel().clearSelection();
    }
}
