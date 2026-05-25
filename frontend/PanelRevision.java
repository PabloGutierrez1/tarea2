package frontend;

import backend.ControladorBackend;
import backend.Ejercicio;
import javax.swing.*;
import java.awt.*;

public class PanelRevision extends JPanel {
    private final ControladorBackend backend;

    private final JLabel lblPosicion = new JLabel("Ejercicio - de -");
    private final JLabel lblNombre = new JLabel("-");
    private final JLabel lblTipo = new JLabel("-");
    private final JLabel lblIntensidad = new JLabel("-");
    private final JLabel lblTiempo = new JLabel("-");
    private final JTextArea txtDescripcion = new JTextArea();

    private final JButton btnVolver = new JButton("Volver");
    private final JButton btnSiguiente = new JButton("Siguiente");

    private int indice = 0;
    private int total = 0;

    public PanelRevision(ControladorBackend backend) {
        this.backend = backend;
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createTitledBorder("Revision de rutina"));

        txtDescripcion.setEditable(false);
        txtDescripcion.setLineWrap(true);
        txtDescripcion.setWrapStyleWord(true);

        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT));
        top.add(lblPosicion);

        JPanel form = new JPanel(new GridLayout(4, 2, 6, 6));
        form.add(new JLabel("Nombre:")); form.add(lblNombre);
        form.add(new JLabel("Tipo:")); form.add(lblTipo);
        form.add(new JLabel("Intensidad:")); form.add(lblIntensidad);
        form.add(new JLabel("Tiempo:")); form.add(lblTiempo);

        JPanel center = new JPanel(new BorderLayout());
        center.add(form, BorderLayout.NORTH);
        center.add(new JScrollPane(txtDescripcion), BorderLayout.CENTER);

        btnVolver.addActionListener(e -> backend.enviar(new ControladorBackend.ComandoEjercicioAnterior()));

        btnSiguiente.addActionListener(e -> {
            if (indice >= total - 1) {
                backend.enviar(new ControladorBackend.ComandoSolicitarResumen());
            } else {
                backend.enviar(new ControladorBackend.ComandoEjercicioSiguiente());
            }
        });

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottom.add(btnVolver);
        bottom.add(btnSiguiente);

        add(top, BorderLayout.NORTH);
        add(center, BorderLayout.CENTER);
        add(bottom, BorderLayout.SOUTH);
    }

    public void actualizarEstado(Ejercicio ejercicio, int indice, int total) {
        this.indice = indice;
        this.total = total;

        lblPosicion.setText("Ejercicio " + (indice + 1) + " de " + total);
        lblNombre.setText(ejercicio.getNombreEjercicio());
        
        // Aquí estaba el error de .getEtiqueta(), se cambia a .toString()
        lblTipo.setText(ejercicio.getTipoEjercicio().toString()); 
        lblIntensidad.setText(ejercicio.getNivelIntensidad().toString());
        
        lblTiempo.setText(ejercicio.getTiempoEstimadoMinutos() + " min");
        txtDescripcion.setText(ejercicio.getDescripcionEjecucion());
        txtDescripcion.setCaretPosition(0);

        btnVolver.setEnabled(indice > 0);
        if (indice >= total - 1) {
            btnSiguiente.setText("Resumen de la rutina");
        } else {
            btnSiguiente.setText("Siguiente");
        }
    }
}