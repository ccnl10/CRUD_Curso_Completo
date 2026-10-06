package service;

import model.Course;
import repository.CourseRepository;

import java.util.List;

public class CourseService {
    // Depende de la INTERFAZ, no de la implementación concreta.
    // Esto es lo que permite cambiar mañana a una base de datos real
    // sin tocar ni una línea de CourseService.

    private CourseRepository repository;

    public CourseService(CourseRepository repository){
        this.repository = repository;
    }

    public Course register (String title, int hours, String modality){
        // --- Validaciones de negocio: viven aquí, no en el modelo ni en el repositorio ---
        if(title == null || title.trim().isEmpty()){
            throw new IllegalArgumentException("El titulo no puede estar vacio");
        }
        if(hours <=0){
            throw new IllegalArgumentException("Las horas deben ser mayores que 0");
        }
        String normalizedModality = normalizeModality(modality);

        Course course = new Course(title, hours, normalizedModality);
        return repository.create(course);
    }
    public Course getById(int id) {
        Course course = repository.findById(id);
        if (course == null) {
            System.out.println("Aviso: no existe ningún curso con id " + id);
        }
        return course;
    }

    public List<Course> listAll() {
        return repository.findAll();
    }

    public boolean update(Course c) {
        return repository.update(c);
    }

    public boolean delete(int id) {
        return repository.deleteById(id);
    }

    public int count() {
        return repository.count();
    }

    // Método privado de apoyo: normaliza y valida la modalidad.
    // No forma parte del contrato público del servicio.
    private String normalizeModality(String modality) {
        if (modality == null) {
            throw new IllegalArgumentException("La modalidad no puede ser nula");
        }
        String normalized = modality.trim().toUpperCase();
        if (!normalized.equals("ONLINE") && !normalized.equals("PRESENTIAL")) {
            throw new IllegalArgumentException(
                    "La modalidad debe ser ONLINE o PRESENTIAL (recibido: " + modality + ")");
        }
        return normalized;
    }
}
