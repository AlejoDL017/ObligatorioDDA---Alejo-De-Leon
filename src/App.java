import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

public class App {

    private Scanner sc = new Scanner(System.in);

    private GestorSocios gestorSocios = new GestorSocios();
    private GestorCanchas gestorCanchas = new GestorCanchas();
    private GestorReservas gestorReserva = new GestorReservas();
    private GestorTarifas gestorTarifas = new GestorTarifas();
    private GestorExtras gestorExtras = new GestorExtras();

    public void ejecutar() {
        int opcion;
        do {
            System.out.println("\n========== CLUB DEPORTIVO ==========");
            System.out.println("1. Gestión de socios");
            System.out.println("2. Gestión de canchas");
            System.out.println("3. Gestión de reservas");
            System.out.println("4. Gestión de tarifas");
            System.out.println("5. Gestión de extras");
            System.out.println("6. Salir");
            opcion = leerEntero("Seleccione una opción: ", 1, 6);

            switch (opcion) {
                case 1 -> menuSocios();
                case 2 -> menuCanchas();
                case 3 -> menuReservas();
                case 4 -> menuTarifas();
                case 5 -> menuExtras();
                case 6 -> System.out.println("Saliendo del sistema...");
                default -> System.out.println("Opción inválida.");
            }
        } while (opcion != 6);
    }

    // ---------------- MENÚ SOCIOS ----------------
    private void menuSocios() {
        int opcion;
        do {
            System.out.println("\n--- GESTIÓN DE SOCIOS ---");
            System.out.println("1. Registrar socio");
            System.out.println("2. Listar socios");
            System.out.println("3. Buscar socio por documento");
            System.out.println("4. Volver al menú principal");
            opcion = leerEntero("Seleccione: ", 1, 4);

            switch (opcion) {
                case 1 -> gestorSocios.registrarSocio();
                case 2 -> gestorSocios.mostrarSocios();
                case 3 -> {
                    String doc = leerTexto("Ingrese documento: ");
                    var socio = gestorSocios.buscarPorDocumento(doc);
                    if (socio != null) System.out.println(socio);
                    else System.out.println("No se encontró el socio.");
                }
                case 4 -> System.out.println("Volviendo...");
                default -> System.out.println("Opción inválida.");
            }
        } while (opcion != 4);
    }

    // ---------------- MENÚ CANCHAS ----------------
    private void menuCanchas() {
        int opcion;
        do {
            System.out.println("\n--- GESTIÓN DE CANCHAS ---");
            System.out.println("1. Registrar cancha");
            System.out.println("2. Modificar cancha");
            System.out.println("3. Eliminar cancha");
            System.out.println("4. Consultas");
            System.out.println("5. Volver al menú principal");
            opcion = leerEntero("Seleccione: ", 1, 5);

            switch (opcion) {
                case 1 -> gestorCanchas.registrarCancha();
                case 2 -> gestorCanchas.modificarCancha();
                case 3 -> gestorCanchas.eliminarCancha();
                case 4 -> gestorCanchas.mostrarMenuConsultas(gestorReserva.getReservas(), sc);
                case 5 -> System.out.println("Volviendo...");
                default -> System.out.println("Opción inválida.");
            }
        } while (opcion != 5);
    }

    // ---------------- MENÚ RESERVAS ----------------
    private void menuReservas() {
        int opcion;
        do {
            System.out.println("\n--- GESTIÓN DE RESERVAS ---");
            System.out.println("1. Registrar reserva");
            System.out.println("2. Listar reservas");
            System.out.println("3. Consultar reservas por fecha");
            System.out.println("4. Marcar cancha como ocupada");
            System.out.println("5. Modificar reserva");
            System.out.println("6. Cancelar reserva");
            System.out.println("7. Registrar pago de reserva");
            System.out.println("8. Volver al menú principal");
            opcion = leerEntero("Seleccione: ", 1, 8);

            switch (opcion) {
                case 1 -> gestorReserva.registrarReserva(
                        gestorSocios.getSocios(),
                        gestorCanchas.getCanchas(),
                        gestorExtras.getExtras(),
                        gestorTarifas.getTarifas()
                );
                case 2 -> gestorReserva.mostrarReservas();
                case 3 -> {
                    LocalDate fecha = leerFecha("Ingrese fecha (AAAA-MM-DD): ");
                    gestorReserva.consultarPorFecha(fecha);
                }
                case 4 -> {
                    int id = leerEntero("Ingrese ID de reserva: ");
                    gestorReserva.ocuparCancha(id);
                }
                case 5 -> gestorReserva.modificarReserva();
                case 6 -> gestorReserva.cancelarReserva();
                case 7 -> gestorReserva.registrarPago();
                case 8 -> System.out.println("Volviendo al menú principal...");
                default -> System.out.println("Opción inválida.");
            }
        } while (opcion != 8);
    }

