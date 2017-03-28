package app.rmi.server.entity;

/**
 * Created by Roman on 25.02.2017.
 */
public class Student extends Entity {
    private int studentId;
    private String surname;
    private int group;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Student student = (Student) o;

        if (studentId != student.studentId) return false;
        if (group != student.group) return false;
        return surname != null ? surname.equals(student.surname) : student.surname == null;

    }

    @Override
    public int hashCode() {
        int result = studentId;
        result = 31 * result + (surname != null ? surname.hashCode() : 0);
        result = 31 * result + group;
        return result;
    }

    public Student() {
    }

    public Student(String surname, int group) {
        this.surname = surname;
        this.group = group;
    }

    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public int getGroup() {
        return group;
    }

    public void setGroup(int group) {
        this.group = group;
    }

    @Override
    public String toString() {
        return "Student{" +
                "surname='" + surname + '\'' +
                ", group=" + group +
                '}';
    }
}
