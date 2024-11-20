package gontor.client_system;

import com.formdev.flatlaf.FlatDarculaLaf;
import gontor.client_system.gui.ClientSystemGUI;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

import javax.swing.*;
@SpringBootApplication
public class ClientSystemSwing {

    public static void main(String[] args) {
        //Configurar modo oscuro con formdev
        FlatDarculaLaf.setup();

        //Instanciar la fabrica de Spring
        ConfigurableApplicationContext contextoSpring =
                new SpringApplicationBuilder(ClientSystemSwing.class)
                        .headless(false)
                        .web(WebApplicationType.NONE)
                        .run(args);

        //Creamos objeto de Swing
        SwingUtilities.invokeLater(() -> {
            ClientSystemGUI clientSystemGUI = contextoSpring.getBean(ClientSystemGUI.class);
            clientSystemGUI.setVisible(true);
        });
    }
}
