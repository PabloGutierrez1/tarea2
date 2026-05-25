package frontend;

import backend.ControladorBackend;
import backend.Ejercicio;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;

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

        JPanel top = new JPanel(new BorderLayout());
        top.add(lblPosicion, BorderLayout.WEST);

        JPanel form = new JPanel(new GridLayout(0, 1, 6, 6));
        form.setBorder(BorderFactory.createTitledBorder("Ejercicio"));
        form.add(crearFila("Nombre:", lblNombre));
        form.add(crearFila("Tipo:", lblTipo));
        form.add(crearFila("Intensidad:", lblIntensidad));
        form.add(crearFila("Tiempo estimado:", lblTiempo));

        JScrollPane descripcionScroll = new JScrollPane(txtDescripcion);
        descripcionScroll.setBorder(BorderFactory.createTitledBorder("Descripcion de ejecucion"));

        JPanel center = new JPanel(new BorderLayout(10, 10));
        center.add(form, BorderLayout.NORTH);
        center.add(descripcionScroll, BorderLayout.CENTER);

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

    private JPanel crearFila(String label, JLabel value) {
        JPanel p = new JPanel(new BorderLayout());
        p.add(new JLabel(label), BorderLayout.WEST);
        p.add(value, BorderLayout.CENTER);
        return p;
    }

    public void actualizarEstado(Ejercicio ejercicio, int indice, int total) {
        this.indice = indice;
        this.total = total;

        lblPosicion.setText("Ejercicio " + (indice + 1) + " de " + total);
        lblNombre.setText(ejercicio.getNombreEjercicio());
        lblTipo.setText(ejercicio.getTipoEjercicio().getEtiqueta());
        lblIntensidad.setText(ejercicio.getNivelIntensidad().getEtiqueta());
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
