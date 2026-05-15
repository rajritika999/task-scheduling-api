package com.taskscheduling.api.service;

import com.taskscheduling.api.dto.CreateTaskRequest;
import com.taskscheduling.api.dto.TaskResponse;
import com.taskscheduling.api.dto.UpdateTaskRequest;
import com.taskscheduling.api.exception.TaskNotFoundException;
import com.taskscheduling.api.model.Task;
import com.taskscheduling.api.model.TaskStatus;
import com.taskscheduling.api.repository.TaskRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private TaskServiceImpl taskService;

    @Test
    void shouldCreateTaskSuccessfully() {
        CreateTaskRequest request = new CreateTaskRequest();
        request.setTitle("Task 1");
        request.setDescription("Desc");
        request.setStatus(TaskStatus.PENDING);
        request.setDueDate(LocalDate.now().plusDays(1));

        when(taskRepository.save(any(Task.class))).thenAnswer(invocation -> invocation.getArgument(0));

        TaskResponse response = taskService.createTask(request);

        assertNotNull(response);
        assertEquals("Task 1", response.getTitle());
        assertEquals(TaskStatus.PENDING, response.getStatus());
        verify(taskRepository, times(1)).save(any(Task.class));
    }

    @Test
    void shouldCreateTaskWithDefaultPendingStatusWhenStatusIsNull() {
        CreateTaskRequest request = new CreateTaskRequest();
        request.setTitle("Task 2");
        request.setDescription("Desc 2");
        request.setDueDate(LocalDate.now().plusDays(2));

        when(taskRepository.save(any(Task.class))).thenAnswer(invocation -> invocation.getArgument(0));

        TaskResponse response = taskService.createTask(request);

        assertNotNull(response);
        assertEquals(TaskStatus.PENDING, response.getStatus());
        verify(taskRepository, times(1)).save(any(Task.class));
    }

    @Test
    void shouldReturnTaskById() {
        Task task = new Task();
        task.setId("1");
        task.setTitle("Task 1");
        task.setStatus(TaskStatus.PENDING);
        task.setDueDate(LocalDate.now());

        when(taskRepository.findById("1")).thenReturn(Optional.of(task));

        TaskResponse response = taskService.getTaskById("1");

        assertEquals("Task 1", response.getTitle());
        assertEquals(TaskStatus.PENDING, response.getStatus());
    }

    @Test
    void shouldThrowExceptionWhenTaskNotFound() {
        when(taskRepository.findById("1")).thenReturn(Optional.empty());

        assertThrows(TaskNotFoundException.class,
                () -> taskService.getTaskById("1"));
    }

    @Test
    void shouldReturnAllTasksSortedByDueDate() {
        Task first = new Task("1", "First", "Desc", TaskStatus.PENDING, LocalDate.now().plusDays(5));
        Task second = new Task("2", "Second", "Desc", TaskStatus.PENDING, LocalDate.now().plusDays(2));

        when(taskRepository.findAll()).thenReturn(Arrays.asList(first, second));

        List<TaskResponse> responses = taskService.getAllTasks(null, 0, 10);

        assertEquals(2, responses.size());
        assertEquals("Second", responses.get(0).getTitle());
        assertEquals("First", responses.get(1).getTitle());
    }

    @Test
    void shouldReturnFilteredPagedTasks() {
        Task first = new Task("1", "First", "Desc", TaskStatus.PENDING, LocalDate.now().plusDays(2));
        Task second = new Task("2", "Second", "Desc", TaskStatus.IN_PROGRESS, LocalDate.now().plusDays(3));
        Task third = new Task("3", "Third", "Desc", TaskStatus.PENDING, LocalDate.now().plusDays(1));

        when(taskRepository.findAll()).thenReturn(Arrays.asList(first, second, third));

        List<TaskResponse> responses = taskService.getAllTasks(TaskStatus.PENDING, 0, 1);

        assertEquals(1, responses.size());
        assertEquals("Third", responses.get(0).getTitle());
    }

    @Test
    void shouldUpdateTaskSuccessfully() {
        Task task = new Task();
        task.setId("1");
        task.setTitle("Old Title");
        task.setStatus(TaskStatus.PENDING);

        UpdateTaskRequest request = new UpdateTaskRequest();
        request.setTitle("New Title");
        request.setStatus(TaskStatus.IN_PROGRESS);

        when(taskRepository.findById("1")).thenReturn(Optional.of(task));
        when(taskRepository.save(any(Task.class))).thenAnswer(invocation -> invocation.getArgument(0));

        TaskResponse response = taskService.updateTask("1", request);

        assertNotNull(response);
        assertEquals("New Title", response.getTitle());
        assertEquals(TaskStatus.IN_PROGRESS, response.getStatus());
        verify(taskRepository, times(1)).save(any(Task.class));
    }

    @Test
    void shouldDeleteTaskSuccessfully() {
        Task task = new Task();
        task.setId("1");

        when(taskRepository.findById("1")).thenReturn(Optional.of(task));
        doNothing().when(taskRepository).deleteById("1");

        assertDoesNotThrow(() -> taskService.deleteTask("1"));
        verify(taskRepository, times(1)).deleteById("1");
    }

    @Test
    void shouldThrowExceptionWhenDeleteTaskNotFound() {
        when(taskRepository.findById("1")).thenReturn(Optional.empty());

        assertThrows(TaskNotFoundException.class, () -> taskService.deleteTask("1"));
    }
}
