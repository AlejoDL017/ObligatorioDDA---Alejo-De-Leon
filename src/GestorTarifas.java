import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
public class GestorTarifas {

    private List<Tarifa> tarifas = new ArrayList<>();
    private Scanner sc = new Scanner(System.in);
    private LocalDate leerFechaFlexible(String mensaje) {
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
        while (true) {
            System.out.print(mensaje);
            String input = sc.nextLine().trim();
            for (DateTimeFormatter formato : formatos) {
                try {
                    return LocalDate.parse(input, formato);
                } catch (DateTimeParseException e) {
                    // Ignorar y probar siguiente formato
                }
            }
            System.out.println("Formato de fecha inválido. Intente nuevamente.");
        }
    }
    public void agregarTarifa(Tarifa t) {
        tarifas.add(t);
    }

    public List<Tarifa> getTarifas() {
        return tarifas;
    }

    // Registrar tarifa
    public void registrarTarifa() {
        System.out.println("\n--- REGISTRAR NUEVA TARIFA ---");
        System.out.print("Deporte: ");
        String deporte = sc.nextLine().trim();

        System.out.print("Monto por hora: ");
        double monto = Double.parseDouble(sc.nextLine());

        LocalDate fecha = leerFechaFlexible("Fecha inicio vigencia: ");

        Tarifa nueva = new Tarifa(tarifas.size() + 1, deporte, monto, fecha);
        agregarTarifa(nueva);

        System.out.println("Tarifa registrada: " + nueva);
    }

    // Mostrar todas las tarifas
    public void mostrarTarifas() {
        if (tarifas.isEmpty()) {
            System.out.println("No hay tarifas registradas.");
            return;
        }
        System.out.println("\n--- LISTADO DE TARIFAS ---");
        tarifas.forEach(System.out::println);
    }

    // Obtener la tarifa vigente para un deporte y fecha
    public Tarifa obtenerTarifaVigente(String deporte, LocalDate fecha) {
        return tarifas.stream()
                .filter(t -> t.correspondeA(deporte) && t.esVigente(fecha))
                .max(Comparator.comparing(Tarifa::getFechaInicioVigencia))
                .orElse(null);
    }

    // Modificar tarifa existente
    public void modificarTarifa() {
        mostrarTarifas();
        System.out.print("Ingrese ID de la tarifa a modificar: ");
        int id = Integer.parseInt(sc.nextLine());

        Tarifa tarifa = tarifas.stream()
                .filter(t -> t.getIdTarifa() == id)
                .findFirst()
                .orElse(null);

        if (tarifa == null) {
            System.out.println("No se encontró una tarifa con ese ID.");
            return;
        }

        System.out.print("Nuevo monto por hora (actual $" + tarifa.getMontoPorHora() + "): ");
        String montoStr = sc.nextLine().trim();
        if (!montoStr.isEmpty()) {
            double nuevoMonto = Double.parseDouble(montoStr);
            tarifas.set(id - 1, new Tarifa(id, tarifa.getDeporte(), nuevoMonto, tarifa.getFechaInicioVigencia()));
            System.out.println("Tarifa modificada correctamente.");
        }
    }

    // Buscar tarifas por deporte
    public void buscarPorDeporte(String deporte) {
        System.out.println("\n--- TARIFAS PARA " + deporte.toUpperCase() + " ---");
        tarifas.stream()
                .filter(t -> t.correspondeA(deporte))
                .forEach(System.out::println);
    }
}
