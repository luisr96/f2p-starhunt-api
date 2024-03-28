package com.f2pstarhunt.stars.model.api;

import java.time.Instant;
import java.util.Objects;

import com.f2pstarhunt.stars.model.shared.StarLocation;
import com.f2pstarhunt.stars.model.shared.StarTier;

/**
 * Star data transfer object.
 * @param id
 * @param world
 * @param location
 * @param tier
 * @param discoveredBy
 * @param detectedAt
 */
public record StarDto(
        Long id,
        int world,
        StarLocation location,
        StarTier tier,
        String discoveredBy,
        Instant detectedAt) {

    public StarDto {
        Objects.requireNonNull(location);
    }
}
