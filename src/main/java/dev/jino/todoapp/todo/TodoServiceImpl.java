package dev.jino.todoapp.todo;

import dev.jino.todoapp.todo.dto.TodoCreateRequestDto;
import dev.jino.todoapp.todo.dto.TodoResponseDto;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
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
        Todo saved = todoRepository.save(newTodo);

        return new TodoResponseDto(
            saved.getId(),
            saved.getContent(),
            saved.getWriterName(),
            saved.getCreatedAt(),
            saved.getUpdatedAt()
        );
    }

    @Override
    public List<TodoResponseDto> getTodos(String writerName, LocalDate updatedAt) {

        List<Todo> todos = todoRepository
            .findByUpdatedAtOrWriterNameOrderByUpdatedAtDesc(writerName, updatedAt);

        return todos.stream().map(todo -> new TodoResponseDto(
            todo.getId(),
            todo.getContent(),
            todo.getWriterName(),
            todo.getCreatedAt(),
            todo.getUpdatedAt())
        ).toList();
    }

    @Override
    public TodoResponseDto getTodo(Long id) {
        Todo todo = todoRepository.findById(id);

        return new TodoResponseDto(
            todo.getId(),
            todo.getContent(),
            todo.getWriterName(),
            todo.getCreatedAt(),
            todo.getUpdatedAt()
        );
    }

    @Override
    @Transactional
    public TodoResponseDto updateTodo(Long id, String content, String writerName, String password) {
        Todo oldTodo = todoRepository.findById(id);

        // password 검증은 나중에 추가
        // 작성자 검증도 나중에 추가?

        Todo updatedTodo = new Todo(
            oldTodo.getId(),
            content != null ? content : oldTodo.getContent(),
            writerName != null ? writerName : oldTodo.getWriterName(),
            oldTodo.getPassword(),
            oldTodo.getCreatedAt(),
            LocalDateTime.now()  // 업데이트 시간은 항상 갱신
        );

        Todo newTodo = todoRepository.update(updatedTodo);

        return new TodoResponseDto(
            newTodo.getId(),
            newTodo.getContent(),
            newTodo.getWriterName(),
            newTodo.getCreatedAt(),
            newTodo.getUpdatedAt()
        );
    }

    @Override
    public boolean deleteTodo(Long id, String password) {
        // password 검증은 나중에 추가
        // 작성자 검증도 나중에 추가
        return todoRepository.deleteById(id);
    }

}
