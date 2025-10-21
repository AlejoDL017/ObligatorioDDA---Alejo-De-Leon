import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.time.format.DateTimeParseException;
import java.time.format.DateTimeFormatter;

public class GestorReservas {

    private List<Reserva> reservas = new ArrayList<>();
    private Scanner sc = new Scanner(System.in);

    private LocalDate parseFechaConFormatos(String fechaStr) {
        DateTimeFormatter[] formatos = {
                DateTimeFormatter.ofPattern("d/M/yy"),
                DateTimeFormatter.ofPattern("d-M-yy"),
                DateTimeFormatter.ofPattern("dd/MM/yy"),
                DateTimeFormatter.ofPattern("dd-MM-yy"),
                DateTimeFormatter.ofPattern("d/M/yyyy"),
                DateTimeFormatter.ofPattern("dd/MM/yyyy"),
                DateTimeFormatter.ofPattern("d-M-yyyy"),
                DateTimeFormatter.ofPattern("dd-MM-yyyy")
        };

        for (DateTimeFormatter formato : formatos) {
            try {
                return LocalDate.parse(fechaStr, formato);
            } catch (DateTimeParseException e) {
                // Intentar siguiente formato
            }
        }
        throw new DateTimeParseException("Formato de fecha inválido", fechaStr, 0);
    }

