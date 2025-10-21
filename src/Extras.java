public class Extras {
    private int idExtra;
    private String descripcion;
    private double costo;

    public Extras(int idExtra, String descripcion, double costo) {
        this.idExtra = idExtra;
        this.descripcion = descripcion;
        this.costo = costo;
    }

    public int getIdExtra() {
        return idExtra;
    }
    public String getDescripcion() {
        return descripcion;
    }
    public double getCosto() {
        return costo;
    }

    @Override
    public String toString() {
        return descripcion + " ($" + costo + ")";
    }
}