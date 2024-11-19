package gontor.client_system.modelo;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

@Entity
@Data // Genera los métodos Get y Set a esta clase
@NoArgsConstructor // Agrega un constructor vacio
@AllArgsConstructor // Agrega un constructor con todos los argumentos
@ToString // Agrega el metodo .toString()
@EqualsAndHashCode // Agrega los metodos Equals() and HashCode
public class Cliente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Para valores autoincrementables de la BBDD
    private Integer id ;
    private String nombre;
    private String apellido;
    private Integer membresia;

}
