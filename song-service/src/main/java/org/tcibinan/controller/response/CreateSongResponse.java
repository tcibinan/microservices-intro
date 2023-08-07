package org.tcibinan.controller.response;

import io.micronaut.serde.annotation.Serdeable;

@Serdeable
public class CreateSongResponse {
    private final Long id;

    public CreateSongResponse(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
