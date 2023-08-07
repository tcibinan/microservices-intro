package org.tcibinan.repository;

import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;
import org.tcibinan.entity.Song;

@Repository
public interface SongRepository extends CrudRepository<Song, Long> {
}
