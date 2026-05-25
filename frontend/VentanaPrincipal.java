package frontend;

import backend.ControladorBackend;
import backend.Ejercicio;
import backend.ResumenRutina;
import backend.Rutina;
import backend.RutinaListener;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class VentanaPrincipal extends JFrame implements ControladorBackend.Escucha, RutinaListener {
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

    private String ultimoErrorRutina;

    public VentanaPrincipal(ControladorBackend backend) {
        super("Sistema de gestion de rutinas (Tarea 2)");
        this.backend = backend;

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(640, 420);
        setLocationRelativeTo(null);

        panelCarga = new PanelCargaDatos(this::mostrarGeneracion);
        panelGeneracion = new PanelGeneracion(backend, this::mostrarCarga);
        panelRevision = new PanelRevision(backend);
        panelResumen = new PanelResumen(this::mostrarGeneracion);

        contenedor.add(panelCarga, CARD_CARGA);
        contenedor.add(panelGeneracion, CARD_GENERACION);
        contenedor.add(panelRevision, CARD_REVISION);
        contenedor.add(panelResumen, CARD_RESUMEN);

        JPanel content = new JPanel(new BorderLayout());
        content.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        content.add(contenedor, BorderLayout.CENTER);
        setContentPane(content);

        backend.suscribir(this);
        backend.suscribirRutinaListener(this);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                backend.detener();
            }
        });

        mostrarCarga();
    }

    public void iniciarCargaAutomatica() {
        try {
            Path dataDir = Paths.get("data");
            if (!Files.exists(dataDir)) {
                panelCarga.setEstadoError("Carpeta 'data/' no encontrada. Cree la carpeta y agregue un archivo .csv o .bd");
                return;
            }

            Path archivoEncontrado = null;
            ControladorBackend.TipoOrigen tipoDetectado = null;
            try (DirectoryStream<Path> stream = Files.newDirectoryStream(dataDir)) {
                for (Path entrada : stream) {
                    String nombre = entrada.getFileName().toString().toLowerCase();
                    if (nombre.endsWith(".csv")) {
                        archivoEncontrado = entrada;
                        tipoDetectado = ControladorBackend.TipoOrigen.CSV;
                        break;
                    } else if (nombre.endsWith(".bd") && archivoEncontrado == null) {
                        archivoEncontrado = entrada;
                        tipoDetectado = ControladorBackend.TipoOrigen.BD_BINARIA;
                    }
                }
            }

            if (archivoEncontrado == null) {
                panelCarga.setEstadoError("No se encontraron archivos .csv o .bd en la carpeta 'data/'");
                return;
            }

            panelCarga.setEstadoInfo("Cargando automaticamente: " + archivoEncontrado.getFileName());
            backend.enviar(new ControladorBackend.ComandoCargarEjercicios(tipoDetectado, archivoEncontrado));
        } catch (Exception ex) {
            panelCarga.setEstadoError("Error al detectar archivos: " + ex.getMessage());
        }
    }

    @Override
    public void alEvento(ControladorBackend.Evento evento) {
        SwingUtilities.invokeLater(() -> procesarEnEdt(evento));
    }

    private void procesarEnEdt(ControladorBackend.Evento evento) {
        if (evento instanceof ControladorBackend.EventoError) {
            ControladorBackend.EventoError e = (ControladorBackend.EventoError) evento;
            if (ultimoErrorRutina != null && ultimoErrorRutina.equals(e.getMensaje())) {
                return;
            }
            JOptionPane.showMessageDialog(this, e.getMensaje(), "Aviso", JOptionPane.WARNING_MESSAGE);
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

    @Override
    public void onRutinaGenerada(Rutina rutina) {
        ultimoErrorRutina = null;
    }

    @Override
    public void onError(String mensaje) {
        ultimoErrorRutina = mensaje;
        panelCarga.setEstadoError(mensaje);
        mostrarCarga();
    }

    private void mostrarCarga() {
        cardLayout.show(contenedor, CARD_CARGA);
    }

    private void mostrarGeneracion() {
        cardLayout.show(contenedor, CARD_GENERACION);
    }

    private void mostrarRevision() {
        cardLayout.show(contenedor, CARD_REVISION);
    }

    private void mostrarResumen() {
        cardLayout.show(contenedor, CARD_RESUMEN);
    }
}
