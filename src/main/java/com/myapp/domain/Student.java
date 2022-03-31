package com.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Student.
 */
@Entity
@Table(name = "student")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Student implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "cpf")
    private String cpf;

    @Column(name = "birth_date")
    private ZonedDateTime birthDate;

    @Column(name = "name")
    private String name;

    @ManyToMany
    @JoinTable(
        name = "rel_student__disciplines",
        joinColumns = @JoinColumn(name = "student_id"),
        inverseJoinColumns = @JoinColumn(name = "disciplines_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "courses", "teacher", "students" }, allowSetters = true)
    private Set<Discipline> disciplines = new HashSet<>();

    @ManyToMany
    @JoinTable(
        name = "rel_student__teachers",
        joinColumns = @JoinColumn(name = "student_id"),
        inverseJoinColumns = @JoinColumn(name = "teachers_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "classRoom", "disciplines", "courses", "students" }, allowSetters = true)
    private Set<Teacher> teachers = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "students", "teacher" }, allowSetters = true)
    private ClassRoom classRoom;

    @ManyToOne
    @JsonIgnoreProperties(value = { "students", "disciplines", "teachers" }, allowSetters = true)
    private Course course;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Student id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCpf() {
        return this.cpf;
    }

    public Student cpf(String cpf) {
        this.setCpf(cpf);
        return this;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public ZonedDateTime getBirthDate() {
        return this.birthDate;
    }

    public Student birthDate(ZonedDateTime birthDate) {
        this.setBirthDate(birthDate);
        return this;
    }

    public void setBirthDate(ZonedDateTime birthDate) {
        this.birthDate = birthDate;
    }

    public String getName() {
        return this.name;
    }

    public Student name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Discipline> getDisciplines() {
        return this.disciplines;
    }

    public void setDisciplines(Set<Discipline> disciplines) {
        this.disciplines = disciplines;
    }

    public Student disciplines(Set<Discipline> disciplines) {
        this.setDisciplines(disciplines);
        return this;
    }

    public Student addDisciplines(Discipline discipline) {
        this.disciplines.add(discipline);
        discipline.getStudents().add(this);
        return this;
    }

    public Student removeDisciplines(Discipline discipline) {
        this.disciplines.remove(discipline);
        discipline.getStudents().remove(this);
        return this;
    }

    public Set<Teacher> getTeachers() {
        return this.teachers;
    }

    public void setTeachers(Set<Teacher> teachers) {
        this.teachers = teachers;
    }

    public Student teachers(Set<Teacher> teachers) {
        this.setTeachers(teachers);
        return this;
    }

    public Student addTeachers(Teacher teacher) {
        this.teachers.add(teacher);
        teacher.getStudents().add(this);
        return this;
    }

    public Student removeTeachers(Teacher teacher) {
        this.teachers.remove(teacher);
        teacher.getStudents().remove(this);
        return this;
    }

    public ClassRoom getClassRoom() {
        return this.classRoom;
    }

    public void setClassRoom(ClassRoom classRoom) {
        this.classRoom = classRoom;
    }

    public Student classRoom(ClassRoom classRoom) {
        this.setClassRoom(classRoom);
        return this;
    }

    public Course getCourse() {
        return this.course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public Student course(Course course) {
        this.setCourse(course);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Student)) {
            return false;
        }
        return id != null && id.equals(((Student) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Student{" +
            "id=" + getId() +
            ", cpf='" + getCpf() + "'" +
            ", birthDate='" + getBirthDate() + "'" +
            ", name='" + getName() + "'" +
            "}";
    }
}
