package com.sample.todolist.controller;

import com.sample.todolist.model.Task;
import com.sample.todolist.model.TaskPrimaryKey;
import com.sample.todolist.repository.TaskRepo;
import com.sample.todolist.service.TaskService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TaskController.class)
public class TaskControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TaskRepo taskRepo;

    @MockBean
    private TaskService service;

    @Test
    @Disabled
    public void getTasksTest() throws Exception {
        TaskPrimaryKey key = new TaskPrimaryKey("charath@gmail.com", UUID.randomUUID(),"inbox");
        Task task = new Task();
        task.setKey(key);
        task.setTitle("deployment");
        task.setDueDate(LocalDate.now());
        task.setStatus("inprogress");
        List<Task> task_list = new ArrayList<>();
        task_list.add(task);
        when(service.getTasks(anyString(), anyString())).thenReturn(task_list);

        mockMvc.perform(get("/todo/tasks?category=inbox").header("email","charath@gmail.com"))
                .andExpect(status().isOk())
                //.andReturn();
                .andExpect(jsonPath("$",Matchers.hasSize(1)))
                .andExpect(jsonPath("$[0].key.userEmail", Matchers.is("charath@gmail.com")));

        //String content = result.getResponse().getContentAsString();
        //System.out.println("content: "+content);
    }


}
