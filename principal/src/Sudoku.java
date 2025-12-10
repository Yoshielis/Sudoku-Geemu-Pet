import java.util.*;
import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
// ==================== CLASE SUDOKU MEJORADA ====================
class Sudoku implements Serializable {
    private int[][] tablero;
    private int[][] solucion;
    private boolean[][] editable;
    private int errores;

    public Sudoku() {
        tablero = new int[9][9];
        solucion = new int[9][9];
        editable = new boolean[9][9];
        errores = 0;
    }

    public boolean jugar(Usuario usuario, Mascota mascota) {
        Scanner scanner = new Scanner(System.in);
        boolean salir = false;
        boolean completado = false;
        String mensaje = "";

        while (!completado && !salir) {
            // Limpiar y mostrar la pantalla
            mostrarPantallaSudoku(usuario, mascota, mensaje);

            // Leer comando
            System.out.print("\nComando (fila,col,valor) o 'pista'/'guardar'/'salir': ");
            String input = scanner.nextLine().trim();

            if (input.equalsIgnoreCase("salir")) {
                salir = true;
                mensaje = "Partida guardada. ¡Vuelve pronto!";
                continue;
            }

            if (input.equalsIgnoreCase("guardar")) {
                guardarEstado("sudoku_actual.dat");
                mensaje = "Sudoku guardado temporalmente.";
                continue;
            }

            if (input.equalsIgnoreCase("pista")) {
                if (mascota.getFelicidad() > 50) {
                    int[] pista = obtenerPista();
                    if (pista != null) {
                        mensaje = "Pista: Prueba " + pista[2] + " en (" +
                                (pista[0]+1) + "," + (pista[1]+1) + ")";
                    } else {
                        mensaje = "No hay pistas disponibles.";
                    }
                } else {
                    mensaje = mascota.getNombre() + " no está feliz para dar pistas.";
                }
                continue;
            }

            // Procesar movimiento normal
            String[] partes = input.split(",");
            if (partes.length != 3) {
                mensaje = "Formato: fila,columna,valor (ej: 5,3,7)";
                continue;
            }

            try {
                int fila = Integer.parseInt(partes[0]) - 1;
                int columna = Integer.parseInt(partes[1]) - 1;
                int valor = Integer.parseInt(partes[2]);

                mensaje = procesarMovimiento(fila, columna, valor, mascota);

                if (mensaje.startsWith("✓")) {
                    // Animación simple
                    animarCelda(fila, columna, valor);
                }

                // Verificar si está completado
                completado = verificarCompletado();
                if (completado) {
                    mensaje = "¡Sudoku completado! ¡Felicidades!";
                }

            } catch (NumberFormatException e) {
                mensaje = "Entrada no válida. Usa números.";
            } catch (ArrayIndexOutOfBoundsException e) {
                mensaje = "Posición fuera de rango (1-9).";
            }
        }

        // Mostrar resultado final una vez
        System.out.print("\033[H\033[2J");
        System.out.flush();
        mostrarPantallaSudoku(usuario, mascota, mensaje);

        return completado;
    }

