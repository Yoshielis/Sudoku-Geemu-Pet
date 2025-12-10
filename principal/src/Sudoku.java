import java.util.*;

public class Sudoku {
    private int[][] tablero;
    private int[][] solucion;
    private boolean[][] editable;
    private Scanner scanner;

    public Sudoku() {
        tablero = new int[9][9];
        solucion = new int[9][9];
        editable = new boolean[9][9];
        scanner = new Scanner(System.in);
    }

    // Método principal para jugar
    public boolean jugar() {
        mostrarTablero();
        boolean completado = false;

        while (!completado) {
            System.out.println("\nOpciones: (fila,columna,valor) o 0 para salir");
            System.out.print("Ingresa movimiento: ");
            String input = scanner.nextLine();

            if (input.equals("0")) {
                return false;
            }

            String[] partes = input.split(",");
            if (partes.length != 3) {
                System.out.println("Formato incorrecto. Usa: fila,columna,valor");
                continue;
            }

            try {
                int fila = Integer.parseInt(partes[0]) - 1;
                int columna = Integer.parseInt(partes[1]) - 1;
                int valor = Integer.parseInt(partes[2]);

                if (fila < 0 || fila >= 9 || columna < 0 || columna >= 9) {
                    System.out.println("Posición fuera de rango (1-9)");
                    continue;
                }

                if (!editable[fila][columna]) {
                    System.out.println("Esta celda no es editable");
                    continue;
                }

                if (valor < 1 || valor > 9) {
                    System.out.println("Valor debe ser entre 1-9");
                    continue;
                }

                if (esMovimientoValido(fila, columna, valor)) {
                    tablero[fila][columna] = valor;
                    System.out.println("Movimiento válido!");
                } else {
                    System.out.println("Movimiento inválido!");
                }

                mostrarTablero();
                completado = verificarCompletado();

            } catch (NumberFormatException e) {
                System.out.println("Entrada no válida");
            }
        }

        return true;
    }

    // Generar puzzle usando algoritmo de Divide y Vencerás (Backtracking)
    public void generarPuzzle(int celdasVacias) {
        // Generar solución completa
        generarSolucionCompleta();

        // Copiar solución al tablero
        for (int i = 0; i < 9; i++) {
            System.arraycopy(solucion[i], 0, tablero[i], 0, 9);
        }

        // Remover celdas aleatoriamente
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

        // Marcar celdas no editables
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (tablero[i][j] != 0) {
                    editable[i][j] = false;
                }
            }
        }
    }

    // ALGORITMO PRINCIPAL: Divide y Vencerás (Backtracking)
    private boolean generarSolucionCompleta() {
        // Inicializar tablero vacío
        for (int i = 0; i < 9; i++) {
            Arrays.fill(solucion[i], 0);
        }

        // Llenar diagonal principal primero (optimización)
        llenarDiagonal();

        // Resolver el resto usando backtracking
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

    // Método recursivo de backtracking (Divide y Vencerás)
    private boolean resolverSudoku(int fila, int col) {
        // Caso base: llegamos al final
        if (fila == 9) {
            return true;
        }

        // Calcular siguiente posición
        int nextFila = col == 8 ? fila + 1 : fila;
        int nextCol = (col + 1) % 9;

        // Si ya tiene valor, pasar a la siguiente
        if (solucion[fila][col] != 0) {
            return resolverSudoku(nextFila, nextCol);
        }

        // Probar números del 1 al 9
        List<Integer> numeros = new ArrayList<>();
        for (int i = 1; i <= 9; i++) {
            numeros.add(i);
        }
        Collections.shuffle(numeros); // Para variedad en los puzzles

        for (int num : numeros) {
            if (esValidoParaSolucion(fila, col, num)) {
                solucion[fila][col] = num;

                if (resolverSudoku(nextFila, nextCol)) {
                    return true;
                }

                solucion[fila][col] = 0; // Backtrack
            }
        }

        return false;
    }

    private boolean esValidoParaSolucion(int fila, int col, int num) {
        // Verificar fila
        for (int j = 0; j < 9; j++) {
            if (solucion[fila][j] == num) {
                return false;
            }
        }

        // Verificar columna
        for (int i = 0; i < 9; i++) {
            if (solucion[i][col] == num) {
                return false;
            }
        }

        // Verificar caja 3x3
        int inicioFila = fila - fila % 3;
        int inicioCol = col - col % 3;

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (solucion[inicioFila + i][inicioCol + j] == num) {
                    return false;
                }
            }
        }

        return true;
    }

    private boolean esMovimientoValido(int fila, int col, int valor) {
        // Verificar fila
        for (int j = 0; j < 9; j++) {
            if (j != col && tablero[fila][j] == valor) {
                return false;
            }
        }

        // Verificar columna
        for (int i = 0; i < 9; i++) {
            if (i != fila && tablero[i][col] == valor) {
                return false;
            }
        }

        // Verificar caja 3x3
        int inicioFila = fila - fila % 3;
        int inicioCol = col - col % 3;

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                int actualFila = inicioFila + i;
                int actualCol = inicioCol + j;
                if (!(actualFila == fila && actualCol == col) &&
                        tablero[actualFila][actualCol] == valor) {
                    return false;
                }
            }
        }

        return true;
    }

    private boolean verificarCompletado() {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (tablero[i][j] == 0) {
                    return false;
                }
            }
        }
        return true;
    }

    private void mostrarTablero() {
        System.out.println("\n    1 2 3   4 5 6   7 8 9");
        System.out.println("  +-------+-------+-------+");

        for (int i = 0; i < 9; i++) {
            System.out.print((i + 1) + " | ");
            for (int j = 0; j < 9; j++) {
                if (tablero[i][j] == 0) {
                    System.out.print(". ");
                } else {
                    System.out.print(tablero[i][j] + " ");
                }

                if ((j + 1) % 3 == 0) {
                    System.out.print("| ");
                }
            }

            System.out.println();
            if ((i + 1) % 3 == 0 && i != 8) {
                System.out.println("  +-------+-------+-------+");
            }
        }
        System.out.println("  +-------+-------+-------+");
    }
}
// ==================== CLASES AUXILIARES ====================
