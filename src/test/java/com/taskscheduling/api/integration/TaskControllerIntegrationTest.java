package com.taskscheduling.api.integration;

import com.taskscheduling.api.dto.CreateTaskRequest;
import com.taskscheduling.api.dto.TaskResponse;
import com.taskscheduling.api.dto.UpdateTaskRequest;
import com.taskscheduling.api.model.TaskStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class TaskControllerIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    private String baseUrl() {
        return "http://localhost:" + port + "/tasks";
    }

    @Test
    void shouldCreateRetrieveUpdateListAndDeleteTask() {
        CreateTaskRequest createRequest = new CreateTaskRequest();
        createRequest.setTitle("Integration Task");
        createRequest.setDescription("Full end-to-end integration test");
        createRequest.setStatus(TaskStatus.PENDING);
        createRequest.setDueDate(LocalDate.now().plusDays(1));

        ResponseEntity<TaskResponse> createResponse = restTemplate.postForEntity(baseUrl(), createRequest, TaskResponse.class);
        assertThat(createResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        TaskResponse created = createResponse.getBody();
        assertThat(created).isNotNull();
        assertThat(created.getId()).isNotBlank();
        assertThat(created.getTitle()).isEqualTo("Integration Task");

        ResponseEntity<TaskResponse> getResponse = restTemplate.getForEntity(baseUrl() + "/" + created.getId(), TaskResponse.class);
        assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(getResponse.getBody()).isNotNull();
        assertThat(getResponse.getBody().getTitle()).isEqualTo("Integration Task");

        UpdateTaskRequest updateRequest = new UpdateTaskRequest();
        updateRequest.setStatus(TaskStatus.IN_PROGRESS);
        HttpEntity<UpdateTaskRequest> updateEntity = new HttpEntity<>(updateRequest);

        ResponseEntity<TaskResponse> updateResponse = restTemplate.exchange(baseUrl() + "/" + created.getId(), HttpMethod.PUT, updateEntity, TaskResponse.class);
        assertThat(updateResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(updateResponse.getBody()).isNotNull();
        assertThat(updateResponse.getBody().getStatus()).isEqualTo(TaskStatus.IN_PROGRESS);

        ResponseEntity<TaskResponse[]> listResponse = restTemplate.getForEntity(baseUrl() + "?status=IN_PROGRESS&page=0&size=10", TaskResponse[].class);
        assertThat(listResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(listResponse.getBody()).hasSize(1);
        assertThat(listResponse.getBody()[0].getId()).isEqualTo(created.getId());

        ResponseEntity<TaskResponse[]> pagedResponse = restTemplate.getForEntity(baseUrl() + "?page=0&size=1", TaskResponse[].class);
        assertThat(pagedResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(pagedResponse.getBody()).hasSize(1);

        ResponseEntity<Void> deleteResponse = restTemplate.exchange(baseUrl() + "/" + created.getId(), HttpMethod.DELETE, null, Void.class);
        assertThat(deleteResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
    }
}
