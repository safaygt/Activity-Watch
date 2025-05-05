package com.smartict.activitywatch.repository;

import com.smartict.activitywatch.entity.WindowActivity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface WindowActivityRepository extends JpaRepository<WindowActivity,Integer> {
    Optional<WindowActivity> findByWindowTitle(String windowTitle);

}
