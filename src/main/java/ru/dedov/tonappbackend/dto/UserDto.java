package ru.dedov.tonappbackend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO сущности Пользователь
 *
 * @author Alexander Dedov
 * @since 01.06.2024
 */
@Data
@NoArgsConstructor
public class UserDto {

	@NotNull(message = "account_id cannot be null")
	@JsonProperty("account_id")
	private String accountId;
}
