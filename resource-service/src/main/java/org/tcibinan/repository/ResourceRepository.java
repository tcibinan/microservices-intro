package org.tcibinan.repository;

import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;
import org.tcibinan.entity.Resource;

@Repository
public interface ResourceRepository extends CrudRepository<Resource, Long> {
}
