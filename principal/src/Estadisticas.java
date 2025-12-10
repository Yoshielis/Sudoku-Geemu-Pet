import java.util.*;
import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

// ==================== CLASE ESTADÍSTICAS ====================
class Estadisticas implements Serializable {
    private List<Partida> partidas;
    private List<Record> records;

    public Estadisticas() {
        partidas = new ArrayList<>();
        records = new ArrayList<>();
        cargarRecordsIniciales();
    }

    private void cargarRecordsIniciales() {
        records.add(new Record("Ana", 1500));
        records.add(new Record("Carlos", 1200));
        records.add(new Record("Beatriz", 900));
        records.add(new Record("David", 800));
        records.add(new Record("Elena", 600));
    }

    public void registrarPartida(int dificultad, int puntos, boolean completada) {
        Partida partida = new Partida(dificultad, puntos, completada);
        partidas.add(partida);

        if (completada && puntos > 0) {
            ordenarRecords();
        }
    }

    private void ordenarRecords() {
        Collections.sort(records, (a, b) -> Integer.compare(b.getPuntos(), a.getPuntos()));
    }

    public void mostrarEstadisticas() {
        if (partidas.isEmpty()) {
            System.out.println("Aún no hay partidas jugadas.");
            return;
        }

        int total = partidas.size();
        int ganadas = (int) partidas.stream().filter(Partida::isCompletada).count();
        int puntosTotales = partidas.stream().mapToInt(Partida::getPuntos).sum();

        Map<Integer, Integer> partidasPorDificultad = new HashMap<>();
        for (Partida partida : partidas) {
            int dificultad = partida.getDificultad();
            partidasPorDificultad.put(dificultad,
                    partidasPorDificultad.getOrDefault(dificultad, 0) + 1);
        }

        System.out.println("Total partidas: " + total);
        System.out.println("Partidas ganadas: " + ganadas);
        System.out.printf("Porcentaje de victorias: %.1f%%\n", (ganadas * 100.0 / total));
        System.out.println("Puntos totales: " + puntosTotales);
        System.out.println("\nPartidas por dificultad:");
        System.out.println("  Fácil: " + partidasPorDificultad.getOrDefault(1, 0));
        System.out.println("  Medio: " + partidasPorDificultad.getOrDefault(2, 0));
        System.out.println("  Difícil: " + partidasPorDificultad.getOrDefault(3, 0));
    }

    public void mostrarRanking() {
        System.out.println("Pos.  Nombre       Puntos");
        System.out.println("-------------------------");

        for (int i = 0; i < Math.min(records.size(), 10); i++) {
            Record record = records.get(i);
            System.out.printf("%2d.   %-10s  %6d\n",
                    i + 1, record.getNombre(), record.getPuntos());
        }
    }

    public int getTotalPartidas() {
        return partidas.size();
    }
}