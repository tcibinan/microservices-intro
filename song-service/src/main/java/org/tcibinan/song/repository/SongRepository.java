package org.tcibinan.song.repository;

import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;
import org.tcibinan.song.entity.Song;

@Repository
public interface SongRepository extends CrudRepository<Song, Long> {
}
