package frontend;

import backend.ControladorBackend;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

public class PanelGeneracion extends JPanel {
    private final ControladorBackend backend;

    private final javax.swing.JTextField txtCliente = new javax.swing.JTextField();
    private final JSpinner spSemana = new JSpinner(new SpinnerNumberModel(1, 1, 200, 1));

    private final JSpinner spCardio = new JSpinner(new SpinnerNumberModel(0, 0, 20, 1));
    private final JComboBox<ControladorBackend.Intensidad> cbIntCardio = new JComboBox<>(ControladorBackend.Intensidad.values());

    private final JSpinner spFuerza = new JSpinner(new SpinnerNumberModel(0, 0, 20, 1));
    private final JComboBox<ControladorBackend.Intensidad> cbIntFuerza = new JComboBox<>(ControladorBackend.Intensidad.values());

    public PanelGeneracion(ControladorBackend backend, Runnable onCancelar) {
        this.backend = backend;

        setLayout(new GridBagLayout());
        setBorder(BorderFactory.createTitledBorder("Generacion de rutina"));

        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(6, 6, 6, 6);
        c.fill = GridBagConstraints.HORIZONTAL;

        int row = 0;
        agregarFila(c, row++, "Cliente:", txtCliente);
        agregarFila(c, row++, "Semana:", spSemana);
        agregarFila(c, row++, "Cantidad cardiovascular:", spCardio);
        agregarFila(c, row++, "Intensidad cardiovascular:", cbIntCardio);
        agregarFila(c, row++, "Cantidad fuerza:", spFuerza);
        agregarFila(c, row++, "Intensidad fuerza:", cbIntFuerza);

        JButton btnGenerar = new JButton("Generar rutina");
        btnGenerar.addActionListener(e -> generar());

        JButton btnCancelar = new JButton("Volver");
        btnCancelar.addActionListener(e -> onCancelar.run());

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottom.add(btnCancelar);
        bottom.add(btnGenerar);

        c.gridx = 0;
        c.gridy = row;
        c.gridwidth = 2;
        c.weightx = 1.0;
        add(bottom, c);
    }

    private void agregarFila(GridBagConstraints c, int row, String label, JComponent component) {
        c.gridx = 0;
        c.gridy = row;
        c.gridwidth = 1;
        c.weightx = 0.25;
        add(new JLabel(label), c);

        c.gridx = 1;
        c.weightx = 0.75;
        add(component, c);
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

        backend.enviar(new ControladorBackend.ComandoGenerarRutina(
                cliente.trim(),
                semana,
                cantCardio,
                intCardio,
                cantFuerza,
                intFuerza
        ));
    }
}
