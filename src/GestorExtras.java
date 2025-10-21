import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class GestorExtras {
    private List<Extras> extras = new ArrayList<>();
    private Scanner sc = new Scanner(System.in);

    // Registrar un nuevo extra
    public void registrarExtra() {
        System.out.println("\n--- REGISTRAR EXTRA ---");

        System.out.print("Descripción del extra: ");
        String descripcion = sc.nextLine().trim();
        if (descripcion.isEmpty()) {
            System.out.println("Descripción inválida. Operación cancelada.");
            return;
        }

        System.out.print("Costo ($): ");
        double costo;
        try {
            costo = Double.parseDouble(sc.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("Costo inválido. Operación cancelada.");
            return;
        }

        Extras e = new Extras(extras.size() + 1, descripcion, costo);
        extras.add(e);
        System.out.println("Extra registrado correctamente: " + e);
    }

    // Mostrar todos los extras disponibles
    public void mostrarExtras() {
        System.out.println("\n--- LISTADO DE EXTRAS ---");
        if (extras.isEmpty()) {
            System.out.println("No hay extras registrados.");
            return;
        }

        for (Extras e : extras) {
            System.out.println("ID " + e.getIdExtra() + ": " + e);
        }
    }

    // Buscar extra por ID
    public Extras buscarExtraPorId(int id) {
        for (Extras e : extras) {
            if (e.getIdExtra() == id) {
                return e;
            }
        }
        return null;
    }

    // Eliminar un extra
    public void eliminarExtra() {
        mostrarExtras();
        if (extras.isEmpty()) return;

        System.out.print("Ingrese el ID del extra a eliminar: ");
        int id;
        try {
            id = Integer.parseInt(sc.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("ID inválido.");
            return;
        }

        Extras encontrado = buscarExtraPorId(id);
        if (encontrado != null) {
            extras.remove(encontrado);
            System.out.println("Extra eliminado correctamente: " + encontrado.getDescripcion());
        } else {
            System.out.println("No se encontró ningún extra con ese ID.");
        }
    }

    // Devuelve todos los extras registrados
    public List<Extras> getExtras() {
        return extras;
    }
}