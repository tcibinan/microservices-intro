package org.tcibinan.resource.controller.request;

import io.micronaut.serde.annotation.Serdeable;

@Serdeable
public record CreateResourceRequest(byte[] data) {
}