    private String procesarMovimiento(int fila, int columna, int valor, Mascota mascota) {
        // Validaciones básicas
        if (fila < 0 || fila >= 9 || columna < 0 || columna >= 9) {
            errores++;
            mascota.reaccionarError();
            return "✗ Posición fuera de rango (1-9)";
        }

        if (!editable[fila][columna]) {
            errores++;
            mascota.reaccionarError();
            return "✗ Esta celda no es editable (número fijo)";
        }

        if (valor < 1 || valor > 9) {
            errores++;
            mascota.reaccionarError();
            return "✗ Valor debe ser entre 1-9";
        }

        // Verificar fila
        for (int j = 0; j < 9; j++) {
            if (j != columna && tablero[fila][j] == valor) {
                errores++;
                mascota.reaccionarError();
                return "✗ Número " + valor + " ya existe en la fila " + (fila+1);
            }
        }

        // Verificar columna
        for (int i = 0; i < 9; i++) {
            if (i != fila && tablero[i][columna] == valor) {
                errores++;
                mascota.reaccionarError();
                return "✗ Número " + valor + " ya existe en la columna " + (columna+1);
            }
        }

        // Verificar región 3x3
        int inicioFila = fila - fila % 3;
        int inicioCol = columna - columna % 3;

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                int actualFila = inicioFila + i;
                int actualCol = inicioCol + j;
                if (!(actualFila == fila && actualCol == columna) &&
                        tablero[actualFila][actualCol] == valor) {
                    errores++;
                    mascota.reaccionarError();
                    return "✗ Número " + valor + " ya existe en la región 3x3";
                }
            }
        }

        // Movimiento válido
        tablero[fila][columna] = valor;
        return "✓ Movimiento válido!";
    }

    private void animarCelda(int fila, int columna, int valor) {
        // Simular animación simple - mostrar mensaje
        System.out.println("\n¡Celda (" + (fila+1) + "," + (columna+1) + ") actualizada con " + valor + "!");
        try { Thread.sleep(300); } catch (InterruptedException e) {}
    }

    private void mostrarPantallaSudoku(Usuario usuario, Mascota mascota, String mensaje) {
        System.out.print("\033[H\033[2J");
        System.out.flush();

        System.out.println("╔══════════════════════════════════════════════════════════════════════════╗");
        System.out.println("║                               SUDOKU                                     ║");
        System.out.println("╚══════════════════════════════════════════════════════════════════════════╝\n");

        // Mostrar información del jugador
        System.out.println("Jugador: " + usuario.getNombre() + " | Puntos: " + usuario.getPuntos() +
                " | Errores: " + errores);
        System.out.println("Mascota: " + mascota.getNombre() + " [" + mascota.getEstadoEmocional() +
                "] | Felicidad: " + mascota.getFelicidad() + "%");

        System.out.println("\n" + mensaje);
        System.out.println();

        // Mostrar tablero
        System.out.println("     1   2   3    4   5   6    7   8   9");
        System.out.println("   ╔═══-═══-═══╦═══-═══-═══╦═══-═══-═══╗");

        for (int i = 0; i < 9; i++) {
            System.out.print(" " + (i+1) + " ║");
            for (int j = 0; j < 9; j++) {
                if (tablero[i][j] == 0) {
                    System.out.print("   ");
                } else {
                    if (!editable[i][j]) {
                        System.out.print("[" + tablero[i][j] + "]");
                    } else {
                        System.out.print(" " + tablero[i][j] + " ");
                    }
                }

                if (j == 2 || j == 5) {
                    System.out.print("║");
                } else if (j < 8) {
                    System.out.print("│");
                }
            }
            System.out.println("║");

            if (i == 2 || i == 5) {
                System.out.println("   ╠═══|═══|═══*═══|═══|═══*═══|═══|═══╣");
            } else if (i < 8) {
                System.out.println("   |───┼───┼───*───┼───┼───*───┼───┼───|");
            }
        }
        System.out.println("   ╚═══-═══-═══*═══-═══-═══*═══-═══-═══╝");

        System.out.println("\n╔══════════════════════════════════════════════════════════════════════════╗");
        System.out.println("║ COMANDOS: fila,col,valor | pista | guardar | salir                       ║");
        System.out.println("╚══════════════════════════════════════════════════════════════════════════╝");
    }

    public int[] obtenerPista() {
        List<int[]> posibles = new ArrayList<>();

        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (tablero[i][j] == 0 && editable[i][j]) {
                    for (int num = 1; num <= 9; num++) {
                        // Verificar si es válido
                        boolean valido = true;

                        // Fila
                        for (int k = 0; k < 9; k++) {
                            if (tablero[i][k] == num) {
                                valido = false;
                                break;
                            }
                        }

                        // Columna
                        if (valido) {
                            for (int k = 0; k < 9; k++) {
                                if (tablero[k][j] == num) {
                                    valido = false;
                                    break;
                                }
                            }
                        }

                        // Región 3x3
                        if (valido) {
                            int inicioFila = i - i % 3;
                            int inicioCol = j - j % 3;
                            for (int k = 0; k < 3; k++) {
                                for (int l = 0; l < 3; l++) {
                                    if (tablero[inicioFila + k][inicioCol + l] == num) {
                                        valido = false;
                                        break;
                                    }
                                }
                                if (!valido) break;
                            }
                        }

                        if (valido) {
                            posibles.add(new int[]{i, j, num});
                        }
                    }
                }
            }
        }

        if (!posibles.isEmpty()) {
            Random rand = new Random();
            return posibles.get(rand.nextInt(posibles.size()));
        }
        return null;
    }

    public void generarPuzzle(int celdasVacias) {
        // Generar solución completa
        for (int i = 0; i < 9; i++) {
            Arrays.fill(solucion[i], 0);
        }

        // Llenar diagonal principal
        Random random = new Random();
        for (int i = 0; i < 9; i += 3) {
            llenarCaja(i, i);
        }

        // Resolver el resto
        resolverSudoku(0, 0);

        // Copiar solución al tablero
        for (int i = 0; i < 9; i++) {
            System.arraycopy(solucion[i], 0, tablero[i], 0, 9);
        }

        // Remover celdas aleatoriamente
        int removidas = 0;
        while (removidas < celdasVacias) {
            int fila = random.nextInt(9);
            int columna = random.nextInt(9);

            if (tablero[fila][columna] != 0) {
                tablero[fila][columna] = 0;
                editable[fila][columna] = true;
                removidas++;
            }
        }

        // Marcar celdas no editables
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (tablero[i][j] != 0) {
                    editable[i][j] = false;
                }
            }
        }
    }

    private void llenarCaja(int filaInicio, int colInicio) {
        Random random = new Random();
        List<Integer> numeros = new ArrayList<>();
        for (int i = 1; i <= 9; i++) {
            numeros.add(i);
        }
        Collections.shuffle(numeros);

        int index = 0;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                solucion[filaInicio + i][colInicio + j] = numeros.get(index++);
            }
        }
    }

    private boolean resolverSudoku(int fila, int col) {
        if (fila == 9) return true;

        int nextFila = col == 8 ? fila + 1 : fila;
        int nextCol = (col + 1) % 9;

        if (solucion[fila][col] != 0) {
            return resolverSudoku(nextFila, nextCol);
        }

        List<Integer> numeros = new ArrayList<>();
        for (int i = 1; i <= 9; i++) numeros.add(i);
        Collections.shuffle(numeros);

        for (int num : numeros) {
            if (esValidoParaSolucion(fila, col, num)) {
                solucion[fila][col] = num;
                if (resolverSudoku(nextFila, nextCol)) return true;
                solucion[fila][col] = 0;
            }
        }
        return false;
    }

    private boolean esValidoParaSolucion(int fila, int col, int num) {
        for (int j = 0; j < 9; j++) {
            if (solucion[fila][j] == num) return false;
        }

        for (int i = 0; i < 9; i++) {
            if (solucion[i][col] == num) return false;
        }

        int inicioFila = fila - fila % 3;
        int inicioCol = col - col % 3;

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (solucion[inicioFila + i][inicioCol + j] == num) return false;
            }
        }

        return true;
    }

    private boolean verificarCompletado() {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (tablero[i][j] == 0) return false;
            }
        }
        return true;
    }

    public void guardarEstado(String nombreArchivo) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(nombreArchivo))) {
            oos.writeObject(tablero);
            oos.writeObject(editable);
            oos.writeInt(errores);
        } catch (IOException e) {
            // Silenciar error para el usuario
        }
    }

    public boolean cargarEstado(String nombreArchivo) {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(nombreArchivo))) {
            tablero = (int[][]) ois.readObject();
            editable = (boolean[][]) ois.readObject();
            errores = ois.readInt();
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