    // ---------------- MENÚ TARIFAS ----------------
    private void menuTarifas() {
        int opcion;
        do {
            System.out.println("\n--- GESTIÓN DE TARIFAS ---");
            System.out.println("1. Registrar nueva tarifa");
            System.out.println("2. Listar todas las tarifas");
            System.out.println("3. Consultar tarifa vigente por deporte y fecha");
            System.out.println("4. Buscar tarifas por deporte");
            System.out.println("5. Modificar tarifa existente");
            System.out.println("6. Volver al menú principal");
            opcion = leerEntero("Seleccione: ", 1, 6);

            switch (opcion) {
                case 1 -> gestorTarifas.registrarTarifa();
                case 2 -> gestorTarifas.mostrarTarifas();
                case 3 -> {
                    String deporte = leerTexto("Ingrese el deporte: ");
                    LocalDate fecha = leerFecha("Ingrese la fecha (AAAA-MM-DD): ");
                    Tarifa vigente = gestorTarifas.obtenerTarifaVigente(deporte, fecha);
                    if (vigente != null)
                        System.out.println("Tarifa vigente encontrada: " + vigente);
                    else
                        System.out.println("No hay tarifa vigente para ese deporte y fecha.");
                }
                case 4 -> {
                    String deporte = leerTexto("Ingrese el deporte: ");
                    gestorTarifas.buscarPorDeporte(deporte);
                }
                case 5 -> gestorTarifas.modificarTarifa();
                case 6 -> System.out.println("Volviendo al menú principal...");
                default -> System.out.println("Opción inválida.");
            }
        } while (opcion != 6);
    }

    // ---------------- MENÚ EXTRAS ----------------
    private void menuExtras() {
        int opcion;
        do {
            System.out.println("\n--- GESTIÓN DE EXTRAS ---");
            System.out.println("1. Registrar nuevo extra");
            System.out.println("2. Listar extras");
            System.out.println("3. Eliminar extra");
            System.out.println("4. Volver al menú principal");
            opcion = leerEntero("Seleccione: ", 1, 4);

            switch (opcion) {
                case 1 -> gestorExtras.registrarExtra();
                case 2 -> gestorExtras.mostrarExtras();
                case 3 -> gestorExtras.eliminarExtra();
                case 4 -> System.out.println("Volviendo al menú principal...");
                default -> System.out.println("Opción inválida.");
            }
        } while (opcion != 4);
    }

    // ======================= MÉTODOS AUXILIARES =======================

    private int leerEntero(String mensaje) {
        while (true) {
            System.out.print(mensaje);
            String entrada = sc.nextLine().trim();
            if (entrada.isEmpty()) {
                System.out.println("El campo no puede estar vacío.");
                continue;
            }
            try {
                return Integer.parseInt(entrada);
            } catch (NumberFormatException e) {
                System.out.println("Debe ingresar un número entero válido.");
            }
        }
    }

    private int leerEntero(String mensaje, int min, int max) {
        while (true) {
            int valor = leerEntero(mensaje);
            if (valor < min || valor > max) {
                System.out.println("Debe ingresar un valor entre " + min + " y " + max + ".");
                continue;
            }
            return valor;
        }
    }

    private String leerTexto(String mensaje) {
        while (true) {
            System.out.print(mensaje);
            String entrada = sc.nextLine().trim();
            if (!entrada.isEmpty()) return entrada;
            System.out.println("El campo no puede estar vacío.");
        }
    }

    private LocalDate leerFecha(String mensaje) {
        while (true) {
            System.out.print(mensaje);
            String entrada = sc.nextLine().trim();
            if (entrada.isEmpty()) {
                System.out.println("El campo no puede estar vacío.");
                continue;
            }
            try {
                return LocalDate.parse(entrada);
            } catch (DateTimeParseException e) {
                System.out.println("Formato inválido. Use AAAA-MM-DD.");
            }
        }
    }
}
