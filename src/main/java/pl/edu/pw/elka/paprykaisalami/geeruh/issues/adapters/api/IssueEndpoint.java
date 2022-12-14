package pl.edu.pw.elka.paprykaisalami.geeruh.issues.adapters.api;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.List;

import static pl.edu.pw.elka.paprykaisalami.geeruh.issues.domain.models.IssueId.ISSUE_ID_REGEX;
import static pl.edu.pw.elka.paprykaisalami.geeruh.projects.domain.models.ProjectCode.PROJECT_CODE_REGEX;
import static pl.edu.pw.elka.paprykaisalami.geeruh.statuses.domain.models.StatusCode.STATUS_CODE_REGEX;

@Validated
@AllArgsConstructor
@RestController
@RequestMapping("issues")
@Tag(name = "Issues")
class IssueEndpoint {

    private final IssueFacade issueFacade;

    @GetMapping
    public List<IssueResponse> list() {
        return issueFacade.list();
    }

    @GetMapping("{issueId}")
    public IssueResponse get(@PathVariable @Pattern(regexp = ISSUE_ID_REGEX) final String issueId) {
        return issueFacade.get(issueId);
    }

    @GetMapping("{issueId}/history")
    public List<IssueHistoryResponse> getHistory(@PathVariable @Pattern(regexp = ISSUE_ID_REGEX) final String issueId) {
        return issueFacade.getHistory(issueId);
    }

    @PutMapping("{issueId}")
    public IssueResponse update(
            @PathVariable @Pattern(regexp = ISSUE_ID_REGEX) final String issueId,
            @Valid @RequestBody final IssueRequest issueRequest
    ) {
        return issueFacade.update(issueId, issueRequest);
    }

    @PutMapping("{issueId}/status")
    public IssueResponse changeStatus(
            @PathVariable @Pattern(regexp = ISSUE_ID_REGEX) final String issueId,
            @Valid @RequestBody final IssueChangeStatusRequest issueChangeStatusRequest
    ) {
        return issueFacade.changeStatus(issueId, issueChangeStatusRequest);
    }

    @PutMapping("{issueId}/assignee")
    public IssueResponse assignUser(
            @PathVariable @Pattern(regexp = ISSUE_ID_REGEX) final String issueId,
            @Valid @RequestBody final IssueAssignUserRequest issueAssignUserRequest
    ) {
        return issueFacade.assignUser(issueId, issueAssignUserRequest);
    }

    @PostMapping("{issueId}/related-to/{relatedIssueId}")
    public IssueResponse relateIssue(
            @PathVariable @Pattern(regexp = ISSUE_ID_REGEX) final String issueId,
            @PathVariable @Pattern(regexp = ISSUE_ID_REGEX) final String relatedIssueId
    ) {
        return issueFacade.relateIssue(issueId, relatedIssueId);
    }

    @DeleteMapping("{issueId}/related-to/{relatedIssueId}")
    public IssueResponse unRelateIssue(
            @PathVariable @Pattern(regexp = ISSUE_ID_REGEX) final String issueId,
            @PathVariable @Pattern(regexp = ISSUE_ID_REGEX) final String relatedIssueId
    ) {
        return issueFacade.unRelateIssue(issueId, relatedIssueId);
    }

    @PostMapping
    public IssueResponse create(
            @RequestParam @NotNull @Pattern(regexp = PROJECT_CODE_REGEX) @Size(min = 2, max = 5) final String projectCode,
            @RequestParam @NotNull @Pattern(regexp = STATUS_CODE_REGEX) final String statusCode,
            @Valid @RequestBody final IssueRequest issueRequest
    ) {
        return issueFacade.create(projectCode, statusCode, issueRequest);
    }

    @DeleteMapping("{issueId}")
    public void deleteIssue(@PathVariable @Pattern(regexp = ISSUE_ID_REGEX) final String issueId) {
        issueFacade.delete(issueId);
    }
}
