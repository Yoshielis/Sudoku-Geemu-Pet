import java.util.*;
import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

// ==================== CLASE PRINCIPAL ====================
public class SudokuMascotaVirtual {
    private static Scanner scanner = new Scanner(System.in);
    private static Mascota mascota;
    private static Sudoku sudoku;
    private static Usuario usuario;
    private static Estadisticas estadisticas;

    public static void main(String[] args) {
        inicializarSistema();
        mostrarMenuPrincipal();
    }

    private static void inicializarSistema() {
        limpiarPantalla();
        mostrarTitulo();

        System.out.println("\n¿Deseas cargar una partida guardada? (S/N): ");
        String respuesta = scanner.nextLine().toUpperCase();

        if (respuesta.equals("S") && cargarPartida()) {
            System.out.println("\n✓ Partida cargada exitosamente!");
        } else {
            crearNuevaPartida();
        }

        System.out.println("\n¡Bienvenido, " + usuario.getNombre() + "!");
        System.out.println("Tu mascota " + mascota.getNombre() + " (" + mascota.getEspecie() + ") te espera.");

        esperarEnter();
    }

    private static void crearNuevaPartida() {
        limpiarPantalla();
        mostrarTitulo();

        System.out.println("\n=== CREAR NUEVA PARTIDA ===");
        System.out.print("Ingresa tu nombre: ");
        String nombre = scanner.nextLine();

        usuario = new Usuario(nombre);
        estadisticas = new Estadisticas();
        sudoku = new Sudoku();

        seleccionarEspecieMascota();
    }

    private static void seleccionarEspecieMascota() {
        limpiarPantalla();
        mostrarTitulo();

        System.out.println("\n=== SELECCIONA LA ESPECIE DE TU MASCOTA ===");

        String[] especies = {"Perro", "Gato", "Conejo", "Pájaro", "Dragón"};
        String[] emojis = {"(🐕)", "(🐈)", "(🐇)", "(🐦)", "(🐉)"};

        for (int i = 0; i < especies.length; i++) {
            System.out.println((i+1) + ". " + emojis[i] + " " + especies[i]);
        }

        System.out.print("\nSelecciona una opción (1-5): ");
        int opcion = leerEntero();

        if (opcion < 1 || opcion > 5) opcion = 1;

        System.out.print("\nIngresa el nombre de tu mascota: ");
        String nombreMascota = scanner.nextLine();

        mascota = new Mascota(nombreMascota, especies[opcion-1], emojis[opcion-1]);

        System.out.println("\n¡Mascota creada! " + emojis[opcion-1] + " " + nombreMascota);
        esperarEnter();
    }

    private static void mostrarMenuPrincipal() {
        boolean salir = false;

        while (!salir) {
            limpiarPantalla();
            mostrarTitulo();

            System.out.println("\n=== MENÚ PRINCIPAL ===");
            System.out.println("1. Jugar Sudoku");
            System.out.println("2. Cuidar Mascota");
            System.out.println("3. Ver Estadísticas");
            System.out.println("4. Mostrar Ranking");
            System.out.println("5. Guardar Partida");
            System.out.println("6. Ver Instrucciones");
            System.out.println("7. Salir");

            System.out.println("\n--- TU MASCOTA ---");
            mascota.mostrarEstadoMini();
            System.out.println("\nTus puntos: " + usuario.getPuntos());

            System.out.print("\nSelecciona una opción (1-7): ");
            int opcion = leerEntero();

            switch (opcion) {
                case 1:
                    jugarSudoku();
                    break;
                case 2:
                    mostrarMenuMascota();
                    break;
                case 3:
                    mostrarEstadisticas();
                    break;
                case 4:
                    mostrarRanking();
                    break;
                case 5:
                    guardarPartida();
                    break;
                case 6:
                    mostrarInstrucciones();
                    break;
                case 7:
                    salir = true;
                    despedida();
                    break;
                default:
                    mostrarMensaje("Opción no válida.");
            }
        }
    }

