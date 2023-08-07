package org.tcibinan.controller.response;

import io.micronaut.serde.annotation.Serdeable;

import java.util.List;

@Serdeable
public record DeleteSongResponse(List<Long> ids) {
}
