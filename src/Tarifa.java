import java.time.LocalDate;

public class Tarifa {
    private int idTarifa;
    private String deporte;
    private double montoPorHora;
    private LocalDate fechaInicioVigencia;

    public Tarifa(int idTarifa, String deporte, double montoPorHora, LocalDate fechaInicioVigencia) {
        this.idTarifa = idTarifa;
        this.deporte = deporte;
        this.montoPorHora = montoPorHora;
        this.fechaInicioVigencia = fechaInicioVigencia;
    }

    public boolean esVigente(LocalDate fecha) {
        return !fecha.isBefore(this.fechaInicioVigencia);
    }

    public boolean correspondeA(String deporte) {
        return this.deporte.equalsIgnoreCase(deporte);
    }

    public int getIdTarifa() {
        return idTarifa;
    }

    public String getDeporte() {
        return deporte;
    }

    public double getMontoPorHora() {
        return montoPorHora;
    }

    public LocalDate getFechaInicioVigencia() {
        return fechaInicioVigencia;
    }

    public boolean fechaInicioPosteriorA(Tarifa otra) {
        if (otra == null || otra.getFechaInicioVigencia() == null) return true;
        return this.fechaInicioVigencia.isAfter(otra.getFechaInicioVigencia());
    }

    @Override
    public String toString() {
        return "Tarifa #" + idTarifa + " (" + deporte + "): $" + montoPorHora + "/hora desde " + fechaInicioVigencia;
    }
}