    // AHORA RECIBE LISTA DE TARIFAS
    public void registrarReserva(List<Socio> socios, List<Cancha> canchas, List<Extras> extras, List<Tarifa> tarifas) {
        try {
            if (socios.isEmpty() || canchas.isEmpty()) {
                System.out.println("Debe haber al menos un socio y una cancha para registrar una reserva.");
                return;
            }
            if (tarifas.isEmpty()) {
                System.out.println("No hay tarifas cargadas. No se puede registrar una reserva.");
                return;
            }

            System.out.println("\n--- REGISTRAR RESERVA ---");

            // Seleccionar socio
            System.out.println("Socios disponibles:");
            for (Socio s : socios) {
                System.out.println(s.getIdSocio() + " - " + s.getNombreCompleto());
            }
            System.out.print("Ingrese ID del socio: ");
            int idSocio = Integer.parseInt(sc.nextLine());
            Socio socioSeleccionado = null;
            for (Socio s : socios) {
                if (s.getIdSocio() == idSocio) {
                    socioSeleccionado = s;
                    break;
                }
            }
            if (socioSeleccionado == null) {
                System.out.println("Socio no encontrado.");
                return;
            }

            // Seleccionar cancha
            System.out.println("\nCanchas disponibles:");
            for (Cancha c : canchas) {
                System.out.println(c.getIdCancha() + " - " + c.getNombre() + " (" + c.getDeporte() + ")");
            }
            System.out.print("Ingrese ID de la cancha: ");
            int idCancha = Integer.parseInt(sc.nextLine());
            Cancha canchaSeleccionada = null;
            for (Cancha c : canchas) {
                if (c.getIdCancha() == idCancha) {
                    canchaSeleccionada = c;
                    break;
                }
            }
            if (canchaSeleccionada == null) {
                System.out.println("Cancha no encontrada.");
                return;
            }

            // Ingresar fecha
            LocalDate fecha = null;
            while (true) {
                try {
                    System.out.print("Fecha de la reserva: ");
                    fecha = parseFechaConFormatos(sc.nextLine().trim());
                    if (fecha.isBefore(LocalDate.now())) {
                        System.out.println("No se puede reservar en fechas pasadas.");
                        continue;
                    }
                    break;
                } catch (DateTimeParseException e) {
                    System.out.println("Formato de fecha inválido. Ej: 20/10/25 o 20-10-2025.");
                }
            }

            // Ingresar hora
            LocalTime horaInicio = null;
            while (true) {
                try {
                    System.out.print("Hora de inicio (7 a 23): ");
                    int horaEntera = Integer.parseInt(sc.nextLine().replaceFirst("^0+", ""));
                    if (horaEntera < 7 || horaEntera > 23) {
                        System.out.println("La hora debe estar entre 7 y 23.");
                        continue;
                    }
                    horaInicio = LocalTime.of(horaEntera, 0);
                    if (fecha.equals(LocalDate.now()) && horaInicio.isBefore(LocalTime.now())) {
                        System.out.println("La hora debe ser igual o posterior a la hora actual.");
                        continue;
                    }
                    break;
                } catch (Exception e) {
                    System.out.println("Ingrese una hora válida.");
                }
            }

            // Ingresar duración
            int duracion = 1;
            while (true) {
                try {
                    System.out.print("Duración (horas): ");
                    duracion = Integer.parseInt(sc.nextLine());
                    if (duracion < 1 || horaInicio.plusHours(duracion).isAfter(LocalTime.of(23, 59))) {
                        System.out.println("Duración inválida o supera las 00:00.");
                        continue;
                    }
                    break;
                } catch (Exception e) {
                    System.out.println("Ingrese un número válido.");
                }
            }

            // Verificar disponibilidad dinámicamente
            String estado = obtenerEstadoCanchaEnFechaHora(canchaSeleccionada, fecha, horaInicio);
            if (!estado.equals("disponible")) {
                System.out.println("La cancha no está disponible en ese horario (" + estado + ").");
                return;
            }

            // Buscar tarifa vigente
            Tarifa tarifaVigente = null;
            for (Tarifa t : tarifas) {
                if (t.getDeporte().equalsIgnoreCase(canchaSeleccionada.getDeporte())
                        && (t.getFechaInicioVigencia().isBefore(LocalDate.now())
                        || t.getFechaInicioVigencia().isEqual(LocalDate.now()))) {
                    tarifaVigente = t;
                }
            }
            if (tarifaVigente == null) {
                System.out.println("No hay tarifa vigente para este deporte.");
                return;
            }

            double montoTotal = tarifaVigente.getMontoPorHora() * duracion;
            boolean prepago = false;
            boolean abonado = false;
            double montoAbonado = 0;

            // Preguntar prepago
            while (true) {
                System.out.println("¿Es prepago? 1: Sí  2: No");
                String opcion = sc.nextLine().trim();
                if (opcion.equals("1")) {
                    prepago = true;
                    while (true) {
                        try {
                            System.out.print("Monto a abonar: ");
                            montoAbonado = Double.parseDouble(sc.nextLine());
                            if (montoAbonado < 0 || montoAbonado > montoTotal) {
                                System.out.println("Monto inválido.");
                                continue;
                            }
                            abonado = montoAbonado >= montoTotal;
                            break;
                        } catch (Exception e) {
                            System.out.println("Monto inválido.");
                        }
                    }
                    break;
                } else if (opcion.equals("2")) {
                    break;
                } else {
                    System.out.println("Opción inválida.");
                }
            }

            // Observaciones
            System.out.print("Observaciones: ");
            String observacion = sc.nextLine();

            // Extras
            List<Extras> extrasSeleccionados = new ArrayList<>();
            if (!extras.isEmpty()) {
                System.out.println("Extras disponibles (ID separados por coma, Enter para ninguno):");
                for (Extras e : extras) {
                    System.out.println(e.getIdExtra() + " - " + e.getDescripcion() + " ($" + e.getCosto() + ")");
                }
                String entrada = sc.nextLine().trim();
                if (!entrada.isEmpty()) {
                    for (String idStr : entrada.split(",")) {
                        int idExtra = Integer.parseInt(idStr.trim());
                        for (Extras e : extras) {
                            if (e.getIdExtra() == idExtra) {
                                extrasSeleccionados.add(e);
                            }
                        }
                    }
                }
            }

            // Crear reserva
            Reserva nueva = new Reserva(
                    reservas.size() + 1,
                    socioSeleccionado,
                    canchaSeleccionada,
                    fecha,
                    horaInicio,
                    duracion,
                    prepago,
                    LocalDate.now(),
                    observacion,
                    extrasSeleccionados,
                    tarifaVigente
            );
            nueva.setMontoAbonado(montoAbonado);
            nueva.setAbonado(abonado);

            reservas.add(nueva);

            System.out.println("Reserva registrada correctamente:\n" + nueva);

        } catch (Exception e) {
            System.out.println("Error al registrar la reserva: " + e.getMessage());
        }
    }

