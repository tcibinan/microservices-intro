package org.tcibinan.resource.service;

import jakarta.inject.Singleton;
import jakarta.transaction.Transactional;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.mp3.Mp3Parser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tcibinan.resource.client.SongService;
import org.tcibinan.resource.controller.request.CreateResourceRequest;
import org.tcibinan.resource.controller.request.DeleteResourceRequest;
import org.tcibinan.resource.entity.Resource;
import org.tcibinan.resource.exception.ValidationException;
import org.tcibinan.resource.repository.ResourceRepository;
import org.tcibinan.song.web.request.CreateSongRequest;
import org.tcibinan.song.web.response.CreateSongResponse;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;

@Singleton
public class ResourceService {

    protected static final Logger LOG = LoggerFactory.getLogger(ResourceService.class);

    private final SongService songs;
    private final ResourceRepository repository;

    public ResourceService(SongService songs,
                           ResourceRepository repository) {
        this.songs = songs;
        this.repository = repository;
    }

    @Transactional
    public Resource create(CreateResourceRequest request) {
        LOG.info("Creating resource...");
        Metadata metadata = parse(request.data());
        Resource resource = save(request.data());
        save(metadata, resource);
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
        return songs.create(request).block();
    }

    public Optional<Resource> get(Long id) {
        LOG.info("Getting resource...");
        return repository.findById(id);
    }

    @Transactional
    public List<Long> delete(DeleteResourceRequest request) {
        LOG.info("Deleting resources...");
        return request.ids().stream()
                .filter(id -> {
                    Optional<Resource> resource = repository.findById(id);
                    resource.ifPresent(repository::delete);
                    return resource.isPresent();
                })
                .toList();
    }
}
