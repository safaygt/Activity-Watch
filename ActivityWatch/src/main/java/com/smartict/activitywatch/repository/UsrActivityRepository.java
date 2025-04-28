package com.smartict.activitywatch.repository;

import com.smartict.activitywatch.entity.UsrActivity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsrActivityRepository  extends JpaRepository<UsrActivity, Integer> {
}
