package org.tcibinan.song.web.response;

import io.micronaut.serde.annotation.Serdeable;

@Serdeable
public record CreateSongResponse(Long id) {
}
