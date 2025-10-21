package com.sample.todolist.service;

import com.sample.todolist.model.Task;
import com.sample.todolist.repository.TaskRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskService {

    @Autowired
    private TaskRepo repo;
    public List<Task> getTasks(String email, String category) {
        return repo.findByUserEmailAndCategory(email, category);
    }

    public List<Task> getTasks(String email) {
        return repo.findByUserEmail(email);
    }
}
