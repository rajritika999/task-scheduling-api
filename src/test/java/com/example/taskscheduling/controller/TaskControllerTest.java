package com.example.taskscheduling.controller;

import com.example.taskscheduling.dto.TaskRequest;
import com.example.taskscheduling.dto.TaskResponse;
import com.example.taskscheduling.model.Priority;
import com.example.taskscheduling.model.Status;
import com.example.taskscheduling.service.TaskService;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TaskController.class)
class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TaskService taskService;

    @Autowired
    private ObjectMapper objectMapper;

    // CREATE TASK TEST

    @Test
    void shouldCreateTask() throws Exception {

        TaskRequest request = new TaskRequest();
        request.setTitle("Test Task");
        request.setDescription("Test Description");
        request.setPriority(Priority.HIGH);

        TaskResponse response = new TaskResponse(
                1L,
                "Test Task",
                "Test Description",
                Priority.HIGH,
                Status.PENDING,
                Instant.now(),
                null
        );

        when(taskService.createTask(any(TaskRequest.class)))
                .thenReturn(response);

        mockMvc.perform(post("/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("Test Task"))
                .andExpect(jsonPath("$.priority").value("HIGH"));
    }


    // GET ALL TASKS TEST


    @Test
    void shouldReturnAllTasks() throws Exception {

        TaskResponse response = new TaskResponse(
                1L,
                "Test Task",
                "Test Description",
                Priority.HIGH,
                Status.PENDING,
                Instant.now(),
                null
        );

        when(taskService.getAllTasks())
                .thenReturn(List.of(response));

        mockMvc.perform(get("/tasks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Test Task"));
    }


    // VALIDATION TEST


    @Test
    void shouldReturnBadRequestWhenTitleIsMissing() throws Exception {

        TaskRequest request = new TaskRequest();
        request.setTitle("");   // invalid (assuming @NotBlank)
        request.setDescription("Some Description");
        request.setPriority(Priority.HIGH);

        mockMvc.perform(post("/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }
}
