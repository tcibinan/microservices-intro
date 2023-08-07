package org.tcibinan.controller.response;

import io.micronaut.serde.annotation.Serdeable;

import java.util.List;

@Serdeable
public record DeleteResourceResponse(List<Long> ids) {
}
