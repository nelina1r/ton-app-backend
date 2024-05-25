package ru.dedov.tonappbackend.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.dedov.tonappbackend.model.entity.User;

/**
 * Репозиторий для пользователей
 *
 * @author Alexander Dedov
 * @since 23.05.2024
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

	boolean existsByUsername(String username);

	boolean existsById(Long id);
}
