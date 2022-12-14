package pl.edu.pw.elka.paprykaisalami.geeruh.projects.domain.ports;

import io.vavr.control.Either;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import pl.edu.pw.elka.paprykaisalami.geeruh.projects.domain.models.Project;
import pl.edu.pw.elka.paprykaisalami.geeruh.projects.domain.models.ProjectCode;
import pl.edu.pw.elka.paprykaisalami.geeruh.utils.DomainError;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Validated
@Component
@Transactional(readOnly = true)
public class ProjectService {

    ProjectRepository projectRepository;

    public List<Project> list() {
        return projectRepository.findAll();
    }

    public Either<DomainError, Project> get(ProjectCode projectCode) {
        return projectRepository.findByCode(projectCode);
    }

    @Valid
    @Transactional
    public Project create(ProjectCode projectCode, String name, String description) {
        var project = Project.builder()
                .projectCode(projectCode)
                .name(name)
                .description(description)
                .build();
        return projectRepository.save(project);
    }

    @Valid
    @Transactional
    public Either<DomainError, Project> update(ProjectCode projectCode, String name, String description) {
        return projectRepository.findByCode(projectCode).map(
                project -> {
                    project.setName(name);
                    project.setDescription(description);
                    return projectRepository.save(project);
                });
    }

    @Valid
    @Transactional
    public Optional<DomainError> delete(ProjectCode projectCode) {
        return projectRepository.findByCode(projectCode)
                .map(project -> {
                    projectRepository.delete(project);
                    return null;
                })
                .swap().toJavaOptional();
    }
}
