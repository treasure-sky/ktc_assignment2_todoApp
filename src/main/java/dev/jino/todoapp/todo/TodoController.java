package dev.jino.todoapp.todo;

import dev.jino.todoapp.todo.dto.TodoCreateRequestDto;
import dev.jino.todoapp.todo.dto.TodoResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/todos")
public class TodoController {

    private final TodoService todoService;

    public TodoController(TodoService todoService) {
        this.todoService = todoService;
    }

    @PostMapping
    ResponseEntity<TodoResponseDto> createTodo(@RequestBody TodoCreateRequestDto requestDto) {
        TodoResponseDto todoResponse = todoService.createTodo(requestDto);
        return ResponseEntity.ok(todoResponse);
    }
}
