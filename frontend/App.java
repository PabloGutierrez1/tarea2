package frontend;

import backend.ControladorBackend;
import javax.swing.SwingUtilities;

public class App {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ControladorBackend backend = new ControladorBackend();
            VentanaPrincipal ventana = new VentanaPrincipal(backend);
            ventana.setVisible(true);
            ventana.iniciarCargaAutomatica();
        });
    }
}