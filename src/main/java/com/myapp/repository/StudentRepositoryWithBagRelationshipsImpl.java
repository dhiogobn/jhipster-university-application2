package com.myapp.repository;

import com.myapp.domain.Student;
import java.util.List;
import java.util.Optional;
import javax.persistence.EntityManager;
import org.hibernate.annotations.QueryHints;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

/**
 * Utility repository to load bag relationships based on https://vladmihalcea.com/hibernate-multiplebagfetchexception/
 */
public class StudentRepositoryWithBagRelationshipsImpl implements StudentRepositoryWithBagRelationships {

    @Autowired
    private EntityManager entityManager;

    @Override
    public Optional<Student> fetchBagRelationships(Optional<Student> student) {
        return student.map(this::fetchDisciplines).map(this::fetchTeachers);
    }

    @Override
    public Page<Student> fetchBagRelationships(Page<Student> students) {
        return new PageImpl<>(fetchBagRelationships(students.getContent()), students.getPageable(), students.getTotalElements());
    }

    @Override
    public List<Student> fetchBagRelationships(List<Student> students) {
        return Optional.of(students).map(this::fetchDisciplines).map(this::fetchTeachers).get();
    }

    Student fetchDisciplines(Student result) {
        return entityManager
            .createQuery("select student from Student student left join fetch student.disciplines where student is :student", Student.class)
            .setParameter("student", result)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getSingleResult();
    }

    List<Student> fetchDisciplines(List<Student> students) {
        return entityManager
            .createQuery(
                "select distinct student from Student student left join fetch student.disciplines where student in :students",
                Student.class
            )
            .setParameter("students", students)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getResultList();
    }

    Student fetchTeachers(Student result) {
        return entityManager
            .createQuery("select student from Student student left join fetch student.teachers where student is :student", Student.class)
            .setParameter("student", result)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getSingleResult();
    }

    List<Student> fetchTeachers(List<Student> students) {
        return entityManager
            .createQuery(
                "select distinct student from Student student left join fetch student.teachers where student in :students",
                Student.class
            )
            .setParameter("students", students)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getResultList();
    }
}
