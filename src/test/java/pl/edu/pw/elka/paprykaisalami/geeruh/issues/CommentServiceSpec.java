package pl.edu.pw.elka.paprykaisalami.geeruh.issues;

import org.junit.jupiter.api.Test;
import pl.edu.pw.elka.paprykaisalami.geeruh.BaseSpec;
import pl.edu.pw.elka.paprykaisalami.geeruh.issues.domain.models.Description;
import pl.edu.pw.elka.paprykaisalami.geeruh.issues.domain.models.IssueType;
import pl.edu.pw.elka.paprykaisalami.geeruh.issues.domain.models.Summary;
import pl.edu.pw.elka.paprykaisalami.geeruh.projects.domain.models.ProjectCode;
import pl.edu.pw.elka.paprykaisalami.geeruh.statuses.domain.models.StatusCode;
import pl.edu.pw.elka.paprykaisalami.geeruh.utils.DomainError.NotFoundDomainError;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.vavr.api.VavrAssertions.assertThat;
import static pl.edu.pw.elka.paprykaisalami.geeruh.support.IssueAttributeDataset.FIRST_ISSUE_TYPE;
import static pl.edu.pw.elka.paprykaisalami.geeruh.support.IssueAttributeDataset.FIRST_ISSUE_SUMMARY;
import static pl.edu.pw.elka.paprykaisalami.geeruh.support.IssueAttributeDataset.FIRST_ISSUE_DESCRIPTION;
import static pl.edu.pw.elka.paprykaisalami.geeruh.support.IssueAttributeDataset.SECOND_ISSUE_TYPE;
import static pl.edu.pw.elka.paprykaisalami.geeruh.support.IssueAttributeDataset.SECOND_ISSUE_DESCRIPTION;
import static pl.edu.pw.elka.paprykaisalami.geeruh.support.ProjectAttributeDataset.FIRST_PROJECT_CODE;
import static pl.edu.pw.elka.paprykaisalami.geeruh.support.StatusAttributeDataset.FIRST_STATUS_CODE;
import static pl.edu.pw.elka.paprykaisalami.geeruh.support.StatusAttributeDataset.SECOND_STATUS_CODE;
import static pl.edu.pw.elka.paprykaisalami.geeruh.support.assertions.GeeruhAssertions.assertThat;
import static pl.edu.pw.elka.paprykaisalami.geeruh.support.datasets.IssueDataset.firstIssue;
import static pl.edu.pw.elka.paprykaisalami.geeruh.support.datasets.IssueDataset.firstIssueId;
import static pl.edu.pw.elka.paprykaisalami.geeruh.support.datasets.IssueDataset.secondIssue;
import static pl.edu.pw.elka.paprykaisalami.geeruh.support.datasets.IssueDataset.secondIssueId;
import static pl.edu.pw.elka.paprykaisalami.geeruh.support.datasets.ProjectDataset.firstProject;
import static pl.edu.pw.elka.paprykaisalami.geeruh.support.datasets.StatusDataset.firstStatus;
import static pl.edu.pw.elka.paprykaisalami.geeruh.support.datasets.StatusDataset.secondStatus;

public class CommentServiceSpec extends BaseSpec {

    @Test
    void shouldCreateIssue() {
        // given
        thereAreProjects(firstProject());

        thereAreStatuses(firstStatus());

        // when
        var issue = issueService.create(
                new ProjectCode(FIRST_PROJECT_CODE),
                new StatusCode(FIRST_STATUS_CODE),
                IssueType.valueOf(FIRST_ISSUE_TYPE),
                new Summary(FIRST_ISSUE_SUMMARY),
                new Description(FIRST_ISSUE_DESCRIPTION)
        );

        // expect
        assertThat(issue).hasRightValueSatisfying(i -> assertThat(i)
                .isEquivalentOmittingIndexTo(firstIssue())
        );
    }

    @Test
    void shouldNotCreateIssue_whenProjectNonExistent() {
        // given
        thereAreStatuses(firstStatus());

        // when
        var issue = issueService.create(
                new ProjectCode(FIRST_PROJECT_CODE),
                new StatusCode(FIRST_STATUS_CODE),
                IssueType.valueOf(FIRST_ISSUE_TYPE),
                new Summary(FIRST_ISSUE_SUMMARY),
                new Description(FIRST_ISSUE_DESCRIPTION)
        );

        // expect
        assertThat(issue).containsLeftInstanceOf(NotFoundDomainError.class);
    }

