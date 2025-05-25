package dev.jino.todoapp.todo;

import java.time.LocalDate;
import java.util.List;

public interface TodoRepository {

    Todo save(Todo todo);

    List<Todo> findByUpdatedAtOrWriterIdOrderByUpdatedAtDesc(Long writerId,
        LocalDate updatedAt);

    Todo findById(Long id);

    Todo update(Todo todo);

    boolean deleteById(Long id);

    List<Todo> findAllWithPagination(int page, int size);

    long countAll();

}
