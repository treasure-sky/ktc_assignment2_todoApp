package dev.jino.todoapp.todo;

import dev.jino.todoapp.todo.dto.PageResponseDto;
import dev.jino.todoapp.todo.dto.TodoCreateRequestDto;
import dev.jino.todoapp.todo.dto.TodoResponseDto;
import dev.jino.todoapp.writer.Writer;
import dev.jino.todoapp.writer.WriterRepository;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TodoServiceImpl implements TodoService {

    private final TodoRepository todoRepository;
    private final WriterRepository writerRepository;

    public TodoServiceImpl(TodoRepository todoRepository, WriterRepository writerRepository) {
        this.todoRepository = todoRepository;
        this.writerRepository = writerRepository;
    }

    @Override
    @Transactional
    public TodoResponseDto createTodo(TodoCreateRequestDto requestDto) {

        Writer writer = writerRepository.findByEmail(requestDto.getEmail())
            .map(existingWriter -> {
                // 이메일은 있지만 이름이 다른 경우
                if (!existingWriter.getName().equals(requestDto.getWriterName())) {
                    throw new IllegalArgumentException(
                        String.format("이메일 %s는 이미 다른 이름으로 등록되어 있습니다.",
                            requestDto.getEmail())
                    );
                }
                return existingWriter;
            })
            .orElseGet(() -> {
                // 이메일이 존재하지 않는 경우 새로운 writer 생성
                Writer newWriter = new Writer(
                    null,
                    requestDto.getWriterName(),
                    requestDto.getEmail(),
                    LocalDateTime.now(),
                    LocalDateTime.now()
                );
                return writerRepository.save(newWriter);
            });

        Todo newTodo = new Todo(
            null,
            requestDto.getContent(),
            requestDto.getPassword(),
            LocalDateTime.now(),
            LocalDateTime.now(), // 초기 생성시 업데이트 시간은 생성시간과 동일
            writer.getId()
        );
        Todo saved = todoRepository.save(newTodo);

        return new TodoResponseDto(
            saved.getId(),
            saved.getContent(),
            writer.getName(),
            saved.getCreatedAt(),
            saved.getUpdatedAt()
        );
    }

    @Override
    public List<TodoResponseDto> getTodos(Long writerId, LocalDate updatedAt) {

        List<Todo> todos = todoRepository
            .findByUpdatedAtOrWriterIdOrderByUpdatedAtDesc(writerId, updatedAt);

        return todos.stream().map(todo -> {

            Writer writer = writerRepository.findById(todo.getWriterId())
                .orElseThrow(() -> new IllegalArgumentException("작성자를 찾을 수 없습니다."));

            return new TodoResponseDto(
                todo.getId(),
                todo.getContent(),
                writer.getName(),
                todo.getCreatedAt(),
                todo.getUpdatedAt()
            );
        }).toList();
    }

    @Override
    public TodoResponseDto getTodo(Long id) {
        Todo todo = todoRepository.findById(id);

        Writer writer = writerRepository.findById(todo.getWriterId())
            .orElseThrow(() -> new IllegalArgumentException("작성자를 찾을 수 없습니다."));

        return new TodoResponseDto(
            todo.getId(),
            todo.getContent(),
            writer.getName(),
            todo.getCreatedAt(),
            todo.getUpdatedAt()
        );
    }

    @Override
    @Transactional
    public TodoResponseDto updateTodo(Long id, String content, String password) {
        Todo oldTodo = todoRepository.findById(id);

        // password 검증은 나중에 추가

        Todo updatedTodo = new Todo(
            oldTodo.getId(),
            content != null ? content : oldTodo.getContent(),
            // Lv3 연관관계 설정으로 writer 수정 기능 삭제
            // writerName != null ? writerName : oldTodo.getWriterName(),
            oldTodo.getPassword(),
            oldTodo.getCreatedAt(),
            LocalDateTime.now(),  // 업데이트 시간은 항상 갱신
            oldTodo.getWriterId()
        );

        Todo newTodo = todoRepository.update(updatedTodo);

        Writer writer = writerRepository.findById(newTodo.getWriterId())
            .orElseThrow(() -> new IllegalArgumentException("작성자를 찾을 수 없습니다."));

        return new TodoResponseDto(
            newTodo.getId(),
            newTodo.getContent(),
            writer.getName(),
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

    @Override
    public PageResponseDto<TodoResponseDto> getTodosWithPagination(int page, int size) {
        if (page < 0) {
            page = 0;
        }
        if (size <= 0) {
            size = 10; // 기본 크기
        }

        long totalElements = todoRepository.countAll();

        // 범위를 넘어선 페이지 요청 시 빈 배열 반환
        if (page * size >= totalElements) {
            return new PageResponseDto<>(List.of(), page, size, totalElements);
        }

        List<Todo> todos = todoRepository.findAllWithPagination(page, size);

        List<TodoResponseDto> todoResponses = todos.stream().map(todo -> {
            Writer writer = writerRepository.findById(todo.getWriterId())
                .orElseThrow(() -> new IllegalArgumentException("작성자를 찾을 수 없습니다."));

            return new TodoResponseDto(
                todo.getId(),
                todo.getContent(),
                writer.getName(),
                todo.getCreatedAt(),
                todo.getUpdatedAt()
            );
        }).toList();

        return new PageResponseDto<>(todoResponses, page, size, totalElements);
    }

}
