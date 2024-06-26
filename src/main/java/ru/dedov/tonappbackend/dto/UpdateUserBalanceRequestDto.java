package ru.dedov.tonappbackend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * TODO Class Description
 *
 * @author Alexander Dedov
 * @since 26.05.2024
 */
@Data
@NoArgsConstructor
public class UpdateUserBalanceRequestDto {

	@NotNull(message = "account_id cannot be null")
	@JsonProperty("account_id")
	private String accountId;
	@NotNull(message = "jetton_id cannot be null")
	@JsonProperty("jetton_id")
	private String jettonId;
	@JsonProperty("start_date")
	private Long startDate;
	@JsonProperty("end_date")
	private Long endDate;
}
