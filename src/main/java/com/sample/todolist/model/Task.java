package com.sample.todolist.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.*;

import java.time.LocalDate;
import java.util.UUID;

@Data
@NoArgsConstructor
@Table("tasks_by_user")
public class Task {
    @PrimaryKey
    //@JsonIgnore
    private TaskPrimaryKey key;

    /*@PrimaryKeyColumn(name="user_email",ordinal=0,type= PrimaryKeyType.PARTITIONED)
    @JsonIgnore
    //@CassandraType(type= CassandraType.Name.UUID)
    private String userEmail;
    @PrimaryKeyColumn(name="taskid",ordinal = 1,type= PrimaryKeyType.PARTITIONED)
    @JsonIgnore
    @CassandraType(type= CassandraType.Name.UUID)
    private UUID taskId;*/


    private String title;
    private String description;
    @Column("duedate")
    //@CassandraType(type = CassandraType.Name.DATE)
    private LocalDate dueDate;
    private String priority;
    private String status;

}
