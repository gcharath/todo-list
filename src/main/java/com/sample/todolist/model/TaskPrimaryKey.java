package com.sample.todolist.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.CassandraType;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyClass;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;

import java.util.UUID;

@Data
@PrimaryKeyClass
@NoArgsConstructor
public class TaskPrimaryKey {

    @PrimaryKeyColumn(name="user_email",ordinal=0,type= PrimaryKeyType.PARTITIONED)
    //@CassandraType(type= CassandraType.Name.UUID)
    private String userEmail;
    @PrimaryKeyColumn(name="taskid",ordinal = 1,type= PrimaryKeyType.PARTITIONED)
    @CassandraType(type= CassandraType.Name.UUID)
    private UUID taskId;

    @PrimaryKeyColumn(name="category",ordinal = 2,type= PrimaryKeyType.CLUSTERED)
    //@CassandraType(type= CassandraType.Name.UUID)
    private String category;

    public TaskPrimaryKey(String email, UUID taskId, String category) {
        this.userEmail = email;
        this.taskId = taskId;
        this.category = category;
    }

}
