package repository.impl;

import model.Course;
import repository.CourseRepository;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementación de CourseRepository que persiste los cursos en un
 * fichero de texto plano, en lugar de en una base de datos o en memoria.
 *
 * Cada curso se guarda como una línea con el formato:
 *   id|title|hours|modality
 *
 * Al no haber "SELECT" ni conexión, cada operación se basa en:
 *   1) Leer TODO el fichero y convertirlo en una List<Course>.
 *   2) Hacer la operación sobre esa lista (igual que InMemoryCourseRepository).
 *   3) Volver a escribir TODA la lista en el fichero.
 *
 * Es una simplificación deliberada (no eficiente para ficheros grandes),
 * pero permite ver claramente el patrón: el contrato (CourseRepository)
 * es el mismo, solo cambia el mecanismo de almacenamiento.
 */
public class FileCourseRepository implements CourseRepository {

    private static final String FILE_PATH = "courses.txt";
    private static final String DELIMITER = "\\|"; // para split() (regex)
    private static final String DELIMITER_WRITE = "|"; // para escribir

    public FileCourseRepository() {
        // Si el fichero no existe todavía (primera ejecución), lo creamos vacío.
        try {
            Path path = Path.of(FILE_PATH);
            if (!Files.exists(path)) {
                Files.createFile(path);
                System.out.println("[Fichero] Creado " + FILE_PATH + " (no existía).");
            }
        } catch (IOException e) {
            throw new RuntimeException("No se ha podido crear el fichero " + FILE_PATH, e);
        }
    }

    @Override
    public Course create(Course c) {
        List<Course> courses = readAll();

        int nextId = calcularSiguienteId(courses);
        c.setId(nextId);
        courses.add(c);

        writeAll(courses);
        System.out.println("[Fichero] Curso añadido con id " + c.getId() + " en " + FILE_PATH);
        return c;
    }

    @Override
    public Course findById(int id) {
        List<Course> courses = readAll();
        for (Course c : courses) {
            if (c.getId() == id) {
                return c;
            }
        }
        return null; // igual que en las otras implementaciones: null si no existe
    }

    @Override
    public List<Course> findAll() {
        return readAll();
    }

    @Override
    public boolean update(Course c) {
        List<Course> courses = readAll();

        boolean encontrado = false;
        for (int i = 0; i < courses.size(); i++) {
            if (courses.get(i).getId() == c.getId()) {
                courses.set(i, c); // sustituimos el curso completo por el actualizado
                encontrado = true;
                break;
            }
        }

        if (!encontrado) {
            return false;
        }

        writeAll(courses);
        return true;
    }

    @Override
    public boolean deleteById(int id) {
        List<Course> courses = readAll();

        boolean eliminado = courses.removeIf(c -> c.getId() == id);

        if (eliminado) {
            writeAll(courses);
        }
        return eliminado;
    }

    @Override
    public int count() {
        return readAll().size();
    }

    // ---------------------------------------------------------------
    // Métodos privados de apoyo: leer/escribir el fichero completo.
    // ---------------------------------------------------------------

    private List<Course> readAll() {
        List<Course> courses = new ArrayList<>();

        try {
            List<String> lineas = Files.readAllLines(Path.of(FILE_PATH), StandardCharsets.UTF_8);

            for (String linea : lineas) {
                if (linea.isBlank()) {
                    continue; // ignoramos líneas vacías
                }
                courses.add(parseLinea(linea));
            }

        } catch (IOException e) {
            throw new RuntimeException("Error al leer el fichero " + FILE_PATH, e);
        }

        return courses;
    }

    private void writeAll(List<Course> courses) {
        List<String> lineas = new ArrayList<>();
        for (Course c : courses) {
            lineas.add(toLinea(c));
        }

        try {
            Files.write(Path.of(FILE_PATH), lineas, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException("Error al escribir en el fichero " + FILE_PATH, e);
        }
    }

    // Convierte una línea del fichero en un objeto Course.
    private Course parseLinea(String linea) {
        String[] campos = linea.split(DELIMITER);
        int id = Integer.parseInt(campos[0]);
        String title = campos[1];
        int hours = Integer.parseInt(campos[2]);
        String modality = campos[3];
        return new Course(id, title, hours, modality);
    }

    // Convierte un Course en la línea que se guardará en el fichero.
    private String toLinea(Course c) {
        return c.getId() + DELIMITER_WRITE + c.getTitle() + DELIMITER_WRITE
                + c.getHours() + DELIMITER_WRITE + c.getModality();
    }

    // Calcula el siguiente id disponible mirando el mayor id actual.
    // Al no tener un "nextId" en memoria (el programa puede reiniciarse),
    // lo recalculamos cada vez a partir de lo que hay en el fichero.
    private int calcularSiguienteId(List<Course> courses) {
        int maxId = 0;
        for (Course c : courses) {
            if (c.getId() > maxId) {
                maxId = c.getId();
            }
        }
        return maxId + 1;
    }
}