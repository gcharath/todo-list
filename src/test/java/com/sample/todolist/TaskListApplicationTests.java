package com.sample.todolist;

import com.sample.todolist.controller.TaskController;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class TaskListApplicationTests {

	@Autowired
	TaskController controller;
	//@Test
	void contextLoads() {
		Assertions.assertNotNull(controller);
	}

}
