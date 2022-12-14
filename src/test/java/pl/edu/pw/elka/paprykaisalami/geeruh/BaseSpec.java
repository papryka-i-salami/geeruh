package pl.edu.pw.elka.paprykaisalami.geeruh;

import org.hibernate.validator.internal.engine.ValidatorImpl;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.util.SerializationUtils;
import pl.edu.pw.elka.paprykaisalami.geeruh.issues.domain.models.Issue;
import pl.edu.pw.elka.paprykaisalami.geeruh.issues.domain.ports.IssueRepository;
import pl.edu.pw.elka.paprykaisalami.geeruh.issues.domain.ports.IssueService;
import pl.edu.pw.elka.paprykaisalami.geeruh.projects.domain.models.Project;
import pl.edu.pw.elka.paprykaisalami.geeruh.projects.domain.ports.ProjectRepository;
import pl.edu.pw.elka.paprykaisalami.geeruh.projects.domain.ports.ProjectService;
import pl.edu.pw.elka.paprykaisalami.geeruh.statuses.domain.models.Status;
import pl.edu.pw.elka.paprykaisalami.geeruh.statuses.domain.ports.StatusRepository;
import pl.edu.pw.elka.paprykaisalami.geeruh.statuses.domain.ports.StatusService;
import pl.edu.pw.elka.paprykaisalami.geeruh.support.FakePasswordEncoder;
import pl.edu.pw.elka.paprykaisalami.geeruh.support.inmemory.IssueInMemoryRepository;
import pl.edu.pw.elka.paprykaisalami.geeruh.support.inmemory.ProjectInMemoryRepository;
import pl.edu.pw.elka.paprykaisalami.geeruh.support.inmemory.StatusInMemoryRepository;
import pl.edu.pw.elka.paprykaisalami.geeruh.support.inmemory.UserInMemoryRepository;
import pl.edu.pw.elka.paprykaisalami.geeruh.users.domain.models.User;
import pl.edu.pw.elka.paprykaisalami.geeruh.users.domain.ports.UserRepository;
import pl.edu.pw.elka.paprykaisalami.geeruh.users.domain.ports.UserService;

import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Arrays;

public abstract class BaseSpec {

    protected final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    protected ProjectRepository projectRepository;
    protected ProjectService projectService;

    protected StatusRepository statusRepository;
    protected StatusService statusService;

    protected IssueRepository issueRepository;
    protected IssueService issueService;

    protected UserRepository userRepository;
    protected UserService userService;

    @BeforeEach
    protected void initialize() {
        projectRepository = new ProjectInMemoryRepository();
        projectService = new ProjectService(projectRepository);

        statusRepository = new StatusInMemoryRepository();
        statusService = new StatusService(statusRepository);

        userRepository = new UserInMemoryRepository();
        userService = new UserService(userRepository, new FakePasswordEncoder());

        issueRepository = new IssueInMemoryRepository();
        issueService = new IssueService(projectService, statusService, userService, issueRepository);
    }

    protected void thereAreProjects(Project... projects) {
        Arrays.stream(projects)
                .forEach(projectRepository::save);
    }

    protected void thereAreStatuses(Status... statuses) {
        Arrays.stream(statuses)
                .forEach(statusRepository::save);
    }

    protected void thereAreIssues(Issue... issues) {
        Arrays.stream(issues).forEach(issueRepository::save);
    }

    protected void thereAreUsers(User... users) {
        Arrays.stream(users)
                .forEach(userRepository::save);
    }
}
