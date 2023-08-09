package org.tcibinan.song.web.request;

import io.micronaut.serde.annotation.Serdeable;

import java.util.List;

@Serdeable
public record DeleteSongRequest(List<Long> ids) {
}
