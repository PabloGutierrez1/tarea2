package backend;

import java.util.ArrayList;
import java.util.List;

public class Rutina {
    private final String cliente;
    private final int semana;
    private final List<Ejercicio> ejercicios = new ArrayList<>();

    public Rutina(String cliente, int semana) {
        this.cliente = cliente;
        this.semana = semana;
    }

    public String getCliente() {
        return cliente;
    }

    public int getSemana() {
        return semana;
    }

    public List<Ejercicio> getEjercicios() {
        return ejercicios;
    }

    public boolean contieneCodigo(int codigo) {
        for (Ejercicio e : ejercicios) {
            if (e.getCodigoIdentificador() == codigo) return true;
        }
        return false;
    }

    public void add(Ejercicio e) {
        ejercicios.add(e);
    }

    public int getTiempoTotal() {
        int total = 0;
        for (Ejercicio e : ejercicios) {
            total += e.getTiempoEstimadoMinutos();
        }
        return total;
    }
}
