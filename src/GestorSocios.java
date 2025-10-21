import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class GestorSocios {

    private List<Socio> socios = new ArrayList<>();
    private Scanner sc = new Scanner(System.in);

    // agregar un nuevo socio
    public void agregarSocio(Socio s) {
        socios.add(s);
    }

    // obtener todos los socios
    public List<Socio> getSocios() {
        return socios;
    }

    // buscar socio por numero de documento
    public Socio buscarPorDocumento(String documento) {
        for (Socio s : socios) {
            if (s.getNumDocumento().equalsIgnoreCase(documento)) {
                return s;
            }
        }
        return null;
    }

    // listar todos los socios
    public void mostrarSocios() {
        if (socios.isEmpty()) {
            System.out.println("No hay socios registrados.");
        } else {
            for (Socio s : socios) {
                System.out.println(s);
            }
        }
    }

    // registrar socio con validaciones robustas
    public void registrarSocio() {
        System.out.println("\n--- REGISTRAR SOCIO ---");

        String nombre = leerTextoSoloLetras("Nombre: ");
        String aPaterno = leerTextoSoloLetras("Apellido paterno: ");
        String aMaterno = leerTextoSoloLetrasOpcional("Apellido materno (opcional): ");
        String numDocumento;

        while (true) {
            numDocumento = leerSoloNumeros("Documento: ");
            if (buscarPorDocumento(numDocumento) != null) {
                System.out.println("Ya existe un socio con ese documento. Intente nuevamente.");
            } else {
                break;
            }
        }

        LocalDate fechaNacimiento = leerFechaFlexible("Fecha de nacimiento (DD/MM/YY o DD-MM-YY): ");
        String telefono = leerSoloNumeros("Teléfono: ");
        String pais = leerTextoSoloLetras("País: ");

        Socio s = new Socio(
                socios.size() + 1,
                nombre,
                aPaterno,
                aMaterno,
                numDocumento,
                fechaNacimiento,
                telefono,
                pais
        );

        agregarSocio(s);
        System.out.println("\nSocio agregado correctamente:\n" + s);
    }

    // ================== MÉTODOS AUXILIARES ==================

    // Solo letras (sin números ni caracteres especiales)
    private String leerTextoSoloLetras(String mensaje) {
        while (true) {
            System.out.print(mensaje);
            String input = sc.nextLine().trim();
            if (input.isEmpty()) {
                System.out.println("El campo no puede estar vacío. Intente nuevamente.");
                continue;
            }
            if (!input.matches("[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]+")) {
                System.out.println("Solo se permiten letras. Intente nuevamente.");
                continue;
            }
            return input;
        }
    }

    // Versión opcional (permite vacío)
    private String leerTextoSoloLetrasOpcional(String mensaje) {
        while (true) {
            System.out.print(mensaje);
            String input = sc.nextLine().trim();
            if (input.isEmpty()) {
                return ""; // puede quedar vacío
            }
            if (!input.matches("[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]+")) {
                System.out.println("Solo se permiten letras. Intente nuevamente.");
                continue;
            }
            return input;
        }
    }

    // Solo números (para documento o teléfono)
    private String leerSoloNumeros(String mensaje) {
        while (true) {
            System.out.print(mensaje);
            String input = sc.nextLine().trim();
            if (input.isEmpty()) {
                System.out.println("El campo no puede estar vacío. Intente nuevamente.");
                continue;
            }
            if (!input.matches("\\d+")) {
                System.out.println("Solo se permiten números. Intente nuevamente.");
                continue;
            }
            return input;
        }
    }

    // Fecha con múltiples formatos posibles
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
            String fechaStr = sc.nextLine().trim();
            if (fechaStr.isEmpty()) {
                System.out.println("La fecha no puede estar vacía.");
                continue;
            }

            for (DateTimeFormatter f : formatos) {
                try {
                    return LocalDate.parse(fechaStr, f);
                } catch (DateTimeParseException ignored) {
                }
            }
            System.out.println("Formato inválido. Use por ejemplo: 25/12/1999 o 25-12-99.");
        }
    }
}
