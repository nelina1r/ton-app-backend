package ru.dedov.tonappbackend.mapper;

import org.mapstruct.Mapper;
import ru.dedov.tonappbackend.dto.UserDto;
import ru.dedov.tonappbackend.model.entity.User;

/**
 * Маппер DTO -> Entity и наоборот для сущности пользователь
 *
 * @author Alexander Dedov
 * @since 01.06.2024
 */
@Mapper(componentModel = "spring")
public interface UserMapper {
	UserDto toDto(User entity);

	User toEntity(UserDto dto);
}
