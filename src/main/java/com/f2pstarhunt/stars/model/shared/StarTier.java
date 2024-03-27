package com.f2pstarhunt.stars.model.shared;

public enum StarTier {

    SIZE_1,
    SIZE_2,
    SIZE_3,
    SIZE_4,
    SIZE_5,
    SIZE_6,
    SIZE_7,
    SIZE_8,
    SIZE_9;

    private static final StarTier[] VALUES = values();

    public static StarTier bySize(int size) {
        return VALUES[size - 1];
    }

    public boolean isGreaterThan(StarTier otherTier) {
        return this.ordinal() > otherTier.ordinal();
    }
}
