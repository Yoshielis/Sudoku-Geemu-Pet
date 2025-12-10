import java.util.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
// ==================== CLASE MASCOTA ====================
class Mascota {
    private String nombre;
    private String tipo;
    private int hambre;
    private int felicidad;
    private int salud;
    private int energia;
    private LocalDate ultimaActualizacion;

    public Mascota(String nombre, String tipo) {
        this.nombre = nombre;
        this.tipo = tipo;
        this.hambre = 50;
        this.felicidad = 50;
        this.salud = 100;
        this.energia = 80;
        this.ultimaActualizacion = LocalDate.now();
    }

    public void mostrarEstado() {
        System.out.println("\n=== " + nombre.toUpperCase() + " (" + tipo + ") ===");
        System.out.println("Hambre:    " + getBarraProgreso(hambre));
        System.out.println("Felicidad: " + getBarraProgreso(felicidad));
        System.out.println("Salud:     " + getBarraProgreso(salud));
        System.out.println("Energía:   " + getBarraProgreso(energia));

        System.out.println("\nEstado: " + getEstadoEmocional());
    }

    private String getBarraProgreso(int valor) {
        StringBuilder barra = new StringBuilder("[");
        int segmentos = valor / 10;
        for (int i = 0; i < 10; i++) {
            if (i < segmentos) {
                barra.append("█");
            } else {
                barra.append("░");
            }
        }
        barra.append("] ").append(valor).append("%");
        return barra.toString();
    }

    private String getEstadoEmocional() {
        if (salud < 30) return "😷 Enfermo";
        if (hambre > 80) return "😫 Hambriento";
        if (felicidad < 30) return "😔 Triste";
        if (felicidad > 80) return "😄 Muy Feliz";
        if (energia < 30) return "😴 Cansado";
        return "😊 Contento";
    }

    public void alimentar(int cantidad) {
        hambre = Math.max(0, hambre - cantidad);
        salud = Math.min(100, salud + cantidad / 3);
        felicidad = Math.min(100, felicidad + cantidad / 4);
    }

    public void jugar() {
        felicidad = Math.min(100, felicidad + 20);
        energia = Math.max(0, energia - 15);
        hambre = Math.min(100, hambre + 10);
    }

    public void curar() {
        salud = Math.min(100, salud + 30);
        energia = Math.max(0, energia - 10);
    }

    public void actualizarEstado() {
        LocalDate hoy = LocalDate.now();
        if (!hoy.equals(ultimaActualizacion)) {
            hambre = Math.min(100, hambre + 10);
            energia = Math.min(100, energia + 20);
            ultimaActualizacion = hoy;
        }
    }

    public void actualizarFeliz() {
        felicidad = Math.min(100, felicidad + 10);
    }

    // Getters y Setters
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public int getFelicidad() { return felicidad; }
    public int getSalud() { return salud; }
}

