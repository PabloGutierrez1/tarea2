package backend;

public interface RutinaListener {
    void onRutinaGenerada(Rutina rutina);

    void onError(String mensaje);
}
