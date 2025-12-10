import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// ==================== CLASE ESTADÍSTICAS ====================
class Estadisticas {
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

        if (completada) {
            // Método de ordenamiento: Insertion Sort para mantener ranking actualizado
            ordenarRecords();
        }
    }

    // MÉTODO DE ORDENAMIENTO: Insertion Sort
    private void ordenarRecords() {
        for (int i = 1; i < records.size(); i++) {
            Record key = records.get(i);
            int j = i - 1;

            // MÉTODO DE BÚSQUEDA: Comparación secuencial dentro del insertion sort
            while (j >= 0 && records.get(j).getPuntos() < key.getPuntos()) {
                records.set(j + 1, records.get(j));
                j--;
            }
            records.set(j + 1, key);
        }
    }

    // MÉTODO DE BÚSQUEDA: Búsqueda secuencial para encontrar posición en ranking
    public int buscarPosicionRanking(int puntos) {
        for (int i = 0; i < records.size(); i++) {
            if (puntos >= records.get(i).getPuntos()) {
                return i + 1;
            }
        }
        return records.size() + 1;
    }

    public void mostrarEstadisticas() {
        System.out.println("\n=== ESTADÍSTICAS ===");

        if (partidas.isEmpty()) {
            System.out.println("Aún no hay partidas jugadas.");
            return;
        }

        int totalPartidas = partidas.size();
        int partidasGanadas = 0;
        int puntosTotales = 0;
        Map<Integer, Integer> partidasPorDificultad = new HashMap<>();

        // Análisis de datos usando búsqueda secuencial
        for (Partida partida : partidas) {
            if (partida.isCompletada()) {
                partidasGanadas++;
            }
            puntosTotales += partida.getPuntos();

            int dificultad = partida.getDificultad();
            partidasPorDificultad.put(dificultad,
                    partidasPorDificultad.getOrDefault(dificultad, 0) + 1);
        }

        System.out.println("Total partidas: " + totalPartidas);
        System.out.println("Partidas ganadas: " + partidasGanadas);
        System.out.printf("Porcentaje de victorias: %.1f%%\n",
                (partidasGanadas * 100.0 / totalPartidas));
        System.out.println("Puntos totales: " + puntosTotales);

        System.out.println("\nPartidas por dificultad:");
        System.out.println("  Fácil: " + partidasPorDificultad.getOrDefault(1, 0));
        System.out.println("  Medio: " + partidasPorDificultad.getOrDefault(2, 0));
        System.out.println("  Difícil: " + partidasPorDificultad.getOrDefault(3, 0));
    }

    public void mostrarRanking() {
        System.out.println("\n=== RANKING GLOBAL ===");
        System.out.println("Pos.  Nombre       Puntos");
        System.out.println("-------------------------");

        for (int i = 0; i < Math.min(records.size(), 10); i++) {
            Record record = records.get(i);
            System.out.printf("%2d.   %-10s  %6d\n",
                    i + 1, record.getNombre(), record.getPuntos());
        }
    }
}