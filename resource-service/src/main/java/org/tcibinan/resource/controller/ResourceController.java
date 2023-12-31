package org.tcibinan.resource.controller;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.annotation.Error;
import io.micronaut.http.annotation.*;
import io.micronaut.http.hateoas.JsonError;
import io.micronaut.http.hateoas.Link;
import org.tcibinan.resource.entity.Resource;
import org.tcibinan.resource.exception.NotFoundException;
import org.tcibinan.resource.exception.ValidationException;
import org.tcibinan.resource.controller.request.CreateResourceRequest;
import org.tcibinan.resource.controller.request.DeleteResourceRequest;
import org.tcibinan.resource.controller.response.CreateResourceResponse;
import org.tcibinan.resource.controller.response.DeleteResourceResponse;
import org.tcibinan.resource.service.ResourceService;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Controller("/resources")
public class ResourceController {
    
    private final ResourceService service;

    public ResourceController(ResourceService service) {
        this.service = service;
    }

    @Post
    @Consumes("audio/mpeg")
    public CreateResourceResponse create(@Body byte[] data) {
        CreateResourceRequest request = new CreateResourceRequest(data);
        Resource resource = service.create(request);
        return new CreateResourceResponse(resource.getId());
    }

    @Get("/{id}")
    @Produces("audio/mpeg")
    public byte[] get(@PathVariable Long id) {
        Resource resource = service.get(id).orElseThrow(NotFoundException::new);
        return resource.getData();
    }

    @Delete
    public DeleteResourceResponse delete(@QueryValue String ids) {
        List<Long> idsList = Optional.ofNullable(ids)
                .map(it -> it.split(","))
                .map(Arrays::asList)
                .stream()
                .flatMap(Collection::stream)
                .map(Long::parseLong)
                .toList();
        DeleteResourceRequest request = new DeleteResourceRequest(idsList);
        List<Long> removedIds = service.delete(request);
        return new DeleteResourceResponse(removedIds);
    }

    @Error
    public HttpResponse<JsonError> error(HttpRequest<?> request, NotFoundException e) {
        return HttpResponse.<JsonError>status(HttpStatus.NOT_FOUND, "Not found")
            .body(new JsonError("Not found")
                .link(Link.SELF, Link.of(request.getUri())));
    }

    @Error
    public HttpResponse<JsonError> error(HttpRequest<?> request, ValidationException e) {
        return HttpResponse.<JsonError>status(HttpStatus.BAD_REQUEST, "Bad request")
            .body(new JsonError("Bad request")
                .link(Link.SELF, Link.of(request.getUri())));
    }
}
