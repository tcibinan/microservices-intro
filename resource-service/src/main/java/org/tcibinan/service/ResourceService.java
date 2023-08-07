package org.tcibinan.service;

import jakarta.inject.Singleton;
import org.tcibinan.controller.request.CreateResourceRequest;
import org.tcibinan.controller.request.DeleteResourceRequest;
import org.tcibinan.entity.Resource;
import org.tcibinan.repository.ResourceRepository;

import java.util.List;
import java.util.Optional;

@Singleton
public class ResourceService {
    
    private final ResourceRepository repository;

    public ResourceService(ResourceRepository repository) {
        this.repository = repository;
    }
    
    public Resource create(CreateResourceRequest request) {
        Resource resource = new Resource();
        resource.setData(request.data());
        return repository.save(resource);
    }

    public Optional<Resource> get(Long id) {
        return repository.findById(id);
    }

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
