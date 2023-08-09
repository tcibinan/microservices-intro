package org.tcibinan.resource.controller.response;

import io.micronaut.serde.annotation.Serdeable;

@Serdeable
public record CreateResourceResponse(Long id) {
}
