import java.util.*;
import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

// ==================== CLASE USUARIO ====================
class Usuario implements Serializable {
    private String nombre;
    private int puntos;
    private List<String> logros;

    public Usuario(String nombre) {
        this.nombre = nombre;
        this.puntos = 100;
        this.logros = new ArrayList<>();
        this.logros.add("Nuevo Jugador");
    }

    public void agregarPuntos(int cantidad) {
        puntos += cantidad;
        verificarLogros();
    }

    public void restarPuntos(int cantidad) {
        puntos = Math.max(0, puntos - cantidad);
    }

    private void verificarLogros() {
        if (puntos >= 500 && !logros.contains("Principiante")) {
            logros.add("Principiante");
            System.out.println("¡Logro desbloqueado: Principiante!");
        }
        if (puntos >= 1000 && !logros.contains("Experto")) {
            logros.add("Experto");
            System.out.println("¡Logro desbloqueado: Experto!");
        }
        if (puntos >= 2000 && !logros.contains("Maestro")) {
            logros.add("Maestro");
            System.out.println("¡Logro desbloqueado: Maestro del Sudoku!");
        }
    }

    public String getNombre() { return nombre; }
    public int getPuntos() { return puntos; }
    public List<String> getLogros() { return logros; }
}
