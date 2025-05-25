package dev.jino.todoapp.todo.dto;

import lombok.Getter;

@Getter
public class TodoUpdateRequestDto {

    private String content;
    // Lv3에서 writer_name을 writer 테이블로 분리하였으므로 writer 수정 기능 삭제
    // private String writerName;
    private String password;
}
