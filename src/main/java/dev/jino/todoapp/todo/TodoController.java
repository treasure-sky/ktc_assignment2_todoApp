package dev.jino.todoapp.todo;

import dev.jino.todoapp.todo.dto.PageResponseDto;
import dev.jino.todoapp.todo.dto.TodoCreateRequestDto;
import dev.jino.todoapp.todo.dto.TodoDeleteRequestDto;
import dev.jino.todoapp.todo.dto.TodoResponseDto;
import dev.jino.todoapp.todo.dto.TodoUpdateRequestDto;
import java.time.LocalDate;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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

    @GetMapping
    ResponseEntity<List<TodoResponseDto>> getTodos(
        @RequestParam(required = false) Long writerId,
        @RequestParam(required = false) LocalDate updatedAt) {
        List<TodoResponseDto> todoResponses = todoService.getTodos(writerId, updatedAt);
        return ResponseEntity.ok(todoResponses);
    }

    @GetMapping("/{id}")
    ResponseEntity<TodoResponseDto> getTodo(@PathVariable Long id) {
        TodoResponseDto todoResponse = todoService.getTodo(id);
        return ResponseEntity.ok(todoResponse);
    }

    @PatchMapping("/{id}")
    ResponseEntity<TodoResponseDto> updatedTodo(
        @PathVariable Long id,
        @RequestBody TodoUpdateRequestDto requestDto) {
        TodoResponseDto todoResponse = todoService.updateTodo(
            id,
            requestDto.getContent(),
//            Lv3 연관관계 설정으로 writer 수정기능 삭제
//            requestDto.getWriterName(),
            requestDto.getPassword()
        );
        return ResponseEntity.ok(todoResponse);
    }

    @DeleteMapping("/{id}")
    ResponseEntity<Void> deleteTodo(
        @PathVariable Long id,
        @RequestBody TodoDeleteRequestDto requestDto) {
        boolean deleted = todoService.deleteTodo(
            id,
            requestDto.getPassword()
        );
        if (deleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/page")
    ResponseEntity<PageResponseDto<TodoResponseDto>> getTodosWithPagination(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size) {
        PageResponseDto<TodoResponseDto> pageResponse = todoService.getTodosWithPagination(page,
            size);
        return ResponseEntity.ok(pageResponse);
    }

}
