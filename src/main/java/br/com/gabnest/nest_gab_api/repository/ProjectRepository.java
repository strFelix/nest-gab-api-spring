package br.com.gabnest.nest_gab_api.repository;

import br.com.gabnest.nest_gab_api.model.Project;
import br.com.gabnest.nest_gab_api.model.enums.ProjectStatus;
import br.com.gabnest.nest_gab_api.model.enums.ProjectStage;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectRepository extends MongoRepository<Project, String> {
    List<Project> findByStatus(ProjectStatus status);
    List<Project> findByStage(ProjectStage stage);
    List<Project> findByCreatedById(String userId);
}