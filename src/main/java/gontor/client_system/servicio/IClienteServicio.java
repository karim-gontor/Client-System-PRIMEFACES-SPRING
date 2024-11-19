package gontor.client_system.servicio;

import gontor.client_system.modelo.Cliente;

import java.util.List;

public interface IClienteServicio {
    public List<Cliente> listarClientes();

    public Cliente buscarClientePorId(Integer idCliente);

    //Segun el valor de la Primary Key JPA se encarga de guardar o modificar el cliente con este metodo
    public void guardarCliente(Cliente cliente);

    public void eliminarCliente(Cliente cliente);

}
