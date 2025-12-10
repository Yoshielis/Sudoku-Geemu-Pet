import java.util.*;
import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

// ==================== CLASE MASCOTA MEJORADA ====================
class Mascota implements Serializable {
    private String nombre;
    private String especie;
    private String emoji;
    private int hambre;
    private int felicidad;
    private int salud;
    private int energia;
    private LocalDate ultimaActualizacion;
    private List<String> dialogos;
    private Random random;

    public Mascota(String nombre, String especie, String emoji) {
        this.nombre = nombre;
        this.especie = especie;
        this.emoji = emoji;
        this.hambre = 50;
        this.felicidad = 50;
        this.salud = 100;
        this.energia = 80;
        this.ultimaActualizacion = LocalDate.now();
        this.random = new Random();
        this.dialogos = new ArrayList<>();

        cargarDialogos();
    }

    private void cargarDialogos() {
        dialogos.add("¡Vamos, tú puedes!");
        dialogos.add("¡Ese número va perfecto ahí!");
        dialogos.add("Mmm... revisa esa columna");
        dialogos.add("¡Excelente jugada!");
        dialogos.add("La región 3x3 necesita atención");
        dialogos.add("¡Sigue así, vas muy bien!");
        dialogos.add("Recuerda: números únicos en cada fila");
        dialogos.add("¡Woohoo! ¡Otra celda completada!");
    }

    public void mostrarEstadoCentrado() {
        ConsoleUtils.clearScreen();
        ConsoleUtils.dibujarMarco(50, 18);

        ConsoleUtils.setCursorPosition(20, 3);
        System.out.println(emoji + " " + nombre.toUpperCase() + " (" + especie + ") " + emoji);

        ConsoleUtils.setCursorPosition(5, 5);
        System.out.print("Hambre:    "); mostrarBarra(hambre, 30);

        ConsoleUtils.setCursorPosition(5, 7);
        System.out.print("Felicidad: "); mostrarBarra(felicidad, 30);

        ConsoleUtils.setCursorPosition(5, 9);
        System.out.print("Salud:     "); mostrarBarra(salud, 30);

        ConsoleUtils.setCursorPosition(5, 11);
        System.out.print("Energía:   "); mostrarBarra(energia, 30);

        ConsoleUtils.setCursorPosition(5, 13);
        System.out.println("Estado: " + getEstadoEmocional());

        ConsoleUtils.setCursorPosition(5, 15);
        System.out.println("Efecto en juego: " + getEfectoJuego());
    }

    public void mostrarEstadoMini() {
        String estado = nombre + " " + emoji + " [" + getEstadoEmocional() + "]";
        ConsoleUtils.setCursorPosition(22, 15);
        System.out.print("Mascota: " + estado);
    }

    private void mostrarBarra(int valor, int longitud) {
        int lleno = (valor * longitud) / 100;
        System.out.print("[");
        for (int i = 0; i < longitud; i++) {
            if (i < lleno) {
                System.out.print("█");
            } else {
                System.out.print("░");
            }
        }
        System.out.println("] " + valor + "%");
    }

    private String getEstadoEmocional() {
        if (salud < 30) return "😷 Enfermo";
        if (hambre > 80) return "😫 Hambriento";
        if (felicidad < 30) return "😔 Triste";
        if (felicidad > 80) return "😄 Muy Feliz";
        if (energia < 30) return "😴 Cansado";
        return "😊 Contento";
    }

    private String getEfectoJuego() {
        if (felicidad > 80) return "✓ Da pistas precisas";
        if (felicidad > 60) return "✓ Ocasionalmente da pistas";
        if (felicidad > 40) return "○ Da pistas básicas";
        if (felicidad > 20) return "✗ Rara vez da pistas";
        return "✗ No da pistas - ¡cuídala más!";
    }

    public void alimentar(int cantidad) {
        hambre = Math.max(0, hambre - cantidad);
        salud = Math.min(100, salud + cantidad / 3);
        felicidad = Math.min(100, felicidad + cantidad / 4);
        energia = Math.min(100, energia + cantidad / 5);
    }

    public void jugar() {
        felicidad = Math.min(100, felicidad + 25);  // Aumenta felicidad
        energia = Math.max(0, energia - 20);        // Disminuye energía por jugar
        hambre = Math.min(100, hambre + 15);        // Aumenta hambre

        ConsoleUtils.mostrarMensaje("¡" + nombre + " está jugando contigo! La energía disminuye pero la felicidad aumenta.", 60, 20);
    }

    public void curar() {
        salud = Math.min(100, salud + 40);
        energia = Math.min(100, energia + 10);
    }

    public void actualizarEstado() {
        LocalDate hoy = LocalDate.now();
        if (!hoy.equals(ultimaActualizacion)) {
            hambre = Math.min(100, hambre + 15);
            energia = Math.min(100, energia + 25);
            felicidad = Math.max(0, felicidad - 5);
            ultimaActualizacion = hoy;
        }
    }

    public void actualizarFeliz() {
        felicidad = Math.min(100, felicidad + 15);
    }

    public void darAnimo() {
        if (!dialogos.isEmpty()) {
            String dialogo = dialogos.get(random.nextInt(dialogos.size()));
            ConsoleUtils.centrarTexto(emoji + " " + nombre + " dice: \"" + dialogo + "\"", 8);
        }
    }

    public void darPista() {
        String[] pistas = {
                "Revisa la fila " + (random.nextInt(9) + 1),
                "La columna " + (random.nextInt(9) + 1) + " necesita atención",
                "Mira la región 3x3 superior derecha",
                "¿Ya revisaste todos los números del 1 al 9 en esa área?"
        };
        ConsoleUtils.mostrarMensaje(emoji + " " + nombre + " sugiere: " +
                pistas[random.nextInt(pistas.length)], 60, 20);
    }

    public void reaccionarError() {
        String[] reacciones = {
                "¡Ups! Ese no era el número correcto",
                "Mmm... intenta otro número",
                "No te preocupes, sigue intentando",
                "Esa celda necesita un número diferente"
        };
        ConsoleUtils.mostrarMensaje(emoji + " " + reacciones[random.nextInt(reacciones.length)], 50, 21);
    }

    public void celebrar() {
        String[] celebraciones = {
                "¡WOOHOO! ¡LO LOGRASTE! 🎉",
                "¡Increíble! ¡Eres un genio del Sudoku!",
                "¡Felicidades! " + nombre + " está muy orgulloso",
                "¡Victoria! Los puntos están en camino"
        };
        ConsoleUtils.centrarTexto(emoji + " " + celebraciones[random.nextInt(celebraciones.length)], 10);
    }

    public void saludar() {
        ConsoleUtils.centrarTexto(emoji + " ¡Hola! Soy " + nombre + ", tu " + especie.toLowerCase() + " virtual.", 12);
    }

    public void despedirse() {
        ConsoleUtils.centrarTexto(emoji + " " + nombre + " dice: ¡Hasta pronto! Cuídate mucho.", 12);
    }

    // Getters y Setters
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getEspecie() { return especie; }
    public int getFelicidad() { return felicidad; }
    public int getSalud() { return salud; }
}
