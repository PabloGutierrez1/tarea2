package frontend;

import backend.ControladorBackend;
import javax.swing.*;
import java.awt.*;

public class PanelCargaDatos extends JPanel {
    private final JLabel lblTotalEjercicios = new JLabel("Total ejercicios: -");
    private final JLabel lblTiempoTotal = new JLabel("Tiempo total disponible: -");
    private final JLabel lblCardio = new JLabel("Cardiovascular: -");
    private final JLabel lblFuerza = new JLabel("Fuerza: -");
    private final JLabel lblBasico = new JLabel("Basico: -");
    private final JLabel lblIntermedio = new JLabel("Intermedio: -");
    private final JLabel lblAvanzado = new JLabel("Avanzado: -");
    private final JLabel lblAlto = new JLabel("Alto rendimiento: -");
    private final JLabel lblEstado = new JLabel("Estado: esperando carga");

    private final JButton btnGenerar = new JButton("Iniciar generacion de rutina");

    public PanelCargaDatos(Runnable onGenerar) {
        setLayout(new GridLayout(0, 1, 8, 8));
        setBorder(BorderFactory.createTitledBorder("Carga de datos"));

        btnGenerar.setEnabled(false);
        btnGenerar.addActionListener(e -> onGenerar.run());

        add(lblTotalEjercicios);
        add(lblTiempoTotal);
        add(lblCardio);
        add(lblFuerza);
        add(lblBasico);
        add(lblIntermedio);
        add(lblAvanzado);
        add(lblAlto);
        add(lblEstado);

        JPanel botones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        botones.add(btnGenerar);
        add(botones);
    }

    public void setGenerarHabilitado(boolean habilitado) {
        btnGenerar.setEnabled(habilitado);
    }

    public void actualizarEstadisticas(ControladorBackend.Estadisticas estadisticas) {
        lblTotalEjercicios.setText("Total ejercicios: " + estadisticas.getTotalEjercicios());
        lblTiempoTotal.setText("Tiempo total disponible: " + estadisticas.getTiempoTotalDisponible() + " min");
        lblCardio.setText("Cardiovascular: " + estadisticas.getTotalCardio());
        lblFuerza.setText("Fuerza: " + estadisticas.getTotalFuerza());
        lblBasico.setText("Basico: " + estadisticas.getTotalBasico());
        lblIntermedio.setText("Intermedio: " + estadisticas.getTotalIntermedio());
        lblAvanzado.setText("Avanzado: " + estadisticas.getTotalAvanzado());
        lblAlto.setText("Alto rendimiento: " + estadisticas.getTotalAltoRendimiento());
    }

    public void setEstadoInfo(String mensaje) {
        lblEstado.setText("Estado: " + mensaje);
    }
}