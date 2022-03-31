package com.myapp.repository;

import com.myapp.domain.Discipline;
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
public class DisciplineRepositoryWithBagRelationshipsImpl implements DisciplineRepositoryWithBagRelationships {

    @Autowired
    private EntityManager entityManager;

    @Override
    public Optional<Discipline> fetchBagRelationships(Optional<Discipline> discipline) {
        return discipline.map(this::fetchCourses);
    }

    @Override
    public Page<Discipline> fetchBagRelationships(Page<Discipline> disciplines) {
        return new PageImpl<>(fetchBagRelationships(disciplines.getContent()), disciplines.getPageable(), disciplines.getTotalElements());
    }

    @Override
    public List<Discipline> fetchBagRelationships(List<Discipline> disciplines) {
        return Optional.of(disciplines).map(this::fetchCourses).get();
    }

    Discipline fetchCourses(Discipline result) {
        return entityManager
            .createQuery(
                "select discipline from Discipline discipline left join fetch discipline.courses where discipline is :discipline",
                Discipline.class
            )
            .setParameter("discipline", result)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getSingleResult();
    }

    List<Discipline> fetchCourses(List<Discipline> disciplines) {
        return entityManager
            .createQuery(
                "select distinct discipline from Discipline discipline left join fetch discipline.courses where discipline in :disciplines",
                Discipline.class
            )
            .setParameter("disciplines", disciplines)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getResultList();
    }
}
