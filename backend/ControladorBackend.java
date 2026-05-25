package backend;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class ControladorBackend {
    public interface Escucha {
        void alEvento(Evento evento);
    }
    public static abstract class Evento {
    }
    public static final class EventoError extends Evento {
        private final String mensaje;

        public EventoError(String mensaje) {
            this.mensaje = mensaje;
        }

        public String getMensaje() {
            return mensaje;
        }
    }

    public static final class EventoEjerciciosCargados extends Evento {
        private final Estadisticas estadisticas;

        public EventoEjerciciosCargados(Estadisticas estadisticas) {
            this.estadisticas = estadisticas;
        }

        public Estadisticas getEstadisticas() {
            return estadisticas;
        }
    }

    public static final class EventoEstadoRevision extends Evento {
        private final Ejercicio ejercicioActual;
        private final int indice;
        private final int total;

        public EventoEstadoRevision(Ejercicio ejercicioActual, int indice, int total) {
            this.ejercicioActual = ejercicioActual;
            this.indice = indice;
            this.total = total;
        }

        public Ejercicio getEjercicioActual() {
            return ejercicioActual;
        }

        public int getIndice() {
            return indice;
        }

        public int getTotal() {
            return total;
        }
    }

    public static final class EventoResumenRutina extends Evento {
        private final ResumenRutina resumen;

        public EventoResumenRutina(ResumenRutina resumen) {
            this.resumen = resumen;
        }

        public ResumenRutina getResumen() {
            return resumen;
        }
    }

    public interface Comando {
    }

    public static final class ComandoCargarEjercicios implements Comando {
        private final TipoOrigen kind;
        private final Path path;

        public ComandoCargarEjercicios(TipoOrigen kind, Path path) {
            this.kind = kind;
            this.path = path;
        }

        public TipoOrigen getKind() {
            return kind;
        }

        public Path getPath() {
            return path;
        }
    }

    public static final class ComandoGenerarRutina implements Comando {
        private final String cliente;
        private final int semana;

        private final int cantidadCardio;
        private final Intensidad intensidadCardio;
        private final int cantidadFuerza;
        private final Intensidad intensidadFuerza;

        public ComandoGenerarRutina(String cliente,
                                   int semana,
                                   int cantidadCardio,
                                   Intensidad intensidadCardio,
                                   int cantidadFuerza,
                                   Intensidad intensidadFuerza) {
            this.cliente = cliente;
            this.semana = semana;
            this.cantidadCardio = cantidadCardio;
            this.intensidadCardio = intensidadCardio;
            this.cantidadFuerza = cantidadFuerza;
            this.intensidadFuerza = intensidadFuerza;
        }

        public String getCliente() {
            return cliente;
        }

        public int getSemana() {
            return semana;
        }

        public int getCantidadCardio() {
            return cantidadCardio;
        }

        public Intensidad getIntensidadCardio() {
            return intensidadCardio;
        }

        public int getCantidadFuerza() {
            return cantidadFuerza;
        }

        public Intensidad getIntensidadFuerza() {
            return intensidadFuerza;
        }
    }

    public static final class ComandoEjercicioAnterior implements Comando {
    }

    public static final class ComandoEjercicioSiguiente implements Comando {
    }

    public static final class ComandoSolicitarResumen implements Comando {
    }

    public enum TipoOrigen {
        CSV,
        BD_BINARIA
    }

    public enum TipoEjercicio {
        CARDIOVASCULAR("Cardiovascular"),
        FUERZA("Fuerza");

        private final String etiqueta;

        TipoEjercicio(String etiqueta) {
            this.etiqueta = etiqueta;
        }

        public String getEtiqueta() {
            return etiqueta;
        }

        public static TipoEjercicio parse(String raw) throws ExcepcionFormatoDatos {
            if (raw == null) throw new ExcepcionFormatoDatos("Tipo vacío");
            String s = normalizar(raw);
            if ("cardiovascular".equals(s) || "cardio".equals(s)) return CARDIOVASCULAR;
            if ("fuerza".equals(s)) return FUERZA;
            throw new ExcepcionFormatoDatos("Tipo no reconocido: " + raw);
        }
    }

    public enum Intensidad {
        BASICO("Básico"),
        INTERMEDIO("Intermedio"),
        AVANZADO("Avanzado"),
        ALTO_RENDIMIENTO("Alto rendimiento");

        private final String etiqueta;

        Intensidad(String etiqueta) {
            this.etiqueta = etiqueta;
        }

        public String getEtiqueta() {
            return etiqueta;
        }

        public static Intensidad parse(String raw) throws ExcepcionFormatoDatos {
            if (raw == null) throw new ExcepcionFormatoDatos("Intensidad vacía");
            String s = normalizar(raw);
            if ("basico".equals(s) || "basica".equals(s)) return BASICO;
            if ("intermedio".equals(s)) return INTERMEDIO;
            if ("avanzado".equals(s)) return AVANZADO;
            if ("alto rendimiento".equals(s) || "altorendimiento".equals(s) || "alto".equals(s)) return ALTO_RENDIMIENTO;
            throw new ExcepcionFormatoDatos("Intensidad no reconocida: " + raw);
        }
    }


    public static final class Estadisticas {
        private final int totalEjercicios;
        private final int tiempoTotalDisponible;

        private final int totalCardio;
        private final int totalFuerza;

        private final int totalBasico;
        private final int totalIntermedio;
        private final int totalAvanzado;
        private final int totalAltoRendimiento;

        public Estadisticas(int totalEjercicios,
                     int tiempoTotalDisponible,
                     int totalCardio,
                     int totalFuerza,
                     int totalBasico,
                     int totalIntermedio,
                     int totalAvanzado,
                     int totalAltoRendimiento) {
            this.totalEjercicios = totalEjercicios;
            this.tiempoTotalDisponible = tiempoTotalDisponible;
            this.totalCardio = totalCardio;
            this.totalFuerza = totalFuerza;
            this.totalBasico = totalBasico;
            this.totalIntermedio = totalIntermedio;
            this.totalAvanzado = totalAvanzado;
            this.totalAltoRendimiento = totalAltoRendimiento;
        }

        public int getTotalEjercicios() {
            return totalEjercicios;
        }

        public int getTiempoTotalDisponible() {
            return tiempoTotalDisponible;
        }

        public int getTotalCardio() {
            return totalCardio;
        }

        public int getTotalFuerza() {
            return totalFuerza;
        }

        public int getTotalBasico() {
            return totalBasico;
        }

        public int getTotalIntermedio() {
            return totalIntermedio;
        }

        public int getTotalAvanzado() {
            return totalAvanzado;
        }

        public int getTotalAltoRendimiento() {
            return totalAltoRendimiento;
        }
    }

    public static final class ResumenRutina {
        private final int totalCardio;
        private final int totalFuerza;

        private final int totalBasico;
        private final int totalIntermedio;
        private final int totalAvanzado;
        private final int totalAltoRendimiento;

        private final int tiempoTotal;

        public ResumenRutina(int totalCardio,
                             int totalFuerza,
                             int totalBasico,
                             int totalIntermedio,
                             int totalAvanzado,
                             int totalAltoRendimiento,
                             int tiempoTotal) {
            this.totalCardio = totalCardio;
            this.totalFuerza = totalFuerza;
            this.totalBasico = totalBasico;
            this.totalIntermedio = totalIntermedio;
            this.totalAvanzado = totalAvanzado;
            this.totalAltoRendimiento = totalAltoRendimiento;
            this.tiempoTotal = tiempoTotal;
        }

        public int getTotalCardio() {
            return totalCardio;
        }

        public int getTotalFuerza() {
            return totalFuerza;
        }

        public int getTotalBasico() {
            return totalBasico;
        }

        public int getTotalIntermedio() {
            return totalIntermedio;
        }

        public int getTotalAvanzado() {
            return totalAvanzado;
        }

        public int getTotalAltoRendimiento() {
            return totalAltoRendimiento;
        }

        public int getTiempoTotal() {
            return tiempoTotal;
        }
    }

    private final CargadorEjerciciosCsv cargadorCsv = new CargadorEjerciciosCsv();

    private final ExecutorService worker = Executors.newSingleThreadExecutor(r -> {
        Thread t = new Thread(r, "backend-worker");
        t.setDaemon(true);
        return t;
    });

    private final ExecutorService notifier = Executors.newSingleThreadExecutor(r -> {
        Thread t = new Thread(r, "backend-notifier");
        t.setDaemon(true);
        return t;
    });

    private final List<Escucha> escuchas = new CopyOnWriteArrayList<>();
    private final List<RutinaListener> rutinaListeners = new CopyOnWriteArrayList<>();

    private List<Ejercicio> ejercicios = new ArrayList<>();
    private Estadisticas estadisticas;

    private final List<Rutina> historialRutinas = new ArrayList<>();
    private Rutina rutinaActual;
    private int indiceRevision = -1;

    public void suscribir(Escucha escucha) {
        if (escucha != null) escuchas.add(escucha);
    }

    public void desuscribir(Escucha escucha) {
        escuchas.remove(escucha);
    }

    public void suscribirRutinaListener(RutinaListener listener) {
        if (listener != null) rutinaListeners.add(listener);
    }

    public void desuscribirRutinaListener(RutinaListener listener) {
        rutinaListeners.remove(listener);
    }

    public void enviar(final Comando comando) {
        if (comando == null) return;
        worker.submit(() -> procesar(comando));
    }

    public void detener() {
        worker.shutdownNow();
        notifier.shutdownNow();
    }

    private void procesar(Comando comando) {
        try {
            if (comando instanceof ComandoCargarEjercicios) {
                procesarCargar((ComandoCargarEjercicios) comando);
                return;
            }
            if (comando instanceof ComandoGenerarRutina) {
                procesarGenerar((ComandoGenerarRutina) comando);
                return;
            }
            if (comando instanceof ComandoEjercicioAnterior) {
                procesarAnterior();
                return;
            }
            if (comando instanceof ComandoEjercicioSiguiente) {
                procesarSiguiente();
                return;
            }
            if (comando instanceof ComandoSolicitarResumen) {
                procesarResumen();
                return;
            }

            publicar(new EventoError("Comando no soportado"));
        } catch (Exception e) {
            publicar(new EventoError("Error: " + obtenerMensajeSeguro(e)));
        }
    }

    private void procesarCargar(ComandoCargarEjercicios c) {
        try {
            if (c.getPath() == null) throw new ExcepcionCargaEjercicios("Ruta nula");
            if (!Files.exists(c.getPath())) throw new ExcepcionCargaEjercicios("Archivo inexistente: " + c.getPath());

            List<Ejercicio> loaded;
            if (c.getKind() == TipoOrigen.CSV) {
                loaded = cargadorCsv.cargar(c.getPath());
            } else if (c.getKind() == TipoOrigen.BD_BINARIA) {
                loaded = cargarDesdeBdBinaria(c.getPath());
            } else {
                throw new ExcepcionCargaEjercicios("Origen de datos no soportado");
            }

            this.ejercicios = loaded;
            this.estadisticas = calcularEstadisticas(loaded);

            this.rutinaActual = null;
            this.indiceRevision = -1;

            publicar(new EventoEjerciciosCargados(estadisticas));
        } catch (ExcepcionCargaEjercicios e) {
            publicar(new EventoError(e.getMessage()));
            publicarErrorRutina(e.getMessage());
        }
    }

    private void procesarGenerar(ComandoGenerarRutina c) {
        try {
            if (ejercicios.isEmpty()) {
                publicar(new EventoError("Debe cargar ejercicios antes de generar una rutina"));
                return;
            }

            validarGeneracion(c);

            Rutina rutina = new Rutina(c.getCliente(), c.getSemana());

            seleccionar(rutina, TipoEjercicio.CARDIOVASCULAR, c.getCantidadCardio(), c.getIntensidadCardio());
            seleccionar(rutina, TipoEjercicio.FUERZA, c.getCantidadFuerza(), c.getIntensidadFuerza());

            if (rutina.getEjercicios().isEmpty()) {
                publicar(new EventoError("No fue posible generar la rutina con los criterios solicitados"));
                return;
            }

            for (Ejercicio e : rutina.getEjercicios()) {
                e.setUltimaSemanaUsado(rutina.getSemana());
            }

            historialRutinas.add(rutina);
            rutinaActual = rutina;
            indiceRevision = 0;

            publicar(new EventoEstadoRevision(rutinaActual.getEjercicios().get(indiceRevision), indiceRevision, rutinaActual.getEjercicios().size()));
            publicarRutinaGenerada(rutinaActual);
        } catch (ExcepcionFormatoDatos e) {
            publicar(new EventoError("Datos inválidos: " + e.getMessage()));
        }
    }

    private void validarGeneracion(ComandoGenerarRutina c) throws ExcepcionFormatoDatos {
        if (c.getCliente() == null || c.getCliente().trim().isEmpty()) {
            throw new ExcepcionFormatoDatos("Cliente vacío");
        }
        if (c.getSemana() <= 0) {
            throw new ExcepcionFormatoDatos("Semana inválida");
        }
        if (c.getCantidadCardio() < 0 || c.getCantidadFuerza() < 0) {
            throw new ExcepcionFormatoDatos("Cantidad inválida");
        }
        if (c.getCantidadCardio() + c.getCantidadFuerza() <= 0) {
            throw new ExcepcionFormatoDatos("Debe solicitar al menos 1 ejercicio");
        }
        if (c.getIntensidadCardio() == null || c.getIntensidadFuerza() == null) {
            throw new ExcepcionFormatoDatos("Intensidad no seleccionada");
        }
    }

    private void procesarAnterior() {
        if (rutinaActual == null || rutinaActual.getEjercicios().isEmpty()) {
            publicar(new EventoError("No hay rutina en revisión"));
            return;
        }
        if (indiceRevision <= 0) {
            indiceRevision = 0;
        } else {
            indiceRevision--;
        }
        publicar(new EventoEstadoRevision(rutinaActual.getEjercicios().get(indiceRevision), indiceRevision, rutinaActual.getEjercicios().size()));
    }

    private void procesarSiguiente() {
        if (rutinaActual == null || rutinaActual.getEjercicios().isEmpty()) {
            publicar(new EventoError("No hay rutina en revisión"));
            return;
        }
        if (indiceRevision < rutinaActual.getEjercicios().size() - 1) {
            indiceRevision++;
        }
        publicar(new EventoEstadoRevision(rutinaActual.getEjercicios().get(indiceRevision), indiceRevision, rutinaActual.getEjercicios().size()));
    }

    private void procesarResumen() {
        if (rutinaActual == null || rutinaActual.getEjercicios().isEmpty()) {
            publicar(new EventoError("No hay rutina generada"));
            return;
        }

        int cardio = 0;
        int fuerza = 0;
        int basico = 0;
        int intermedio = 0;
        int avanzado = 0;
        int alto = 0;

        for (Ejercicio e : rutinaActual.getEjercicios()) {
            if (e.getTipoEjercicio() == TipoEjercicio.CARDIOVASCULAR) cardio++;
            if (e.getTipoEjercicio() == TipoEjercicio.FUERZA) fuerza++;

            if (e.getNivelIntensidad() == Intensidad.BASICO) basico++;
            if (e.getNivelIntensidad() == Intensidad.INTERMEDIO) intermedio++;
            if (e.getNivelIntensidad() == Intensidad.AVANZADO) avanzado++;
            if (e.getNivelIntensidad() == Intensidad.ALTO_RENDIMIENTO) alto++;
        }

        ResumenRutina resumen = new ResumenRutina(cardio, fuerza, basico, intermedio, avanzado, alto, rutinaActual.getTiempoTotal());
        publicar(new EventoResumenRutina(resumen));
    }

    private void seleccionar(Rutina rutina, TipoEjercicio tipo, int cantidad, Intensidad intensidad) {
        if (cantidad <= 0) return;

        int agregados = 0;
        for (Ejercicio e : ejercicios) {
            if (agregados >= cantidad) break;

            if (e.getTipoEjercicio() != tipo) continue;
            if (e.getNivelIntensidad() != intensidad) continue;
            if (rutina.contieneCodigo(e.getCodigoIdentificador())) continue;
            if (usadoSemanaConsecutiva(rutina.getCliente(), e.getCodigoIdentificador(), rutina.getSemana())) continue;

            rutina.add(e);
            agregados++;
        }

        if (agregados < cantidad) {
            publicar(new EventoError("Solo fue posible asignar " + agregados + " ejercicio(s) de tipo " + tipo.getEtiqueta() + " con intensidad " + intensidad.getEtiqueta() + "."));
        }
    }

    private boolean usadoSemanaConsecutiva(String cliente, int codigoEjercicio, int semanaActual) {
        for (Rutina r : historialRutinas) {
            if (!r.getCliente().equals(cliente)) continue;
            int dif = semanaActual - r.getSemana();
            if (dif < 0) dif = -dif;
            if (dif == 1 && r.contieneCodigo(codigoEjercicio)) return true;
        }
        return false;
    }

    private void publicar(final Evento evento) {
        notifier.submit(() -> {
            for (Escucha e : escuchas) {
                try {
                    e.alEvento(evento);
                } catch (Exception ignored) {
                }
            }
        });
    }

    private void publicarRutinaGenerada(Rutina rutina) {
        notifier.submit(() -> {
            for (RutinaListener listener : rutinaListeners) {
                try {
                    listener.onRutinaGenerada(rutina);
                } catch (Exception ignored) {
                }
            }
        });
    }

    private void publicarErrorRutina(String mensaje) {
        notifier.submit(() -> {
            for (RutinaListener listener : rutinaListeners) {
                try {
                    listener.onError(mensaje);
                } catch (Exception ignored) {
                }
            }
        });
    }


    private List<Ejercicio> cargarDesdeBdBinaria(Path path) throws ExcepcionCargaEjercicios {
        if (path == null) throw new ExcepcionCargaEjercicios("Ruta nula");

        try (DataInputStream in = new DataInputStream(new BufferedInputStream(new FileInputStream(path.toFile())))) {
            byte[] magic = new byte[5];
            in.readFully(magic);
            String m = new String(magic, StandardCharsets.US_ASCII);
            if (!"EXDB1".equals(m)) {
                throw new ExcepcionCargaEjercicios("BD binaria inválida (magic incorrecto)");
            }

            int version = in.readInt();
            if (version != 1) {
                throw new ExcepcionCargaEjercicios("BD binaria inválida (versión no soportada): " + version);
            }

            int n = in.readInt();
            if (n < 0) throw new ExcepcionCargaEjercicios("BD binaria inválida (N negativo)");

            List<Ejercicio> out = new ArrayList<>();
            for (int i = 0; i < n; i++) {
                int codigo = in.readInt();
                String nombre = in.readUTF();
                String tipoRaw = in.readUTF();
                String intensidadRaw = in.readUTF();
                int tiempo = in.readInt();
                String descripcion = in.readUTF();
                int ultima = in.readInt();

                TipoEjercicio tipo = TipoEjercicio.parse(tipoRaw);
                Intensidad intensidad = Intensidad.parse(intensidadRaw);
                if (codigo <= 0) throw new ExcepcionCargaEjercicios("Código inválido en BD");
                if (tiempo <= 0) throw new ExcepcionCargaEjercicios("Tiempo inválido en BD");

                out.add(crearEjercicio(tipo, codigo, nombre, intensidad, tiempo, descripcion, ultima));
            }
            return out;
        } catch (IOException e) {
            throw new ExcepcionCargaEjercicios("No se pudo leer la BD: " + obtenerMensajeSeguro(e));
        } catch (ExcepcionFormatoDatos e) {
            throw new ExcepcionCargaEjercicios("BD con datos inválidos: " + e.getMessage());
        }
    }

    private Ejercicio crearEjercicio(TipoEjercicio tipo,
                                     int codigo,
                                     String nombre,
                                     Intensidad intensidad,
                                     int tiempo,
                                     String descripcion,
                                     int ultima) {
        if (tipo == TipoEjercicio.CARDIOVASCULAR) {
            return new EjercicioCardiovascular(codigo, nombre, intensidad, tiempo, descripcion, ultima);
        }
        return new EjercicioFuerza(codigo, nombre, intensidad, tiempo, descripcion, ultima);
    }

    private Estadisticas calcularEstadisticas(List<Ejercicio> ejercicios) {
        if (ejercicios == null) return new Estadisticas(0, 0, 0, 0, 0, 0, 0, 0);

        int tiempoTotal = 0;
        int cardio = 0;
        int fuerza = 0;
        int basico = 0;
        int intermedio = 0;
        int avanzado = 0;
        int alto = 0;

        for (Ejercicio e : ejercicios) {
            tiempoTotal += e.getTiempoEstimadoMinutos();
            if (e.getTipoEjercicio() == TipoEjercicio.CARDIOVASCULAR) cardio++;
            if (e.getTipoEjercicio() == TipoEjercicio.FUERZA) fuerza++;

            if (e.getNivelIntensidad() == Intensidad.BASICO) basico++;
            if (e.getNivelIntensidad() == Intensidad.INTERMEDIO) intermedio++;
            if (e.getNivelIntensidad() == Intensidad.AVANZADO) avanzado++;
            if (e.getNivelIntensidad() == Intensidad.ALTO_RENDIMIENTO) alto++;
        }

        return new Estadisticas(ejercicios.size(), tiempoTotal, cardio, fuerza, basico, intermedio, avanzado, alto);
    }

    private static String normalizar(String raw) {
        String s = raw.trim().toLowerCase();
        s = s.replace('á', 'a');
        s = s.replace('é', 'e');
        s = s.replace('í', 'i');
        s = s.replace('ó', 'o');
        s = s.replace('ú', 'u');
        s = s.replace('ñ', 'n');
        return s;
    }

    private static String obtenerMensajeSeguro(Exception e) {
        if (e == null) return "";
        String m = e.getMessage();
        return m != null ? m : e.getClass().getSimpleName();
    }

    public List<Ejercicio> getEjerciciosCargadosSoloLectura() {
        return Collections.unmodifiableList(ejercicios);
    }
}
