package frontend;

import backend.ControladorBackend;
import javax.swing.*;
import java.awt.*;

public class PanelResumen extends JPanel {
    private final JLabel lblCardio = new JLabel("- Cardiovascular: -");
    private final JLabel lblFuerza = new JLabel("- Fuerza: -");
    private final JLabel lblBasico = new JLabel("- Basico: -");
    private final JLabel lblIntermedio = new JLabel("- Intermedio: -");
    private final JLabel lblAvanzado = new JLabel("- Avanzado: -");
    private final JLabel lblAlto = new JLabel("- Alto rendimiento: -");
    private final JLabel lblTiempo = new JLabel("Tiempo total estimado: -");
    private final JTextArea txtEjercicios = new JTextArea(6, 30);

    public PanelResumen(Runnable onCerrar) {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel pnlEstadisticas = new JPanel(new GridLayout(0, 1, 6, 6));
        pnlEstadisticas.setBorder(BorderFactory.createTitledBorder("Estadísticas de la rutina"));

        pnlEstadisticas.add(new JLabel("Cantidad por tipo:"));
        pnlEstadisticas.add(lblCardio);
        pnlEstadisticas.add(lblFuerza);

        pnlEstadisticas.add(new JLabel(" "));
        pnlEstadisticas.add(new JLabel("Cantidad por intensidad:"));
        pnlEstadisticas.add(lblBasico);
        pnlEstadisticas.add(lblIntermedio);
        pnlEstadisticas.add(lblAvanzado);
        pnlEstadisticas.add(lblAlto);

        pnlEstadisticas.add(new JLabel(" "));
        pnlEstadisticas.add(lblTiempo);

        add(pnlEstadisticas, BorderLayout.WEST);

        txtEjercicios.setEditable(false);
        txtEjercicios.setLineWrap(true);
        txtEjercicios.setWrapStyleWord(true);
        JScrollPane scrollEjercicios = new JScrollPane(txtEjercicios);
        scrollEjercicios.setBorder(BorderFactory.createTitledBorder("Ejercicios asignados"));
        
        add(scrollEjercicios, BorderLayout.CENTER);

        JButton btnCerrar = new JButton("Generar otra rutina");
        btnCerrar.addActionListener(e -> onCerrar.run());

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottom.add(btnCerrar);
        add(bottom, BorderLayout.SOUTH);
    }

    // Aquí estaba el error: había que especificar que viene del ControladorBackend
    public void actualizarResumen(ControladorBackend.ResumenRutina resumen) {
        lblCardio.setText("- Cardiovascular: " + resumen.getTotalCardio());
        lblFuerza.setText("- Fuerza: " + resumen.getTotalFuerza());
        lblBasico.setText("- Basico: " + resumen.getTotalBasico());
        lblIntermedio.setText("- Intermedio: " + resumen.getTotalIntermedio());
        lblAvanzado.setText("- Avanzado: " + resumen.getTotalAvanzado());
        lblAlto.setText("- Alto rendimiento: " + resumen.getTotalAltoRendimiento());
        lblTiempo.setText("Tiempo total estimado: " + resumen.getTiempoTotal() + " min");

        StringBuilder sb = new StringBuilder();
        if (resumen.getNombresEjercicios() != null) {
            for (String nombre : resumen.getNombresEjercicios()) {
                sb.append(nombre).append("\n");
            }
        }
        txtEjercicios.setText(sb.toString());
        txtEjercicios.setCaretPosition(0);
    }
}