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
    private static String nombreArchivo = "partida_guardada.dat";

    public static void main(String[] args) {
        inicializarSistema();
        mostrarMenuPrincipal();
    }

    private static void inicializarSistema() {
        ConsoleUtils.clearScreen();
        ConsoleUtils.centrarTexto("=== SUDOKU CON MASCOTA VIRTUAL ===", 2);

        System.out.print("\n".repeat(5));
        ConsoleUtils.centrarTexto("¿Deseas cargar una partida guardada? (S/N): ", 2);
        String cargar = scanner.nextLine().toUpperCase();

        if (cargar.equals("S") && cargarPartida()) {
            ConsoleUtils.centrarTexto("¡Partida cargada exitosamente!", 2);
        } else {
            crearNuevaPartida();
        }

        scanner.nextLine(); // Limpiar buffer
        System.out.print("\n".repeat(2));
        ConsoleUtils.centrarTexto("¡Bienvenido, " + usuario.getNombre() + "!", 2);
        ConsoleUtils.centrarTexto("Tu mascota " + mascota.getNombre() + " (" + mascota.getEspecie() + ") te espera.", 2);

        mascota.saludar();
        mascota.mostrarEstadoCentrado();
    }

    private static void crearNuevaPartida() {
        ConsoleUtils.clearScreen();
        ConsoleUtils.centrarTexto("=== CREAR NUEVA PARTIDA ===", 2);

        System.out.print("\n".repeat(3));
        ConsoleUtils.centrarTexto("Ingresa tu nombre: ", 2);
        String nombre = scanner.nextLine();

        usuario = new Usuario(nombre);
        estadisticas = new Estadisticas();
        sudoku = new Sudoku();

        // Seleccionar especie de mascota
        seleccionarEspecieMascota();
    }

    private static void seleccionarEspecieMascota() {
        ConsoleUtils.clearScreen();
        ConsoleUtils.centrarTexto("=== SELECCIONA LA ESPECIE DE TU MASCOTA ===", 2);

        System.out.print("\n".repeat(2));
        String[] especies = {"Perro", "Gato", "Conejo", "Pájaro", "Dragón"};
        String[] emojis = {"🐕", "🐈", "🐇", "🐦", "🐉"};

        for (int i = 0; i < especies.length; i++) {
            ConsoleUtils.centrarTexto((i+1) + ". " + emojis[i] + " " + especies[i], 2);
        }

        System.out.print("\n".repeat(2));
        ConsoleUtils.centrarTexto("Selecciona una opción (1-5): ", 2);

        int opcion = 1;
        try {
            opcion = Integer.parseInt(scanner.nextLine());
            if (opcion < 1 || opcion > 5) opcion = 1;
        } catch (NumberFormatException e) {
            opcion = 1;
        }

        System.out.print("\n".repeat(2));
        ConsoleUtils.centrarTexto("Ingresa el nombre de tu mascota: ", 2);
        String nombreMascota = scanner.nextLine();

        mascota = new Mascota(nombreMascota, especies[opcion-1], emojis[opcion-1]);
    }

    private static void mostrarMenuPrincipal() {
        boolean salir = false;

        while (!salir) {
            ConsoleUtils.clearScreen();
            ConsoleUtils.dibujarMarco(60, 20);

            ConsoleUtils.setCursorPosition(25, 3);
            System.out.println("=== MENÚ PRINCIPAL ===");

            String[] opciones = {
                    "1. Jugar Sudoku",
                    "2. Cuidar Mascota",
                    "3. Ver Estadísticas",
                    "4. Mostrar Ranking",
                    "5. Guardar Partida",
                    "6. Ver Instrucciones",
                    "7. Salir"
            };

            for (int i = 0; i < opciones.length; i++) {
                ConsoleUtils.setCursorPosition(22, 6 + i);
                System.out.println(opciones[i]);
            }

            ConsoleUtils.setCursorPosition(22, 15);
            mascota.mostrarEstadoMini();

            ConsoleUtils.setCursorPosition(22, 17);
            System.out.print("Selecciona una opción: ");

            int opcion = leerEntero();

            switch (opcion) {
                case 1:
                    jugarSudoku();
                    break;
                case 2:
                    mostrarMenuMascota();
                    break;
                case 3:
                    estadisticas.mostrarEstadisticasCentrado();
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
                    ConsoleUtils.clearScreen();
                    ConsoleUtils.centrarTexto("¡Gracias por jugar! Hasta pronto.", 10);
                    mascota.despedirse();
                    break;
                default:
                    ConsoleUtils.mostrarMensaje("Opción no válida. Intenta de nuevo.", 40, 20);
            }
        }
    }

    private static void jugarSudoku() {
        ConsoleUtils.clearScreen();
        ConsoleUtils.centrarTexto("=== JUEGO DE SUDOKU ===", 2);

        // Mostrar mensaje de la mascota
        mascota.darAnimo();

        System.out.print("\n".repeat(2));
        ConsoleUtils.centrarTexto("Selecciona la dificultad:", 2);
        ConsoleUtils.centrarTexto("1. Fácil (40 números dados)", 2);
        ConsoleUtils.centrarTexto("2. Medio (35 números dados)", 2);
        ConsoleUtils.centrarTexto("3. Difícil (30 números dados)", 2);
        ConsoleUtils.centrarTexto("4. Cargar partida guardada", 2);

        System.out.print("\n".repeat(2));
        ConsoleUtils.centrarTexto("Opción: ", 2);

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
                    ConsoleUtils.mostrarMensaje("No hay partida de Sudoku guardada.", 40, 20);
                    return;
                }
                break;
            default: celdasVacias = 46;
        }

        if (!cargar) {
            sudoku.generarPuzzle(celdasVacias);
        }

        boolean completado = sudoku.jugar(mascota);

        if (completado) {
            int puntos = calcularPuntos(dificultad);
            usuario.agregarPuntos(puntos);
            mascota.alimentar(puntos / 10);
            mascota.actualizarFeliz();

            ConsoleUtils.clearScreen();
            ConsoleUtils.centrarTexto("¡FELICIDADES! 🎉", 5);
            ConsoleUtils.centrarTexto("Completaste el Sudoku correctamente.", 6);
            ConsoleUtils.centrarTexto("Ganaste " + puntos + " puntos.", 7);

            mascota.celebrar();

            estadisticas.registrarPartida(dificultad, puntos, true);
            guardarSudoku(); // Guardar estado vacío
        } else {
            ConsoleUtils.centrarTexto("Sudoku incompleto. ¡Sigue intentando!", 10);
            estadisticas.registrarPartida(dificultad, 0, false);
            guardarSudoku(); // Guardar estado actual
        }

        ConsoleUtils.centrarTexto("Presiona Enter para continuar...", 15);
        scanner.nextLine();
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

    private static void mostrarMenuMascota() {
        boolean volver = false;

        while (!volver) {
            ConsoleUtils.clearScreen();
            mascota.mostrarEstadoCentrado();

            ConsoleUtils.centrarTexto("=== CUIDAR MASCOTA ===", 2);
            ConsoleUtils.centrarTexto("1. Alimentar (10 puntos)", 10);
            ConsoleUtils.centrarTexto("2. Jugar con mascota (5 puntos)", 11);
            ConsoleUtils.centrarTexto("3. Dar medicina (15 puntos)", 12);
            ConsoleUtils.centrarTexto("4. Pedir pista para Sudoku (20 puntos)", 13);
            ConsoleUtils.centrarTexto("5. Cambiar nombre", 14);
            ConsoleUtils.centrarTexto("6. Volver al menú principal", 15);

            ConsoleUtils.setCursorPosition(35, 17);
            System.out.print("Selecciona: ");

            int opcion = leerEntero();

            switch (opcion) {
                case 1:
                    if (usuario.getPuntos() >= 10) {
                        usuario.restarPuntos(10);
                        mascota.alimentar(20); // Aumenta más la barra
                        ConsoleUtils.mostrarMensaje("¡Has alimentado a " + mascota.getNombre() + "!", 40, 20);
                    } else {
                        ConsoleUtils.mostrarMensaje("No tienes puntos suficientes.", 40, 20);
                    }
                    break;
                case 2:
                    if (usuario.getPuntos() >= 5) {
                        usuario.restarPuntos(5);
                        mascota.jugar();
                        ConsoleUtils.mostrarMensaje("¡Has jugado con " + mascota.getNombre() + "!", 40, 20);
                    } else {
                        ConsoleUtils.mostrarMensaje("No tienes puntos suficientes.", 40, 20);
                    }
                    break;
                case 3:
                    if (usuario.getPuntos() >= 15) {
                        usuario.restarPuntos(15);
                        mascota.curar();
                        ConsoleUtils.mostrarMensaje("¡Has curado a " + mascota.getNombre() + "!", 40, 20);
                    } else {
                        ConsoleUtils.mostrarMensaje("No tienes puntos suficientes.", 40, 20);
                    }
                    break;
                case 4:
                    if (usuario.getPuntos() >= 20) {
                        usuario.restarPuntos(20);
                        int[] pista = sudoku.obtenerPista();
                        if (pista != null) {
                            ConsoleUtils.mostrarMensaje("Pista: Intenta " + pista[2] + " en posición (" +
                                    (pista[0]+1) + "," + (pista[1]+1) + ")", 50, 20);
                            mascota.darPista();
                        } else {
                            ConsoleUtils.mostrarMensaje("No hay pistas disponibles en este momento.", 50, 20);
                        }
                    } else {
                        ConsoleUtils.mostrarMensaje("No tienes puntos suficientes.", 40, 20);
                    }
                    break;
                case 5:
                    ConsoleUtils.centrarTexto("Nuevo nombre: ", 18);
                    String nuevoNombre = scanner.nextLine();
                    mascota.setNombre(nuevoNombre);
                    ConsoleUtils.mostrarMensaje("¡Nombre cambiado a " + nuevoNombre + "!", 40, 20);
                    break;
                case 6:
                    volver = true;
                    break;
                default:
                    ConsoleUtils.mostrarMensaje("Opción no válida.", 40, 20);
            }

            mascota.actualizarEstado();
        }
    }

    private static void mostrarRanking() {
        estadisticas.mostrarRankingCentrado();
        ConsoleUtils.centrarTexto("Presiona Enter para continuar...", 25);
        scanner.nextLine();
    }

    private static void mostrarInstrucciones() {
        ConsoleUtils.clearScreen();
        ConsoleUtils.dibujarMarco(70, 25);

        String[] instrucciones = {
                "=== INSTRUCCIONES DEL JUEGO ===",
                "",
                "1. JUEGO DE SUDOKU:",
                "   • Completa el tablero con números 1-9",
                "   • Cada fila, columna y región 3x3 debe tener números únicos",
                "   • Usa formato: fila,columna,valor (ej: 5,3,7)",
                "   • Gana puntos según la dificultad",
                "",
                "2. CUIDADO DE MASCOTA:",
                "   • Gasta puntos para alimentar, jugar o curar",
                "   • Mascota feliz da mejores pistas",
                "   • Niveles bajos afectan el rendimiento",
                "",
                "3. PUNTOS Y RECOMPENSAS:",
                "   • Fácil: 100 puntos | Medio: 200 | Difícil: 300",
                "   • Desbloquea logros especiales",
                "   • Compara tu puntuación en el ranking",
                "",
                "4. COMANDOS ESPECIALES:",
                "   • 'pista' - Obtener ayuda (cuesta puntos)",
                "   • 'guardar' - Guardar partida actual",
                "   • 'salir' - Volver al menú"
        };

        for (int i = 0; i < instrucciones.length; i++) {
            ConsoleUtils.setCursorPosition(5, 2 + i);
            System.out.println(instrucciones[i]);
        }

        ConsoleUtils.setCursorPosition(5, 28);
        System.out.print("Presiona Enter para continuar...");
        scanner.nextLine();
    }

    private static void guardarPartida() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(nombreArchivo))) {
            oos.writeObject(usuario);
            oos.writeObject(mascota);
            oos.writeObject(estadisticas);
            ConsoleUtils.mostrarMensaje("¡Partida guardada exitosamente!", 40, 20);
        } catch (IOException e) {
            ConsoleUtils.mostrarMensaje("Error al guardar la partida.", 40, 20);
        }
    }

    private static boolean cargarPartida() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(nombreArchivo))) {
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
        sudoku.guardarEstado("sudoku_guardado.dat");
    }

    private static boolean cargarSudoku() {
        return sudoku.cargarEstado("sudoku_guardado.dat");
    }

    private static int leerEntero() {
        while (true) {
            try {
                return Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                ConsoleUtils.setCursorPosition(22, 19);
                System.out.print("Entrada no válida. Ingresa un número: ");
            }
        }
    }
}
