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
    }

    public void mostrarEstadoCompleto() {
        System.out.println(emoji + " " + nombre.toUpperCase() + " (" + especie + ") " + emoji);
        System.out.print("Hambre:    "); mostrarBarra(hambre);
        System.out.print("Felicidad: "); mostrarBarra(felicidad);
        System.out.print("Salud:     "); mostrarBarra(salud);
        System.out.print("Energía:   "); mostrarBarra(energia);
        System.out.println("Estado: " + getEstadoEmocional());
        System.out.println("Efecto: " + getEfectoJuego());
    }

    public void mostrarEstadoMini() {
        System.out.print(nombre + " " + emoji + " [" + getEstadoEmocional() + "] ");
        mostrarBarraMini(felicidad);
        System.out.println();
    }

    private void mostrarBarra(int valor) {
        int lleno = valor / 10;
        System.out.print("[");
        for (int i = 0; i < 10; i++) {
            if (i < lleno) {
                System.out.print("█");
            } else {
                System.out.print("░");
            }
        }
        System.out.println("] " + valor + "%");
    }

    private void mostrarBarraMini(int valor) {
        int lleno = valor / 20;
        System.out.print("[");
        for (int i = 0; i < 5; i++) {
            if (i < lleno) {
                System.out.print("█");
            } else {
                System.out.print("░");
            }
        }
        System.out.print("]");
    }

    public String getEstadoEmocional() {
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
        felicidad = Math.min(100, felicidad + 25);
        energia = Math.max(0, energia - 20);
        hambre = Math.min(100, hambre + 15);
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
        String[] dialogos = {
                "¡Vamos, tú puedes!",
                "¡Ese número va perfecto ahí!",
                "Mmm... revisa esa columna",
                "¡Excelente jugada!",
                "La región 3x3 necesita atención",
                "¡Sigue así, vas muy bien!",
                "Recuerda: números únicos en cada fila",
                "¡Woohoo! ¡Otra celda completada!"
        };

        System.out.println(emoji + " " + nombre + " dice: \"" + dialogos[random.nextInt(dialogos.length)] + "\"");
    }

    public void darPista() {
        String[] pistas = {
                "Revisa la fila " + (random.nextInt(9) + 1),
                "La columna " + (random.nextInt(9) + 1) + " necesita atención",
                "Mira la región 3x3 superior derecha",
                "¿Ya revisaste todos los números del 1 al 9 en esa área?"
        };
        System.out.println(emoji + " " + nombre + " sugiere: " + pistas[random.nextInt(pistas.length)]);
    }

    public void reaccionarError() {
        String[] reacciones = {
                "¡Ups! Ese no era el número correcto",
                "Mmm... intenta otro número",
                "No te preocupes, sigue intentando",
                "Esa celda necesita un número diferente"
        };
        System.out.println(emoji + " " + reacciones[random.nextInt(reacciones.length)]);
    }

    public void celebrar() {
        String[] celebraciones = {
                "¡WOOHOO! ¡LO LOGRASTE! 🎉",
                "¡Increíble! ¡Eres un genio del Sudoku!",
                "¡Felicidades! " + nombre + " está muy orgulloso",
                "¡Victoria! Los puntos están en camino"
        };
        System.out.println(emoji + " " + celebraciones[random.nextInt(celebraciones.length)]);
    }

    public void despedirse() {
        System.out.println(emoji + " " + nombre + " dice: ¡Hasta pronto! Cuídate mucho.");
    }

    // Getters
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getEspecie() { return especie; }
    public int getFelicidad() { return felicidad; }
    public int getSalud() { return salud; }
}


