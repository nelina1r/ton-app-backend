package ru.dedov.tonappbackend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO для запроса списания токенов с баланса
 *
 * @author Alexander Dedov
 * @since 25.05.2024
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TokenDeductionRequestDto {

	@NotNull(message = "account_id cannot be null")
	@JsonProperty("account_id")
	private String accountId;
	@NotNull(message = "deduction_amount cannot be null")
	@JsonProperty("deduction_amount")
	private Double deductionAmount;
}
