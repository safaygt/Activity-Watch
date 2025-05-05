package com.smartict.activitywatch.repository;


import com.smartict.activitywatch.entity.ApplicationActivity;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ApplicationActivityRepository extends JpaRepository<ApplicationActivity, Integer> {

    Optional<ApplicationActivity> findByApplicationText(String applicationText);


}
