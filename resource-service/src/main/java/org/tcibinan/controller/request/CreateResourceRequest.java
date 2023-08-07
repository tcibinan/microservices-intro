package org.tcibinan.controller.request;

import io.micronaut.serde.annotation.Serdeable;

@Serdeable
public record CreateResourceRequest(String data) {
}
