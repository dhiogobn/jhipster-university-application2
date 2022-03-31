package com.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Teacher.
 */
@Entity
@Table(name = "teacher")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Teacher implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "address")
    private String address;

    @Column(name = "phone")
    private String phone;

    @Column(name = "specialization")
    private String specialization;

    @JsonIgnoreProperties(value = { "students", "teacher" }, allowSetters = true)
    @OneToOne
    @JoinColumn(unique = true)
    private ClassRoom classRoom;

    @OneToMany(mappedBy = "teacher")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "courses", "teacher", "students" }, allowSetters = true)
    private Set<Discipline> disciplines = new HashSet<>();

    @ManyToMany
    @JoinTable(
        name = "rel_teacher__courses",
        joinColumns = @JoinColumn(name = "teacher_id"),
        inverseJoinColumns = @JoinColumn(name = "courses_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "students", "disciplines", "teachers" }, allowSetters = true)
    private Set<Course> courses = new HashSet<>();

    @ManyToMany(mappedBy = "teachers")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "disciplines", "teachers", "classRoom", "course" }, allowSetters = true)
    private Set<Student> students = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Teacher id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Teacher name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return this.address;
    }

    public Teacher address(String address) {
        this.setAddress(address);
        return this;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return this.phone;
    }

    public Teacher phone(String phone) {
        this.setPhone(phone);
        return this;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getSpecialization() {
        return this.specialization;
    }

    public Teacher specialization(String specialization) {
        this.setSpecialization(specialization);
        return this;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public ClassRoom getClassRoom() {
        return this.classRoom;
    }

    public void setClassRoom(ClassRoom classRoom) {
        this.classRoom = classRoom;
    }

    public Teacher classRoom(ClassRoom classRoom) {
        this.setClassRoom(classRoom);
        return this;
    }

    public Set<Discipline> getDisciplines() {
        return this.disciplines;
    }

    public void setDisciplines(Set<Discipline> disciplines) {
        if (this.disciplines != null) {
            this.disciplines.forEach(i -> i.setTeacher(null));
        }
        if (disciplines != null) {
            disciplines.forEach(i -> i.setTeacher(this));
        }
        this.disciplines = disciplines;
    }

    public Teacher disciplines(Set<Discipline> disciplines) {
        this.setDisciplines(disciplines);
        return this;
    }

    public Teacher addDisciplines(Discipline discipline) {
        this.disciplines.add(discipline);
        discipline.setTeacher(this);
        return this;
    }

    public Teacher removeDisciplines(Discipline discipline) {
        this.disciplines.remove(discipline);
        discipline.setTeacher(null);
        return this;
    }

    public Set<Course> getCourses() {
        return this.courses;
    }

    public void setCourses(Set<Course> courses) {
        this.courses = courses;
    }

    public Teacher courses(Set<Course> courses) {
        this.setCourses(courses);
        return this;
    }

    public Teacher addCourses(Course course) {
        this.courses.add(course);
        course.getTeachers().add(this);
        return this;
    }

    public Teacher removeCourses(Course course) {
        this.courses.remove(course);
        course.getTeachers().remove(this);
        return this;
    }

    public Set<Student> getStudents() {
        return this.students;
    }

    public void setStudents(Set<Student> students) {
        if (this.students != null) {
            this.students.forEach(i -> i.removeTeachers(this));
        }
        if (students != null) {
            students.forEach(i -> i.addTeachers(this));
        }
        this.students = students;
    }

    public Teacher students(Set<Student> students) {
        this.setStudents(students);
        return this;
    }

    public Teacher addStudents(Student student) {
        this.students.add(student);
        student.getTeachers().add(this);
        return this;
    }

    public Teacher removeStudents(Student student) {
        this.students.remove(student);
        student.getTeachers().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Teacher)) {
            return false;
        }
        return id != null && id.equals(((Teacher) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Teacher{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", address='" + getAddress() + "'" +
            ", phone='" + getPhone() + "'" +
            ", specialization='" + getSpecialization() + "'" +
            "}";
    }
}
