package com.f2pstarhunt.stars.repository;

import com.f2pstarhunt.stars.model.Star;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StarRepository extends JpaRepository<Star, Long> {
}
