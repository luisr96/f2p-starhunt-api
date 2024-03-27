package com.f2pstarhunt.stars.service;

public enum StarDeleteResult {

    /** The star was marked dead on the server. */
    DELETED,

    /** This star was already marked dead. */
    ALREADY_DELETED,

    /** No visible star was found. */
    NOT_FOUND;

}
