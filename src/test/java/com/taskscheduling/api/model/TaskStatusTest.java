package com.taskscheduling.api.model;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class TaskStatusTest {

    @Test
    void shouldContainExpectedValues() {
        assertThat(TaskStatus.values()).containsExactly(TaskStatus.PENDING, TaskStatus.IN_PROGRESS, TaskStatus.DONE);
    }

    @Test
    void shouldResolveValueByName() {
        assertThat(TaskStatus.valueOf("DONE")).isEqualTo(TaskStatus.DONE);
    }
}
