package ru.fbtw.navigator.rest_api_service.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.fbtw.navigator.rest_api_service.domain.Project;

@Repository
public interface ProjectRepo extends CrudRepository<Project, Long> {
}
