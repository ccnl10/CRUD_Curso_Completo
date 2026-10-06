import model.Course;
import repository.CourseRepository;
import repository.impl.FileCourseRepository;
import repository.impl.InMemoryCourseRepository;
import repository.impl.SimulatedFileCourseRepository;
import repository.impl.SimulatedMySQLCourseRepository;
import service.CourseService;

import java.util.List;

public class Main {
    public static void main(String[] args) {


        // El repositorio se declara como la INTERFAZ, aunque el objeto real
        // sea un InMemoryCourseRepository. Esto es POLIMORFISMO aplicado
        // a la propia arquitectura del programa.
        CourseRepository repository = new InMemoryCourseRepository();
        //CourseRepository repository = new SimulatedMySQLCourseRepository();
        //CourseRepository repository = new FileCourseRepository();
        //CourseRepository repository = new SimulatedFileCourseRepository();
        CourseService service = new CourseService(repository);

        // 1) Crear 3 cursos
        Course c1 = service.register("Java Básico", 40, "online");
        Course c2 = service.register("SQL Avanzado", 20, "presential");
        Course c3 = service.register("Desarrollo Web", 60, "ONLINE");

        // 2) Listar todos
        System.out.println("--- Listado inicial ---");
        for (Course c : service.listAll()) {
            System.out.println(c);
        }

        // 3) Buscar por id (uno existente y uno inexistente)
        System.out.println("\n--- Búsqueda por id ---");
        Course encontrado = service.getById(c2.getId());
        System.out.println("Encontrado: " + encontrado);
        Course noEncontrado = service.getById(999);
        System.out.println("No encontrado: " + noEncontrado);

        // 4) Actualizar uno (cambiar horas y modalidad)
        System.out.println("\n--- Actualización ---");
        c1.setHours(50);
        c1.setModality("PRESENTIAL");
        boolean actualizado = service.update(c1);
        System.out.println("¿Actualizado? " + actualizado);
        System.out.println(service.getById(c1.getId()));

        // 5) Eliminar uno
        System.out.println("\n--- Eliminación ---");
        boolean eliminado = service.delete(c3.getId());
        System.out.println("¿Eliminado? " + eliminado);

        // 6) Listado final y count()
        System.out.println("\n--- Listado final ---");
        List<Course> finales = service.listAll();
        for (Course c : finales) {
            System.out.println(c);
        }
        System.out.println("Total de cursos: " + service.count());
    }

}
