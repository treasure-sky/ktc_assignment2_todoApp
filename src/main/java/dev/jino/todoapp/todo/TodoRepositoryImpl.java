package dev.jino.todoapp.todo;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class TodoRepositoryImpl implements TodoRepository {

    private final JdbcTemplate jdbcTemplate;

    public TodoRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Todo save(Todo todo) {
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

    @Override
    public List<Todo> findByUpdatedAtOrWriterNameOrderByUpdatedAtDesc(String writerName,
        LocalDate updatedAt) {

        List<String> params = new ArrayList<>();

        // 1+1은 AND로 WHERE절을 쉽게 append 하기 위해 추가
        StringBuilder sql = new StringBuilder(
            "SELECT id, content, writer_name, password, created_at, updated_at "
                + "FROM todo WHERE 1=1"
        );

        if (writerName != null && !writerName.isBlank()) {
            sql.append(" AND writer_name = ?");
            params.add(writerName);
        }

        if (updatedAt != null) {
            sql.append(" AND DATE(updated_at) = ?");
            params.add(updatedAt.toString());
        }

        sql.append(" ORDER BY updated_at DESC");

        return jdbcTemplate.query(sql.toString(),
            (rs, rowNum) -> new Todo(
                rs.getLong("id"),
                rs.getString("content"),
                rs.getString("writer_name"),
                rs.getString("password"),
                rs.getTimestamp("created_at").toLocalDateTime(),
                rs.getTimestamp("updated_at").toLocalDateTime()
            ),
            params.toArray()
        );
    }

    @Override
    public Todo findById(Long id) {
        String sql = "SELECT id, content, writer_name, password, created_at, updated_at "
            + "FROM todo WHERE id = ?";

        return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> new Todo(
            rs.getLong("id"),
            rs.getString("content"),
            rs.getString("writer_name"),
            rs.getString("password"),
            rs.getTimestamp("created_at").toLocalDateTime(),
            rs.getTimestamp("updated_at").toLocalDateTime()
        ), id);
    }

    @Override
    public Todo update(Todo todo) {
        String sql = "UPDATE todo SET content = ?, writer_name = ?, updated_at = ? WHERE id = ?";

        jdbcTemplate.update(sql,
            todo.getContent(),
            todo.getWriterName(),
            todo.getUpdatedAt(),
            todo.getId()
        );

        return todo;
    }
}
