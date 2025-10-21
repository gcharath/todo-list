package com.sample.todolist.service;

import com.sample.todolist.model.Task;
import com.sample.todolist.model.TaskPrimaryKey;
import com.sample.todolist.repository.TaskRepo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
public class TaskServiceTest {

    @Mock
    private TaskRepo repo;
    @InjectMocks
    private TaskService service;

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void getTasksTest() {
        TaskPrimaryKey key = new TaskPrimaryKey("charath@gmail.com", UUID.randomUUID(),"inbox");
        Task task = new Task();
        task.setKey(key);
        task.setTitle("deployment");
        task.setDueDate(LocalDate.now());
        task.setStatus("inprogress");
        List<Task> task_list = new ArrayList<>();
        task_list.add(task);
        Mockito.when(repo.findByUserEmail(Mockito.anyString())).thenReturn(task_list);

        List<Task> tasks = service.getTasks("charath@gmail.com");
        Assertions.assertNotNull(tasks);
        Assertions.assertEquals("charath@gmail.com",tasks.get(0).getKey().getUserEmail());
    }

}
