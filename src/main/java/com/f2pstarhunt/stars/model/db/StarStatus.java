package com.f2pstarhunt.stars.model.db;

public enum StarStatus {

    /** The star is currently live in the world. */
    ALIVE,

    /** The star has been mined out. */
    DEPLETED,

    /** The star has disintegrated into dust (at the end of the wave). */
    DISINTEGRATED,

    /**
     * The star has outlived its maximum lifetime.
     * This status can never be sent by clients of the star server.
     */
    EXPIRED;

}
