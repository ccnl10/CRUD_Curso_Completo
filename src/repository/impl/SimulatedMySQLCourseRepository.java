package repository.impl;

import model.Course;
import repository.CourseRepository;

import java.util.ArrayList;
import java.util.List;

/**
 * Implementación "simulada" de CourseRepository.
 *
 * No se conecta a ninguna base de datos real: por dentro sigue usando
 * una lista en memoria (igual que InMemoryCourseRepository), pero antes
 * de cada operación imprime por consola la sentencia SQL que SE HARÍA
 * si hubiera una base de datos MySQL detrás.
 *
 * Sirve para practicar la estructura de un repositorio JDBC (qué SQL
 * corresponde a cada método) sin necesidad de instalar MySQL.
 */
public class SimulatedMySQLCourseRepository implements CourseRepository {

    private List<Course> courses = new ArrayList<>();
    private int nextId = 1;

    @Override
    public Course create(Course c) {
        c.setId(nextId);
        nextId++;
        courses.add(c);

        System.out.println("[SQL simulado] INSERT INTO courses (title, hours, modality) VALUES ('"
                + c.getTitle() + "', " + c.getHours() + ", '" + c.getModality() + "');");
        System.out.println("[MySQL simulado] Fila insertada con id autogenerado = " + c.getId());

        return c;
    }

    @Override
    public Course findById(int id) {
        System.out.println("[SQL simulado] SELECT * FROM courses WHERE id = " + id + ";");

        for (Course c : courses) {
            if (c.getId() == id) {
                System.out.println("[MySQL simulado] 1 fila encontrada.");
                return c;
            }
        }
        System.out.println("[MySQL simulado] 0 filas encontradas.");
        return null;
    }

    @Override
    public List<Course> findAll() {
        System.out.println("[SQL simulado] SELECT * FROM courses;");
        System.out.println("[MySQL simulado] " + courses.size() + " filas devueltas.");
        return new ArrayList<>(courses);
    }

    @Override
    public boolean update(Course c) {
        System.out.println("[SQL simulado] UPDATE courses SET title = '" + c.getTitle()
                + "', hours = " + c.getHours() + ", modality = '" + c.getModality()
                + "' WHERE id = " + c.getId() + ";");

        Course existing = findByIdSinLog(c.getId());
        if (existing == null) {
            System.out.println("[MySQL simulado] 0 filas afectadas (no existe ese id).");
            return false;
        }
        existing.setTitle(c.getTitle());
        existing.setHours(c.getHours());
        existing.setModality(c.getModality());

        System.out.println("[MySQL simulado] 1 fila afectada.");
        return true;
    }

    @Override
    public boolean deleteById(int id) {
        System.out.println("[SQL simulado] DELETE FROM courses WHERE id = " + id + ";");

        Course existing = findByIdSinLog(id);
        if (existing == null) {
            System.out.println("[MySQL simulado] 0 filas afectadas (no existe ese id).");
            return false;
        }
        courses.remove(existing);
        System.out.println("[MySQL simulado] 1 fila afectada.");
        return true;
    }

    @Override
    public int count() {
        System.out.println("[SQL simulado] SELECT COUNT(*) FROM courses;");
        System.out.println("[MySQL simulado] Resultado = " + courses.size());
        return courses.size();
    }

    // Versión interna de findById que NO imprime el SQL, para poder
    // reutilizarla dentro de update/deleteById sin duplicar el mensaje.
    private Course findByIdSinLog(int id) {
        for (Course c : courses) {
            if (c.getId() == id) {
                return c;
            }
        }
        return null;
    }
}