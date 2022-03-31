package com.myapp.repository;

import com.myapp.domain.Teacher;
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
public class TeacherRepositoryWithBagRelationshipsImpl implements TeacherRepositoryWithBagRelationships {

    @Autowired
    private EntityManager entityManager;

    @Override
    public Optional<Teacher> fetchBagRelationships(Optional<Teacher> teacher) {
        return teacher.map(this::fetchCourses);
    }

    @Override
    public Page<Teacher> fetchBagRelationships(Page<Teacher> teachers) {
        return new PageImpl<>(fetchBagRelationships(teachers.getContent()), teachers.getPageable(), teachers.getTotalElements());
    }

    @Override
    public List<Teacher> fetchBagRelationships(List<Teacher> teachers) {
        return Optional.of(teachers).map(this::fetchCourses).get();
    }

    Teacher fetchCourses(Teacher result) {
        return entityManager
            .createQuery("select teacher from Teacher teacher left join fetch teacher.courses where teacher is :teacher", Teacher.class)
            .setParameter("teacher", result)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getSingleResult();
    }

    List<Teacher> fetchCourses(List<Teacher> teachers) {
        return entityManager
            .createQuery(
                "select distinct teacher from Teacher teacher left join fetch teacher.courses where teacher in :teachers",
                Teacher.class
            )
            .setParameter("teachers", teachers)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getResultList();
    }
}