    // LISTADO DE RESERVAS
    public void mostrarReservas() {
        try {
            actualizarCanchasOcupadasYDisponibles();
            System.out.println("\n--- LISTADO DE RESERVAS ---");
            if (reservas.isEmpty()) {
                System.out.println("No hay reservas registradas.");
                return;
            }
            for (Reserva r : reservas) {
                System.out.println(r);
                System.out.println(" - Socio: " + r.getSocio().getNombreCompleto());
                System.out.println(" - Fecha de reserva: " + r.getFechaReserva());
                System.out.println(" - Prepago: " + (r.isPrepago() ? "Sí" : "No"));
                System.out.println(" - Observaciones: " + r.getObservacion());

                if (!r.getExtras().isEmpty()) {
                    System.out.println(" - Extras:");
                    for (Extras e : r.getExtras()) {
                        System.out.println("   • " + e.getDescripcion() + " ($" + e.getCosto() + ")");
                    }
                }
                System.out.println();
            }
        } catch (Exception e) {
            System.out.println("Error al mostrar reservas: " + e.getMessage());
        }
    }

    public void actualizarTarifaReserva(int idReserva, Tarifa nuevaTarifa) {
        for (Reserva r : reservas) {
            if (r.getIdReserva() == idReserva) {
                r.setTarifaAplicada(nuevaTarifa);
                System.out.println("Tarifa actualizada correctamente.");
                return;
            }
        }
        System.out.println("Reserva no encontrada.");
    }
    // registrar pago modificado para usar tarifa de la reserva
    public void registrarPago() {
        try {
            System.out.print("Ingrese ID de reserva: ");
            int id = Integer.parseInt(sc.nextLine());
            Reserva encontrada = null;

            for (Reserva r : reservas) {
                if (r.getIdReserva() == id) {
                    encontrada = r;
                    break;
                }
            }

            if (encontrada == null) {
                System.out.println("Reserva no encontrada.");
                return;
            }

            if (encontrada.isAbonado()) {
                System.out.println("Esta reserva ya está abonada.");
                return;
            }

            Tarifa tarifaBase = encontrada.getTarifaAplicada(); // USAR TARIFA ASOCIADA

            System.out.print("Ingrese monto abonado: ");
            double monto = Double.parseDouble(sc.nextLine());

            double saldoPendiente = encontrada.calcularSaldoPendiente();

            if (monto >= saldoPendiente) {
                encontrada.abonar();
                encontrada.setAbonado(true);
                System.out.println("Pago completo registrado. Reserva abonada.");
            } else {
                double restante = saldoPendiente - monto;
                System.out.println("Pago parcial registrado. Saldo restante: $" + restante);
            }
        } catch (Exception e) {
            System.out.println("Error al registrar pago: " + e.getMessage());
        }
    }

    // canchas reservadas por fecha
    public void consultarPorFecha(LocalDate fecha) {
        try {
            System.out.println("\n--- RESERVAS DEL " + fecha + " ---");
            boolean encontrada = false;
            for (Reserva r : reservas) {
                if (r.getFecha().equals(fecha)) {
                    System.out.println(r);
                    encontrada = true;
                }
            }
            if (!encontrada) {
                System.out.println("No hay reservas en esa fecha.");
            }
        } catch (Exception e) {
            System.out.println("Error al consultar reservas por fecha: " + e.getMessage());
        }
    }

