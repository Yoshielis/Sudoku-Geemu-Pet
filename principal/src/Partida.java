import java.util.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

class Partida {
    private LocalDate fecha;
    private int dificultad;
    private int puntos;
    private boolean completada;

    public Partida(int dificultad, int puntos, boolean completada) {
        this.fecha = LocalDate.now();
        this.dificultad = dificultad;
        this.puntos = puntos;
        this.completada = completada;
    }

    // Getters
    public LocalDate getFecha() { return fecha; }
    public int getDificultad() { return dificultad; }
    public int getPuntos() { return puntos; }
    public boolean isCompletada() { return completada; }
}


