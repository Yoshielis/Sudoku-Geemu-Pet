import java.util.*;
import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
// ==================== CLASE SUDOKU MEJORADA ====================
class Sudoku implements Serializable {
    private int[][] tablero;
    private int[][] solucion;
    private boolean[][] editable;
    private Scanner scanner;
    private int errores;

    public Sudoku() {
        tablero = new int[9][9];
        solucion = new int[9][9];
        editable = new boolean[9][9];
        scanner = new Scanner(System.in);
        errores = 0;
    }

    public boolean jugar(Mascota mascota) {
        int filaActual = 0, colActual = 0;
        boolean completado = false;

        while (!completado) {
            mostrarTableroMejorado(filaActual, colActual);

            ConsoleUtils.setCursorPosition(5, 22);
            System.out.print("Comando (fila,col,valor) o 'pista'/'guardar'/'salir': ");
            String input = scanner.nextLine().trim();

            if (input.equalsIgnoreCase("salir")) {
                return false;
            }

            if (input.equalsIgnoreCase("guardar")) {
                guardarEstado("sudoku_actual.dat");
                ConsoleUtils.mostrarMensaje("Sudoku guardado temporalmente.", 40, 23);
                continue;
            }

            if (input.equalsIgnoreCase("pista")) {
                if (mascota.getFelicidad() > 50) {
                    int[] pista = obtenerPista();
                    if (pista != null) {
                        ConsoleUtils.mostrarMensaje("Pista: Intenta " + pista[2] +
                                " en (" + (pista[0]+1) + "," + (pista[1]+1) + ")", 50, 23);
                    }
                } else {
                    ConsoleUtils.mostrarMensaje("Tu mascota no está lo suficientemente feliz para dar pistas.", 60, 23);
                }
                continue;
            }

            String[] partes = input.split(",");
            if (partes.length != 3) {
                ConsoleUtils.mostrarMensaje("Formato incorrecto. Usa: fila,columna,valor", 50, 23);
                continue;
            }

            try {
                int fila = Integer.parseInt(partes[0]) - 1;
                int columna = Integer.parseInt(partes[1]) - 1;
                int valor = Integer.parseInt(partes[2]);

                filaActual = fila;
                colActual = columna;

                String mensajeError = validarMovimiento(fila, columna, valor);

                if (mensajeError.isEmpty()) {
                    tablero[fila][columna] = valor;
                    ConsoleUtils.mostrarMensaje("✓ Movimiento válido!", 40, 23);

                    // Animar la celda
                    animarCelda(fila, columna, valor);

                } else {
                    ConsoleUtils.mostrarMensaje("✗ " + mensajeError, 60, 23);
                    errores++;
                    mascota.reaccionarError();
                }

                completado = verificarCompletado();

            } catch (NumberFormatException e) {
                ConsoleUtils.mostrarMensaje("Entrada no válida. Usa números.", 50, 23);
            }
        }

        return true;
    }

