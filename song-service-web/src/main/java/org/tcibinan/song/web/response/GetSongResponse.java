package org.tcibinan.song.web.response;

import io.micronaut.serde.annotation.Serdeable;

@Serdeable
public record GetSongResponse(String name,
                              String artist,
                              String album,
                              String length,
                              String year,
                              Long resourceId) {
}
