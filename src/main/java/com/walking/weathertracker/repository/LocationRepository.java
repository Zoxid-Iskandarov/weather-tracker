package com.walking.weathertracker.repository;

import com.walking.weathertracker.domain.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LocationRepository extends JpaRepository<Location, Long> {
    List<Location> findByUserIdOrderByCreatedAt(Long userId);

    boolean existsByUserIdAndName(Long userId, String name);
}
