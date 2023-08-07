package org.tcibinan.controller.response;

import io.micronaut.serde.annotation.Serdeable;

@Serdeable
public record GetResourceResponse(String data) {
}
