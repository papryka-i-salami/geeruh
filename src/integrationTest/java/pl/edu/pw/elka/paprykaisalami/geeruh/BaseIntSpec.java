package pl.edu.pw.elka.paprykaisalami.geeruh;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.val;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import pl.edu.pw.elka.paprykaisalami.geeruh.comments.adapters.api.CommentResponse;
import pl.edu.pw.elka.paprykaisalami.geeruh.issues.adapters.api.IssueResponse;
import pl.edu.pw.elka.paprykaisalami.geeruh.projects.adapters.api.ProjectResponse;
import pl.edu.pw.elka.paprykaisalami.geeruh.statuses.adapters.api.StatusResponse;
import pl.edu.pw.elka.paprykaisalami.geeruh.utils.ResetDbService;
import pl.edu.pw.elka.paprykaisalami.geeruh.users.adapters.api.UserResponse;

import java.io.IOException;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static pl.edu.pw.elka.paprykaisalami.geeruh.support.ProjectAttributeDataset.FIRST_PROJECT_CODE;
import static pl.edu.pw.elka.paprykaisalami.geeruh.support.ProjectDataset.FIRST_PROJECT;
import static pl.edu.pw.elka.paprykaisalami.geeruh.support.StatusAttributeDataset.FIRST_STATUS_CODE;
import static pl.edu.pw.elka.paprykaisalami.geeruh.support.StatusDataset.FIRST_STATUS;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public abstract class BaseIntSpec {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    protected ResetDbService resetDbService;

    protected static MockHttpServletRequestBuilder get(String url, Object... uriVariables) {
        return MockMvcRequestBuilders.get(url, uriVariables)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);
    }

    protected static MockHttpServletRequestBuilder put(String url, Object... uriVariables) {
        return MockMvcRequestBuilders.put(url, uriVariables)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);
    }

    protected static MockHttpServletRequestBuilder post(String url, Object... uriVariables) {
        return MockMvcRequestBuilders.post(url, uriVariables)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);
    }

    protected static MockHttpServletRequestBuilder delete(String url, Object... uriVariables) {
        return MockMvcRequestBuilders.delete(url, uriVariables)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);
    }

    protected <T> T mapContent(byte[] content, Class<T> clazz) throws IOException {
        return objectMapper.readValue(content, clazz);
    }

    @BeforeEach
    protected void clearDb() {
        resetDbService.resetDatabase();
    }

    public ProjectResponse thereIsProject(String code, Object body) throws Exception {
        var request = post("/projects/{code}", code)
                .content(body.toString());

        var reader = mockMvc.perform(request)
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsByteArray();

        return mapContent(reader, ProjectResponse.class);
    }

    public StatusResponse thereIsStatus(String code, Object body) throws Exception {
        var request = post("/statuses/{code}", code)
                .content(body.toString());

        var reader = mockMvc.perform(request)
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsByteArray();

        return mapContent(reader, StatusResponse.class);
    }


    public UserResponse thereIsUser(Object body) throws Exception {
        var request = post("/users")
                .content(body.toString());

        var reader = mockMvc.perform(request)
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsByteArray();

        return mapContent(reader, UserResponse.class);
    }

    protected IssueResponse thereIsIssue(Object body) throws Exception {
        thereIsProject(FIRST_PROJECT_CODE, FIRST_PROJECT);
        thereIsStatus(FIRST_STATUS_CODE, FIRST_STATUS);


        val request = post("/issues")
                .param("projectCode", FIRST_PROJECT_CODE)
                .param("statusCode", FIRST_STATUS_CODE)
                .content(body.toString());

        val reader = mockMvc.perform(request)
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsByteArray();

        return mapContent(reader, IssueResponse.class);
    }

    protected CommentResponse thereIsComment(Object commentBody, Object issueBody, Object userBody) throws Exception {
        var issue = thereIsIssue(issueBody);
        if (userBody != null) {
            thereIsUser(userBody);
        }

        val request = post("/comments")
                .param("issueId", issue.issueId())
                .content(commentBody.toString());

        val reader = mockMvc.perform(request)
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsByteArray();

        return mapContent(reader, CommentResponse.class);
    }
}
