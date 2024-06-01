package ru.dedov.tonappbackend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO для запроса баланса по конкретному токену и id аккаунта
 *
 * @author Alexander Dedov
 * @since 25.05.2024
 */
@Data
@NoArgsConstructor
public class TokenBalanceRequestDto {

	@NotNull(message = "account_id cannot be null")
	@JsonProperty("account_id")
	private String accountId;
	@NotNull(message = "token_symbol cannot be null")
	@JsonProperty("token_symbol")
	private String tokenSymbol;
}
