package org.tcibinan.song.service;

import jakarta.inject.Singleton;
import jakarta.transaction.Transactional;
import org.tcibinan.song.web.request.CreateSongRequest;
import org.tcibinan.song.web.request.DeleteSongRequest;
import org.tcibinan.song.entity.Song;
import org.tcibinan.song.repository.SongRepository;

import java.util.List;
import java.util.Optional;

@Singleton
public class SongService {

    private final SongRepository repository;

    public SongService(SongRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public Song create(CreateSongRequest request) {
        Song song = new Song();
        song.setName(request.name());
        song.setArtist(request.artist());
        song.setAlbum(request.album());
        song.setLength(request.length());
        song.setYear(request.year());
        song.setResourceId(request.resourceId());
        return repository.save(song);
    }

    public Optional<Song> get(Long id) {
        return repository.findById(id);
    }

    @Transactional
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
