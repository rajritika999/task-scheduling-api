package com.taskscheduling.api.repository;

import com.taskscheduling.api.model.Task;
import com.taskscheduling.api.model.TaskStatus;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class InMemoryTaskRepositoryTest {

    @Test
    void shouldSaveFindListAndDeleteTask() {
        InMemoryTaskRepository repository = new InMemoryTaskRepository();
        Task task = new Task("1", "Integration Task", "Repository test task", TaskStatus.PENDING, LocalDate.now().plusDays(1));

        repository.save(task);

        Optional<Task> found = repository.findById("1");
        assertThat(found).isPresent();
        assertThat(found.get().getTitle()).isEqualTo("Integration Task");

        List<Task> tasks = repository.findAll();
        assertThat(tasks).containsExactly(task);

        repository.deleteById("1");
        assertThat(repository.findById("1")).isEmpty();
    }
}
