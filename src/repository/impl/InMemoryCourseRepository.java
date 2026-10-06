package repository.impl;

import model.Course;
import repository.CourseRepository;

import java.util.ArrayList;
import java.util.List;

public class InMemoryCourseRepository implements CourseRepository {

    // El almacén real: una lista en memoria.
    private List<Course> courses = new ArrayList<>();

    //Contador para generar ids autoincrementales, empezando en 1.
    private int nextId = 1;

    @Override
    public Course create(Course c) {
        c.setId(nextId);
        nextId++;
        courses.add(c);
        return c;
    }

    @Override
    public Course findById(int id) {
        for(Course c: courses){
            if(c.getId() == id){
                return c;
            }
        }
        return null; //La interfaz exige devolver null si no existe
    }

    @Override
    public List<Course> findAll() {
        //Devolvemos una COPIA de la lista, no la lista original.
        //Así, quien reciba este lista no puede añadir o quitar cursos
        //"por la puerta de atras" sin pasar por create/deleteById().
        return new ArrayList<>(courses);

    }

    @Override
    public boolean update(Course c) {
        Course existing = findById(c.getId());
        if(existing == null){
            return false; //no podemos actualizar lo que no existe
        }
        existing.setTitle(c.getTitle());
        existing.setHours(c.getHours());
        existing.setModality(c.getModality());
        return true;
    }

    @Override
    public boolean deleteById(int id) {
        Course existing = findById(id);
        if(existing == null){
            return false;
        }
        courses.remove(existing);
        return true;
    }

    @Override
    public int count() {
        return courses.size();
    }
}
