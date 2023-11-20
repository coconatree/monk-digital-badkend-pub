package com.monk.repository;

import com.monk.model.entity.HttpProjectNotification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HttpProjectNotificationRepository extends JpaRepository<HttpProjectNotification, Long> {}
