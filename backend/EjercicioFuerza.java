package backend;

import backend.ControladorBackend.Intensidad;
import backend.ControladorBackend.TipoEjercicio;

public class EjercicioFuerza extends Ejercicio {
    public EjercicioFuerza(int codigoIdentificador,
                           String nombreEjercicio,
                           Intensidad nivelIntensidad,
                           int tiempoEstimadoMinutos,
                           String descripcionEjecucion,
                           int ultimaSemanaUsado) {
        super(codigoIdentificador,
                nombreEjercicio,
                TipoEjercicio.FUERZA,
                nivelIntensidad,
                tiempoEstimadoMinutos,
                descripcionEjecucion,
                ultimaSemanaUsado);
    }
}
