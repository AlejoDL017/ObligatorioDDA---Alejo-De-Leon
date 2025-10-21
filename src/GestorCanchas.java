import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class GestorCanchas {

    private List<Cancha> canchas = new ArrayList<>();
    private Scanner sc = new Scanner(System.in);

    public void agregarCancha(Cancha c) {
        canchas.add(c);
    }
    public List<Cancha> getCanchas() {
        return canchas;
    }

    // registrar cancha
    public void registrarCancha() {
        String nombre = "";
        String deporte = "";
        boolean cubierta = false;
        int capacidad = 0;
        String estado = "disponible";
        String caracteristicas;

        // Validar nombre
        while (true) {
            System.out.print("Nombre cancha: ");
            nombre = sc.nextLine().trim();
            if (nombre.equals("")) {
                System.out.println("El nombre no puede estar vacío.");
            } else {
                break;
            }
        }

        // Validar deporte (no vacío y no numérico)
        boolean deporteValido = false;
        while (!deporteValido) {
            System.out.print("Deporte: ");
            deporte = sc.nextLine().trim();

            if (deporte.equals("")) {
                System.out.println("El deporte no puede estar vacío.");
                continue;
            }

            boolean esNumero = true;
            for (int i = 0; i < deporte.length(); i++) {
                char c = deporte.charAt(i);
                if (c < '0' || c > '9') {
                    esNumero = false;
                    break;
                }
            }

            if (esNumero) {
                System.out.println("El deporte no puede ser un número.");
            } else {
                deporteValido = true;
            }
        }

        // Validar si es cubierta (1 o 2)
        boolean opcionValida = false;
        while (!opcionValida) {
            try {
                System.out.println("¿La cancha es cubierta?");
                System.out.println("1. Sí");
                System.out.println("2. No");
                System.out.print("Opción: ");
                String entrada = sc.nextLine().trim();
                if (entrada.equals("")) {
                    System.out.println("Debe ingresar una opción.");
                    continue;
                }
                int opcion = Integer.parseInt(entrada);
                if (opcion == 1) {
                    cubierta = true;
                    opcionValida = true;
                } else if (opcion == 2) {
                    cubierta = false;
                    opcionValida = true;
                } else {
                    System.out.println("Debe ingresar 1 (Sí) o 2 (No).");
                }
            } catch (NumberFormatException e) {
                System.out.println("Entrada inválida. Ingrese un número (1 o 2).");
            }
        }

        // Validar capacidad (número positivo)
        boolean capacidadValida = false;
        while (!capacidadValida) {
            try {
                System.out.print("Capacidad: ");
                String entrada = sc.nextLine().trim();
                if (entrada.equals("")) {
                    System.out.println("La capacidad no puede estar vacía.");
                    continue;
                }
                capacidad = Integer.parseInt(entrada);
                if (capacidad < 0) {
                    System.out.println("La capacidad debe ser un número mayor o igual a 0.");
                } else {
                    capacidadValida = true;
                }
            } catch (NumberFormatException e) {
                System.out.println("Entrada inválida. Ingrese un número entero positivo.");
            }
        }


        // Características (opcional)
        System.out.print("Características (opcional): ");
        caracteristicas = sc.nextLine().trim();
        if (caracteristicas.equals("")) {
            caracteristicas = null;
        }

        Cancha c = new Cancha(
                canchas.size() + 1,
                nombre,
                deporte,
                cubierta,
                capacidad,
                estado,
                caracteristicas
        );
        agregarCancha(c);
        System.out.println("Cancha registrada:\n" + c);
    }

    // modificar cancha
    public void modificarCancha() {
        System.out.print("Ingrese el ID de la cancha a modificar: ");
        int id = Integer.parseInt(sc.nextLine());

        Cancha cancha = null;
        for (Cancha c : canchas) {
            if (c.getIdCancha() == id) {
                cancha = c;
                break;
            }
        }

        if (cancha == null) {
            System.out.println("No se encontró una cancha con ese ID.");
            return;
        }

        System.out.println("Modificando cancha: " + cancha.getNombre());

        System.out.print("Nuevo nombre (" + cancha.getNombre() + "): ");
        String nombre = sc.nextLine().trim();
        if (!nombre.isEmpty()) cancha.setNombre(nombre);

        System.out.print("Nuevo deporte (" + cancha.getDeporte() + "): ");
        String deporte = sc.nextLine().trim();
        if (!deporte.isEmpty()) cancha.setDeporte(deporte);

        System.out.print("¿Es cubierta? (true/false) [" + cancha.getCubierta() + "]: ");
        String cubiertaStr = sc.nextLine().trim();
        if (!cubiertaStr.isEmpty()) cancha.setCubierta(Boolean.parseBoolean(cubiertaStr));

        System.out.print("Nueva capacidad (" + cancha.getCapacidad() + "): ");
        String capacidadStr = sc.nextLine().trim();
        if (!capacidadStr.isEmpty()) cancha.setCapacidad(Integer.parseInt(capacidadStr));

        System.out.print("Nuevo estado (" + cancha.getEstado() + "): ");
        String estado = sc.nextLine().trim();
        if (!estado.isEmpty()) cancha.setEstado(estado);

        System.out.print("Nuevas características (" + (cancha.getCaracteristicas() != null ? cancha.getCaracteristicas() : "ninguna") + "): ");
        String caracteristicas = sc.nextLine().trim();
        cancha.setCaracteristicas(caracteristicas.isEmpty() ? null : caracteristicas);

        System.out.println("Cancha modificada correctamente.");
    }
    // eliminar cancha
    public void eliminarCancha() {
        System.out.print("Ingrese el ID de la cancha a eliminar: ");
        int id = Integer.parseInt(sc.nextLine());

        Cancha canchaAEliminar = null;
        for (Cancha c : canchas) {
            if (c.getIdCancha() == id) {
                canchaAEliminar = c;
                break;
            }
        }

        if (canchaAEliminar == null) {
            System.out.println("No se encontró una cancha con ese ID.");
            return;
        }

        System.out.println("¿Está seguro que desea eliminar la cancha '" + canchaAEliminar.getNombre() + "'? (s/n): ");
        String confirmacion = sc.nextLine().trim().toLowerCase();
        if (confirmacion.equals("s")) {
            canchas.remove(canchaAEliminar);
            System.out.println("Cancha eliminada correctamente.");
        } else {
            System.out.println("Operación cancelada.");
        }
    }

    // submenu consultas
    public void mostrarMenuConsultas(List<Reserva> reservas, Scanner sc) {
        int opcion;
        do {
            System.out.println("\n--- CONSULTAS DE CANCHAS ---");
            System.out.println("1. Listar todas las canchas");
            System.out.println("2. Buscar por deporte");
            System.out.println("3. Mostrar por nombre");
            System.out.println("4. Ver canchas cubiertas");
            System.out.println("5. Ver canchas descubiertas");
            System.out.println("6. Ver estado de canchas por fecha y hora");
            System.out.println("7. Volver");

            System.out.print("Seleccione una opción: ");
            try {
                opcion = Integer.parseInt(sc.nextLine());

                switch (opcion) {
                    case 1 -> mostrarCanchas();
                    case 2 -> {
                        System.out.print("Ingrese el deporte: ");
                        String deporte = sc.nextLine();
                        mostrarPorDeporte(deporte);
                    }
                    case 3 -> {
                        System.out.print("Ingrese el nombre de la cancha: ");
                        String nombre = sc.nextLine();
                        mostrarPorNombre(nombre);
                    }
                    case 4 -> mostrarCubiertas(true);
                    case 5 -> mostrarCubiertas(false);
                    case 6 -> mostrarEstadoPorFechaYHora(reservas, sc);

                    case 7 -> System.out.println("Volviendo al menú anterior...");
                    default -> System.out.println("Opción inválida.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Entrada inválida. Intente nuevamente.");
                opcion = -1; // Para que no salga del bucle si falla
            }
        } while (opcion != 7);
    }

    // listar canchas
    public void mostrarCanchas() {
        if (canchas.isEmpty()) {
            System.out.println("No hay canchas registradas.");
            return;
        }
        System.out.println("--- LISTADO DE CANCHAS ---");
        for (Cancha c : canchas) {
            System.out.println(c);
        }
    }

    // listar canchas por deporte
    public void mostrarPorDeporte(String deporte) {
        boolean encontrado = false;
        System.out.println("--- CANCHAS DE " + deporte.toUpperCase() + " ---");
        for (Cancha c : canchas) {
            if (c.getDeporte().equalsIgnoreCase(deporte)) {
                System.out.println(c);
                encontrado = true;
            }
        }
        if (!encontrado) {
            System.out.println("No hay canchas registradas para ese deporte.");
        }
    }

    // listar canchas por nombre
    public void mostrarPorNombre(String nombre) {
        boolean encontrado = false;
        System.out.println("--- CANCHAS LLAMADA " + nombre.toUpperCase() + " ---");
        for (Cancha c : canchas) {
            if (c.getNombre().equalsIgnoreCase(nombre)) {
                System.out.println(c);
                encontrado = true;
            }
        }
        if (!encontrado) {
            System.out.println("No hay canchas con ese nombre.");
        }
    }

    // mostrar canchas cubiertas o descubiertas
    public void mostrarCubiertas(boolean cubiertas) {
        String tipo = cubiertas ? "cubiertas" : "descubiertas";
        System.out.println("--- CANCHAS " + tipo.toUpperCase() + " ---");
        boolean encontrado = false;
        for (Cancha c : canchas) {
            if (c.isCubierta() == cubiertas) {
                System.out.println(c);
                encontrado = true;
            }
        }
        if (!encontrado) {
            System.out.println("No hay canchas " + tipo + " registradas.");
        }
    }
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
                // Ignorar y probar siguiente formato
            }
        }
        throw new DateTimeParseException("Formato de fecha inválido", fechaStr, 0);
    }

    public void mostrarEstadoCanchasPorFechaHora(List<Reserva> reservas, LocalDate fecha, LocalTime hora) {
        GestorReservas gestorTemporal = new GestorReservas();
        for (Reserva r : reservas) {
            gestorTemporal.getReservas().add(r); // cargar reservas temporalmente
        }

        if (canchas.isEmpty()) {
            System.out.println("No hay canchas registradas.");
            return;
        }

        System.out.println("--- ESTADO DE CANCHAS EN " + fecha + " a las " + hora + " ---");
        for (Cancha cancha : canchas) {
            String estado = gestorTemporal.obtenerEstadoCanchaEnFechaHora(cancha, fecha, hora);
            System.out.println(cancha.getNombre() + " (" + cancha.getDeporte() + ") -> Estado: " + estado);
        }
    }
    public void mostrarEstadoPorFechaYHora(List<Reserva> reservas, Scanner sc) {
        try {
            System.out.print("Ingrese fecha (por ejemplo 25/12/23 o 25-12-2023): ");
            String fechaInput = sc.nextLine();
            LocalDate fecha = parseFechaConFormatos(fechaInput);

            System.out.print("Ingrese hora (solo número de 7 a 23): ");
            String horaInput = sc.nextLine().trim();
            int hora = Integer.parseInt(horaInput);

            if (hora < 0 || hora > 23) {
                System.out.println("Hora inválida. Debe ser entre 0 y 23.");
                return;
            }

            LocalTime horaLocal = LocalTime.of(hora, 0);

            mostrarEstadoCanchasPorFechaHora(reservas, fecha, horaLocal);
        } catch (NumberFormatException e) {
            System.out.println("Entrada inválida. Debe ingresar un número entero para la hora.");
        } catch (Exception e) {
            System.out.println("Error en la entrada. Asegúrese de ingresar una fecha y hora válidas.");
        }
    }

}

