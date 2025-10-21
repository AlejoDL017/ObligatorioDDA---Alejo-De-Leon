public class Cancha {

    private int idCancha;
    private String nombre;
    private String deporte;
    private Boolean cubierta;
    private Integer capacidad;
    private String estado;
    private String caracteristicas;

    public Cancha(int idCancha, String nombre, String deporte, Boolean cubierta,
                  Integer capacidad, String estado, String caracteristicas) {
        this.idCancha = idCancha;
        this.nombre = nombre;
        this.deporte = deporte;
        this.cubierta = cubierta;
        this.capacidad = capacidad;
        this.estado = estado;
        this.caracteristicas = caracteristicas;
    }

    public int getIdCancha() {
        return idCancha;
    }
    public String getNombre() {
        return nombre;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    public String getDeporte() {
        return deporte;
    }
    public void setDeporte(String deporte) {
        this.deporte = deporte;
    }
    public Boolean getCubierta() {
        return cubierta;
    }
    public void setCubierta(Boolean cubierta) {
        this.cubierta = cubierta;
    }
    public Integer getCapacidad() {
        return capacidad;
    }
    public void setCapacidad(Integer capacidad) {
        this.capacidad = capacidad;
    }
    public String getEstado() {
        return estado;
    }
    public void setEstado(String estado) {
        this.estado = estado;
    }
    public String getCaracteristicas() {
        return caracteristicas;
    }
    public void setCaracteristicas(String caracteristicas) {
        this.caracteristicas = caracteristicas;
    }
    public void actualizarEstado(String nuevoEstado){
        this.estado = nuevoEstado;
    }
    public boolean isCubierta() {
        return cubierta != null && cubierta;
    }
    @Override
    public String toString() {
        return "Cancha {" +
                "IdCancha=" + idCancha +
                ", Nombre='" + nombre + '\'' +
                ", Deporte='" + deporte + '\'' +
                ", Cubierta='" + cubierta + '\'' +
                ", Capacidad='" + capacidad + '\'' +
                ", Estado='" + estado + '\'' +
                ", Caracteristicas='" + caracteristicas + '\'' +
                '}';
    }
}
