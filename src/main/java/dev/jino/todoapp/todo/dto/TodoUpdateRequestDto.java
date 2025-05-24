package dev.jino.todoapp.todo.dto;

import lombok.Getter;

@Getter
public class TodoUpdateRequestDto {

    private String content;
    private String writerName;
    private String password;
}
