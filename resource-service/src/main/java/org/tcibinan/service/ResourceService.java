package org.tcibinan.service;

import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import jakarta.inject.Singleton;
import jakarta.transaction.Transactional;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.mp3.Mp3Parser;
import org.tcibinan.controller.request.CreateResourceRequest;
import org.tcibinan.controller.request.CreateSongRequest;
import org.tcibinan.controller.request.DeleteResourceRequest;
import org.tcibinan.controller.response.CreateSongResponse;
import org.tcibinan.entity.Resource;
import org.tcibinan.exception.ValidationException;
import org.tcibinan.repository.ResourceRepository;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static io.micronaut.http.HttpRequest.POST;

@Singleton
public class ResourceService {

    private final HttpClient songs;
    private final ResourceRepository repository;

    public ResourceService(@Client("song") HttpClient songs,
                           ResourceRepository repository) {
        this.songs = songs;
        this.repository = repository;
    }

    @Transactional
    public Resource create(CreateResourceRequest request) {
        Metadata metadata = parse(request.data());
        Resource resource = save(request.data());
        CreateSongResponse song = save(metadata, resource);
        return resource;
    }

    private Resource save(byte[] data) {
        Resource resource = new Resource();
        resource.setData(data);
        return repository.save(resource);
    }

    private Metadata parse(byte[] data) {
        InputStream is = new ByteArrayInputStream(data);
        DefaultHandler handler = new DefaultHandler();
        Metadata metadata = new Metadata();
        Mp3Parser parser = new Mp3Parser();
        ParseContext parseCtx = new ParseContext();
        try {
            parser.parse(is, handler, metadata, parseCtx);
        } catch (IOException | SAXException | TikaException e) {
            throw new ValidationException(e);
        }
        System.out.println(String.join(",", metadata.names()));
        return metadata;
    }

    private CreateSongResponse save(Metadata metadata, Resource resource) {
        CreateSongRequest request = new CreateSongRequest(
                metadata.get("title"),
                metadata.get("xmpDM:artist"),
                metadata.get("xmpDM:album"),
                metadata.get("xmpDM:duration"),
                metadata.get("xmpDM:releaseDate"),
                resource.getId());
        return songs.toBlocking().retrieve(POST("/songs", request), CreateSongResponse.class);
    }

    public Optional<Resource> get(Long id) {
        return repository.findById(id);
    }

    @Transactional
    public List<Long> delete(DeleteResourceRequest request) {
        return request.ids().stream()
                .filter(id -> {
                    Optional<Resource> resource = repository.findById(id);
                    resource.ifPresent(repository::delete);
                    return resource.isPresent();
                })
                .toList();
    }
}
