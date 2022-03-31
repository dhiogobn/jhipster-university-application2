package com.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Course.
 */
@Entity
@Table(name = "course")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Course implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "duration")
    private String duration;

    @OneToMany(mappedBy = "course")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "disciplines", "teachers", "classRoom", "course" }, allowSetters = true)
    private Set<Student> students = new HashSet<>();

    @ManyToMany(mappedBy = "courses")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "courses", "teacher", "students" }, allowSetters = true)
    private Set<Discipline> disciplines = new HashSet<>();

    @ManyToMany(mappedBy = "courses")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "classRoom", "disciplines", "courses", "students" }, allowSetters = true)
    private Set<Teacher> teachers = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Course id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Course name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDuration() {
        return this.duration;
    }

    public Course duration(String duration) {
        this.setDuration(duration);
        return this;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public Set<Student> getStudents() {
        return this.students;
    }

    public void setStudents(Set<Student> students) {
        if (this.students != null) {
            this.students.forEach(i -> i.setCourse(null));
        }
        if (students != null) {
            students.forEach(i -> i.setCourse(this));
        }
        this.students = students;
    }

    public Course students(Set<Student> students) {
        this.setStudents(students);
        return this;
    }

    public Course addStudents(Student student) {
        this.students.add(student);
        student.setCourse(this);
        return this;
    }

    public Course removeStudents(Student student) {
        this.students.remove(student);
        student.setCourse(null);
        return this;
    }

    public Set<Discipline> getDisciplines() {
        return this.disciplines;
    }

    public void setDisciplines(Set<Discipline> disciplines) {
        if (this.disciplines != null) {
            this.disciplines.forEach(i -> i.removeCourses(this));
        }
        if (disciplines != null) {
            disciplines.forEach(i -> i.addCourses(this));
        }
        this.disciplines = disciplines;
    }

    public Course disciplines(Set<Discipline> disciplines) {
        this.setDisciplines(disciplines);
        return this;
    }

    public Course addDisciplines(Discipline discipline) {
        this.disciplines.add(discipline);
        discipline.getCourses().add(this);
        return this;
    }

    public Course removeDisciplines(Discipline discipline) {
        this.disciplines.remove(discipline);
        discipline.getCourses().remove(this);
        return this;
    }

    public Set<Teacher> getTeachers() {
        return this.teachers;
    }

    public void setTeachers(Set<Teacher> teachers) {
        if (this.teachers != null) {
            this.teachers.forEach(i -> i.removeCourses(this));
        }
        if (teachers != null) {
            teachers.forEach(i -> i.addCourses(this));
        }
        this.teachers = teachers;
    }

    public Course teachers(Set<Teacher> teachers) {
        this.setTeachers(teachers);
        return this;
    }

    public Course addTeachers(Teacher teacher) {
        this.teachers.add(teacher);
        teacher.getCourses().add(this);
        return this;
    }

    public Course removeTeachers(Teacher teacher) {
        this.teachers.remove(teacher);
        teacher.getCourses().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Course)) {
            return false;
        }
        return id != null && id.equals(((Course) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Course{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", duration='" + getDuration() + "'" +
            "}";
    }
}
