package com.sample.todolist.controller;

import com.sample.todolist.model.Task;
import com.sample.todolist.model.TaskPrimaryKey;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.UUID;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TaskSystemTest {
    @Autowired
    private TaskController controller;

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    //@Test
    public void createReadTask() {
        TaskPrimaryKey key = new TaskPrimaryKey("charath@gmail.com", UUID.randomUUID(),"inbox");
        Task task = new Task();
        task.setKey(key);
        task.setTitle("deployment");
        task.setDueDate(LocalDate.now());
        task.setStatus("inprogress");

        String url = "http://localhost:"+port+"/todo/task";

        HttpHeaders headers = new HttpHeaders();
        headers.add("email","charath@gmail.com");
        headers.add("category","work");

        HttpEntity entity = new HttpEntity(task,headers);

        ResponseEntity<Task> response = restTemplate.postForEntity(url, entity, Task.class);

        Assertions.assertEquals(200,response.getStatusCode().value());
        Assertions.assertEquals("charath@gmail.com",response.getBody().getKey().getUserEmail());


    }
}
