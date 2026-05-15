package com.taskscheduling.api.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

class TaskModelTest {

    @Test
    void shouldAllowReadAndWriteFields() {
        LocalDate dueDate = LocalDate.of(2026, 12, 31);
        Task task = new Task("1", "Write tests", "Write unit tests for API", TaskStatus.PENDING, dueDate);

        assertThat(task.getId()).isEqualTo("1");
        assertThat(task.getTitle()).isEqualTo("Write tests");
        assertThat(task.getDescription()).isEqualTo("Write unit tests for API");
        assertThat(task.getStatus()).isEqualTo(TaskStatus.PENDING);
        assertThat(task.getDueDate()).isEqualTo(dueDate);

        task.setTitle("Write more tests");
        assertThat(task.getTitle()).isEqualTo("Write more tests");
    }

    @Test
    void shouldSupportEqualsAndHashCode() {
        LocalDate dueDate = LocalDate.of(2026, 12, 31);
        Task task1 = new Task("1", "Task", "Description", TaskStatus.PENDING, dueDate);
        Task task2 = new Task("1", "Task", "Description", TaskStatus.PENDING, dueDate);

        assertThat(task1).isEqualTo(task2);
        assertThat(task1.hashCode()).isEqualTo(task2.hashCode());
    }
}
