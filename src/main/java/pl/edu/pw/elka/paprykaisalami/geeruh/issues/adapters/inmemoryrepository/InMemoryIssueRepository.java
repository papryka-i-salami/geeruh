package pl.edu.pw.elka.paprykaisalami.geeruh.issues.adapters.inmemoryrepository;

import io.vavr.control.Either;
import lombok.val;
import org.springframework.stereotype.Component;
import pl.edu.pw.elka.paprykaisalami.geeruh.issues.domain.models.Description;
import pl.edu.pw.elka.paprykaisalami.geeruh.issues.domain.models.Issue;
import pl.edu.pw.elka.paprykaisalami.geeruh.issues.domain.models.IssueHistoryEntry;
import pl.edu.pw.elka.paprykaisalami.geeruh.issues.domain.models.IssueId;
import pl.edu.pw.elka.paprykaisalami.geeruh.issues.domain.models.IssueType;
import pl.edu.pw.elka.paprykaisalami.geeruh.issues.domain.models.Summary;
import pl.edu.pw.elka.paprykaisalami.geeruh.issues.domain.ports.IssueRepository;
import pl.edu.pw.elka.paprykaisalami.geeruh.projects.domain.models.ProjectCode;
import pl.edu.pw.elka.paprykaisalami.geeruh.utils.DomainError;
import pl.edu.pw.elka.paprykaisalami.geeruh.utils.DomainError.NotFoundDomainError;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class InMemoryIssueRepository implements IssueRepository {

    private final Map<IssueId, Issue> issues = new HashMap<>();

    private AtomicInteger nextIssueNumber = new AtomicInteger(0);

    @Override
    public List<Issue> findAll() {
        return issues.values().stream()
                .toList();
    }

    @Override
    public Either<DomainError, Issue> findById(IssueId issueId) {
        val issue = issues.get(issueId);
        if (issue == null) {
            return Either.left(new NotFoundDomainError<>(Issue.class, issueId));
        } else {
            return Either.right(issue);
        }
    }

    @Override
    public Issue create(ProjectCode projectCode, IssueType type, Summary summary, Description description) {
        val issueId = IssueId.of(projectCode, nextIssueNumber.incrementAndGet());
        val issue = Issue.builder()
                .issueId(issueId)
                .type(type)
                .summary(summary)
                .description(description)
                .build();
        issues.put(issueId, issue);
        return issue;
    }

    @Override
    public Issue save(Issue issue) {
        issues.put(issue.getIssueId(), issue);
        return issue;
    }

    @Override
    public List<IssueHistoryEntry> getHistory(IssueId issueId) {
        return List.of();
    }
}
