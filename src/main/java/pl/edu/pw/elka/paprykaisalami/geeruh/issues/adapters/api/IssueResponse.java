package pl.edu.pw.elka.paprykaisalami.geeruh.issues.adapters.api;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Value;
import pl.edu.pw.elka.paprykaisalami.geeruh.issues.domain.models.Issue;
import pl.edu.pw.elka.paprykaisalami.geeruh.issues.domain.models.IssueType;

import java.util.UUID;

@Builder(access = AccessLevel.PRIVATE)
@Value
class IssueResponse {

    UUID issueId;

    IssueType type;

    String summary;

    String description;

    public static IssueResponse of(Issue issue) {
        return IssueResponse.builder()
                .issueId(issue.getIssueId().getValue())
                .type(issue.getType())
                .summary(issue.getSummary().getValue())
                .description(issue.getSummary().getValue())
                .build();
    }
}