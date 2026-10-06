package repository.impl;

import model.Course;
import repository.CourseRepository;

import java.util.ArrayList;
import java.util.List;

/**
 * Implementación "simulada" de persistencia en fichero.
 *
 * NO escribe ni lee ningún fichero real: por dentro usa una lista en
 * memoria (igual que InMemoryCourseRepository), pero antes de cada
 * operación imprime por consola los mensajes que aparecerían si de
 * verdad estuviera leyendo/escribiendo "courses.txt" en disco.
 *
 * Útil para entender el PATRÓN de una implementación basada en fichero
 * (leer todo -> operar -> escribir todo) sin tener que gestionar
 * excepciones de E/S (IOException) ni depender del sistema de archivos.
 */
public class SimulatedFileCourseRepository implements CourseRepository {

    private static final String FILE_NAME = "courses.txt";

    // El "almacén real" sigue siendo una lista en memoria.
    private List<Course> courses = new ArrayList<>();

    public SimulatedFileCourseRepository() {
        System.out.println("[Fichero simulado] Comprobando si existe " + FILE_NAME + "...");
        System.out.println("[Fichero simulado] No existía. Se crea " + FILE_NAME + " vacío.");
    }

    @Override
    public Course create(Course c) {
        System.out.println("[Fichero simulado] Leyendo " + FILE_NAME + " completo...");
        System.out.println("[Fichero simulado] " + courses.size() + " líneas leídas.");

        int nextId = calcularSiguienteId();
        c.setId(nextId);
        courses.add(c);

        System.out.println("[Fichero simulado] Añadiendo línea: "
                + c.getId() + "|" + c.getTitle() + "|" + c.getHours() + "|" + c.getModality());
        System.out.println("[Fichero simulado] Escribiendo " + courses.size()
                + " líneas en " + FILE_NAME + "...");
        System.out.println("[Fichero simulado] Escritura completada.");

        return c;
    }

    @Override
    public Course findById(int id) {
        System.out.println("[Fichero simulado] Leyendo " + FILE_NAME
                + " y buscando línea con id = " + id + "...");

        for (Course c : courses) {
            if (c.getId() == id) {
                System.out.println("[Fichero simulado] Línea encontrada.");
                return c;
            }
        }
        System.out.println("[Fichero simulado] Ninguna línea coincide con ese id.");
        return null;
    }

    @Override
    public List<Course> findAll() {
        System.out.println("[Fichero simulado] Leyendo todas las líneas de " + FILE_NAME + "...");
        System.out.println("[Fichero simulado] " + courses.size() + " líneas leídas.");
        return new ArrayList<>(courses);
    }

    @Override
    public boolean update(Course c) {
        System.out.println("[Fichero simulado] Leyendo " + FILE_NAME + " completo...");

        boolean encontrado = false;
        for (int i = 0; i < courses.size(); i++) {
            if (courses.get(i).getId() == c.getId()) {
                courses.set(i, c);
                encontrado = true;
                break;
            }
        }

        if (!encontrado) {
            System.out.println("[Fichero simulado] No existe ninguna línea con id "
                    + c.getId() + ". No se modifica el fichero.");
            return false;
        }

        System.out.println("[Fichero simulado] Línea con id " + c.getId() + " actualizada.");
        System.out.println("[Fichero simulado] Reescribiendo " + courses.size()
                + " líneas en " + FILE_NAME + "...");
        return true;
    }

    @Override
    public boolean deleteById(int id) {
        System.out.println("[Fichero simulado] Leyendo " + FILE_NAME + " completo...");

        boolean eliminado = courses.removeIf(c -> c.getId() == id);

        if (!eliminado) {
            System.out.println("[Fichero simulado] No existe ninguna línea con id "
                    + id + ". No se modifica el fichero.");
            return false;
        }

        System.out.println("[Fichero simulado] Línea con id " + id + " eliminada.");
        System.out.println("[Fichero simulado] Reescribiendo " + courses.size()
                + " líneas en " + FILE_NAME + "...");
        return true;
    }

    @Override
    public int count() {
        System.out.println("[Fichero simulado] Leyendo " + FILE_NAME + " y contando líneas...");
        System.out.println("[Fichero simulado] Total = " + courses.size());
        return courses.size();
    }

    // Igual que en la versión real: recalculamos el siguiente id
    // mirando el máximo id actual, simulando que no hay un contador
    // persistente entre "ejecuciones" del fichero.
    private int calcularSiguienteId() {
        int maxId = 0;
        for (Course c : courses) {
            if (c.getId() > maxId) {
                maxId = c.getId();
            }
        }
        return maxId + 1;
    }
}