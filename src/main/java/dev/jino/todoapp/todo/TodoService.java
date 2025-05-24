package dev.jino.todoapp.todo;

import dev.jino.todoapp.todo.dto.TodoCreateRequestDto;
import dev.jino.todoapp.todo.dto.TodoResponseDto;
import java.time.LocalDate;
import java.util.List;

public interface TodoService {

    TodoResponseDto createTodo(TodoCreateRequestDto requestDto);

    List<TodoResponseDto> getTodos(String writerName, LocalDate updatedAt);

    TodoResponseDto getTodo(Long id);

    TodoResponseDto updateTodo(Long id, String content, String writerName, String password);

    boolean deleteTodo(Long id, String writerName, String password);

}
