package gontor.client_system.controlador;

import gontor.client_system.modelo.Cliente;
import gontor.client_system.servicio.IClienteServicio;
import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import lombok.Data;
import org.primefaces.PrimeFaces;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Data
@ViewScoped //Es para asociar el ciclo de vida del Bean a una vista
public class IndexControlador {

    @Autowired
    IClienteServicio clienteServicio;
    private List<Cliente> clientes;
    private Cliente clienteSeleccionado;

    private static final Logger logger = LoggerFactory.getLogger(IndexControlador.class);

//    La instancia de esta clase la genera de forma automatica jsf
//    por lo que no podemos crear un constructor tradicional, podemos usar:
    @PostConstruct
    public void init(){
        cargarDatos();
    }

    public void cargarDatos(){
        this.clientes = this.clienteServicio.listarClientes();
        this.clientes.forEach(cliente -> logger.info(cliente.toString()));
    }

    public void agregarCliente(){
        this.clienteSeleccionado = new Cliente();


    }

    public void guardarCliente(){
        logger.info("Cliente a guardar: " + this.clienteSeleccionado);

        //Guardar cliente nuevo
        if(this.clienteSeleccionado.getId() == null){
            //Guardar en DB
            this.clienteServicio.guardarCliente(this.clienteSeleccionado);
            //Refrescar la lista en memoria
            this.clientes.add(this.clienteSeleccionado);
            //Alert con mensaje
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage("Cliente agregado"));
        } else {
            //Modificar cliente
            this.clienteServicio.guardarCliente(this.clienteSeleccionado);
            //Alert con mensaje
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage("Cliente modificado"));
        }
        //Ocultar modal
        PrimeFaces.current().executeScript("PF('ventanaModalCliente').hide()");

        //Actualizacion parcial de la tabla usando AJAX
        PrimeFaces.current().ajax().update("forma-clientes:mensajes",
                "forma-clientes:clientes-tabla");

        //Reset del objeto cliente
        this.clienteSeleccionado = null;
    }

    public void eliminarCliente(){
        if(this.clienteSeleccionado != null){
            this.clienteServicio.eliminarCliente(this.clienteSeleccionado);
            this.clientes.remove(this.clienteSeleccionado);
            //Actualizacion parcial de la tabla usando AJAX
            PrimeFaces.current().ajax().update("forma-clientes:mensajes",
                    "forma-clientes:clientes-tabla");

            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage("Cliente eliminado"));

        }
        this.clienteSeleccionado = null;
    }
}