    private static void jugarSudoku() {
        limpiarPantalla();
        mostrarTitulo();

        System.out.println("\n=== JUEGO DE SUDOKU ===");

        // Mostrar mensaje de ánimo de la mascota
        mascota.darAnimo();

        System.out.println("\nSelecciona la dificultad:");
        System.out.println("1. Fácil (40 números dados)");
        System.out.println("2. Medio (35 números dados)");
        System.out.println("3. Difícil (30 números dados)");
        System.out.println("4. Cargar partida guardada");

        System.out.print("\nOpción: ");
        int dificultad = leerEntero();

        int celdasVacias = 0;
        boolean cargar = false;

        switch (dificultad) {
            case 1: celdasVacias = 41; break;
            case 2: celdasVacias = 46; break;
            case 3: celdasVacias = 51; break;
            case 4:
                cargar = true;
                if (!cargarSudoku()) {
                    mostrarMensaje("No hay partida de Sudoku guardada.");
                    return;
                }
                break;
            default: celdasVacias = 46;
        }

        if (!cargar) {
            sudoku.generarPuzzle(celdasVacias);
        }

        // Jugar el Sudoku
        boolean completado = sudoku.jugar(usuario, mascota);

        // Mostrar resultado
        if (completado) {
            limpiarPantalla();
            mostrarTitulo();

            int puntos = calcularPuntos(dificultad);
            usuario.agregarPuntos(puntos);
            mascota.alimentar(puntos / 10);
            mascota.actualizarFeliz();

            System.out.println("\n¡FELICIDADES! 🎉");
            System.out.println("Completaste el Sudoku correctamente.");
            System.out.println("Ganaste " + puntos + " puntos.");

            mascota.celebrar();

            estadisticas.registrarPartida(dificultad, puntos, true);
            guardarSudoku();

            esperarEnter();
        } else {
            estadisticas.registrarPartida(dificultad, 0, false);
            guardarSudoku();
        }
    }

    private static void mostrarMenuMascota() {
        boolean volver = false;

        while (!volver) {
            limpiarPantalla();
            mostrarTitulo();

            System.out.println("\n=== CUIDAR MASCOTA ===");

            // Mostrar estado completo de la mascota
            mascota.mostrarEstadoCompleto();

            System.out.println("\nOPCIONES:");
            System.out.println("1. Alimentar (10 puntos)");
            System.out.println("2. Jugar con mascota (5 puntos)");
            System.out.println("3. Dar medicina (15 puntos)");
            System.out.println("4. Pedir pista para Sudoku (20 puntos)");
            System.out.println("5. Cambiar nombre");
            System.out.println("6. Volver al menú principal");

            System.out.print("\nSelecciona una opción: ");
            int opcion = leerEntero();

            switch (opcion) {
                case 1:
                    if (usuario.getPuntos() >= 10) {
                        usuario.restarPuntos(10);
                        mascota.alimentar(20);
                        mostrarMensaje("¡Has alimentado a " + mascota.getNombre() + "!");
                    } else {
                        mostrarMensaje("No tienes puntos suficientes.");
                    }
                    break;
                case 2:
                    if (usuario.getPuntos() >= 5) {
                        usuario.restarPuntos(5);
                        mascota.jugar();
                        mostrarMensaje("¡Has jugado con " + mascota.getNombre() + "!");
                    } else {
                        mostrarMensaje("No tienes puntos suficientes.");
                    }
                    break;
                case 3:
                    if (usuario.getPuntos() >= 15) {
                        usuario.restarPuntos(15);
                        mascota.curar();
                        mostrarMensaje("¡Has curado a " + mascota.getNombre() + "!");
                    } else {
                        mostrarMensaje("No tienes puntos suficientes.");
                    }
                    break;
                case 4:
                    if (usuario.getPuntos() >= 20) {
                        usuario.restarPuntos(20);
                        int[] pista = sudoku.obtenerPista();
                        if (pista != null) {
                            mostrarMensaje("Pista: Intenta " + pista[2] +
                                    " en posición (" + (pista[0]+1) + "," + (pista[1]+1) + ")");
                            mascota.darPista();
                        } else {
                            mostrarMensaje("No hay pistas disponibles en este momento.");
                        }
                    } else {
                        mostrarMensaje("No tienes puntos suficientes.");
                    }
                    break;
                case 5:
                    System.out.print("\nNuevo nombre: ");
                    String nuevoNombre = scanner.nextLine();
                    mascota.setNombre(nuevoNombre);
                    mostrarMensaje("¡Nombre cambiado a " + nuevoNombre + "!");
                    break;
                case 6:
                    volver = true;
                    break;
                default:
                    mostrarMensaje("Opción no válida.");
            }

            mascota.actualizarEstado();
        }
    }

    private static void mostrarEstadisticas() {
        limpiarPantalla();
        mostrarTitulo();

        System.out.println("\n=== ESTADÍSTICAS ===");

        if (estadisticas.getTotalPartidas() == 0) {
            System.out.println("Aún no hay partidas jugadas.");
        } else {
            estadisticas.mostrarEstadisticas();
        }

        esperarEnter();
    }

