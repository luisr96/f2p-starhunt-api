package com.f2pstarhunt.stars.repository;

import java.time.Instant;
import java.util.List;

import com.f2pstarhunt.stars.model.Star;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface StarRepository extends JpaRepository<Star, Long> {

    @Query("""
           SELECT s FROM Star s
           WHERE s.detectedAt >= :timestamp
           AND s.disappearedAt = NULL
           """)
    public List<Star> findLiveStarsSince(Instant timestamp);
}
