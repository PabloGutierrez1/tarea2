package backend;

import backend.ControladorBackend.Intensidad;
import backend.ControladorBackend.TipoEjercicio;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class CargadorEjerciciosCsv {
    public List<Ejercicio> cargar(Path path) throws ExcepcionCargaEjercicios {
        if (path == null) throw new ExcepcionCargaEjercicios("Ruta nula");
        if (!Files.exists(path)) throw new ExcepcionCargaEjercicios("Archivo inexistente: " + path);
        if (!Files.isRegularFile(path)) throw new ExcepcionCargaEjercicios("Ruta no es archivo: " + path);

        List<Ejercicio> out = new ArrayList<>();
        try (BufferedReader br = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
            String line;
            boolean firstLine = true;
            while ((line = br.readLine()) != null) {
                String raw = line.trim();
                if (raw.isEmpty()) continue;
                if (raw.startsWith("#")) continue;

                String[] parts = raw.split(";", -1);
                if (parts.length != 7) {
                    throw new ExcepcionFormatoDatos("Se esperaban 7 campos: " + raw);
                }

                if (firstLine) {
                    firstLine = false;
                    if (!esEntero(parts[0].trim())) {
                        continue;
                    }
                }

                out.add(analizarEjercicioCsv(parts));
            }
        } catch (IOException e) {
            throw new ExcepcionCargaEjercicios("No se pudo leer el archivo: " + obtenerMensajeSeguro(e));
        } catch (ExcepcionFormatoDatos e) {
            throw new ExcepcionCargaEjercicios("Formato invalido: " + e.getMessage());
        }

        if (out.isEmpty()) {
            throw new ExcepcionCargaEjercicios("No se cargaron ejercicios (archivo vacio o invalido)");
        }

        return out;
    }

    private Ejercicio analizarEjercicioCsv(String[] parts) throws ExcepcionFormatoDatos {
        int codigo = analizarEnteroPositivo(parts[0].trim(), "codigo");
        String nombre = requerirNoVacio(parts[1], "nombre");
        TipoEjercicio tipo = TipoEjercicio.parse(parts[2].trim());
        Intensidad intensidad = Intensidad.parse(parts[3].trim());
        int tiempo = analizarEnteroPositivo(parts[4].trim(), "tiempo");
        String desc = requerirNoVacio(parts[5], "descripcion");
        int ultima = analizarEnteroNoNegativo(parts[6].trim(), "ultimaSemanaUsado");

        if (tipo == TipoEjercicio.CARDIOVASCULAR) {
            return new EjercicioCardiovascular(codigo, nombre, intensidad, tiempo, desc, ultima);
        }
        return new EjercicioFuerza(codigo, nombre, intensidad, tiempo, desc, ultima);
    }

    private static String requerirNoVacio(String raw, String field) throws ExcepcionFormatoDatos {
        if (raw == null) throw new ExcepcionFormatoDatos("Campo vacio: " + field);
        String s = raw.trim();
        if (s.isEmpty()) throw new ExcepcionFormatoDatos("Campo vacio: " + field);
        return s;
    }

    private static int analizarEnteroPositivo(String raw, String field) throws ExcepcionFormatoDatos {
        int v = analizarEntero(raw, field);
        if (v <= 0) throw new ExcepcionFormatoDatos("Campo invalido (debe ser > 0): " + field);
        return v;
    }

    private static int analizarEnteroNoNegativo(String raw, String field) throws ExcepcionFormatoDatos {
        int v = analizarEntero(raw, field);
        if (v < 0) throw new ExcepcionFormatoDatos("Campo invalido (debe ser >= 0): " + field);
        return v;
    }

    private static int analizarEntero(String raw, String field) throws ExcepcionFormatoDatos {
        try {
            return Integer.parseInt(raw.trim());
        } catch (Exception e) {
            throw new ExcepcionFormatoDatos("Campo no es numero (" + field + "): " + raw);
        }
    }

    private static boolean esEntero(String raw) {
        if (raw == null) return false;
        try {
            Integer.parseInt(raw.trim());
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private static String obtenerMensajeSeguro(Exception e) {
        if (e == null) return "";
        String m = e.getMessage();
        return m != null ? m : e.getClass().getSimpleName();
    }
}