    private static void mostrarRanking() {
        limpiarPantalla();
        mostrarTitulo();

        System.out.println("\n=== RANKING GLOBAL ===");
        estadisticas.mostrarRanking();

        esperarEnter();
    }

    private static void mostrarInstrucciones() {
        limpiarPantalla();
        mostrarTitulo();

        System.out.println("\n=== INSTRUCCIONES DEL JUEGO ===");
        System.out.println("");
        System.out.println("1. JUEGO DE SUDOKU:");
        System.out.println("   • Completa el tablero con números 1-9");
        System.out.println("   • Cada fila, columna y región 3x3 debe tener");
        System.out.println("     números únicos");
        System.out.println("   • Usa formato: fila,columna,valor (ej: 5,3,7)");
        System.out.println("   • Comandos especiales:");
        System.out.println("     - 'pista' - Obtener ayuda");
        System.out.println("     - 'guardar' - Guardar partida");
        System.out.println("     - 'salir' - Volver al menú");
        System.out.println("");
        System.out.println("2. CUIDADO DE MASCOTA:");
        System.out.println("   • Gasta puntos para cuidar tu mascota");
        System.out.println("   • Mascota feliz da mejores pistas");
        System.out.println("   • Si la mascota está triste o enferma,");
        System.out.println("     afecta tu juego");
        System.out.println("");
        System.out.println("3. PUNTOS Y RECOMPENSAS:");
        System.out.println("   • Fácil: 100 puntos");
        System.out.println("   • Medio: 200 puntos");
        System.out.println("   • Difícil: 300 puntos");
        System.out.println("   • Desbloquea logros especiales");
        System.out.println("");
        System.out.println("¡Diviértete y cuida a tu mascota!");

        esperarEnter();
    }

    private static void despedida() {
        limpiarPantalla();
        mostrarTitulo();

        System.out.println("\n¡Gracias por jugar!");
        System.out.println("Hasta pronto, " + usuario.getNombre() + "!");

        mascota.despedirse();

        guardarPartida();

        System.out.println("\nPartida guardada automáticamente.");
        System.out.println("Puntos finales: " + usuario.getPuntos());
        System.out.println("Partidas jugadas: " + estadisticas.getTotalPartidas());

        esperarEnter();
    }

    private static int calcularPuntos(int dificultad) {
        int base = 100;
        switch (dificultad) {
            case 1: return base;
            case 2: return base * 2;
            case 3: return base * 3;
            default: return base;
        }
    }

    // ========== MÉTODOS DE ARCHIVO ==========
    private static void guardarPartida() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("partida_guardada.dat"))) {
            oos.writeObject(usuario);
            oos.writeObject(mascota);
            oos.writeObject(estadisticas);
        } catch (IOException e) {
            System.out.println("Error al guardar: " + e.getMessage());
        }
    }

    private static boolean cargarPartida() {
        File archivo = new File("partida_guardada.dat");
        if (!archivo.exists()) return false;

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(archivo))) {
            usuario = (Usuario) ois.readObject();
            mascota = (Mascota) ois.readObject();
            estadisticas = (Estadisticas) ois.readObject();
            sudoku = new Sudoku();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private static void guardarSudoku() {
        sudoku.guardarEstado("sudoku_actual.dat");
    }

    private static boolean cargarSudoku() {
        return sudoku.cargarEstado("sudoku_actual.dat");
    }

    // ========== MÉTODOS DE INTERFAZ ==========
    private static void limpiarPantalla() {
        // Para Windows
        try {
            if (System.getProperty("os.name").contains("Windows")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                // Para Linux/Mac
                System.out.print("\033[H\033[2J");
                System.out.flush();
            }
        } catch (Exception e) {
            // Fallback: imprimir muchas líneas vacías
            for (int i = 0; i < 50; i++) {
                System.out.println();
            }
        }
    }

    private static void mostrarTitulo() {
        System.out.println("╔══════════════════════════════════════════════════════════════════════════╗");
        System.out.println("║                    SUDOKU CON MASCOTA VIRTUAL v3.0                       ║");
        System.out.println("╚══════════════════════════════════════════════════════════════════════════╝");
    }

    private static int leerEntero() {
        while (true) {
            try {
                return Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.print("Ingresa un número válido: ");
            }
        }
    }

    private static void mostrarMensaje(String mensaje) {
        System.out.println("\n[" + mensaje + "]");
        esperarEnter();
    }

    private static void esperarEnter() {
        System.out.print("\nPresiona Enter para continuar...");
        scanner.nextLine();
    }
}