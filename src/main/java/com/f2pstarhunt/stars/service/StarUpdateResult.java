package com.f2pstarhunt.stars.service;

public enum StarUpdateResult {

    /** The star was updated successfully. */
    UPDATED,

    /** No update was necessary, the server already knew about the new tier. */
    ALREADY_UPDATED,

    /** No active star was found. */
    NOT_FOUND,

}
