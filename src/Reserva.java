import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class Reserva {
    private int idReserva;
    private Socio socio;
    private Cancha cancha;
    private LocalDate fecha;
    private LocalTime horaInicio;
    private int duracionHoras;
    private boolean prepago;
    private LocalDate fechaReserva;
    private String observacion;
    private List<Extras> extras;
    private boolean abonado;
    private double montoAbonado;
    private Tarifa tarifaAplicada;
    private double montoTotal;

    public Reserva(int idReserva, Socio socio, Cancha cancha, LocalDate fecha, LocalTime horaInicio,
                   int duracionHoras, boolean prepago, LocalDate fechaReserva, String observacion,
                   List<Extras> extras, Tarifa tarifaAplicada) {

        this.idReserva = idReserva;
        this.socio = socio;
        this.cancha = cancha;
        this.fecha = fecha;
        this.horaInicio = horaInicio;
        this.duracionHoras = duracionHoras;
        this.prepago = prepago;
        this.fechaReserva = fechaReserva;
        this.observacion = observacion;
        this.extras = extras;
        this.tarifaAplicada = tarifaAplicada;
        this.abonado = prepago;
        this.montoTotal = calcularMonto(this.tarifaAplicada);
    }

    public int getIdReserva() {
        return idReserva;
    }

    public Socio getSocio() {
        return socio;
    }

    public Cancha getCancha() {
        return cancha;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public LocalTime getHoraInicio() {
        return horaInicio;
    }

    public int getDuracionHoras() {
        return duracionHoras;
    }

    public boolean isPrepago() {
        return prepago;
    }

    public LocalDate getFechaReserva() {
        return fechaReserva;
    }

    public String getObservacion() {
        return observacion;
    }

    public List<Extras> getExtras() {
        return extras;
    }

    public boolean isAbonado() {
        return abonado;
    }
    public double getMontoAbonado() {
        return montoAbonado;
    }
    public Tarifa getTarifaAplicada() {
        return tarifaAplicada;
    }


    public void setTarifaAplicada(Tarifa tarifaAplicada) {
        this.tarifaAplicada = tarifaAplicada;
        this.montoTotal = calcularMonto(this.tarifaAplicada); // recalcular si se cambia tarifa
    }
    public double getMontoTotal() {
        double total = (tarifaAplicada != null ? tarifaAplicada.getMontoPorHora() * duracionHoras : 0);
        for (Extras e : extras) {
            total += e.getCosto();
        }
        return total;
    }
    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public void setHoraInicio(LocalTime horaInicio) {
        this.horaInicio = horaInicio;
    }

    public void setDuracionHoras(int duracionHoras) {
        this.duracionHoras = duracionHoras;
        this.montoTotal = calcularMonto(this.tarifaAplicada); // actualizar también si cambia duración
    }

    public void setAbonado(boolean abonado) {
        this.abonado = abonado;
    }

    public boolean superponeCon(Reserva otra) {
        if (!this.cancha.equals(otra.cancha) || !this.fecha.equals(otra.fecha))
            return false;

        LocalTime fin = horaInicio.plusHours(duracionHoras);
        LocalTime finOtra = otra.horaInicio.plusHours(otra.duracionHoras);

        return !(fin.isBefore(otra.horaInicio) || finOtra.isBefore(this.horaInicio));
    }

    public double calcularMonto(Tarifa tarifa) {
        Tarifa usada = (tarifaAplicada != null) ? tarifaAplicada : tarifa;
        double total = (usada != null ? usada.getMontoPorHora() : 0) * duracionHoras;

        for (Extras e : extras) {
            total += e.getCosto();
        }
        return total;
    }

    public void abonar() {
        this.abonado = true;
    }
    public void setMontoAbonado(double montoAbonado) {
        this.montoAbonado = montoAbonado;
    }
    public double calcularSaldoPendiente() {
        return getMontoTotal() - montoAbonado;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Reserva registrada correctamente:\n");
        sb.append("ID Reserva: ").append(idReserva).append("\n");
        sb.append("Socio: ").append(socio.getNombreCompleto()).append("\n");
        sb.append("Cancha: ").append(cancha.getNombre()).append(" (").append(cancha.getDeporte()).append(")\n");
        sb.append("Fecha: ").append(fecha).append("\n");
        sb.append("Hora inicio: ").append(horaInicio).append("\n");
        sb.append("Duración: ").append(duracionHoras).append("h\n");
        sb.append("Prepago: ").append(prepago ? "Sí" : "No").append("\n");

        // Mostrar extras primero
        if (!extras.isEmpty()) {
            sb.append("Extras:\n");
            for (Extras e : extras) {
                sb.append(" - ").append(e.getDescripcion()).append(" ($").append(e.getCosto()).append(")\n");
            }
        }

        sb.append("Monto total: $").append(getMontoTotal()).append("\n");
        sb.append("Monto abonado: $").append(montoAbonado).append("\n");
        sb.append("Saldo pendiente: $").append(calcularSaldoPendiente()).append("\n");
        sb.append("Observaciones: ").append(observacion.isEmpty() ? "ninguna" : observacion);

        return sb.toString();
    }
}
