package dev.jino.todoapp.todo;

import dev.jino.todoapp.todo.dto.TodoCreateRequestDto;
import dev.jino.todoapp.todo.dto.TodoResponseDto;
import java.time.LocalDate;
import java.util.List;

public interface TodoService {

    TodoResponseDto createTodo(TodoCreateRequestDto requestDto);

    List<TodoResponseDto> getTodos(String writerName, LocalDate updatedAt);

    TodoResponseDto getTodo(Long id);

}
