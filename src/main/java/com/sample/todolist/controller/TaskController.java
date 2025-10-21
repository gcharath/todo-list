package com.sample.todolist.controller;

import com.sample.todolist.model.Task;
import com.sample.todolist.model.TaskPrimaryKey;
import com.sample.todolist.repository.TaskRepo;
import com.sample.todolist.service.TaskService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.log4j.Log4j2;
//import org.apache.logging.log4j.LogManager;
//import org.apache.logging.log4j.Logger;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
//import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
//import org.springframework.util.StringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/todo")
@Log4j2
//@Slf4j
public class TaskController {

    @Autowired
    private TaskRepo repo;

    @Autowired
    private TaskService taskService;

    //private static final Logger log = LogManager.getLogger(TaskController.class);
    @PostMapping("/task")
    public ResponseEntity createTask(HttpServletRequest request, @RequestBody Task task) {
        //LocalDate date;
        //System.out.println(todo.getDueDate().toString());
        String userEmail = request.getHeader("email");
        String category = request.getHeader("category");
        log.info("userEmail: "+userEmail);
        log.info("category: "+category);
        if(userEmail==null || userEmail.isEmpty()) {
            return new ResponseEntity("Header field email cannot be null",HttpStatus.BAD_REQUEST);
        }
        /*try{
            date = LocalDate.parse(todo.getDueDate().toString(),DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        } catch (Exception e) {
            return new ResponseEntity("Invalid date, please enter in MM-dd-yyyy format",HttpStatus.BAD_REQUEST);
        }*/
        TaskPrimaryKey key = new TaskPrimaryKey(userEmail,UUID.randomUUID(),category);
        //task.setTaskId(UUID.randomUUID());
        //task.setUserEmail(userEmail);
        task.setKey(key);
        repo.save(task);
        return new ResponseEntity(task, HttpStatus.OK);
    }

    @GetMapping("/tasks")
    public ResponseEntity getTasks(HttpServletRequest request) {

        String email = request.getHeader("email");
        String category = request.getParameter("category");
        log.info("userEmail: "+email);
        log.info("category: "+category);
        System.out.println("category: "+category);

        if(StringUtils.isBlank(email)) {
            return new ResponseEntity("email cannot be empty", HttpStatus.BAD_REQUEST);
        }

        if(!StringUtils.isEmpty(category)) {
            return new ResponseEntity(taskService.getTasks(email, category), HttpStatus.OK);
        }

        //TaskPrimaryKey key = new TaskPrimaryKey();
        //key.setUserEmail(email);
        return new ResponseEntity(taskService.getTasks(email), HttpStatus.OK);

    }

    @PutMapping("/task")
    public ResponseEntity updateTask(HttpServletRequest request, @RequestBody Task task) {
        //LocalDate date;
        //System.out.println(todo.getDueDate().toString());
        String userEmail = request.getHeader("email");
        String taskId = request.getHeader("taskId");
        String category = request.getHeader("category");
        if(userEmail==null || userEmail.isEmpty()) {
            return new ResponseEntity("Header field email cannot be null",HttpStatus.BAD_REQUEST);
        }

        if(taskId==null || taskId.isEmpty()) {
            return new ResponseEntity("taskId cannot be null",HttpStatus.BAD_REQUEST);
        }

        /*repo.updateTask(userEmail, task.getTitle(), task.getDescription(), task.getDueDate(), task.getPriority(),
                task.getStatus(), category, UUID.fromString(taskId));*/

        TaskPrimaryKey key = new TaskPrimaryKey(userEmail,UUID.fromString(taskId),category);
        task.setKey(key);
        repo.save(task);
        //task.setTaskId(UUID.randomUUID());
        //task.setUserEmail(userEmail);

        //repo.findById(key);

        return new ResponseEntity(repo.findById(key).get(), HttpStatus.OK);
    }

    @PutMapping("/status")
    public ResponseEntity updateStatus(HttpServletRequest request) {
        //LocalDate date;
        //System.out.println(todo.getDueDate().toString());
        String userEmail = request.getHeader("email");
        String taskId = request.getHeader("taskId");
        String category = request.getHeader("category");
        if(userEmail==null || userEmail.isEmpty()) {
            return new ResponseEntity("Header field email cannot be null",HttpStatus.BAD_REQUEST);
        }

        if(taskId==null || taskId.isEmpty()) {
            return new ResponseEntity("taskId cannot be null",HttpStatus.BAD_REQUEST);
        }

        /*repo.updateTask(userEmail, task.getTitle(), task.getDescription(), task.getDueDate(), task.getPriority(),
                task.getStatus(), category, UUID.fromString(taskId));*/

        TaskPrimaryKey key = new TaskPrimaryKey(userEmail,UUID.fromString(taskId),category);

        Optional<Task> task = repo.findById(key);

        if(task.isPresent()) {
            task.get().setStatus("completed");
            repo.save(task.get());
        } else {
            return new ResponseEntity("Task not found", HttpStatus.NOT_FOUND);
        }

        //task.setKey(key);
        //repo.save(task);
        //task.setTaskId(UUID.randomUUID());
        //task.setUserEmail(userEmail);

        //repo.findById(key);

        return new ResponseEntity(repo.findById(key).get(), HttpStatus.OK);
    }

    @DeleteMapping("/task")
    public ResponseEntity deleteTask(HttpServletRequest request) {
        //LocalDate date;
        //System.out.println(todo.getDueDate().toString());
        String userEmail = request.getHeader("email");
        String taskId = request.getHeader("taskId");
        String category = request.getHeader("category");
        if(userEmail==null || userEmail.isEmpty()) {
            return new ResponseEntity("Header field email cannot be null",HttpStatus.BAD_REQUEST);
        }

        if(taskId==null || taskId.isEmpty()) {
            return new ResponseEntity("taskId cannot be null",HttpStatus.BAD_REQUEST);
        }

        TaskPrimaryKey key = new TaskPrimaryKey(userEmail,UUID.fromString(taskId),category);

        Optional<Task> task = repo.findById(key);

        if(!task.isPresent())
            return new ResponseEntity("Task not found", HttpStatus.NOT_FOUND);

        repo.delete(task.get());

        return new ResponseEntity("Task deleted successfully", HttpStatus.OK);
    }


}
