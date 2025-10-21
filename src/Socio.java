import java.time.LocalDate;
public class Socio {
    private int idSocio;
    private String nombre;
    private String aPaterno;
    private String aMaterno;
    private String numDocumento;
    private LocalDate fechaNacimiento;
    private String telefono;
    private String pais;

    public Socio(int idSocio, String nombre, String apaterno, String amaterno,
                 String numDocumento, LocalDate fechaNacimiento,
                 String telefono, String pais) {
        this.idSocio = idSocio;
        this.nombre = nombre;
        this.aPaterno = apaterno;
        this.aMaterno = amaterno;
        this.numDocumento = numDocumento;
        this.fechaNacimiento = fechaNacimiento;
        this.telefono = telefono;
        this.pais = pais;
    }
    public int getIdSocio() {
        return idSocio;
    }
    public void setIdSocio(int idSocio) {
        this.idSocio = idSocio;
    }
    public String getNombre() {
        return nombre;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    public String getApaterno() {
        return aPaterno;
    }
    public void setApaterno(String apaterno) {
        this.aPaterno = apaterno;
    }
    public String getAmaterno() {
        return aMaterno;
    }
    public void setAmaterno(String amaterno) {
        this.aMaterno = amaterno;
    }
    public String getNumDocumento() {
        return numDocumento;
    }
    public void setNumDocumento(String numDocumento) {
        this.numDocumento = numDocumento;
    }
    public LocalDate getFechaNacimiento() {
        return fechaNacimiento;
    }
    public void setFechaNacimiento(LocalDate fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }
    public String getTelefono() {
        return telefono;
    }
    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }
    public String getPais() {
        return pais;
    }
    public void setPais(String pais) {
        this.pais = pais;
    }
    public String getNombreCompleto() {
        if (aMaterno != null && !aMaterno.isEmpty()) {
            return nombre + " " + aPaterno + " " + aMaterno;
        } else {
            return nombre + " " + aPaterno;
        }
    }
    @Override
    public String toString() {
        return "Socio {" +
                "IdSocio=" + idSocio +
                ", Nombre='" + nombre + '\'' +
                ", Apellido Paterno='" + aPaterno + '\'' +
                ", Apellido Materno='" + aMaterno + '\'' +
                ", numDocumento='" + numDocumento + '\'' +
                ", fechaNacimiento='" + fechaNacimiento + '\'' +
                ", telefono='" + telefono + '\'' +
                ", pais='" + pais + '\'' +
                '}';
    }
}