    @Test
    void shouldNotCreateIssue_whenStatusNonExistent() {
        // given
        thereAreProjects(firstProject());

        // when
        var issue = issueService.create(
                new ProjectCode(FIRST_PROJECT_CODE),
                new StatusCode(FIRST_STATUS_CODE),
                IssueType.valueOf(FIRST_ISSUE_TYPE),
                new Summary(FIRST_ISSUE_SUMMARY),
                new Description(FIRST_ISSUE_DESCRIPTION)
        );

        // expect
        assertThat(issue).containsLeftInstanceOf(NotFoundDomainError.class);
    }

    @Test
    void shouldGetIssue() {
        // given
        thereAreIssues(firstIssue());

        // when
        var issue = issueService.get(firstIssueId());

        // expect
        assertThat(issue).hasRightValueSatisfying(p -> assertThat(p)
                .isEquivalentTo(firstIssue())
        );
    }

    @Test
    void shouldNotGetIssue_whenAbsent() {
        // given
        thereAreIssues(firstIssue());

        // when
        var issue = issueService.get(secondIssueId());

        // expect
        assertThat(issue).containsLeftInstanceOf(NotFoundDomainError.class);
    }

    @Test
    void shouldListAllIssues() {
        // given
        thereAreIssues(firstIssue(), secondIssue());

        // when
        var issues = issueService.list();

        // expect
        assertThat(issues)
                .usingRecursiveFieldByFieldElementComparator()
                .containsExactlyInAnyOrder(firstIssue(), secondIssue());
    }

    @Test
    void shouldListNoIssues_whenNoneCreated() {
        // given
        var issues = issueService.list();

        // expect
        assertThat(issues).isEmpty();
    }

    @Test
    void shouldUpdateIssue() {
        // given
        thereAreIssues(firstIssue());
        thereAreStatuses(firstStatus());

        // when
        var issue = issueService.update(
                firstIssueId(),
                IssueType.valueOf(SECOND_ISSUE_TYPE),
                new Summary(FIRST_ISSUE_SUMMARY),
                new Description(SECOND_ISSUE_DESCRIPTION)
        );

        // expect
        assertThat(issue).hasRightValueSatisfying(p -> assertThat(p)
                .hasIssueId(firstIssueId())
                .hasType(SECOND_ISSUE_TYPE)
                .hasStatusCode(FIRST_STATUS_CODE)
                .hasSummary(FIRST_ISSUE_SUMMARY)
                .hasDescription(SECOND_ISSUE_DESCRIPTION)
        );
    }

    @Test
    void shouldNotUpdateIssue_whenProjectIsAbsent() {
        // given
        thereAreStatuses(firstStatus());

        // when
        var issue = issueService.update(
                firstIssueId(),
                IssueType.valueOf(SECOND_ISSUE_TYPE),
                new Summary(FIRST_ISSUE_SUMMARY),
                new Description(SECOND_ISSUE_DESCRIPTION)
        );

        // expect
        assertThat(issue).containsLeftInstanceOf(NotFoundDomainError.class);
    }

    @Test
    void shouldChangeStatus() {
        // given
        thereAreIssues(firstIssue());
        thereAreStatuses(firstStatus());
        thereAreStatuses(secondStatus());

        // when
        var issue = issueService.changeStatus(
                firstIssueId(),
                new StatusCode(SECOND_STATUS_CODE)
        );

        // expect
        assertThat(issue).hasRightValueSatisfying(p -> assertThat(p)
                .hasIssueId(firstIssueId())
                .hasType(FIRST_ISSUE_TYPE)
                .hasStatusCode(SECOND_STATUS_CODE)
                .hasSummary(FIRST_ISSUE_SUMMARY)
                .hasDescription(FIRST_ISSUE_DESCRIPTION)
        );
    }

    @Test
    void shouldRelateIssue() {
        // given
        thereAreIssues(firstIssue());
        thereAreIssues(secondIssue());

        // when
        var issue = issueService.relateIssue(firstIssueId(), secondIssueId());

        // expect
        assertThat(issue).hasRightValueSatisfying(p -> assertThat(p)
                .hasIssueId(firstIssueId())
                .hasType(FIRST_ISSUE_TYPE)
                .hasStatusCode(FIRST_STATUS_CODE)
                .hasSummary(FIRST_ISSUE_SUMMARY)
                .hasDescription(FIRST_ISSUE_DESCRIPTION)
                .hasRelatedIssue(secondIssueId())
        );
    }

    @Test
    void shouldNotChangeStatus_whenStatusDoesNotExists() {
        // given
        thereAreIssues(firstIssue());
        thereAreStatuses(firstStatus());

        // when
        var issue = issueService.changeStatus(
                firstIssueId(),
                new StatusCode(SECOND_STATUS_CODE)
        );

        // expect
        assertThat(issue).containsLeftInstanceOf(NotFoundDomainError.class);
    }

}
