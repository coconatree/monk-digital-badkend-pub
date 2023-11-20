package com.monk.repository;

import com.monk.model.entity.Letter;
import com.monk.model.projection.LetterProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LetterRepository extends JpaRepository<Letter, Long> {
    @Query(nativeQuery = true, value = "select id from letter")
    List<LetterProjection> findAllProjections();
}
