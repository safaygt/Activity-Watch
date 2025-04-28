package com.smartict.activitywatch.repository;

import com.smartict.activitywatch.entity.Activity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ActivityRepository extends JpaRepository<Activity, Integer> {
}
