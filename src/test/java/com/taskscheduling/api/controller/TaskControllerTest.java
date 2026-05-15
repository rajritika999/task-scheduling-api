package com.taskscheduling.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.taskscheduling.api.dto.CreateTaskRequest;
import com.taskscheduling.api.dto.TaskResponse;
import com.taskscheduling.api.dto.UpdateTaskRequest;
import com.taskscheduling.api.model.TaskStatus;
import com.taskscheduling.api.service.TaskService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TaskController.class)
class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TaskService taskService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldCreateTask() throws Exception {
        CreateTaskRequest request = new CreateTaskRequest();
        request.setTitle("Task");
        request.setDescription("Desc");
        request.setStatus(TaskStatus.PENDING);
        request.setDueDate(LocalDate.now().plusDays(1));

        TaskResponse response = new TaskResponse("1", "Task", "Desc", TaskStatus.PENDING, request.getDueDate());
        when(taskService.createTask(any(CreateTaskRequest.class))).thenReturn(response);

        mockMvc.perform(post("/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.title").value("Task"))
                .andExpect(jsonPath("$.status").value("PENDING"));

        verify(taskService, times(1)).createTask(any(CreateTaskRequest.class));
    }

    @Test
    void shouldReturnTaskById() throws Exception {
        TaskResponse response = new TaskResponse("1", "Task", "Desc", TaskStatus.PENDING, LocalDate.now().plusDays(2));
        when(taskService.getTaskById("1")).thenReturn(response);

        mockMvc.perform(get("/tasks/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.title").value("Task"));

        verify(taskService, times(1)).getTaskById("1");
    }

    @Test
    void shouldUpdateTask() throws Exception {
        UpdateTaskRequest request = new UpdateTaskRequest();
        request.setStatus(TaskStatus.IN_PROGRESS);

        TaskResponse response = new TaskResponse("1", "Task", "Desc", TaskStatus.IN_PROGRESS, LocalDate.now().plusDays(2));
        when(taskService.updateTask(eq("1"), any(UpdateTaskRequest.class))).thenReturn(response);

        mockMvc.perform(put("/tasks/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("IN_PROGRESS"));

        verify(taskService, times(1)).updateTask(eq("1"), any(UpdateTaskRequest.class));
    }

    @Test
    void shouldListTasksWithStatusAndPagination() throws Exception {
        TaskResponse response = new TaskResponse("1", "Task", "Desc", TaskStatus.PENDING, LocalDate.now().plusDays(2));
        when(taskService.getAllTasks(eq(TaskStatus.PENDING), eq(0), eq(5))).thenReturn(List.of(response));

        mockMvc.perform(get("/tasks?status=PENDING&page=0&size=5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value("1"))
                .andExpect(jsonPath("$[0].status").value("PENDING"));

        verify(taskService, times(1)).getAllTasks(eq(TaskStatus.PENDING), eq(0), eq(5));
    }

    @Test
    void shouldDeleteTask() throws Exception {
        doNothing().when(taskService).deleteTask("1");

        mockMvc.perform(delete("/tasks/1"))
                .andExpect(status().isOk());

        verify(taskService, times(1)).deleteTask("1");
    }
}
