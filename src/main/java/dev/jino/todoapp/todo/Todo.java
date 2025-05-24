package dev.jino.todoapp.todo;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Todo {

    private Long id;
    private String content;
    private String writerName;
    private String password;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
}
