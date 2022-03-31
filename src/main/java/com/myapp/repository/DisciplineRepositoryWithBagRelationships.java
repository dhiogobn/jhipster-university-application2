package com.myapp.repository;

import com.myapp.domain.Discipline;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;

public interface DisciplineRepositoryWithBagRelationships {
    Optional<Discipline> fetchBagRelationships(Optional<Discipline> discipline);

    List<Discipline> fetchBagRelationships(List<Discipline> disciplines);

    Page<Discipline> fetchBagRelationships(Page<Discipline> disciplines);
}
