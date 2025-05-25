package dev.jino.todoapp.todo.dto;

import lombok.Getter;

@Getter
public class TodoCreateRequestDto {

    private String content;
    private String email;
    private String writerName;
    private String password;

}
