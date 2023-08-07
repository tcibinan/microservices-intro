package org.tcibinan.controller.response;

import io.micronaut.serde.annotation.Serdeable;

@Serdeable
public record GetSongResponse(String name,
                              String artist,
                              String album,
                              Long length,
                              Long resourceId,
                              Long year) {
}
