package repository;
import model.Course;
import java.util.List;

// CONTRATO: cualquier clase que quiera actuar como "almacén de cursos"
// debe implementar estos seis métodos. Es la base de la ABSTRACCIÓN:
// el resto del programa hablará con esta interfaz, nunca con los
// detalles de cómo se guardan los datos por dentro.



public interface CourseRepository {
    Course create(Course c);

    Course findById(int id); //debe devolver NULL si no existe

    List<Course> findAll();

    boolean update(Course c);

    boolean deleteById(int id);

    int count();
}

