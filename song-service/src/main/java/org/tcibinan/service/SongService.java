package org.tcibinan.service;

import jakarta.inject.Singleton;
import org.tcibinan.controller.request.CreateSongRequest;
import org.tcibinan.controller.request.DeleteSongRequest;
import org.tcibinan.entity.Song;
import org.tcibinan.repository.SongRepository;

import java.util.List;
import java.util.Optional;

@Singleton
public class SongService {
    
    private final SongRepository repository;

    public SongService(SongRepository repository) {
        this.repository = repository;
    }
    
    public Song create(CreateSongRequest request) {
        Song song = new Song();
        song.setName(request.name());
        song.setArtist(request.artist());
        song.setAlbum(request.album());
        song.setLength(request.length());
        song.setResourceId(request.resourceId());
        song.setYear(request.year());
        return repository.save(song);
    }

    public Optional<Song> get(Long id) {
        return repository.findById(id);
    }

    public List<Long> delete(DeleteSongRequest request) {
        return request.ids().stream()
        .filter(id -> {
            Optional<Song> song = repository.findById(id);
            song.ifPresent(repository::delete);
            return song.isPresent();
        })
        .toList();
    }
}