    private String validarMovimiento(int fila, int col, int valor) {
        if (fila < 0 || fila >= 9 || col < 0 || col >= 9) {
            return "Posición fuera de rango (1-9)";
        }

        if (!editable[fila][col]) {
            return "Esta celda no es editable (número fijo)";
        }

        if (valor < 1 || valor > 9) {
            return "Valor debe ser entre 1-9";
        }

        // Verificar fila
        for (int j = 0; j < 9; j++) {
            if (j != col && tablero[fila][j] == valor) {
                return "Número " + valor + " ya existe en la fila " + (fila+1);
            }
        }

        // Verificar columna
        for (int i = 0; i < 9; i++) {
            if (i != fila && tablero[i][col] == valor) {
                return "Número " + valor + " ya existe en la columna " + (col+1);
            }
        }

        // Verificar región 3x3
        int inicioFila = fila - fila % 3;
        int inicioCol = col - col % 3;

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                int actualFila = inicioFila + i;
                int actualCol = inicioCol + j;
                if (!(actualFila == fila && actualCol == col) &&
                        tablero[actualFila][actualCol] == valor) {
                    return "Número " + valor + " ya existe en la región 3x3";
                }
            }
        }

        return ""; // Válido
    }

    private void animarCelda(int fila, int col, int valor) {
        int x = 20 + col * 4;
        int y = 5 + fila * 2;

        for (int i = 0; i < 3; i++) {
            ConsoleUtils.setCursorPosition(x, y);
            System.out.print("[" + valor + "]");
            try { Thread.sleep(100); } catch (InterruptedException e) {}

            ConsoleUtils.setCursorPosition(x, y);
            System.out.print(" " + valor + " ");
            try { Thread.sleep(100); } catch (InterruptedException e) {}
        }
    }

    public int[] obtenerPista() {
        List<int[]> posibles = new ArrayList<>();

        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (tablero[i][j] == 0 && editable[i][j]) {
                    for (int num = 1; num <= 9; num++) {
                        if (validarMovimiento(i, j, num).isEmpty()) {
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
        generarSolucionCompleta();

        for (int i = 0; i < 9; i++) {
            System.arraycopy(solucion[i], 0, tablero[i], 0, 9);
        }

        Random random = new Random();
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

        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (tablero[i][j] != 0) {
                    editable[i][j] = false;
                }
            }
        }
    }

    private boolean generarSolucionCompleta() {
        for (int i = 0; i < 9; i++) {
            Arrays.fill(solucion[i], 0);
        }

        llenarDiagonal();
        return resolverSudoku(0, 0);
    }

    private void llenarDiagonal() {
        Random random = new Random();
        for (int i = 0; i < 9; i += 3) {
            llenarCaja(i, i);
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

    private void mostrarTableroMejorado(int filaDestacada, int colDestacada) {
        ConsoleUtils.clearScreen();
        ConsoleUtils.dibujarMarco(50, 21);

        ConsoleUtils.setCursorPosition(18, 2);
        System.out.println("=== SUDOKU ===");
        ConsoleUtils.setCursorPosition(5, 3);
        System.out.println("Errores: " + errores + " | Usa 'pista' para ayuda");

        ConsoleUtils.setCursorPosition(15, 4);
        System.out.println("    1   2   3    4   5   6    7   8   9");
        ConsoleUtils.setCursorPosition(15, 5);
        System.out.println("  ╔═══╤═══╤═══╦═══╤═══╤═══╦═══╤═══╤═══╗");

        for (int i = 0; i < 9; i++) {
            ConsoleUtils.setCursorPosition(15, 6 + i*2);
            System.out.print((i+1) + " ║ ");

            for (int j = 0; j < 9; j++) {
                String contenido;
                if (tablero[i][j] == 0) {
                    contenido = " ";
                } else {
                    contenido = String.valueOf(tablero[i][j]);
                }

                // Destacar celda actual
                if (i == filaDestacada && j == colDestacada && tablero[i][j] == 0) {
                    System.out.print("[" + contenido + "]");
                } else if (!editable[i][j]) {
                    System.out.print("[" + contenido + "]"); // Números fijos
                } else {
                    System.out.print(" " + contenido + " ");
                }

                if (j == 2 || j == 5) {
                    System.out.print(" ║ ");
                } else if (j < 8) {
                    System.out.print(" │ ");
                }
            }
            System.out.print(" ║");

            ConsoleUtils.setCursorPosition(15, 7 + i*2);
            if (i == 2 || i == 5) {
                System.out.println("  ╠═══╪═══╪═══╬═══╪═══╪═══╬═══╪═══╪═══╣");
            } else if (i < 8) {
                System.out.println("  ╟───┼───┼───╫───┼───┼───╫───┼───┼───╢");
            }
        }

        ConsoleUtils.setCursorPosition(15, 24);
        System.out.println("  ╚═══╧═══╧═══╩═══╧═══╧═══╩═══╧═══╧═══╝");
    }

    public void guardarEstado(String nombreArchivo) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(nombreArchivo))) {
            oos.writeObject(tablero);
            oos.writeObject(editable);
            oos.writeInt(errores);
        } catch (IOException e) {
            e.printStackTrace();
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
