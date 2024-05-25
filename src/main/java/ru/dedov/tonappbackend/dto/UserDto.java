package ru.dedov.tonappbackend.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO сущности Пользователь
 *
 * @author Alexander Dedov
 * @since 23.05.2024
 */
@Data
@NoArgsConstructor
public class UserDto {

	@JsonIgnore
	private Long id;
	private String username;
	private Double tonBalance;
	private Double accountBalance;
}
