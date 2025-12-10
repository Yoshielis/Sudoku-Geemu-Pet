class Record {
    private String nombre;
    private int puntos;

    public Record(String nombre, int puntos) {
        this.nombre = nombre;
        this.puntos = puntos;
    }

    // Getters
    public String getNombre() { return nombre; }
    public int getPuntos() { return puntos; }
}