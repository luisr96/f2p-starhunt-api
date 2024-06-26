package com.f2pstarhunt.stars.repository;

import java.util.List;

import com.f2pstarhunt.stars.model.db.Star;
import com.f2pstarhunt.stars.model.shared.StarLocation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface StarRepository extends JpaRepository<Star, Long> {

    // Note: in SQL we can compare boolean values using >=.
    // When we query for invisible stars, the visible stars will also be returned.
    @Query("""
           SELECT s FROM Star s
           WHERE s.status = com.f2pstarhunt.stars.model.db.StarStatus.ALIVE AND s.visible >= :visible
           """)
    List<Star> findAliveStarsWithVisibilityAtLeast(@Param("visible") boolean visible);

    @Query("""
           SELECT s FROM Star s
           WHERE s.id = :id AND s.status = com.f2pstarhunt.stars.model.db.StarStatus.ALIVE
           """)
    Star findAliveStar(@Param("id") long id);

    @Query("""
           SELECT s FROM Star s
           WHERE s.world = :world AND s.location = :location
           AND s.status = com.f2pstarhunt.stars.model.db.StarStatus.ALIVE
           """)
    Star findAliveStar(
            @Param("world") int world,
            @Param("location") StarLocation location);

    @Query("""
           SELECT s FROM Star s
           WHERE s.status = com.f2pstarhunt.stars.model.db.StarStatus.ALIVE AND s.visible
           AND s.id = :id
           """)
    Star findActiveById(@Param("id") long id);

    @Query("""
           SELECT s FROM Star s
           WHERE s.status = com.f2pstarhunt.stars.model.db.StarStatus.ALIVE AND s.visible
           AND s.world = :world AND s.location = :location
           """)
    Star findActiveByLocation(
            @Param("world") int world,
            @Param("location") StarLocation location);

}
