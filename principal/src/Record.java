import java.util.*;
import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

class Record implements Serializable {
    private String nombre;
    private int puntos;

    public Record(String nombre, int puntos) {
        this.nombre = nombre;
        this.puntos = puntos;
    }

    public String getNombre() { return nombre; }
    public int getPuntos() { return puntos; }
}