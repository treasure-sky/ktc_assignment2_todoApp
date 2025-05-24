package dev.jino.todoapp.todo;

import java.time.LocalDate;
import java.util.List;

public interface TodoRepository {

    Todo saveTodo(Todo todo);

    List<Todo> findByUpdatedAtOrWriterNameOrderByUpdatedAtDesc(String writerName,
        LocalDate updatedAt);

    Todo findById(Long id);

    Todo update(Todo todo);

}
