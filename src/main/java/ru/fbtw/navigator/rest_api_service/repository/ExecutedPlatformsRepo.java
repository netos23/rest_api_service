package ru.fbtw.navigator.rest_api_service.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.fbtw.navigator.rest_api_service.domain.ExecutedProject;
import ru.fbtw.navigator.rest_api_service.domain.Platform;
import ru.fbtw.navigator.rest_api_service.domain.Project;

import java.util.Optional;
import java.util.Set;

@Repository
public interface ExecutedPlatformsRepo extends CrudRepository<ExecutedProject, Long> {
	Set<ExecutedProject> findAllByProject(Project project);


	Optional<ExecutedProject> findByProjectAndPlatforms(Project project, Platform platform);

	boolean removeByProjectAndPlatforms(Project project, Platform platform);
	boolean removeById(Long id);
}
