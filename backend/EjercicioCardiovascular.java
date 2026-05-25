package backend;

import backend.ControladorBackend.Intensidad;
import backend.ControladorBackend.TipoEjercicio;

public class EjercicioCardiovascular extends Ejercicio {
    public EjercicioCardiovascular(int codigoIdentificador,
                                   String nombreEjercicio,
                                   Intensidad nivelIntensidad,
                                   int tiempoEstimadoMinutos,
                                   String descripcionEjecucion,
                                   int ultimaSemanaUsado) {
        super(codigoIdentificador,
                nombreEjercicio,
                TipoEjercicio.CARDIOVASCULAR,
                nivelIntensidad,
                tiempoEstimadoMinutos,
                descripcionEjecucion,
                ultimaSemanaUsado);
    }
}
