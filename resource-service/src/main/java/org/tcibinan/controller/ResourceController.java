package org.tcibinan.controller;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.*;
import io.micronaut.http.annotation.Error;
import io.micronaut.http.hateoas.JsonError;
import io.micronaut.http.hateoas.Link;
import org.tcibinan.controller.request.CreateResourceRequest;
import org.tcibinan.controller.request.DeleteResourceRequest;
import org.tcibinan.controller.response.CreateResourceResponse;
import org.tcibinan.controller.response.DeleteResourceResponse;
import org.tcibinan.controller.response.GetResourceResponse;
import org.tcibinan.entity.Resource;
import org.tcibinan.exception.NotFoundException;
import org.tcibinan.exception.ValidationError;
import org.tcibinan.service.ResourceService;

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
    public CreateResourceResponse create(@Body String body) {
        CreateResourceRequest request = new CreateResourceRequest(body);
        Resource resource = service.create(request);
        return new CreateResourceResponse(resource.getId());
    }

    @Get("/{id}")
    public GetResourceResponse get(@PathVariable Long id) {
        Resource resource = service.get(id).orElseThrow(NotFoundException::new);
        return new GetResourceResponse(resource.getData());
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
    public HttpResponse<JsonError> jsonError(HttpRequest<?> request, NotFoundException e) {
        return HttpResponse.<JsonError>status(HttpStatus.NOT_FOUND, "Not found")
            .body(new JsonError("Not found")
                .link(Link.SELF, Link.of(request.getUri())));
    }

    @Error
    public HttpResponse<JsonError> jsonError(HttpRequest<?> request, ValidationError e) {
        return HttpResponse.<JsonError>status(HttpStatus.BAD_REQUEST, "Bad request")
            .body(new JsonError("Bad request")
                .link(Link.SELF, Link.of(request.getUri())));
    }
}
