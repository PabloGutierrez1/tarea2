package backend;

import backend.ControladorBackend.Intensidad;
import backend.ControladorBackend.TipoEjercicio;

public abstract class Ejercicio {
    private final int codigoIdentificador;
    private final String nombreEjercicio;
    private final TipoEjercicio tipoEjercicio;
    private final Intensidad nivelIntensidad;
    private final int tiempoEstimadoMinutos;
    private final String descripcionEjecucion;
    private int ultimaSemanaUsado;

    protected Ejercicio(int codigoIdentificador,
                        String nombreEjercicio,
                        TipoEjercicio tipoEjercicio,
                        Intensidad nivelIntensidad,
                        int tiempoEstimadoMinutos,
                        String descripcionEjecucion,
                        int ultimaSemanaUsado) {
        this.codigoIdentificador = codigoIdentificador;
        this.nombreEjercicio = nombreEjercicio;
        this.tipoEjercicio = tipoEjercicio;
        this.nivelIntensidad = nivelIntensidad;
        this.tiempoEstimadoMinutos = tiempoEstimadoMinutos;
        this.descripcionEjecucion = descripcionEjecucion;
        this.ultimaSemanaUsado = ultimaSemanaUsado;
    }

    public int getCodigoIdentificador() {
        return codigoIdentificador;
    }

    public String getNombreEjercicio() {
        return nombreEjercicio;
    }

    public TipoEjercicio getTipoEjercicio() {
        return tipoEjercicio;
    }

    public Intensidad getNivelIntensidad() {
        return nivelIntensidad;
    }

    public int getTiempoEstimadoMinutos() {
        return tiempoEstimadoMinutos;
    }

    public String getDescripcionEjecucion() {
        return descripcionEjecucion;
    }

    public int getUltimaSemanaUsado() {
        return ultimaSemanaUsado;
    }

    public void setUltimaSemanaUsado(int ultimaSemanaUsado) {
        this.ultimaSemanaUsado = ultimaSemanaUsado;
    }
}
