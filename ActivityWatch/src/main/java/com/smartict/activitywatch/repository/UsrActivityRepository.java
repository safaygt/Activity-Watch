package com.smartict.activitywatch.repository;

import com.smartict.activitywatch.entity.UsrActivity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;

public interface UsrActivityRepository  extends JpaRepository<UsrActivity, Integer> {

    void deleteAllByDateBefore(LocalDateTime date);

    UsrActivity findTopByOrderByDateDesc();
}
