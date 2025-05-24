package dev.jino.todoapp.todo.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TodoResponseDto {

    private Long id;
    private String content;
    private String writerName;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
