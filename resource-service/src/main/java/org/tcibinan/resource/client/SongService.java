package org.tcibinan.resource.client;

import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.client.annotation.Client;
import org.tcibinan.song.web.request.CreateSongRequest;
import org.tcibinan.song.web.response.CreateSongResponse;
import reactor.core.publisher.Mono;

@Client("songservice")
public interface SongService {

    @Post("/songs")
    Mono<CreateSongResponse> create(@Body CreateSongRequest request);
}
