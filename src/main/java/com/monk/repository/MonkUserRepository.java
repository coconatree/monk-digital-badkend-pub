package com.monk.repository;

import com.monk.model.entity.MonkUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MonkUserRepository extends JpaRepository<MonkUser, Long> {
    Optional<MonkUser> findByEmail(String email);
    boolean existsByEmail(String email);
}
