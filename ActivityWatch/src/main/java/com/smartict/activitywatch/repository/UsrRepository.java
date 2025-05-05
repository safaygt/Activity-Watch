package com.smartict.activitywatch.repository;

import com.smartict.activitywatch.entity.Usr;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsrRepository extends JpaRepository<Usr, Integer> {

    Optional<Usr> findByUsername(String username);
}
