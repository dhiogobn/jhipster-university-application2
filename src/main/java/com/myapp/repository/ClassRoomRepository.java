package com.myapp.repository;

import com.myapp.domain.ClassRoom;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the ClassRoom entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ClassRoomRepository extends JpaRepository<ClassRoom, Long> {}
