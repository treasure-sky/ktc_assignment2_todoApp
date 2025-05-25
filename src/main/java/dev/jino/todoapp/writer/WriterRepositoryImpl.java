package dev.jino.todoapp.writer;

import java.util.Optional;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class WriterRepositoryImpl implements WriterRepository {

    private final JdbcTemplate jdbcTemplate;

    public WriterRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Writer save(Writer writer) {
        String sql = "INSERT INTO writer (name, email, created_at, updated_at) "
            + "VALUES (?, ?, ?, ?)";

        jdbcTemplate.update(sql,
            writer.getName(),
            writer.getEmail(),
            writer.getCreatedAt(),
            writer.getUpdatedAt()
        );

        // 생성된 id는 db에서 가져옴
        Long id = jdbcTemplate.queryForObject("SELECT LAST_INSERT_ID()", Long.class);

        return new Writer(
            id,
            writer.getName(),
            writer.getEmail(),
            writer.getCreatedAt(),
            writer.getUpdatedAt()
        );
    }

    @Override
    public Optional<Writer> findByEmail(String email) {
        String sql = "SELECT id, name, email, created_at, updated_at "
            + "FROM writer WHERE email = ?";

        try {
            Writer writer = jdbcTemplate.queryForObject(sql, (rs, rowNum) -> new Writer(
                rs.getLong("id"),
                rs.getString("name"),
                rs.getString("email"),
                rs.getTimestamp("created_at").toLocalDateTime(),
                rs.getTimestamp("updated_at").toLocalDateTime()
            ), email);
            return Optional.of(writer);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }

    }

    @Override
    public Optional<Writer> findById(Long id) {
        String sql = "SELECT id, name, email, created_at, updated_at "
            + "FROM writer WHERE id = ?";

        try {
            Writer writer = jdbcTemplate.queryForObject(sql, (rs, rowNum) -> new Writer(
                rs.getLong("id"),
                rs.getString("name"),
                rs.getString("email"),
                rs.getTimestamp("created_at").toLocalDateTime(),
                rs.getTimestamp("updated_at").toLocalDateTime()
            ), id);
            return Optional.of(writer);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }


}
