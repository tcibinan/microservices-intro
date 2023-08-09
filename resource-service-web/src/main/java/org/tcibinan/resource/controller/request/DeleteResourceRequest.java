package org.tcibinan.resource.controller.request;

import io.micronaut.serde.annotation.Serdeable;

import java.util.List;

@Serdeable
public record DeleteResourceRequest(List<Long> ids) {
}
