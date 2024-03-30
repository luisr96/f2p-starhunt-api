package com.f2pstarhunt.stars.model.db;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

import com.f2pstarhunt.stars.model.shared.StarLocation;
import com.f2pstarhunt.stars.model.shared.StarTier;

import org.hibernate.annotations.JdbcType;
import org.hibernate.dialect.PostgreSQLEnumJdbcType;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
public class Star {

    /** Primary key of the star. Unique, 0 or NULL for un-persisted stars. */
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    /** World of the star. */
    private int world;

    /** Location of the star. */
    @JdbcType(PostgreSQLEnumJdbcType.class)
    private StarLocation location;

    /** Size of the star. */
    @JdbcType(PostgreSQLEnumJdbcType.class)
    private StarTier tier;

    /** Username of the scout. */
    private String discoveredBy;

    /** Instant at which the star was detected. */
    private Instant detectedAt = Instant.now();

    /** Whether this star is publicly accessible. */
    private boolean visible;

    /** Instant at which the star disappeared. */
    private Instant disappearedAt;

    /** Status of the star. */
    private StarStatus status;

    /** Whether the star is 'active' for being mined. */
    public boolean isActive() {
        return visible && status == StarStatus.ALIVE;
    }
}
