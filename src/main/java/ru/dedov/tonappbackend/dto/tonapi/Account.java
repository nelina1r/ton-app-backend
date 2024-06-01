package ru.dedov.tonappbackend.dto.tonapi;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Account DTO
 *
 * @author Alexander Dedov
 * @since 02.06.2024
 */
@Data
@NoArgsConstructor
public class Account {

	private String address;
	private Long balance;
	@JsonProperty("last_activity")
	private String lastActivity;
}
