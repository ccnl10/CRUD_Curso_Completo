package model;

public class Course {
    // Atributos privados: nadie fuera de la clase accede directamente a ellos.
    // Esto es ENCAPSULACIÓN.
    private int id;
    private String title;
    private int hours;
    private String modality; // Se espera "ONLINE" o "PRESENTIAL"

    // Constructor CON id: se usa cuando reconstruimos un curso que ya existe
    // (por ejemplo, al leerlo desde el repositorio para actualizarlo).
    public Course(int id, String title, int hours, String modality){
        this.id = id;
        this.title = title;
        this.hours = hours;
        this.modality = modality;
    }

    // Constructor SIN id: se usa al crear un curso nuevo, antes de que el
    // repositorio le asigne un identificador autoincremental.
    public Course(String title, int hours, String modality){
        this(0,title, hours, modality);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getHours() {
        return hours;
    }

    public void setHours(int hours) {
        this.hours = hours;
    }

    public String getModality() {
        return modality;
    }

    public void setModality(String modality) {
        this.modality = modality;
    }
    // toString() legible: fundamental para poder depurar y listar cursos
    // por consola sin tener que escribir getters a mano cada vez.
    @Override
    public String toString() {
        return String.format("Course[id=%d, title='%s', hours=%d, modality=%s]",
                id, title, hours, modality);
    }
}

