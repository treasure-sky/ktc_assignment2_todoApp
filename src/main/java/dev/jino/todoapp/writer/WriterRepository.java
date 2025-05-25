package dev.jino.todoapp.writer;

import java.util.Optional;

public interface WriterRepository {

    Writer save(Writer writer);

    Optional<Writer> findByEmail(String email);

    Optional<Writer> findById(Long id);
}
