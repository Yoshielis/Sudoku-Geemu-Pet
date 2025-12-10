import java.util.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

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
        System.out.println("=== SUDOKU CON MASCOTA VIRTUAL ===");
        System.out.print("Ingresa tu nombre: ");
        String nombre = scanner.nextLine();

        usuario = new Usuario(nombre);
        estadisticas = new Estadisticas();
        sudoku = new Sudoku();
        mascota = new Mascota("Pixel", "Perro");

        System.out.println("\n¡Bienvenido, " + nombre + "!");
        System.out.println("Tu mascota " + mascota.getNombre() + " te espera.");
        mascota.mostrarEstado();
    }

    private static void mostrarMenuPrincipal() {
        boolean salir = false;

        while (!salir) {
            System.out.println("\n=== MENÚ PRINCIPAL ===");
            System.out.println("1. Jugar Sudoku");
            System.out.println("2. Cuidar Mascota");
            System.out.println("3. Ver Estadísticas");
            System.out.println("4. Mostrar Ranking");
            System.out.println("5. Ver Instrucciones");
            System.out.println("6. Salir");
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
                    estadisticas.mostrarEstadisticas();
                    break;
                case 4:
                    mostrarRanking();
                    break;
                case 5:
                    mostrarInstrucciones();
                    break;
                case 6:
                    salir = true;
                    System.out.println("¡Gracias por jugar! Hasta pronto.");
                    break;
                default:
                    System.out.println("Opción no válida. Intenta de nuevo.");
            }
        }
    }

    private static void jugarSudoku() {
        System.out.println("\n=== JUEGO DE SUDOKU ===");
        System.out.println("Selecciona la dificultad:");
        System.out.println("1. Fácil (40 números dados)");
        System.out.println("2. Medio (35 números dados)");
        System.out.println("3. Difícil (30 números dados)");
        System.out.print("Opción: ");

        int dificultad = leerEntero();
        int celdasVacias = 0;

        switch (dificultad) {
            case 1: celdasVacias = 41; break;
            case 2: celdasVacias = 46; break;
            case 3: celdasVacias = 51; break;
            default: celdasVacias = 46;
        }

        sudoku.generarPuzzle(celdasVacias);
        boolean completado = sudoku.jugar();

        if (completado) {
            int puntos = calcularPuntos(dificultad);
            usuario.agregarPuntos(puntos);
            mascota.alimentar(puntos / 10);
            mascota.actualizarFeliz();

            System.out.println("\n¡Felicidades! Completaste el Sudoku.");
            System.out.println("Ganaste " + puntos + " puntos.");
            System.out.println("Tu mascota está más feliz y alimentada.");

            estadisticas.registrarPartida(dificultad, puntos, true);
        } else {
            System.out.println("\nSudoku incompleto. ¡Sigue intentando!");
            estadisticas.registrarPartida(dificultad, 0, false);
        }
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
            System.out.println("\n=== CUIDAR MASCOTA ===");
            mascota.mostrarEstado();
            System.out.println("\nOpciones:");
            System.out.println("1. Alimentar (10 puntos)");
            System.out.println("2. Jugar con mascota (5 puntos)");
            System.out.println("3. Dar medicina (15 puntos)");
            System.out.println("4. Cambiar nombre");
            System.out.println("5. Volver al menú principal");
            System.out.print("Selecciona: ");

            int opcion = leerEntero();

            switch (opcion) {
                case 1:
                    if (usuario.getPuntos() >= 10) {
                        usuario.restarPuntos(10);
                        mascota.alimentar(15);
                        System.out.println("¡Has alimentado a tu mascota!");
                    } else {
                        System.out.println("No tienes puntos suficientes.");
                    }
                    break;
                case 2:
                    if (usuario.getPuntos() >= 5) {
                        usuario.restarPuntos(5);
                        mascota.jugar();
                        System.out.println("¡Has jugado con tu mascota!");
                    } else {
                        System.out.println("No tienes puntos suficientes.");
                    }
                    break;
                case 3:
                    if (usuario.getPuntos() >= 15) {
                        usuario.restarPuntos(15);
                        mascota.curar();
                        System.out.println("¡Has curado a tu mascota!");
                    } else {
                        System.out.println("No tienes puntos suficientes.");
                    }
                    break;
                case 4:
                    System.out.print("Nuevo nombre: ");
                    String nuevoNombre = scanner.nextLine();
                    mascota.setNombre(nuevoNombre);
                    break;
                case 5:
                    volver = true;
                    break;
                default:
                    System.out.println("Opción no válida.");
            }

            mascota.actualizarEstado();
        }
    }

    private static void mostrarRanking() {
        estadisticas.mostrarRanking();
    }

    private static void mostrarInstrucciones() {
        System.out.println("\n=== INSTRUCCIONES ===");
        System.out.println("1. Juega Sudoku para ganar puntos");
        System.out.println("2. Usa puntos para cuidar tu mascota");
        System.out.println("3. Mantén a tu mascota feliz y saludable");
        System.out.println("4. Resuelve Sudoku ingresando números del 1-9");
        System.out.println("5. Cada fila, columna y región 3x3 debe tener números únicos");
        System.out.println("6. ¡Diviértete!");
    }

    private static int leerEntero() {
        while (true) {
            try {
                return Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.print("Entrada no válida. Ingresa un número: ");
            }
        }
    }
}