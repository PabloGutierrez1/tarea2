package frontend;

import backend.ResumenRutina;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.FlowLayout;
import java.awt.GridLayout;

public class PanelResumen extends JPanel {
    private final JLabel lblCardio = new JLabel("- Cardiovascular: -");
    private final JLabel lblFuerza = new JLabel("- Fuerza: -");
    private final JLabel lblBasico = new JLabel("- Basico: -");
    private final JLabel lblIntermedio = new JLabel("- Intermedio: -");
    private final JLabel lblAvanzado = new JLabel("- Avanzado: -");
    private final JLabel lblAlto = new JLabel("- Alto rendimiento: -");
    private final JLabel lblTiempo = new JLabel("Tiempo total estimado: -");

    public PanelResumen(Runnable onCerrar) {
        setLayout(new GridLayout(0, 1, 6, 6));
        setBorder(BorderFactory.createTitledBorder("Resumen de la rutina"));

        add(new JLabel("Cantidad por tipo:"));
        add(lblCardio);
        add(lblFuerza);

        add(new JLabel(" "));
        add(new JLabel("Cantidad por intensidad:"));
        add(lblBasico);
        add(lblIntermedio);
        add(lblAvanzado);
        add(lblAlto);

        add(new JLabel(" "));
        add(lblTiempo);

        JButton btnCerrar = new JButton("Generar otra rutina");
        btnCerrar.addActionListener(e -> onCerrar.run());

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottom.add(btnCerrar);
        add(bottom);
    }

    public void actualizarResumen(ResumenRutina resumen) {
        lblCardio.setText("- Cardiovascular: " + resumen.getTotalCardio());
        lblFuerza.setText("- Fuerza: " + resumen.getTotalFuerza());
        lblBasico.setText("- Basico: " + resumen.getTotalBasico());
        lblIntermedio.setText("- Intermedio: " + resumen.getTotalIntermedio());
        lblAvanzado.setText("- Avanzado: " + resumen.getTotalAvanzado());
        lblAlto.setText("- Alto rendimiento: " + resumen.getTotalAltoRendimiento());
        lblTiempo.setText("Tiempo total estimado: " + resumen.getTiempoTotal() + " min");
    }
}
