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
        for (int i = 1; i < records.size(); i++) {
            Record key = records.get(i);
            int j = i - 1;

            while (j >= 0 && records.get(j).getPuntos() < key.getPuntos()) {
                records.set(j + 1, records.get(j));
                j--;
            }
            records.set(j + 1, key);
        }
    }

    public void mostrarEstadisticasCentrado() {
        ConsoleUtils.clearScreen();
        ConsoleUtils.dibujarMarco(60, 22);

        ConsoleUtils.setCursorPosition(20, 3);
        System.out.println("=== ESTADÍSTICAS ===");

        if (partidas.isEmpty()) {
            ConsoleUtils.setCursorPosition(15, 10);
            System.out.println("Aún no hay partidas jugadas.");
            return;
        }

        int totalPartidas = partidas.size();
        int partidasGanadas = 0;
        int puntosTotales = 0;
        Map<Integer, Integer> partidasPorDificultad = new HashMap<>();

        for (Partida partida : partidas) {
            if (partida.isCompletada()) partidasGanadas++;
            puntosTotales += partida.getPuntos();

            int dificultad = partida.getDificultad();
            partidasPorDificultad.put(dificultad,
                    partidasPorDificultad.getOrDefault(dificultad, 0) + 1);
        }

        ConsoleUtils.setCursorPosition(5, 6);
        System.out.println("Total partidas: " + totalPartidas);

        ConsoleUtils.setCursorPosition(5, 8);
        System.out.println("Partidas ganadas: " + partidasGanadas);

        ConsoleUtils.setCursorPosition(5, 10);
        System.out.printf("Porcentaje de victorias: %.1f%%\n",
                (partidasGanadas * 100.0 / totalPartidas));

        ConsoleUtils.setCursorPosition(5, 12);
        System.out.println("Puntos totales: " + puntosTotales);

        ConsoleUtils.setCursorPosition(5, 14);
        System.out.println("\nPartidas por dificultad:");
        ConsoleUtils.setCursorPosition(5, 15);
        System.out.println("  Fácil:   " + partidasPorDificultad.getOrDefault(1, 0));
        ConsoleUtils.setCursorPosition(5, 16);
        System.out.println("  Medio:   " + partidasPorDificultad.getOrDefault(2, 0));
        ConsoleUtils.setCursorPosition(5, 17);
        System.out.println("  Difícil: " + partidasPorDificultad.getOrDefault(3, 0));
    }

    public void mostrarRankingCentrado() {
        ConsoleUtils.clearScreen();
        ConsoleUtils.dibujarMarco(50, 20);

        ConsoleUtils.setCursorPosition(18, 3);
        System.out.println("=== RANKING GLOBAL ===");
        ConsoleUtils.setCursorPosition(10, 4);
        System.out.println("Pos.  Nombre       Puntos");
        ConsoleUtils.setCursorPosition(10, 5);
        System.out.println("-------------------------");

        for (int i = 0; i < Math.min(records.size(), 10); i++) {
            ConsoleUtils.setCursorPosition(10, 6 + i);
            Record record = records.get(i);
            System.out.printf("%2d.   %-10s  %6d\n",
                    i + 1, record.getNombre(), record.getPuntos());
        }
    }
}


