package gontor.client_system;

import gontor.client_system.modelo.Cliente;
import gontor.client_system.servicio.IClienteServicio;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;
import java.util.Scanner;

@SpringBootApplication
public class ClientSystemApplication implements CommandLineRunner {

	@Autowired
	private IClienteServicio clienteServicio;

	//Forma generica de un salto de linea
	String newLine = System.lineSeparator();

	//Para enviar información a la consola, sustituimos a print.
	private static final Logger logger =
			LoggerFactory.getLogger(ClientSystemApplication.class);

	//Main se utiliza para inicializar Spring factory
	public static void main(String[] args) {
		logger.info("---------------Iniciando aplicacion");

		//Inicia la fabrica de Spring
		SpringApplication.run(ClientSystemApplication.class, args);

		logger.info("---------------Aplicacion finalizada");
	}


	//run sustituye al concepto de main para ejecutar nuestra app
	@Override
	public void run(String... args) throws Exception {

		clientSystem();
	}



	private void clientSystem(){
		boolean salir = false;
		Scanner consola = new Scanner(System.in);

		while(!salir){
			int opcion = mostrarMenu(consola);
			salir = ejecutarOpciones(consola, opcion);
			logger.info(newLine);
		}

	}

	private int mostrarMenu(Scanner consola){
		logger.info("""
			\n*** Sistema de clientes ***
			1. Listar Cliente
			2. Buscar Cliente
			3. Agregar Cliente
			4. Modificar Cliente
			5. Eliminar Cliente
			6. Salir
			Elija una opcion\s""");

		return Integer.parseInt(consola.nextLine());
	}

	private boolean ejecutarOpciones(Scanner consola, int opcion){
		boolean salir = false;

		switch (opcion){
			case 1 -> {
				logger.info(newLine + "--- Listado de Clientes ---" + newLine);
				List<Cliente> clientes = clienteServicio.listarClientes();
				clientes.forEach(cliente -> logger.info(cliente.toString() + newLine));

			}
			case 2 -> {
				logger.info(newLine + "--- Buscar Cliente por ID ---" + newLine);
				logger.info("Introduzca ID del cliente: ");
				int idCliente = Integer.parseInt(consola.nextLine());

				Cliente cliente = clienteServicio.buscarClientePorId(idCliente);

				if (cliente != null)
					logger.info("Cliente encontrado: " + cliente + newLine);
				else
					logger.info("Cliente NO encontrado: " + cliente + newLine);
			}
			case 3 -> {
				logger.info(newLine + "--- Agregar nuevo cliente ---" + newLine);
				logger.info("Introduzca nombre del cliente: ");
				String nombre = consola.nextLine();
				logger.info("Introduzca apellido del cliente: ");
				String apellido = consola.nextLine();
				logger.info("Introduzca membresia del cliente: ");
				int membresia = Integer.parseInt(consola.nextLine());

				Cliente cliente = new Cliente();
				cliente.setNombre(nombre);
				cliente.setApellido(apellido);
				cliente.setMembresia(membresia);

				clienteServicio.guardarCliente(cliente);
				logger.info("Cliente agregado: " + cliente + newLine);
			}
			case 4 -> {
				logger.info(newLine + "--- Modificar cliente ---" + newLine);
				logger.info("Introduzca ID del cliente: ");
				int idCliente = Integer.parseInt(consola.nextLine());

				Cliente cliente = clienteServicio.buscarClientePorId(idCliente);

				if (cliente != null){
					logger.info("Introduzca nombre del cliente: ");
					String nombre = consola.nextLine();
					logger.info("Introduzca apellido del cliente: ");
					String apellido = consola.nextLine();
					logger.info("Introduzca membresia del cliente: ");
					int membresia = Integer.parseInt(consola.nextLine());

					cliente.setNombre(nombre);
					cliente.setApellido(apellido);
					cliente.setMembresia(membresia);

					clienteServicio.guardarCliente(cliente);
					logger.info("Cliente modificado: " + cliente + newLine);
				} else
					logger.info("Cliente con ID: " + idCliente + " NO encontrado");
			}
			case 5 -> {
				logger.info(newLine + "--- Modificar cliente ---" + newLine);
				logger.info("Introduzca ID del cliente: ");
				int idCliente = Integer.parseInt(consola.nextLine());

				Cliente cliente = clienteServicio.buscarClientePorId(idCliente);

				if (cliente != null){
					clienteServicio.eliminarCliente(cliente);
					logger.info("Cliente con ID " + idCliente + " eliminado.");
				} else
					logger.info("No se ha encontrado el cliente con ID: "+ idCliente);
			}
			case 6 -> {
				salir = true;
				logger.info("Saliendo del sistema...");
			}
			default -> {
				logger.info("Opcion NO reconocida: "+ opcion + newLine);
			}
		}

		return salir;
	}

}
