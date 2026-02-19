package com.example.taskscheduling.service;

import com.example.taskscheduling.dto.TaskRequest;
import com.example.taskscheduling.dto.TaskResponse;
import com.example.taskscheduling.exception.TaskNotFoundException;
import com.example.taskscheduling.model.*;
import com.example.taskscheduling.repository.TaskRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private TaskService taskService;

    private Task task;

    @BeforeEach
    void setUp() {
        task = new Task();
        task.setId(1L);
        task.setTitle("Learn Spring Boot");
        task.setDescription("Complete REST API");
        task.setPriority(Priority.HIGH);
        task.setStatus(Status.PENDING);
        task.setCreatedAt(Instant.now());
        task.setDeleted(false);
    }

    // ==============================
    // CREATE TASK TEST
    // ==============================
    @Test
    void shouldCreateTaskSuccessfully() {
        TaskRequest request = new TaskRequest();
        request.setTitle("Learn Spring Boot");
        request.setDescription("Complete REST API");
        request.setPriority(Priority.HIGH);

        when(taskRepository.save(any(Task.class))).thenReturn(task);

        TaskResponse response = taskService.createTask(request);

        assertNotNull(response);
        assertEquals("Learn Spring Boot", response.getTitle());
        assertEquals(Priority.HIGH, response.getPriority());

        verify(taskRepository, times(1)).save(any(Task.class));
    }


    // GET TASK BY ID TEST

    @Test
    void shouldReturnTaskWhenIdExists() {
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));

        TaskResponse response = taskService.getTaskById(1L);

        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals("Learn Spring Boot", response.getTitle());
    }

    @Test
    void shouldThrowExceptionWhenTaskNotFound() {
        when(taskRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(TaskNotFoundException.class,
                () -> taskService.getTaskById(1L));

        verify(taskRepository, times(1)).findById(1L);
    }


    // UPDATE STATUS TEST

    @Test
    void shouldUpdateTaskStatus() {
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        when(taskRepository.save(any(Task.class))).thenReturn(task);

        TaskResponse response = taskService.updateStatus(1L, Status.COMPLETED);

        assertEquals(Status.COMPLETED, response.getStatus());

        verify(taskRepository).save(task);
    }

    // DELETE TASK TEST
    @Test
    void shouldSoftDeleteTask() {
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        when(taskRepository.save(any(Task.class))).thenReturn(task);

        taskService.deleteTask(1L);

        assertTrue(task.isDeleted());

        verify(taskRepository).save(task);
    }
}