    // ocupar cancha
    public void ocuparCancha(int idReserva) {
        try {
            for (Reserva r : reservas) {
                if (r.getIdReserva() == idReserva) {
                    r.getCancha().actualizarEstado("ocupada");
                    System.out.println("Cancha marcada como ocupada.");
                    return;
                }
            }
            System.out.println("Reserva no encontrada.");
        } catch (Exception e) {
            System.out.println("Error al ocupar cancha: " + e.getMessage());
        }
    }

    // modificar reserva (solo fecha/hora/duración)
    public void modificarReserva() {
        try {
            System.out.print("Ingrese ID de reserva a modificar: ");
            int id = Integer.parseInt(sc.nextLine());
            Reserva encontrada = null;

            for (Reserva r : reservas) {
                if (r.getIdReserva() == id) {
                    encontrada = r;
                    break;
                }
            }

            if (encontrada == null) {
                System.out.println("Reserva no encontrada.");
                return;
            }

            System.out.print("Nueva fecha (AAAA-MM-DD): ");
            LocalDate nuevaFecha = LocalDate.parse(sc.nextLine());
            System.out.print("Nueva hora (HH:MM): ");
            LocalTime nuevaHora = LocalTime.parse(sc.nextLine());
            System.out.print("Nueva duración (horas): ");
            int nuevaDuracion = Integer.parseInt(sc.nextLine());

            // uso de setters
            encontrada.setFecha(nuevaFecha);
            encontrada.setHoraInicio(nuevaHora);
            encontrada.setDuracionHoras(nuevaDuracion);

            System.out.println("Reserva modificada correctamente.");
        } catch (Exception e) {
            System.out.println("Error al modificar la reserva: " + e.getMessage());
        }
    }

    // cancelar reserva
    public void cancelarReserva() {
        try {
            System.out.print("Ingrese ID de reserva a cancelar: ");
            int id = Integer.parseInt(sc.nextLine());
            Reserva encontrada = null;

            for (Reserva r : reservas) {
                if (r.getIdReserva() == id) {
                    encontrada = r;
                    break;
                }
            }

            if (encontrada == null) {
                System.out.println("Reserva no encontrada.");
                return;
            }

            reservas.remove(encontrada);
            encontrada.getCancha().actualizarEstado("disponible");
            System.out.println("Reserva cancelada correctamente.");
        } catch (Exception e) {
            System.out.println("Error al cancelar la reserva: " + e.getMessage());
        }
    }


    public String obtenerEstadoCanchaEnFechaHora(Cancha cancha, LocalDate fecha, LocalTime hora) {
        try {
            for (Reserva r : reservas) {
                if (r.getCancha().getIdCancha() == cancha.getIdCancha() && r.getFecha().equals(fecha)) {
                    LocalTime horaFin = r.getHoraInicio().plusHours(r.getDuracionHoras());

                    if (!hora.isBefore(r.getHoraInicio()) && hora.isBefore(horaFin)) {
                        if ("ocupada".equalsIgnoreCase(cancha.getEstado())) {
                            return "ocupada";
                        } else {
                            return "reservada";
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Error al obtener estado de cancha: " + e.getMessage());
        }
        return "disponible";
    }

    public void actualizarCanchasOcupadasYDisponibles() {
        LocalDate hoy = LocalDate.now();
        LocalTime ahora = LocalTime.now();

        // marcar las canchas como disponibles
        for (Reserva r : reservas) {
            if (r.getFecha().isBefore(hoy) || (r.getFecha().isEqual(hoy) && ahora.isAfter(r.getHoraInicio().plusHours(r.getDuracionHoras())))) {
                r.getCancha().actualizarEstado("disponible");
            }
        }

        // marcar como ocupadas las que están en uso
        for (Reserva r : reservas) {
            if (r.getFecha().equals(hoy)) {
                LocalTime inicio = r.getHoraInicio();
                LocalTime fin = inicio.plusHours(r.getDuracionHoras());

                if (!ahora.isBefore(inicio) && ahora.isBefore(fin)) {
                    r.getCancha().actualizarEstado("ocupada");
                }
            }
        }
    }

    public List<Reserva> getReservas() {
        return reservas;
    }
}