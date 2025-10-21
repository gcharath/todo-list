//package com.sample.todolist.service;
//
//import com.sample.todolist.model.Task;
//import com.sample.todolist.model.TaskPrimaryKey;
//import com.sample.todolist.repository.TaskRepo;
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
//import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
//import org.springframework.test.context.junit.jupiter.SpringExtension;
//
//import java.time.LocalDate;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.UUID;
//
//@DataJpaTest
//@AutoConfigureTestDatabase
//@ExtendWith(SpringExtension.class)
//public class TaskRepoTests {
//
//    @Autowired
//    TaskRepo taskRepo;
//    @Test
//    public void testFindByEmail() {
//        TaskPrimaryKey key = new TaskPrimaryKey("charath@gmail.com", UUID.randomUUID(),"inbox");
//        Task task = new Task();
//        task.setKey(key);
//        task.setTitle("deployment");
//        task.setDueDate(LocalDate.now());
//        task.setStatus("inprogress");
//        List<Task> task_list = new ArrayList<>();
//        task_list.add(task);
//
//        taskRepo.save(task);
//
//        Assertions.assertEquals(1,taskRepo.findAll().size());
//    }
//}
