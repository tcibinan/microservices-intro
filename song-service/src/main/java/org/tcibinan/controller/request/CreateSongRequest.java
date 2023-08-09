package org.tcibinan.controller.request;

import io.micronaut.serde.annotation.Serdeable;

@Serdeable
public record CreateSongRequest(String name,
                                String artist,
                                String album,
                                String length,
                                String year,
                                Long resourceId) {
}
