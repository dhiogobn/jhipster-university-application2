package com.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A ClassRoom.
 */
@Entity
@Table(name = "class_room")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ClassRoom implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "number")
    private String number;

    @Column(name = "capacity")
    private String capacity;

    @OneToMany(mappedBy = "classRoom")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "disciplines", "teachers", "classRoom", "course" }, allowSetters = true)
    private Set<Student> students = new HashSet<>();

    @JsonIgnoreProperties(value = { "classRoom", "disciplines", "courses", "students" }, allowSetters = true)
    @OneToOne(mappedBy = "classRoom")
    private Teacher teacher;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ClassRoom id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumber() {
        return this.number;
    }

    public ClassRoom number(String number) {
        this.setNumber(number);
        return this;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getCapacity() {
        return this.capacity;
    }

    public ClassRoom capacity(String capacity) {
        this.setCapacity(capacity);
        return this;
    }

    public void setCapacity(String capacity) {
        this.capacity = capacity;
    }

    public Set<Student> getStudents() {
        return this.students;
    }

    public void setStudents(Set<Student> students) {
        if (this.students != null) {
            this.students.forEach(i -> i.setClassRoom(null));
        }
        if (students != null) {
            students.forEach(i -> i.setClassRoom(this));
        }
        this.students = students;
    }

    public ClassRoom students(Set<Student> students) {
        this.setStudents(students);
        return this;
    }

    public ClassRoom addStudents(Student student) {
        this.students.add(student);
        student.setClassRoom(this);
        return this;
    }

    public ClassRoom removeStudents(Student student) {
        this.students.remove(student);
        student.setClassRoom(null);
        return this;
    }

    public Teacher getTeacher() {
        return this.teacher;
    }

    public void setTeacher(Teacher teacher) {
        if (this.teacher != null) {
            this.teacher.setClassRoom(null);
        }
        if (teacher != null) {
            teacher.setClassRoom(this);
        }
        this.teacher = teacher;
    }

    public ClassRoom teacher(Teacher teacher) {
        this.setTeacher(teacher);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ClassRoom)) {
            return false;
        }
        return id != null && id.equals(((ClassRoom) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ClassRoom{" +
            "id=" + getId() +
            ", number='" + getNumber() + "'" +
            ", capacity='" + getCapacity() + "'" +
            "}";
    }
}
