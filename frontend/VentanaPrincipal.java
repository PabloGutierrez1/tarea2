package frontend;

import backend.ControladorBackend;
import backend.ControladorBackend.Evento;
import javax.swing.*;
import java.awt.CardLayout;
import java.nio.file.Paths;

public class VentanaPrincipal extends JFrame implements ControladorBackend.Escucha {
    private static final String CARD_CARGA = "carga";
    private static final String CARD_GENERACION = "generacion";
    private static final String CARD_REVISION = "revision";
    private static final String CARD_RESUMEN = "resumen";

    private final ControladorBackend backend;
    private final CardLayout cardLayout = new CardLayout();
    private final JPanel contenedor = new JPanel(cardLayout);

    private final PanelCargaDatos panelCarga;
    private final PanelGeneracion panelGeneracion;
    private final PanelRevision panelRevision;
    private final PanelResumen panelResumen;

    public VentanaPrincipal(ControladorBackend backend) {
        super("Sistema de gestion de rutinas");
        this.backend = backend;
        this.backend.suscribir(this);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(700, 500);
        setLocationRelativeTo(null);

        panelCarga = new PanelCargaDatos(() -> mostrarGeneracion());
        panelGeneracion = new PanelGeneracion(backend, () -> mostrarCarga());
        panelRevision = new PanelRevision(backend);
        panelResumen = new PanelResumen(() -> mostrarGeneracion());

        contenedor.add(panelCarga, CARD_CARGA);
        contenedor.add(panelGeneracion, CARD_GENERACION);
        contenedor.add(panelRevision, CARD_REVISION);
        contenedor.add(panelResumen, CARD_RESUMEN);

        add(contenedor);
        mostrarCarga();
    }

public void iniciarCargaAutomatica() {
        backend.enviar(new ControladorBackend.ComandoCargarEjercicios(ControladorBackend.TipoOrigen.CSV, Paths.get("data", "ejercicios.csv")));
    }

    @Override
    public void alEvento(Evento evento) {
        SwingUtilities.invokeLater(() -> procesarEvento(evento));
    }

    private void procesarEvento(Evento evento) {
        if (evento instanceof ControladorBackend.EventoError) {
            ControladorBackend.EventoError e = (ControladorBackend.EventoError) evento;
            JOptionPane.showMessageDialog(this, e.getMensaje(), "Error", JOptionPane.ERROR_MESSAGE);
            panelCarga.setEstadoInfo("Error: " + e.getMensaje());
            return;
        }

        if (evento instanceof ControladorBackend.EventoEjerciciosCargados) {
            ControladorBackend.EventoEjerciciosCargados e = (ControladorBackend.EventoEjerciciosCargados) evento;
            panelCarga.actualizarEstadisticas(e.getEstadisticas());
            panelCarga.setGenerarHabilitado(true);
            panelCarga.setEstadoInfo("Ejercicios cargados exitosamente");
            mostrarCarga();
            return;
        }

        if (evento instanceof ControladorBackend.EventoEstadoRevision) {
            ControladorBackend.EventoEstadoRevision e = (ControladorBackend.EventoEstadoRevision) evento;
            panelRevision.actualizarEstado(e.getEjercicioActual(), e.getIndice(), e.getTotal());
            mostrarRevision();
            return;
        }

        if (evento instanceof ControladorBackend.EventoResumenRutina) {
            ControladorBackend.EventoResumenRutina e = (ControladorBackend.EventoResumenRutina) evento;
            panelResumen.actualizarResumen(e.getResumen());
            mostrarResumen();
        }
    }

    private void mostrarCarga() { cardLayout.show(contenedor, CARD_CARGA); }
    private void mostrarGeneracion() { cardLayout.show(contenedor, CARD_GENERACION); }
    private void mostrarRevision() { cardLayout.show(contenedor, CARD_REVISION); }
    private void mostrarResumen() { cardLayout.show(contenedor, CARD_RESUMEN); }
}