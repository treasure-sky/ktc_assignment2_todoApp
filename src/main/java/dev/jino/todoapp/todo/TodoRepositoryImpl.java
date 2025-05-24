package dev.jino.todoapp.todo;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class TodoRepositoryImpl implements TodoRepository {

    private final JdbcTemplate jdbcTemplate;

    public TodoRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Todo saveTodo(Todo todo) {
        String sql = "INSERT INTO todo (content, writer_name, password, created_at, updated_at) "
            + "VALUES (?, ?, ?, ?, ?)";

        jdbcTemplate.update(sql,
            todo.getContent(),
            todo.getWriterName(),
            todo.getPassword(),
            todo.getCreatedAt(),
            todo.getUpdatedAt()
        );

        // 생성된 id는 db에서 가져옴
        Long id = jdbcTemplate.queryForObject("SELECT LAST_INSERT_ID()", Long.class);

        return new Todo(
            id,
            todo.getContent(),
            todo.getWriterName(),
            todo.getPassword(),
            todo.getCreatedAt(),
            todo.getUpdatedAt()
        );
    }
}
