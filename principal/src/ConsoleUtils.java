
// ==================== CLASE CONSOLEUTILS ====================
class ConsoleUtils {
    public static void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    public static void setCursorPosition(int x, int y) {
        System.out.print("\033[" + y + ";" + x + "H");
    }

    public static void centrarTexto(String texto, int y) {
        int ancho = 80;
        int x = (ancho - texto.length()) / 2;
        setCursorPosition(x, y);
        System.out.println(texto);
    }

    public static void dibujarMarco(int ancho, int alto) {
        // Esquina superior izquierda
        setCursorPosition(0, 0);
        System.out.print("╔");
        for (int i = 0; i < ancho - 2; i++) System.out.print("═");
        System.out.print("╗");

        // Lados verticales
        for (int i = 1; i < alto - 1; i++) {
            setCursorPosition(0, i);
            System.out.print("║");
            setCursorPosition(ancho - 1, i);
            System.out.print("║");
        }

        // Esquina inferior derecha
        setCursorPosition(0, alto - 1);
        System.out.print("╚");
        for (int i = 0; i < ancho - 2; i++) System.out.print("═");
        System.out.print("╝");
    }

    public static void mostrarMensaje(String mensaje, int ancho, int y) {
        int x = (80 - ancho) / 2;
        setCursorPosition(x, y);
        // Limpiar línea
        System.out.print("\033[2K");
        setCursorPosition(x, y);
        System.out.print(mensaje);
    }
}