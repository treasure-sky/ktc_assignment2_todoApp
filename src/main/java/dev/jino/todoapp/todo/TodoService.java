package dev.jino.todoapp.todo;

import dev.jino.todoapp.todo.dto.TodoCreateRequestDto;
import dev.jino.todoapp.todo.dto.TodoResponseDto;

public interface TodoService {

    TodoResponseDto createTodo(TodoCreateRequestDto requestDto);

}
