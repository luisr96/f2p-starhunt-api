DO $$ BEGIN
    CREATE TYPE star_tier AS ENUM (
        'SIZE_1',
        'SIZE_2',
        'SIZE_3',
        'SIZE_4',
        'SIZE_5',
        'SIZE_6',
        'SIZE_7',
        'SIZE_8',
        'SIZE_9'
    );
EXCEPTION
    WHEN duplicate_object THEN null;
END $$;

DO $$ BEGIN
    CREATE TYPE star_location AS ENUM (
        'WILDERNESS_RUNITE_MINE',
        'WILDERNESS_CENTRE_MINE',
        'WILDERNESS_SOUTH_WEST_MINE',
        'WILDERNESS_SOUTH_MINE',

        'DWARVEN_MINE',
        'MINING_GUILD',
        'CRAFTING_GUILD',
        'RIMMINGTON_MINE',

        'DRAYNOR_VILLAGE_BANK',
        'LUMBRIDGE_SWAMP_SOUTH_WEST_MINE',
        'LUMBRIDGE_SWAMP_SOUTH_EAST_MINE',

        'VARROCK_SOUTH_WEST_MINE',
        'VARROCK_SOUTH_EAST_MINE',
        'VARROCK_AUBURY',

        'AL_KHARID_MINE',
        'AL_KHARID_BANK',
        'PVP_ARENA',

        'CRANDOR_NORTH_MINE',
        'CRANDOR_SOUTH_MINE',
        'CORSAIR_COVE_BANK',
        'CORSAIR_COVE_RESOURCE_AREA'
    );
EXCEPTION
    WHEN duplicate_object THEN null;
END $$;

DO $$ BEGIN
    CREATE TYPE start_status AS ENUM (
        'ALIVE',
        'DEPLETED',
        'DISINTEGRATED',
        'EXPIRED'
    );
EXCEPTION
    WHEN duplicate_object THEN null;
END $$;

CREATE TABLE IF NOT EXISTS star (
    id SERIAL PRIMARY KEY,
    world SMALLINT NOT NULL,
    location star_location NOT NULL,
    tier start_tier,
    discovered_by VARCHAR(32) DEFAULT NULL,
    detected_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    disappeared_at TIMESTAMP WITH TIME ZONE DEFAULT NULL,
    visible BOOLEAN DEFAULT FALSE,
    status star_status NOT NULL DEFAULT 'ALIVE',

    CONSTRAINT tier_iff_alive CHECK ((tier == NULL) == (status <> 'ALIVE'))
);

CREATE INDEX IF NOT EXISTS index_star_discovered_at ON star USING BTREE (detected_at);
CREATE INDEX IF NOT EXISTS index_star_disappeared_at ON star USING BTREE (disappeared_at);
CREATE INDEX IF NOT EXISTS index_star_world ON star USING HASH (world);
CREATE INDEX IF NOT EXISTS index_star_location ON star USING HASH (location);
CREATE INDEX IF NOT EXISTS index_star_active ON star USING BTREE (status, visible);
