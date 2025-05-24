package dev.jino.todoapp.todo;

import dev.jino.todoapp.todo.dto.TodoCreateRequestDto;
import dev.jino.todoapp.todo.dto.TodoResponseDto;
import java.time.LocalDateTime;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TodoServiceImpl implements TodoService {

    private final TodoRepository todoRepository;

    public TodoServiceImpl(TodoRepository todoRepository) {
        this.todoRepository = todoRepository;
    }

    @Override
    @Transactional
    public TodoResponseDto createTodo(TodoCreateRequestDto requestDto) {
        Todo newTodo = new Todo(
            null,
            requestDto.getContent(),
            requestDto.getWriterName(),
            requestDto.getPassword(),
            LocalDateTime.now(),
            LocalDateTime.now() // 초기 생성시 업데이트 시간은 생성시간과 동일
        );
        Todo saved = todoRepository.saveTodo(newTodo);

        return new TodoResponseDto(
            saved.getId(),
            saved.getContent(),
            saved.getWriterName(),
            saved.getCreatedAt(),
            saved.getUpdatedAt()
        );
    }
}
