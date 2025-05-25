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
        String sql = "INSERT INTO todo (content, password, created_at, updated_at, writer_id) "
            + "VALUES (?, ?, ?, ?, ?)";

        jdbcTemplate.update(sql,
            todo.getContent(),
            todo.getPassword(),
            todo.getCreatedAt(),
            todo.getUpdatedAt(),
            todo.getWriterId()
        );

        // 생성된 id는 db에서 가져옴
        Long id = jdbcTemplate.queryForObject("SELECT LAST_INSERT_ID()", Long.class);

        return new Todo(
            id,
            todo.getContent(),
            todo.getPassword(),
            todo.getCreatedAt(),
            todo.getUpdatedAt(),
            todo.getWriterId()
        );
    }

    @Override
    public List<Todo> findByUpdatedAtOrWriterIdOrderByUpdatedAtDesc(Long writerId,
        LocalDate updatedAt) {

        List<String> params = new ArrayList<>();

        // 1+1은 AND로 WHERE절을 쉽게 append 하기 위해 추가
        StringBuilder sql = new StringBuilder(
            "SELECT id, content, password, created_at, updated_at, writer_id "
                + "FROM todo WHERE 1=1"
        );

        if (writerId != null) {
            sql.append(" AND writer_id = ?");
            params.add(writerId.toString());
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
                rs.getString("password"),
                rs.getTimestamp("created_at").toLocalDateTime(),
                rs.getTimestamp("updated_at").toLocalDateTime(),
                rs.getLong("writer_id")
            ),
            params.toArray()
        );
    }

    @Override
    public Todo findById(Long id) {
        String sql = "SELECT id, content, password, created_at, updated_at, writer_id "
            + "FROM todo WHERE id = ?";

        return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> new Todo(
            rs.getLong("id"),
            rs.getString("content"),
            rs.getString("password"),
            rs.getTimestamp("created_at").toLocalDateTime(),
            rs.getTimestamp("updated_at").toLocalDateTime(),
            rs.getLong("writer_id")
        ), id);
    }

    @Override
    public Todo update(Todo todo) {
        String sql = "UPDATE todo SET content = ?, updated_at = ? WHERE id = ?";

        jdbcTemplate.update(sql,
            todo.getContent(),
            todo.getUpdatedAt(),
            todo.getId()
        );

        return todo;
    }

    @Override
    public boolean deleteById(Long id) {
        String sql = "DELETE FROM todo WHERE id = ?";
        int deletedRows = jdbcTemplate.update(sql, id);
        return deletedRows > 0;
    }

    @Override
    public List<Todo> findAllWithPagination(int page, int size) {
        int offset = page * size;

        String sql = "SELECT t.id, t.content, t.password, t.created_at, t.updated_at, t.writer_id "
            + "FROM todo t ORDER BY t.updated_at DESC LIMIT ? OFFSET ?";

        return jdbcTemplate.query(sql,
            (rs, rowNum) -> new Todo(
                rs.getLong("id"),
                rs.getString("content"),
                rs.getString("password"),
                rs.getTimestamp("created_at").toLocalDateTime(),
                rs.getTimestamp("updated_at").toLocalDateTime(),
                rs.getLong("writer_id")
            ),
            size, offset
        );
    }

    @Override
    public long countAll() {
        String sql = "SELECT COUNT(*) FROM todo";
        return jdbcTemplate.queryForObject(sql, Long.class);
    }
}
