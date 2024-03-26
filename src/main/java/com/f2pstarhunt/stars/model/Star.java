package com.f2pstarhunt.stars.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

import org.hibernate.annotations.JdbcType;
import org.hibernate.dialect.PostgreSQLEnumJdbcType;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
public class Star {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
}
