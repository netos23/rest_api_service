package ru.fbtw.navigator.rest_api_service.repository;

import org.springframework.data.repository.CrudRepository;
import ru.fbtw.navigator.rest_api_service.domain.Project;

public interface ProjectRepo extends CrudRepository<Project, Long> {
}
