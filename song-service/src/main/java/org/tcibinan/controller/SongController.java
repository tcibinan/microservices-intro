package org.tcibinan.controller;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.annotation.*;
import io.micronaut.http.annotation.Error;
import io.micronaut.http.hateoas.JsonError;
import io.micronaut.http.hateoas.Link;
import org.tcibinan.controller.request.CreateSongRequest;
import org.tcibinan.controller.request.DeleteSongRequest;
import org.tcibinan.controller.response.CreateSongResponse;
import org.tcibinan.controller.response.DeleteSongResponse;
import org.tcibinan.controller.response.GetSongResponse;
import org.tcibinan.entity.Song;
import org.tcibinan.exception.NotFoundException;
import org.tcibinan.exception.ValidationException;
import org.tcibinan.service.SongService;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Controller("/songs")
public class SongController {
    
    private final SongService service;

    public SongController(SongService service) {
        this.service = service;
    }

    @Post
    public CreateSongResponse create(@Body CreateSongRequest request) {
        Optional.ofNullable(request).orElseThrow(ValidationException::new);
        Song song = service.create(request);
        return new CreateSongResponse(song.getId());
    }

    @Get("/{id}")
    public GetSongResponse get(@PathVariable Long id) {
        Song song = service.get(id).orElseThrow(NotFoundException::new);
        return new GetSongResponse(song.getName(),
                                   song.getArtist(),
                                   song.getAlbum(),
                                   song.getLength(),
                                   song.getResourceId(),
                                   song.getYear());
    }

    @Delete
    public DeleteSongResponse delete(@QueryValue String ids) {
        List<Long> idsList = Optional.ofNullable(ids)
                .map(it -> it.split(","))
                .map(Arrays::asList)
                .stream()
                .flatMap(Collection::stream)
                .map(Long::parseLong)
                .toList();
        DeleteSongRequest request = new DeleteSongRequest(idsList);
        List<Long> removedIds = service.delete(request);
        return new DeleteSongResponse(removedIds);
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
