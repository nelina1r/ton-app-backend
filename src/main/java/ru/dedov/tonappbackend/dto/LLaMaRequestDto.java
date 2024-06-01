package ru.dedov.tonappbackend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO to generation request by model
 *
 * @author Alexander Dedov
 * @since 28.05.2024
 */
@Data
@NoArgsConstructor
public class LLaMaRequestDto {

	@NotNull(message = "text cannot be null")
	private String text;
	@NotNull(message = "question cannot be null")
	private String question;
	@NotNull(message = "account_id cannot be null")
	@JsonProperty("account_id")
	private String accountId;
}
