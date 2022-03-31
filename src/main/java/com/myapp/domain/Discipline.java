package com.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Discipline.
 */
@Entity
@Table(name = "discipline")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Discipline implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "credits")
    private String credits;

    @Column(name = "phone")
    private String phone;

    @ManyToMany
    @JoinTable(
        name = "rel_discipline__courses",
        joinColumns = @JoinColumn(name = "discipline_id"),
        inverseJoinColumns = @JoinColumn(name = "courses_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "students", "disciplines", "teachers" }, allowSetters = true)
    private Set<Course> courses = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "classRoom", "disciplines", "courses", "students" }, allowSetters = true)
    private Teacher teacher;

    @ManyToMany(mappedBy = "disciplines")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "disciplines", "teachers", "classRoom", "course" }, allowSetters = true)
    private Set<Student> students = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Discipline id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Discipline name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCredits() {
        return this.credits;
    }

    public Discipline credits(String credits) {
        this.setCredits(credits);
        return this;
    }

    public void setCredits(String credits) {
        this.credits = credits;
    }

    public String getPhone() {
        return this.phone;
    }

    public Discipline phone(String phone) {
        this.setPhone(phone);
        return this;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Set<Course> getCourses() {
        return this.courses;
    }

    public void setCourses(Set<Course> courses) {
        this.courses = courses;
    }

    public Discipline courses(Set<Course> courses) {
        this.setCourses(courses);
        return this;
    }

    public Discipline addCourses(Course course) {
        this.courses.add(course);
        course.getDisciplines().add(this);
        return this;
    }

    public Discipline removeCourses(Course course) {
        this.courses.remove(course);
        course.getDisciplines().remove(this);
        return this;
    }

    public Teacher getTeacher() {
        return this.teacher;
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }

    public Discipline teacher(Teacher teacher) {
        this.setTeacher(teacher);
        return this;
    }

    public Set<Student> getStudents() {
        return this.students;
    }

    public void setStudents(Set<Student> students) {
        if (this.students != null) {
            this.students.forEach(i -> i.removeDisciplines(this));
        }
        if (students != null) {
            students.forEach(i -> i.addDisciplines(this));
        }
        this.students = students;
    }

    public Discipline students(Set<Student> students) {
        this.setStudents(students);
        return this;
    }

    public Discipline addStudents(Student student) {
        this.students.add(student);
        student.getDisciplines().add(this);
        return this;
    }

    public Discipline removeStudents(Student student) {
        this.students.remove(student);
        student.getDisciplines().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Discipline)) {
            return false;
        }
        return id != null && id.equals(((Discipline) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Discipline{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", credits='" + getCredits() + "'" +
            ", phone='" + getPhone() + "'" +
            "}";
    }
}
