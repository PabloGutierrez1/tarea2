package frontend;

import backend.ControladorBackend;
import javax.swing.*;
import java.awt.*;

public class PanelGeneracion extends JPanel {
    private final ControladorBackend backend;

    private final JTextField txtCliente = new JTextField();
    private final JSpinner spSemana = new JSpinner(new SpinnerNumberModel(1, 1, 200, 1));

    private final JSpinner spCardio = new JSpinner(new SpinnerNumberModel(0, 0, 20, 1));
    private final JComboBox<ControladorBackend.Intensidad> cbIntCardio = new JComboBox<>(ControladorBackend.Intensidad.values());

    private final JSpinner spFuerza = new JSpinner(new SpinnerNumberModel(0, 0, 20, 1));
    private final JComboBox<ControladorBackend.Intensidad> cbIntFuerza = new JComboBox<>(ControladorBackend.Intensidad.values());

    public PanelGeneracion(ControladorBackend backend, Runnable onCancelar) {
        this.backend = backend;
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createTitledBorder("Generacion de rutina"));

        JPanel form = new JPanel(new GridLayout(6, 2, 10, 10));
        form.add(new JLabel("Nombre cliente:")); form.add(txtCliente);
        form.add(new JLabel("Semana:")); form.add(spSemana);
        form.add(new JLabel("Cant. Cardio:")); form.add(spCardio);
        form.add(new JLabel("Intensidad Cardio:")); form.add(cbIntCardio);
        form.add(new JLabel("Cant. Fuerza:")); form.add(spFuerza);
        form.add(new JLabel("Intensidad Fuerza:")); form.add(cbIntFuerza);

        JButton btnGenerar = new JButton("Generar");
        btnGenerar.addActionListener(e -> generar());

        JButton btnCancelar = new JButton("Volver");
        btnCancelar.addActionListener(e -> onCancelar.run());

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottom.add(btnCancelar);
        bottom.add(btnGenerar);

        add(form, BorderLayout.CENTER);
        add(bottom, BorderLayout.SOUTH);
    }

    private void generar() {
        String cliente = txtCliente.getText();
        if (cliente == null || cliente.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Debe ingresar el nombre del cliente", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int semana = (Integer) spSemana.getValue();
        int cantCardio = (Integer) spCardio.getValue();
        int cantFuerza = (Integer) spFuerza.getValue();

        if (cantCardio + cantFuerza <= 0) {
            JOptionPane.showMessageDialog(this, "Debe solicitar al menos 1 ejercicio", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        ControladorBackend.Intensidad intCardio = (ControladorBackend.Intensidad) cbIntCardio.getSelectedItem();
        ControladorBackend.Intensidad intFuerza = (ControladorBackend.Intensidad) cbIntFuerza.getSelectedItem();

        // Enviamos el comando tal cual lo espera tu backend
        backend.enviar(new ControladorBackend.ComandoGenerarRutina(
                cliente.trim(), semana, cantCardio, intCardio, cantFuerza, intFuerza
        ));
    }
}