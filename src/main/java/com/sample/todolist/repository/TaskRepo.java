package com.sample.todolist.repository;

import com.sample.todolist.model.Task;
import com.sample.todolist.model.TaskPrimaryKey;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.cassandra.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Repository
public interface TaskRepo extends CassandraRepository<Task, TaskPrimaryKey> {

    //List<Task> getTasksByKey(String key);

    @Query("SELECT * FROM tasks_by_user WHERE user_email = ?0 ALLOW FILTERING")
    List<Task> findByUserEmail(String userEmail);
    @Query("SELECT * FROM tasks_by_user WHERE user_email = ?0 and category = ?1 ALLOW FILTERING")
    List<Task> findByUserEmailAndCategory(String userEmail, String category);

    @Query("update tasks_by_user set title=?1, description=?2, duedate=?3, priority=?4, status=?5, category=?6 where " +
            "user_email=?0 and taskid=?7")
    void updateTask(String userEmail, String title, String desc, LocalDate dueDate, String priority, String status,
                    String category, UUID taskid);
}
